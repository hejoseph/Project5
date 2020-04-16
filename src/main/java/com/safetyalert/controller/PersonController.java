package com.safetyalert.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalert.exception.PersonAlreadyExists;
import com.safetyalert.model.Person;
import com.safetyalert.service.IPersonService;

@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class PersonController {

	private static final Logger logger = LogManager.getLogger("SafetyAlertController");

	@Autowired
	private IPersonService personService;


	@PostMapping(path = "/person", consumes = "application/json", produces = "application/json")
	public Person createStation(@RequestBody Person person) throws PersonAlreadyExists{
		logger.info("Post Request : \n /person\n"+person);
		Person result = personService.createPerson(person);
		logger.info("Response : \n "+result);	
		return result;
	}
	
	
	@PutMapping("/person")
	@ResponseBody
	public Person updateStation(@RequestBody Person person){
		logger.info("Put Request : \n /person\n"+person);
		Person result = personService.updatePerson(person);
		logger.info("Response : \n "+result);
		return result;
	}
	
	@DeleteMapping("/person")
	public String deleteStation(@RequestBody Person person){
		logger.info("Delete Request : \n /person\n"+person);
		Person deleted = personService.deletePerson(person);
		String msg = "person deleted : "+deleted;
		logger.info("Response : \n "+msg);
		return msg;
	}
	
	@DeleteMapping("/person/{prenom}/{nom}")
	public String deleteStation(@RequestBody Person person, @PathVariable("prenom") String prenom, @PathVariable("nom") String nom){
		logger.info("Delete Request : \n /person/"+prenom+"/"+nom+"\n"+person);
		Person deleted = personService.deletePerson(person);
		String msg = "person deleted : "+deleted;
		logger.info("Response : \n "+msg);
		return msg;
	}

	@DeleteMapping("/person/{id}")
	public String deleteStationById(@RequestBody Person person, @PathVariable("id") String id){
		logger.info("Delete Request : \n /person/"+id+"\n"+person);
		Person deleted = personService.deletePerson(person);
		String msg = "person deleted : "+deleted;
		logger.info("Response : \n "+msg);
		return msg;
	}
}
