package com.safetyalert.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.dao.StationRepository;
import com.safetyalert.exception.PersonAlreadyExists;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.Person;

@Service
public class PersonServiceImpl implements IPersonService{
	
	private static final Logger logger = LogManager.getLogger("PersonServiceImpl");

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private StationRepository stationRepository;
	
	@Autowired
	private MedicalRepository medicalRepository;


	public List<Person> getAllPeople() {
		return (List<Person>) personRepository.findAll();
	}

	public List<Person> getPersonsCoveredByStation(String stationNumber) {
		return personRepository.findByFireStation_Station(stationNumber);
	}

	public Map<String, String> countAdultChildren(List<Person> persons) {
		String result = "";
		int nbAdult = 0;
		int nbChildren = 0;

		for (Person person : persons) {
			if (person.getAge() > 18) {
				nbAdult++;
			} else {
				nbChildren++;
			}
		}
		Map<String, String> map = new HashMap<>();
		map.put("adult", "" + nbAdult);
		map.put("child", "" + nbChildren);
		return map;
	}
	
	public Map<String, List<Person>> getChildrenByAddressAndRelatives(String address) {
		List<Person> persons = personRepository.findByAddress(address);
		List<Person> children = new ArrayList<Person>();
		List<Person> adults = new ArrayList<Person>();
		
		for(Person person : persons) {
			if(person.getAge() > 18) {
				adults.add(person);
			}else {
				children.add(person);
			}
		}
		
		Map<String, List<Person>> map = new HashMap<>();
		map.put("children", children);
		map.put("adults", adults);
		
		return map;
	}
	
	public Map<String, List<String>> getPhonesByStation(String stationNumber){
		List<Person> persons = personRepository.findByFireStation_Station(stationNumber);
		List<String> phones = new ArrayList<String>();
		for(Person person : persons) {
			String phone = person.getPhone();
			if(!phones.contains(phone)) {
				phones.add(phone);
			}
		}
		Map<String, List<String>> map = new HashMap<>();
		map.put("phones", phones);
		return map;
	}

	public List<Person> getPersonsByAddress(String address) {
		return personRepository.findByAddress(address);
	}

	public List<Person> getPersonsFromStations(String[] stations) {
		return personRepository.findByFireStation_StationIn(stations);
	}
	
//	public List<String> getUniqueAddressFromPersons(List<Person> persons){
//		List<String> result = new ArrayList<String>();
//		for(Person person : persons) {
//			String address = person.getAddress();
//			if(!result.contains(address)) {
//				result.add(address);
//			}
//		}
//		return result;
//	}
	
//	public List<Person> retrievePersonFromAddress(List<Person> persons, String address){
//		List<Person> result = new ArrayList<Person>();
//		
//		for(int i = persons.size()-1; i>=0;i--) {
//			Person person = persons.get(i);
//			if(person.getAddress().equals(address)) {
//				result.add(persons.remove(i));
//			}
//		}
//		return result;
//	}

	public List<Person> getPersonsByLastName(String lastName) {
		return personRepository.findByLastName(lastName);
	}

	public List<Person> getPersonsByCity(String city) {
		return personRepository.findByCity(city);
	}

	public List<String> getUniqueAddressesFromStations(String[] stations) {
		return personRepository.findDistinctAddressesByStations(stations);
	}
	
	private boolean canCreate(String firstName, String lastName) {
		Person person = personRepository.findByFirstNameAndLastName(firstName, lastName);
		return person==null;
	}

	public Person createPerson(Person person) throws PersonAlreadyExists {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		if(!canCreate(firstName, lastName)) {
			throw new PersonAlreadyExists("cannot create, person already exists with name : "+firstName + " "+ lastName);
		}
		attachStationAndMedicalToPerson(person);
		personRepository.save(person);
		return person;
	}
	
	private void attachStationAndMedicalToPerson(Person person) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		String address = person.getAddress();
		
		FireStation foundStation = stationRepository.findOneByAddress(address);
		MedicalRecord foundRecord = medicalRepository.findByFirstNameAndLastName(firstName, lastName);
		
		person.setFireStation(foundStation);
		person.setMedicalRecord(foundRecord);
		
	}

	public Person updatePerson(Person person) {
		Person updated = null;
		if(person.getId()!=null) {
			updated = updatePersonById(person);
		}else {
			updated = updatePersonByFirstAndLastName(person);
		}
		return updated;
	}

	private Person updatePersonById(Person person) {
		Long id = person.getId();
		Person found = personRepository.findOneById(id);
		if(found==null) {
			return null;
		}
		
		String personId = person.getFirstName()+person.getLastName();
		String foundId = found.getFirstName()+found.getLastName();
		transpose(person, found);
		
		if(personId.equals(foundId)) {
			return personRepository.save(found);
		}
		
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		logger.info("can create ?"+canCreate(firstName, lastName));
		if(!canCreate(firstName, lastName)) {
			return null;
		}
		
		found.setFirstName(firstName);
		found.setLastName(lastName);
		
		attachStationAndMedicalToPerson(found);
		
		return personRepository.save(found);
		
	}
	
	private void transpose(Person from, Person to) {
		String address = from.getAddress();
		int age = from.getAge();
		String city = from.getCity();
		String email = from.getEmail();
		String phone = from.getPhone();
		String zip = from.getZip();
		
		to.setAddress(address);
		to.setAge(age);
		to.setCity(city);
		to.setEmail(email);
		to.setPhone(phone);
		to.setZip(zip);
	}

	private Person updatePersonByFirstAndLastName(Person person) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		Person found = personRepository.findByFirstNameAndLastName(firstName, lastName);
		if(found==null) {
			return null;
		}
		
		transpose(person, found);
		
		personRepository.save(found);
		return found;
	}

	public Person deletePerson(Person person) {
		Person deleted = null;
		deleted = deletePersonById(person);
		if(deleted==null) {
			deleted = deletePersonByFirstAndLastName(person);
		};
		return deleted;
	}

	private Person deletePersonByFirstAndLastName(Person person) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		if(firstName == null || lastName == null) {
			return null;
		}
		
		Person found = personRepository.findByFirstNameAndLastName(firstName, lastName);
		personRepository.delete(found);
		
		return found;
	}

	private Person deletePersonById(Person person) {
		Long id = person.getId();
		if(id==null) {
			return null;
		}
		Person found = personRepository.findOneById(id);
		personRepository.delete(found);
		return found;
	}

	@Override
	public Person getPersonById(Long id) {
		return personRepository.findOneById(id);
	}

	@Override
	public Person getPersonByFirstAndLastName(String firstName, String lastName) {
		return personRepository.findByFirstNameAndLastName(firstName, lastName);
	}

	@Override
	public Person getPersonByMedicalId(Long id) {
		return personRepository.findByMedicalId(id.toString());
	}

	@Override
	public List<Person> getPersonByStationId(Long id) {
		return personRepository.findByStationId(id.toString());
	}

	@Override
	public Person deletePerson(String firstName, String lastName) {
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		return deletePersonByFirstAndLastName(person);
	}

	@Override
	public Person deletePerson(Long id) {
		Person person = new Person();
		person.setId(id);
		return deletePersonById(person);
	}

}
