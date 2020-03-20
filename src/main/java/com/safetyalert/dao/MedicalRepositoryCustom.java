package com.safetyalert.dao;

import org.springframework.data.repository.CrudRepository;

import com.safetyalert.model.MedicalRecordCustom;

public interface MedicalRepositoryCustom extends CrudRepository<MedicalRecordCustom, Long>{
	MedicalRecordCustom findByFirstNameAndLastName(String firstName, String lastName);
}
