package com.safetyalert.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.safetyalert.model.FireStation;
import com.safetyalert.model.Person;
import com.safetyalert.model.PersonCustom;
import com.safetyalert.model.PersonCustom;
import com.safetyalert.model.id.PersonId;

public interface PersonRepository extends CrudRepository<Person, PersonId>{
	public static final String FIND_DISTINCT_ADDRESS_BY_STATION = "select distinct(p.address) from person p, fire_station fs where p.address like fs.address and fs.station in (:stations)";

	List<Person> findByFireStation_Station(String station);
	List<Person> findByAddress(String address);
	Person findByFirstNameAndLastName(String firstName, String lastName);
	List<Person> findByFireStation_StationIn(String[] stations);
	List<Person> findByLastName(String lastName);
	List<Person> findByCity(String city);
	
	@Query(value = FIND_DISTINCT_ADDRESS_BY_STATION, nativeQuery = true)
	List<String> findDistinctAddressesByStations(@Param("stations") String[] stations);
}
