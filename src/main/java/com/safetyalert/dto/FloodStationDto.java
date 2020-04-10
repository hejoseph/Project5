package com.safetyalert.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;

@JGlobalMap
public class FloodStationDto {
	private String lastName;
	private String phone;
	private int age;
	private MedicalDto medicalRecord;
	public String getLastName() {
		return lastName;
	}
	public String getPhone() {
		return phone;
	}
	public int getAge() {
		return age;
	}
	public MedicalDto getMedicalRecord() {
		return medicalRecord;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setMedicalRecord(MedicalDto medicalRecord) {
		this.medicalRecord = medicalRecord;
	}
	
}
