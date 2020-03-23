package com.safetyalert.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safetyalert.model.FireStationCustom;

@Repository
public interface StationRepositoryCustom extends CrudRepository<FireStationCustom, Long>{
	public static final String FIND_ONE_STATION_BY_ADDRESS = "select * from fire_station_custom where address like :address limit 1";
	
	
	@Query(value = FIND_ONE_STATION_BY_ADDRESS, nativeQuery = true)
	FireStationCustom findOneByAddress(@Param("address") String address);
}
