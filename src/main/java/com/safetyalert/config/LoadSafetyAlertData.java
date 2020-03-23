package com.safetyalert.config;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.MedicalRepositoryCustom;
import com.safetyalert.dao.PersonDAO;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.dao.PersonRepositoryCustom;
import com.safetyalert.dao.StationRepository;
import com.safetyalert.dao.StationRepositoryCustom;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.FireStationCustom;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.MedicalRecordCustom;
import com.safetyalert.model.Person;
import com.safetyalert.model.PersonCustom;
import com.safetyalert.model.SafetyAlertJsonData;
import com.safetyalert.model.id.PersonId;
import com.safetyalert.service.PersonService;

@Configuration
public class LoadSafetyAlertData {

	private static final Logger logger = LogManager.getLogger("LoadSafetyAlertData");
	
	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private MedicalRepositoryCustom medicalRepository;
	@Autowired
	private StationRepositoryCustom stationRepository;
	
	
	
	@Bean
	CommandLineRunner initMedicalData(MedicalRepository medicalRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getMedicalrecords().forEach(record->{
				MedicalRecord newRecord = new MedicalRecord();
				newRecord.setFirstName(record.getFirstName());
				newRecord.setLastName(record.getLastName());
				newRecord.setBirthdate(record.getBirthdate());
				medicalRepository.save(newRecord);
			});
			logger.info("medical records loaded");
		};
	}
	
	@Bean
	CommandLineRunner initMedicalDataCustom(MedicalRepositoryCustom medicalRepositoryCustom) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getMedicalrecords().forEach(record->{
				String firstName = record.getFirstName();
				String lastName = record.getLastName();
				String birthDate = record.getBirthdate();
				List<String> allergies = record.getAllergies();
				List<String> medications = record.getMedications();
				MedicalRecordCustom newRecord = new MedicalRecordCustom(firstName,lastName,birthDate);
				newRecord.setAllergies(allergies);
				newRecord.setMedications(medications);
				medicalRepositoryCustom.save(newRecord);
			});
			logger.info("medical records loaded");
		};
	}
	
	@Bean
	CommandLineRunner initStationData(StationRepository stationRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getFirestations().forEach(station->{
				FireStation newStation = new FireStation();
				newStation.setAddress(station.getAddress());
				newStation.setStation(station.getStation());
				stationRepository.save(newStation);
			});
			logger.info("stations loaded");
		};
	}
	
	@Bean
	CommandLineRunner initStationDataCustom(StationRepositoryCustom stationRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getFirestations().forEach(station->{
				FireStationCustom newStation = new FireStationCustom();
				newStation.setAddress(station.getAddress());
				newStation.setStation(station.getStation());
				stationRepository.save(newStation);
			});
			logger.info("stations loaded");
		};
	}
	
	@Bean
	CommandLineRunner initPersonData(PersonRepository personRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getPersons().forEach(person->{
				personRepository.save(person);
				Person p1 = personRepository.findByFirstNameAndLastName(person.getFirstName(),person.getLastName());
				p1.setAge(PersonService.calculteAge(p1.getMedicalRecord().getBirthdate()));
				personRepository.save(p1);
			});
			logger.info("persons loaded");
		};
	}
	
	@Bean
	CommandLineRunner initPersonDataCustom(PersonRepositoryCustom personRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getPersons().forEach(person->{
				PersonCustom newPerson = new PersonCustom();
				String firstName = person.getFirstName();
				String lastName = person.getLastName();
				newPerson.setFirstName(firstName);
				newPerson.setLastName(lastName);
				newPerson.setAddress(person.getAddress());
//				PersonCustom p1 = personRepository.findByFirstNameAndLastName(person.getFirstName(),person.getLastName());
//				p1.setAge(PersonService.calculteAge(p1.getMedicalRecordCustom().getBirthdate()));
//				personRepository.save(p1);
				
				MedicalRecordCustom medical = medicalRepository.findByFirstNameAndLastName(firstName, lastName);
				newPerson.setMedicalId(medical.getId());
				
				FireStationCustom station = stationRepository.findOneByAddress(newPerson.getAddress());
				logger.info("found station : "+station);
				newPerson.setStationId(station.getId());
				personRepository.save(newPerson);
			});
			logger.info("persons loaded");
		};
	}
	
	@Bean
	CommandLineRunner initDataFromJson() {
		return args -> {
			logger.info("loading data ...");
	        ObjectMapper objectMapper = new ObjectMapper();
	        //read json file and convert to customer object
	        SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
	        associateData(jsonData);
	        personDAO.setPersons(jsonData.getPersons());
		};
	}
	
	private void associateData(SafetyAlertJsonData jsonData) {
		for(Person person : jsonData.getPersons()) {
			//associate one fireStation to one person
			for(FireStation fireStation : jsonData.getFirestations()) {
				if(person.getAddress().equals(fireStation.getAddress())) {
					person.setFireStation(fireStation);
					break;
				}
			}
			
			//associate one medicalRecord to one person
			for(MedicalRecord record : jsonData.getMedicalrecords()) {
				String keyPerson = person.getFirstName()+person.getLastName();
				String keyRecord = record.getFirstName()+record.getLastName();
				if(keyPerson.equals(keyRecord)) {
					person.setMedicalRecord(record);
					person.setAge(PersonService.calculteAge(record.getBirthdate()));
				}
			}
			
			
		}
	}
	
}
