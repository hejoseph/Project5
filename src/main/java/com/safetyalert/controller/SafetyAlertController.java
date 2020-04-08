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
import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.dao.StationRepository;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.Person;
import com.safetyalert.service.IPersonService;

@RestController
public class SafetyAlertController {

	private static final Logger logger = LogManager.getLogger("SafetyAlertController");

	@Autowired
	private IPersonService personService;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private MedicalRepository medicalRepository;
	@Autowired
	private StationRepository stationRepository;

	@GetMapping("/medicalAll")
	public List<MedicalRecord> medical() {
		return (List<MedicalRecord>) medicalRepository.findAll();
	}

	@GetMapping("/stationAll")
	public List<FireStation> station() {
		List<FireStation> stations = (List<FireStation>) stationRepository.findAll();
		logger.info(stations.get(0).getPerson());
		return stations;
	}

	@GetMapping("/personAll")
	public List<Person> person() {
		return (List<Person>) personRepository.findAll();
	}

	@GetMapping("/all")
	public String all() {
		String links = ""
				+ "<li><a href='https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DAJava_P5/URLs.pdf'>Enoncé</a></li>"
				+ "<li><a href='https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DAJava_P5/Endpoints.pdf'>Enoncé2</a></li>"
				+ "<li><a href='http://localhost:8080/firestation?stationNumber=1'>FireStation</a></li>"
				+ "<li><a href='http://localhost:8080/childAlert?address=1509 Culver St'>ChildAlert</a></li>"
				+ "<li><a href='http://localhost:8080/phoneAlert?firestation=1'>PhoneAlert</a></li>"
				+ "<li><a href='http://localhost:8080/fire?address=1509 Culver St'>Fire</a></li>"
				+ "<li><a href='http://localhost:8080/flood/stations?stations=1,2'>FloodStations</a></li>"
				+ "<li><a href='http://localhost:8080/personInfo?firstName=John&lastName=Boyd'>PersonInfo</a></li>"
				+ "<li><a href='http://localhost:8080/communityEmail?city=Culver'>CommunityEmail</a></li>" + "";

		return links;
	}

	@GetMapping("/firestation")
	public String getPersonsByStationNumberFilteredProperties(@RequestParam String stationNumber,
			@RequestParam(required = false) boolean showAll) {
		List<Person> persons = personService.getPersonsCoveredByStation(stationNumber);
		ObjectMapper mapper = new ObjectMapper();

		String result = "";

		Map<String, Object> map = new HashMap<>();

		try {
			String personDetails = mapper.writer().writeValueAsString(persons);

			String count = personService.countAdultChildren(persons);

			Map[] tempPerson = mapper.readValue(personDetails, Map[].class);
			Map<String, Object> tempCount = mapper.readValue(count, Map.class);

			map.put("personDetails", tempPerson);
			map.put("count", tempCount);
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot write to json", e);
		} catch (IOException e) {
			logger.error("error", e);
		}
		return result;
	}

	@GetMapping("/childAlert")
	public String getChildrenByAddress(@RequestParam String address, @RequestParam(required = false) boolean showAll) {
		String result = "";

		Map map = personService.getChildrenByAddressAndRelatives(address);

		ObjectMapper mapper = new ObjectMapper();

		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}

		return result;
	}

	@GetMapping("/phoneAlert")
	public String getPhonesByStation(@RequestParam String firestation,
			@RequestParam(required = false) boolean showAll) {
		String result = "";
		Map map = personService.getPhonesByStation(firestation);
		ObjectMapper mapper = new ObjectMapper();
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}
		return result;
	}

	@GetMapping("/fire")
	public String getPersonsByAddress(@RequestParam String address, @RequestParam(required = false) boolean showAll) {
		logger.info("fire/");
		logger.debug("fire2/");

		String result = "";
		List<Person> persons = personService.getPersonsByAddress(address);

		ObjectMapper mapper = new ObjectMapper();


		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(persons);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}

		return result;
	}

	@GetMapping("/flood/stations")
	public String getPersonsByStationsGroupByAddress(@RequestParam String[] stations) {
		List<Person> persons = personService.getPersonsFromStations(stations);

		logger.info(stations.length);
		logger.info(persons.size());
		// List<String> addresses = personService.getUniqueAddressFromPersons(persons);
		List<String> addresses = personService.getUniqueAddressesFromStations(stations);
		Map<String, Object> map = new HashMap<>();
		for (String address : addresses) {
			// List<Person> temp = personService.retrievePersonFromAddress(persons,
			// address);
			List<Person> temp = personService.getPersonsByAddress(address);
			map.put(address, temp);
		}

		ObjectMapper mapper = new ObjectMapper();

		String result = "";
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}

		return result;
	}

	@GetMapping("/personInfo")
	public String getPersonsByStationsGroupByAddress(@RequestParam String firstName, @RequestParam String lastName) {
		List<Person> persons = personService.getPersonsByLastName(lastName);

		ObjectMapper mapper = new ObjectMapper();

		String result = "";
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(persons);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}

		return result;

	}

	@GetMapping("/communityEmail")
	public String getPersonsByStationsGroupByAddress(@RequestParam String city) {
		List<Person> persons = personService.getPersonsByCity(city);

		ObjectMapper mapper = new ObjectMapper();

		String result = "";
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(persons);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}

		return result;
	}

}
