package com.safetyalert.dao;

import org.springframework.data.repository.CrudRepository;

import com.safetyalert.model.FireStation;
import com.safetyalert.model.Person;
import com.safetyalert.model.PersonCustom;
import com.safetyalert.model.PersonCustom;
import com.safetyalert.model.id.PersonId;

public interface PersonRepository extends CrudRepository<Person, PersonId>{
	
}
