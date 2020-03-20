package com.safetyalert.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safetyalert.model.Person;
import com.safetyalert.model.PersonCustom;
import com.safetyalert.model.id.PersonId;

@Repository
public interface PersonRepositoryCustom extends CrudRepository<PersonCustom, Long>{
	public static final String FIND_DISTINCT_ADDRESS_BY_STATION = "select distinct(p.address) from person p, fire_station fs where p.address like fs.address and fs.station in (:stations)";

	List<PersonCustom> findByFireStation_Station(String station);
	List<PersonCustom> findByAddress(String address);
	PersonCustom findByFirstNameAndLastName(String firstName, String lastName);
	List<PersonCustom> findByFireStation_StationIn(String[] stations);
	List<PersonCustom> findByLastName(String lastName);
	List<PersonCustom> findByCity(String city);
	
	@Query(value = FIND_DISTINCT_ADDRESS_BY_STATION, nativeQuery = true)
	List<String> findDistinctAddressesByStations(@Param("stations") String[] stations);
}
