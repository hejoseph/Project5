package com.safetyalert.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.safetyalert.model.MedicalRecord;

@Repository
public interface MedicalRepository extends CrudRepository<MedicalRecord, Long>{
	MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);
	MedicalRecord findOneById(Long id);
}
