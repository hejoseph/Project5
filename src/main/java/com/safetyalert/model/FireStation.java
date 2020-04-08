package com.safetyalert.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="FireStations")
public class FireStation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "address")
	private String address;
	@Column(name = "station")
	private String station;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fireStation")
	private List<Person> person;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAddress() {
		return address;
	}
	public String getStation() {
		return station;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public List<Person> getPerson() {
		return person;
	}
	public void setPerson(List<Person> person) {
		this.person = person;
	}
	@Override
	public String toString() {
		return "FireStation [id=" + id + ", address=" + address + ", station=" + station + "]";
	}
}
