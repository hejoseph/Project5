package com.safetyalert.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalert.exception.MedicalRecordAlreadyExists;
import com.safetyalert.exception.MedicalRecordNotFoundException;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.MedicalRecordDto;
import com.safetyalert.service.IMedicalService;

@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class MedicalController {

	private static final Logger logger = LogManager.getLogger("SafetyAlertController");

	@Autowired
	private IMedicalService medicalService;

	@PostMapping("/medicalRecord")
	public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordAlreadyExists{
		logger.info("Post Request : \n /medicalRecord\n"+medicalRecord);
		MedicalRecord result = medicalService.createMedicalRecord(medicalRecord);
		logger.info("Response : \n "+result);
		return result;
	}
	
	@PutMapping("/medicalRecord")
	public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException, MedicalRecordAlreadyExists {
		logger.info("Put Request : \n /medicalRecord\n"+medicalRecord);
		MedicalRecord result = medicalService.updateMedicalRecord(medicalRecord);
		logger.info("Response : \n "+result);
		return result;
	}
	
	@DeleteMapping("/medicalRecord")
	public String deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
		logger.info("Delete Request : \n /medicalRecord\n"+medicalRecord);
		MedicalRecordDto deleted = medicalService.deleteMedicalRecord(medicalRecord);
		String msg = "medical deleted : "+deleted.toStringDeleted();
		logger.info("Response : \n "+msg);
		return msg;
	}

}
