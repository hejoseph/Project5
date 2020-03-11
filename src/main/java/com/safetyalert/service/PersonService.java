package com.safetyalert.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyalert.dao.IPersonDAO;
import com.safetyalert.model.Person;

@Service
public class PersonService {
	
	private static final Logger logger = LogManager.getLogger("PersonService");

	private final IPersonDAO personDAO;

	@Autowired
	public PersonService(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public List<Person> getAllPeople() {
		return personDAO.getPersons();
	}

	public List<Person> getPersonsCoveredByStation(String stationNumber) {
		return personDAO.getPersonsCoveredByStation(stationNumber);
	}

	/**
	 * 
	 * @param birthDate
	 *            in format like 12/31/1994
	 * @return
	 */
	private static int calculteAge(String birthDate) {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate localDate = LocalDate.parse(birthDate, formatter);
		return Period.between(localDate, today).getYears();
	}

	public String countAdultChildren(List<Person> persons) {
		String result = "";
		int nbAdult = 0;
		int nbChildren = 0;

		for (Person person : persons) {
			if (calculteAge(person.getMedicalRecord().getBirthdate()) >= 18) {
				nbAdult++;
			} else {
				nbChildren++;
			}
		}

		Map<String, String> map = new HashMap<>();
		map.put("adult", "" + nbAdult);
		map.put("children", "" + nbChildren);

		ObjectMapper mapper = new ObjectMapper();
		try {
			result += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error("cannot map to json");
		}

		return result;
	}

}
