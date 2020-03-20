package com.safetyalert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity
@Table(name="FireStationCustom")
//@JsonFilter("FireStationFilter")
public class FireStationCustom {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
	private Long id;
	private String address;
	private String station;
	
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
	@Override
	public String toString() {
		return "FireStation [address=" + address + ", station=" + station + "]";
	}
	
}
