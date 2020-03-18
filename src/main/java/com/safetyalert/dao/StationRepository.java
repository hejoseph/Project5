package com.safetyalert.dao;

import org.springframework.data.repository.CrudRepository;

import com.safetyalert.model.FireStation;

public interface StationRepository extends CrudRepository<FireStation, String>{
	
}
