package com.safetyalert.config;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.safetyalert.dao.PersonDAO;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.Person;
import com.safetyalert.model.SafetyAlertJsonData;
import com.safetyalert.service.PersonService;

@Configuration
public class LoadSafetyAlertData {

	private static final Logger logger = LogManager.getLogger("LoadSafetyAlertData");
	
	@Autowired
	private PersonDAO personDAO;
	
	@Bean
	CommandLineRunner initDataBaseFromJson(MedicalRepository medicalRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getMedicalrecords().forEach(record->{
				medicalRepository.save(record);
			});
			logger.info("medical records loaded");
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
