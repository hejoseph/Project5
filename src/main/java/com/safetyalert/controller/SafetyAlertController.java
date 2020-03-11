package com.safetyalert.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetyalert.jsonfilter.ChildAlertFilter;
import com.safetyalert.jsonfilter.MedicalRecordFilter;
import com.safetyalert.jsonfilter.StationNumberPersonFilter;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.Person;
import com.safetyalert.service.PersonService;

@RestController
public class SafetyAlertController {

	private static final Logger logger = LogManager.getLogger("SafetyAlertController");
	
	private final PersonService personService;

	@Autowired
	public SafetyAlertController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/firestation")
	public String getPersonsByStationNumberFilteredProperties(@RequestParam String stationNumber, @RequestParam(required = false) boolean showAll) {
		List<Person> persons = personService.getPersonsCoveredByStation(stationNumber);
		ObjectMapper mapper = new ObjectMapper();
		
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		if(showAll) {
			filterProvider.addFilter("PersonFilter", SimpleBeanPropertyFilter.serializeAllExcept(""));
		}else {
			filterProvider.addFilter("PersonFilter", new StationNumberPersonFilter());
		}
		mapper.setFilterProvider(filterProvider);
		String result = "";
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			String personDetails = mapper.writer(filterProvider).writeValueAsString(persons);
			
			String count = personService.countAdultChildren(persons);
			
			Map[] tempPerson = mapper.readValue(personDetails, Map[].class);
			Map<String,Object> tempCount = mapper.readValue(count, Map.class);
			
			map.put("personDetails", tempPerson);
			map.put("count", tempCount);
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot write to json",e);
		} catch (IOException e) {
			logger.error("error",e);
		}
		return result;
	}
	
	@GetMapping("/childAlert")
	public String getChildrenByAddress(@RequestParam String address, @RequestParam(required = false) boolean showAll) {
		String result = "";
		
		Map map = personService.getChildrenByAddressAndRelatives(address);
		
		ObjectMapper mapper = new ObjectMapper();
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		if(showAll) {
			filterProvider.addFilter("PersonFilter", SimpleBeanPropertyFilter.serializeAllExcept(""));
		}else {
			filterProvider.addFilter("PersonFilter", new ChildAlertFilter());
		}
		mapper.setFilterProvider(filterProvider);
		
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string",e);
		}
		
		return result;
	}
	
}
