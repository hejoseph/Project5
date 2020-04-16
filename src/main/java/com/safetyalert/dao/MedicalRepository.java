package com.safetyalert.dao;

import org.springframework.data.repository.CrudRepository;

import com.safetyalert.model.MedicalRecord;

public interface MedicalRepository extends CrudRepository<MedicalRecord, Long>{
	MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);
	MedicalRecord findOneById(Long id);
}
