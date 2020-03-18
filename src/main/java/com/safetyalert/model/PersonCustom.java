package com.safetyalert.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.safetyalert.model.id.PersonId;

//@JsonFilter("PersonFilter")
@Entity
@IdClass(PersonId.class)
public class PersonCustom {
	@Id
	private String firstName;
	@Id
	private String lastName;
	private String address;
	private String city;
	private String zip;
	private String phone;
	private String email;
	private int age;
	@OneToOne(cascade = CascadeType.ALL)
	private FireStation fireStation;
	@OneToOne(cascade = CascadeType.ALL)
	  @JoinColumns({
		  @JoinColumn(name = "firstName", referencedColumnName = "firstName"),
		  @JoinColumn(name = "lastName", referencedColumnName = "lastName")
	  })
	private MedicalRecord medicalRecord;
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getAddress() {
		return address;
	}
	public String getCity() {
		return city;
	}
	public String getZip() {
		return zip;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public FireStation getFireStation() {
		return fireStation;
	}
	public MedicalRecord getMedicalRecord() {
		return medicalRecord;
	}
	public void setFireStation(FireStation fireStation) {
		this.fireStation = fireStation;
	}
	public void setMedicalRecord(MedicalRecord medicalRecord) {
		this.medicalRecord = medicalRecord;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", city=" + city
				+ ", zip=" + zip + ", phone=" + phone + ", email=" + email + ", age=" + age + ", fireStation="
				/*+ fireStation*/ + ", medicalRecord=" + medicalRecord + "]";
	}
}
