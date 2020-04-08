package com.safetyalert.service;

import com.safetyalert.exception.MedicalRecordAlreadyExists;
import com.safetyalert.exception.MedicalRecordNotFoundException;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.MedicalRecordDto;

public interface IMedicalService {
	public MedicalRecord getMedicalById(Long id);
	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordAlreadyExists;
	public MedicalRecord updateMedicalRecord(MedicalRecord newRecord) throws MedicalRecordNotFoundException, MedicalRecordAlreadyExists;
	public MedicalRecordDto deleteMedicalRecord(MedicalRecord record) throws MedicalRecordNotFoundException;
}
