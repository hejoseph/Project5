package com.safetyalert.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.safetyalert.model.Person;

@Repository
public class PersonDAO {

	private List<Person> persons;
	
	public PersonDAO(List<Person> persons) {
		this.persons = persons;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
}
