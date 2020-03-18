package com.safetyalert.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.id.PersonId;

public interface MedicalRepository extends CrudRepository<MedicalRecord, PersonId>{
	MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);
}
