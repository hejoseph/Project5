package com.safetyalert.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.safetyalert.model.Person;

@Repository
public class PersonDAO implements IPersonDAO{

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

	@Override
	public List<Person> getPersonsCoveredByStation(String number) {
		List<Person> result = new ArrayList<Person>();
		for(Person person : this.persons) {
			if(person.getFireStation().getStation().equals(number)) {
				result.add(person);
			}
		}
		return result;
	}
	
}
