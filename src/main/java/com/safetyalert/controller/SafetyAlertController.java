package com.safetyalert.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.dao.StationRepository;
import com.safetyalert.exception.MedicalRecordNotFoundException;
import com.safetyalert.jsonfilter.ChildAlertFilter;
import com.safetyalert.jsonfilter.CommunityEmailFilter;
import com.safetyalert.jsonfilter.FirePersonFilter;
import com.safetyalert.jsonfilter.FireStationFilter;
import com.safetyalert.jsonfilter.FloodStationsPersonFilter;
import com.safetyalert.jsonfilter.MedicalRecordFilter;
import com.safetyalert.jsonfilter.PersonInfoFilter;
import com.safetyalert.jsonfilter.StationNumberPersonFilter;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.Person;
import com.safetyalert.model.id.PersonId;
import com.safetyalert.service.MedicalService;
import com.safetyalert.service.PersonService;

@RestController
public class SafetyAlertController {

	private static final Logger logger = LogManager.getLogger("SafetyAlertController");

	private final PersonService personService;
	private final MedicalService medicalService;
	private final PersonRepository personRepository;
	private final MedicalRepository medicalRepository;
	private final StationRepository stationRepository;

	@Autowired
	public SafetyAlertController(PersonService personService, MedicalService medicalService,  
			PersonRepository personRepository, MedicalRepository medicalRepository, StationRepository stationRepository) {
		this.personService = personService;
		this.medicalService = medicalService;
		this.personRepository = personRepository;
		this.medicalRepository = medicalRepository;
		this.stationRepository = stationRepository;
	}

	@GetMapping("/medical")
	public List<MedicalRecord> medical() {
		return (List<MedicalRecord>) medicalRepository.findAll();
	}

	@GetMapping("/station")
	public List<FireStation> station() {
		return (List<FireStation>) stationRepository.findAll();
	}

	@GetMapping("/person")
	public List<Person> person() {
		return (List<Person>) personRepository.findAll();
	}

	@GetMapping("/person2")
	public List<Person> personByStation(@RequestParam String station) {
		return (List<Person>) personRepository.findByFireStation_Station(station);
	}

	@GetMapping("/person3")
	public List<Person> personByStations(@RequestParam String[] stations) {
		return (List<Person>) personRepository.findByFireStation_StationIn(stations);
	}

	@GetMapping("/address")
	public List<String> address(@RequestParam String[] stations) {
		return personRepository.findDistinctAddressesByStations(stations);
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

		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		if (showAll) {
			filterProvider.addFilter("PersonFilter", SimpleBeanPropertyFilter.serializeAllExcept(""));
		} else {
			filterProvider.addFilter("PersonFilter", new StationNumberPersonFilter());
		}
		mapper.setFilterProvider(filterProvider);
		String result = "";

		Map<String, Object> map = new HashMap<>();

		try {
			String personDetails = mapper.writer(filterProvider).writeValueAsString(persons);

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
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		if (showAll) {
			filterProvider.addFilter("PersonFilter", SimpleBeanPropertyFilter.serializeAllExcept(""));
		} else {
			filterProvider.addFilter("PersonFilter", new ChildAlertFilter());
		}
		mapper.setFilterProvider(filterProvider);

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

		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		if (showAll) {
			filterProvider.addFilter("PersonFilter", SimpleBeanPropertyFilter.serializeAllExcept(""));
		} else {
			filterProvider.addFilter("PersonFilter", new FirePersonFilter());
			filterProvider.addFilter("MedicalRecordFilter", new MedicalRecordFilter());
			filterProvider.addFilter("FireStationFilter", new FireStationFilter());
		}
		mapper.setFilterProvider(filterProvider);

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
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		filterProvider.addFilter("PersonFilter", new FloodStationsPersonFilter());
		filterProvider.addFilter("MedicalRecordFilter", new MedicalRecordFilter());
		mapper.setFilterProvider(filterProvider);

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
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		filterProvider.addFilter("PersonFilter", new PersonInfoFilter());
		filterProvider.addFilter("MedicalRecordFilter", new MedicalRecordFilter());
		mapper.setFilterProvider(filterProvider);

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
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		filterProvider.addFilter("PersonFilter", new CommunityEmailFilter());
		mapper.setFilterProvider(filterProvider);

		String result = "";
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(persons);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}

		return result;
	}

	@PostMapping("/medicalRecord")
	public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		return medicalService.createMedicalRecord(medicalRecord);
	}
	
	@PutMapping("/medicalRecord")
	public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
		return medicalService.updateMedicalRecord(medicalRecord);
	}
	
	@DeleteMapping("/medicalRecord")
	public String deleteMedicalRecord(@RequestBody PersonId id) throws MedicalRecordNotFoundException {
		medicalService.deleteMedicalRecord(id);
		String msg = "person deleted : "+id;
		logger.info(msg);
		return msg;
	}

}
