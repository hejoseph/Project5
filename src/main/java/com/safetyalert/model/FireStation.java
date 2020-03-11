package com.safetyalert.model;

public class FireStation {
	private String address;
	private String station;
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
