package com.safetyalert.dto;

import java.util.List;

import com.googlecode.jmapper.annotations.JMap;

public class MedicalDto{
	@JMap
	private List<String> medications;
	@JMap
	private List<String> allergies;
	
	public List<String> getMedications() {
		return medications;
	}
	public List<String> getAllergies() {
		return allergies;
	}
	public void setMedications(List<String> medications) {
		this.medications = medications;
	}
	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}
	
}
