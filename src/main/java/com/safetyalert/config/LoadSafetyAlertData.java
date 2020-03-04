package com.safetyalert.config;
import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyalert.dao.PersonDAO;
import com.safetyalert.model.Person;
import com.safetyalert.model.SafetyAlertJsonData;

@Configuration
public class LoadSafetyAlertData {

	private static final Logger logger = LogManager.getLogger("LoadSafetyAlertData");
	
	@Autowired
	private PersonDAO personDAO;
	
	@Bean
	CommandLineRunner initDataFromJson() {
		return args -> {
//			logger.info("loading data ...");
//			List<Person> persons = new ArrayList<Person>();
//			persons.add(p1);
//			personDAO.setPersons(persons);
			logger.info("loading data 2 ...");
			//create ObjectMapper instance
	        ObjectMapper objectMapper = new ObjectMapper();
	        //read json file and convert to customer object
	        SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
	        //print customer details
	        System.out.println(jsonData);
	        personDAO.setPersons(jsonData.getPersons());
	        logger.info("end2 ...");
		};
	}
	
}
