package com.safetyalert.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.safetyalert.model.MedicalRecordCustom;

@Repository
public interface MedicalRepositoryCustom extends CrudRepository<MedicalRecordCustom, Long>{
	MedicalRecordCustom findByFirstNameAndLastName(String firstName, String lastName);
}
