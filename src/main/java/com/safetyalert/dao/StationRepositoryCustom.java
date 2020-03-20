package com.safetyalert.dao;

import org.springframework.data.repository.CrudRepository;

import com.safetyalert.model.FireStationCustom;

public interface StationRepositoryCustom extends CrudRepository<FireStationCustom, Long>{
	
}
