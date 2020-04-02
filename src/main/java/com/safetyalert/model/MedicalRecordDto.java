package com.safetyalert.model;

import java.util.List;

import com.googlecode.jmapper.annotations.JMap;

public class MedicalRecordDto{
	@JMap
	private Long id;
	@JMap
	private String firstName;
	@JMap
	private String lastName;
	@JMap
	private String birthdate;
	@JMap
	private List<String> medications;
	@JMap
	private List<String> allergies;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public List<String> getMedications() {
		return medications;
	}
	public List<String> getAllergies() {
		return allergies;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public void setMedications(List<String> medications) {
		this.medications = medications;
	}
	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}
//	public Person getPerson() {
//		return person;
//	}
//	public void setPerson(Person person) {
//		this.person = person;
//	}
	@Override
	public String toString() {
		return "MedicalRecord [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthdate="
				+ birthdate + ", medications="  + medications + ", allergies="  + allergies+"]";
	}
	
	public String toStringDeleted() {
		return "MedicalRecord [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthdate="
				+ birthdate + "]";
	}
	
}
