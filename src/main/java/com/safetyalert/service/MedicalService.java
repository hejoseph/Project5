package com.safetyalert.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.exception.MedicalRecordNotFoundException;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.id.PersonId;

@Service
public class MedicalService {
	private static final Logger logger = LogManager.getLogger("MedicalService");
	private final MedicalRepository medicalRepository;

	@Autowired
	public MedicalService(MedicalRepository medicalRepository) {
		this.medicalRepository = medicalRepository; 
	}
	
	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		return medicalRepository.save(medicalRecord);
	}
	
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
		String firstName = medicalRecord.getFirstName();
		String lastName = medicalRecord.getFirstName();
		PersonId id = new PersonId(firstName, lastName);
		return medicalRepository.findById(id).map(record->{
			record.setBirthdate(medicalRecord.getBirthdate());
			record.setAllergies(medicalRecord.getAllergies());
			record.setMedications(medicalRecord.getMedications());
			return medicalRepository.save(record);
		}).orElseThrow(() ->{
			logger.info("id="+id+" not found");
			//todo throw exception
			return new MedicalRecordNotFoundException("id="+id+" not found");
		});
	}

	public MedicalRecord deleteMedicalRecord(PersonId id) {
		MedicalRecord record = medicalRepository.findByFirstNameAndLastName(id.getFirstName(), id.getLastName());
		medicalRepository.deleteById(id);
		return record;
	}
	
}
