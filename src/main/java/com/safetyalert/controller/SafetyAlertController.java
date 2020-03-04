package com.safetyalert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalert.service.PersonService;

@RestController
public class SafetyAlertController {
	
	private final PersonService personService;
	
	@Autowired
	public SafetyAlertController(PersonService personService) {
		this.personService = personService;
	}
	
	@GetMapping("/hello")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "ok";
	}
	
	@GetMapping("/firestation")
	public String getPersonsByStationNumber(@RequestParam String stationNumber) {
		return "ok "+stationNumber+ " "+personService.getAllPeople().size();
	}
	
}
