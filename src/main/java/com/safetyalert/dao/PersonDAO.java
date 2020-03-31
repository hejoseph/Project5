package com.safetyalert.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.safetyalert.model.Person;

@Repository
public class PersonDAO implements IPersonDAO{

	private static final Logger logger = LogManager.getLogger("PersonDAO");
	private List<Person> persons;
	
	public PersonDAO(List<Person> persons) {
		this.persons = persons;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	@Override
	public List<Person> getPersonsCoveredByStation(String number) {
		List<Person> result = new ArrayList<Person>();
		for(Person person : this.persons) {
//			if(person.getFireStation().getStation().equals(number)) {
//				result.add(person);
//			}
		}
		return result;
	}

	@Override
	public List<Person> getPersonsByAddress(String address) {
		List<Person> result = new ArrayList<Person>();
		for(Person person : this.persons) {
			if(person.getAddress().equals(address)) {
				result.add(person);
			}
		}
		return result;
	}

	@Override
	public List<Person> getPersonsFromStations(String[] stations) {
		List<Person> result = new ArrayList<Person>();
		List<String> arrays = Arrays.asList(stations);
		for(Person person : this.persons) {
//			if(arrays.contains(person.getFireStation().getStation())) {
//				result.add(person);
//			}
		}
		return result;
	}

	@Override
	public List<Person> getPersonsByLastName(String lastName) {
		List<Person> result = new ArrayList<Person>();
		for(Person person : this.persons) {
			if(person.getLastName().equals(lastName)) {
				result.add(person);
			}
		}
		return result;
	}

	@Override
	public List<Person> getPersonsByCity(String city) {
		List<Person> result = new ArrayList<Person>();
		for(Person person : this.persons) {
			if(person.getCity().equals(city)) {
				result.add(person);
			}
		}
		return result;
	}
	
	
	
}
