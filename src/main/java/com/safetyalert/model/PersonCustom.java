package com.safetyalert.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.safetyalert.model.id.PersonId;

//@JsonFilter("PersonFilter")
@Table(name="PersonCustom")
@Entity
public class PersonCustom {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String zip;
	private String phone;
	private String email;
	private int age;
//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name="address",referencedColumnName="address", insertable=false, updatable=false)
//	private FireStationCustom fireStation;
//	@OneToOne(cascade = CascadeType.ALL)
//	  @JoinColumns({
//		  @JoinColumn(name = "firstName", referencedColumnName = "firstName", insertable=false, updatable=false),
//		  @JoinColumn(name = "lastName", referencedColumnName = "lastName", insertable=false, updatable=false)
//	  })
//	private MedicalRecordCustom medicalRecord;
	
	private Long stationId;
	private Long medicalId;
	
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
//	public FireStationCustom getFireStationCustom() {
//		return fireStation;
//	}
//	public MedicalRecordCustom getMedicalRecordCustom() {
//		return medicalRecord;
//	}
//	public void setFireStationCustom(FireStationCustom fireStation) {
//		this.fireStation = fireStation;
//	}
//	public void setMedicalRecordCustom(MedicalRecordCustom medicalRecord) {
//		this.medicalRecord = medicalRecord;
//	}
	
	public int getAge() {
		return age;
	}
	public Long getStationId() {
		return stationId;
	}
	public Long getMedicalId() {
		return medicalId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public void setMedicalId(Long medicalId) {
		this.medicalId = medicalId;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
