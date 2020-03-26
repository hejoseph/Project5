package com.safetyalert.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.safetyalert.model.FireStation;

public interface StationRepository extends CrudRepository<FireStation, Long> {
	public static final String FIND_ONE_STATION_BY_ADDRESS = "select * from fire_stations where address like :address limit 1";

	@Query(value = FIND_ONE_STATION_BY_ADDRESS, nativeQuery = true)
	FireStation findOneByAddress(@Param("address") String address);

	FireStation findOneById(Long id);
	
}
