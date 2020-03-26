package com.safetyalert.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.safetyalert.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long>{
	public static final String FIND_DISTINCT_ADDRESS_BY_STATION = "select distinct(p.address) from persons p, fire_station fs where p.address like fs.address and fs.station in (:stations)";
	public static final String FIND_BY_MEDICAL_ID = "select * from persons where persons.medical_id = :medicalId";
	public static final String FIND_BY_STATION_ID = "select * from persons where persons.station_id = :stationId";
//	public static final String FIND_UNIQUE_PERSON = "select * from persons p, medical_records m where p.firstName like :firstName and p.lastName like : lastName";

	List<Person> findByFireStation_Station(String station);
	List<Person> findByAddress(String address);
	Person findByFirstNameAndLastName(String firstName, String lastName);
	List<Person> findByFireStation_StationIn(String[] stations);
	List<Person> findByLastName(String lastName);
	List<Person> findByCity(String city);
	
	@Query(value = FIND_DISTINCT_ADDRESS_BY_STATION, nativeQuery = true)
	List<String> findDistinctAddressesByStations(@Param("stations") String[] stations);

	@Query(value = FIND_BY_MEDICAL_ID, nativeQuery = true)
	Person findByMedicalId(@Param("medicalId") String medicalId);
	
	@Query(value = FIND_BY_STATION_ID, nativeQuery = true)
	List<Person> findByStationId(@Param("stationId") String stationId);
}
