package com.safetyalert.model;

public class FireStationDto {
	private Long id;
	private String address;
	private String station;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public FireStationDto() {
		
	}
	
	public FireStationDto(Long id, String address, String station) {
		this.id = id;
		this.address = address;
		this.station = station;
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
		return "FireStation [id=" + id + ", address=" + address + ", station=" + station + "]";
	}
}
