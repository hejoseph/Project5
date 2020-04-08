package com.safetyalert.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalert.exception.MedicalRecordAlreadyExists;
import com.safetyalert.exception.MedicalRecordNotFoundException;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.MedicalRecordDto;
import com.safetyalert.service.IMedicalService;

@RestController
public class MedicalController {

	private static final Logger logger = LogManager.getLogger("SafetyAlertController");

	@Autowired
	private IMedicalService medicalService;

	@PostMapping("/medicalRecord")
	public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordAlreadyExists{
		return medicalService.createMedicalRecord(medicalRecord);
	}
	
	@PutMapping("/medicalRecord")
	public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException, MedicalRecordAlreadyExists {
		return medicalService.updateMedicalRecord(medicalRecord);
	}
	
	@DeleteMapping("/medicalRecord")
	public String deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
		MedicalRecordDto deleted = medicalService.deleteMedicalRecord(medicalRecord);
		String msg = "medical deleted : "+deleted.toStringDeleted();
//		logger.info(dto);
		return msg;
	}

}
