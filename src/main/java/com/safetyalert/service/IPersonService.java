package com.safetyalert.service;

import java.util.List;
import java.util.Map;

import com.safetyalert.exception.PersonAlreadyExists;
import com.safetyalert.model.Person;

public interface IPersonService {
	public List<Person> getAllPeople();
	public List<Person> getPersonsCoveredByStation(String stationNumber);
	public String countAdultChildren(List<Person> persons);
	public Map<String, List<Person>> getChildrenByAddressAndRelatives(String address);
	public Map<String, List<String>> getPhonesByStation(String stationNumber);
	public List<Person> getPersonsByAddress(String address);
	public List<Person> getPersonsFromStations(String[] stations);
	public List<String> getUniqueAddressFromPersons(List<Person> persons);
	public List<Person> retrievePersonFromAddress(List<Person> persons, String address);
	public List<Person> getPersonsByLastName(String lastName);
	public List<Person> getPersonsByCity(String city);
	public List<String> getUniqueAddressesFromStations(String[] stations);
	public Person getPersonById(Long id);
	public Person getPersonByFirstAndLastName(String firstName, String lastName);
	public Person createPerson(Person person) throws PersonAlreadyExists;
	public Person updatePerson(Person person);
	public Person deletePerson(Person person);
}
