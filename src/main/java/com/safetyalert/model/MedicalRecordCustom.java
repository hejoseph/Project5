package com.safetyalert.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.safetyalert.model.id.PersonId;

@Entity
@Table(name="MedicalRecordCustom")
//@JsonFilter("MedicalRecordFilter")
public class MedicalRecordCustom{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
	private Long id;
	
	private String firstName;
	private String lastName;
	private String birthdate;
	@ElementCollection
	private List<String> medications;
	@ElementCollection
	private List<String> allergies;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "medicalRecord")
	private PersonCustom personCustom;
	
	public MedicalRecordCustom() {
	}
	
	public MedicalRecordCustom(String firstName, String lastName, String birthDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthDate;
	}
	public String getFirstName() {
		return firstName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	public PersonCustom getPersonCustom() {
		return personCustom;
	}

	public void setPersonCustom(PersonCustom personCustom) {
		this.personCustom = personCustom;
	}

	@Override
	public String toString() {
		return "MedicalRecordCustom [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthdate="
				+ birthdate + ", medications=" + medications + ", allergies=" + allergies + ", personCustom=" + "]";
	}
	
//	public Customer getCustomer() {
//		return customer;
//	}
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}
	
}
