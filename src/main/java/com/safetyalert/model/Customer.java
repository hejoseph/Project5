package com.safetyalert.model;


import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;

import com.safetyalert.model.id.PersonId;

@Entity
@IdClass(PersonId.class)
public class Customer implements Serializable{

//  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  @Id
  private String firstName;
  @Id
  private String lastName;
  
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumns({
	  @JoinColumn(name = "firstName", referencedColumnName = "firstName"),
	  @JoinColumn(name = "lastName", referencedColumnName = "lastName")
  })
  private MedicalRecord medicalRecord;

  protected Customer() {}

  public Customer(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  

@Override
public String toString() {
	return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
}

public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

public MedicalRecord getMedicalRecord() {
	return medicalRecord;
}

public void setMedicalRecord(MedicalRecord medicalRecord) {
	this.medicalRecord = medicalRecord;
}
  
  
  
}