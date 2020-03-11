package com.safetyalert.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.safetyalert.model.Person;

@Repository
public interface IPersonDAO {
	public List<Person> getPersons();
	public List<Person> getPersonsCoveredByStation(String number);
	public List<Person> getPersonsByAddress(String address);
}
