package com.safetyalert.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetyalert.dao.IPersonDAO;
import com.safetyalert.dao.PersonDAO;
import com.safetyalert.model.Person;

@Service
public class PersonService {

	private final IPersonDAO personDAO;
	
	@Autowired
	public PersonService(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}
	
	public List<Person> getAllPeople(){
		return personDAO.getPersons();
	}
	
	public List<Person> getPersonsCoveredByStation(String stationNumber) {
		return personDAO.getPersonsCoveredByStation(stationNumber);
	}
	
	
}
