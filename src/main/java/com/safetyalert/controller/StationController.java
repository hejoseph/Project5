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
import com.safetyalert.exception.MedicalRecordAlreadyExists;
import com.safetyalert.exception.MedicalRecordNotFoundException;
import com.safetyalert.exception.StationAlreadyExists;
import com.safetyalert.exception.StationNotFoundException;
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
import com.safetyalert.service.StationService;

@RestController
public class StationController {

	private static final Logger logger = LogManager.getLogger("StationController");

	@Autowired
	private StationService stationService;

	@GetMapping("/station")
	public FireStation getStationById(@RequestParam Long id) {
		return stationService.getStationById(id);
	}

	@PostMapping("/firestation")
	public FireStation createStation(@RequestBody FireStation station) throws StationAlreadyExists{
		return stationService.createStation(station);
	}
	
	@PutMapping("/firestation")
	public FireStation updateStation(@RequestBody FireStation station) throws StationNotFoundException, StationAlreadyExists{
		return stationService.updateStation(station);
	}
	
	@DeleteMapping("/firestation")
	public String deleteStation(@RequestBody FireStation station) throws StationNotFoundException{
		FireStation deleted = stationService.deleteStation(station);
		String msg = "station deleted : "+deleted;
		logger.info(msg);
		return msg;
	}

}
