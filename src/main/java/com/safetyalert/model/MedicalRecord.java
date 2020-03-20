package com.safetyalert.model;

import java.io.Serializable;
import java.util.List;

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
import com.safetyalert.model.id.PersonId;

@Entity
@Table(name="MedicalRecord")
@IdClass(PersonId.class)
//@JsonFilter("MedicalRecordFilter")
public class MedicalRecord implements Serializable{
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Id
	private String firstName;
	@Id
	private String lastName;
	private String birthdate;
	@ElementCollection
	private List<String> medications;
	@ElementCollection
	private List<String> allergies;
	
//	@OneToOne(mappedBy = "medicalRecord")
//	private Customer customer;
	
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
	
//	public Customer getCustomer() {
//		return customer;
//	}
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}
}
