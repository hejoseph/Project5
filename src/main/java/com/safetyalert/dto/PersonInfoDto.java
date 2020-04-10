package com.safetyalert.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;

@JGlobalMap
public class PersonInfoDto {
	private String lastName;
	private String address;
	private int age;
	private String email;
	private MedicalDto medicalRecord;
	public String getLastName() {
		return lastName;
	}
	public String getAddress() {
		return address;
	}
	public int getAge() {
		return age;
	}
	public String getEmail() {
		return email;
	}
	public MedicalDto getMedicalRecord() {
		return medicalRecord;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setMedicalRecord(MedicalDto medicalRecord) {
		this.medicalRecord = medicalRecord;
	}
	
	
}
