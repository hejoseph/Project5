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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.dao.StationRepository;
import com.safetyalert.dto.ChildAlertDto;
import com.safetyalert.dto.CommunityEmailDto;
import com.safetyalert.dto.FireDto;
import com.safetyalert.dto.FloodStationDto;
import com.safetyalert.dto.GetChildAlertByAddress;
import com.safetyalert.dto.GetPersonByStationNumber;
import com.safetyalert.dto.PersonInfoDto;
import com.safetyalert.dto.StationNumberDto;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.Person;
import com.safetyalert.service.IPersonService;
import com.safetyalert.util.Util;

@RestController
public class SafetyAlertController {
	private static final Logger logger = LogManager.getLogger("SafetyAlertController");
	
	@Autowired
	private IPersonService personService;

	@GetMapping(path = "/firestation", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public GetPersonByStationNumber getPersonsByStationNumberFilteredProperties(@RequestParam String stationNumber) {
		
		logger.info("Request : \n /firestation?stationNumber="+stationNumber);
		
		List<Person> persons = personService.getPersonsCoveredByStation(stationNumber);
		
		List<StationNumberDto> dto = Util.copyListObject(persons, StationNumberDto.class);
		Map<String, String> count = personService.countAdultChildren(persons);
		GetPersonByStationNumber result = new GetPersonByStationNumber(dto, count);

		logger.info("Response : \n "+result);
		
		return result;
	}

	@GetMapping(path = "/childAlert", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public GetChildAlertByAddress getChildrenByAddress(@RequestParam String address) {
		logger.info("Request : \n /childAlert?address="+address);
		
		Map<String, List<Person>> map = personService.getChildrenByAddressAndRelatives(address);
		
		List<ChildAlertDto> children = Util.copyListObject(map.get("children"), ChildAlertDto.class);
		List<ChildAlertDto> adults = Util.copyListObject(map.get("adults"), ChildAlertDto.class);
		GetChildAlertByAddress result = new GetChildAlertByAddress(children, adults);

		logger.info("Response : \n "+result);
		
		return result;
	}

	@GetMapping(path = "/phoneAlert", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getPhonesByStation(@RequestParam String firestation,
			@RequestParam(required = false) boolean showAll) {
		logger.info("Request : \n /phoneAlert?firestation="+firestation);
		String result = "";
		Map map = personService.getPhonesByStation(firestation);
		ObjectMapper mapper = new ObjectMapper();
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}
		
		logger.info("Response : \n "+result);
		return result;
	}

	@GetMapping(path = "/fire", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getPersonsByAddress(@RequestParam String address) {
		logger.info("Request : \n /fire?address="+address);
		String result = "";
		List<Person> persons = personService.getPersonsByAddress(address);

		List<FireDto> copied = Util.copyListObject(persons, FireDto.class);
		
		ObjectMapper mapper = new ObjectMapper();

		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(copied);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}
		logger.info("Response : \n "+result);
		return result;
	}

	@GetMapping(path = "/flood/stations", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getPersonsByStationsGroupByAddress(@RequestParam String[] stations) {
		logger.info("Request : \n /flood/stations?stations="+stations);
		List<Person> persons = personService.getPersonsFromStations(stations);

		List<String> addresses = personService.getUniqueAddressesFromStations(stations);
		Map<String, Object> map = new HashMap<>();
		for (String address : addresses) {
			List<Person> temp = personService.getPersonsByAddress(address);
//			List<FloodStationDto> copied = Util.copyToFloodStation(temp);
			List<FloodStationDto> copied = Util.copyListObject(temp, FloodStationDto.class);
			map.put(address, copied);
		}

		ObjectMapper mapper = new ObjectMapper();

		String result = "";
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}
		logger.info("Response : \n "+result);
		return result;
	}

	@GetMapping(path = "/personInfo", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getPersonsByStationsGroupByAddress(@RequestParam String firstName, @RequestParam String lastName) {
		logger.info("Request : \n /personInfo?firstName="+firstName+"&lastName="+lastName);
		List<Person> persons = personService.getPersonsByLastName(lastName);

		List<PersonInfoDto> copied = Util.copyListObject(persons, PersonInfoDto.class);
		
		ObjectMapper mapper = new ObjectMapper();

		String result = "";
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(copied);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}
		logger.info("Response : \n "+result);
		return result;

	}

	@GetMapping(path = "/communityEmail", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getPersonsByStationsGroupByAddress(@RequestParam String city) {
		logger.info("Request : \n /communityEmail?city="+city);
		List<Person> persons = personService.getPersonsByCity(city);

		List<CommunityEmailDto> copied = Util.copyListObject(persons, CommunityEmailDto.class);
		
		ObjectMapper mapper = new ObjectMapper();

		String result = "";
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(copied);
		} catch (JsonProcessingException e) {
			logger.error("cannot write json to string", e);
		}
		logger.info("Response : \n "+result);
		return result;
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

}
