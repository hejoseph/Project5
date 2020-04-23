package com.safetyalert.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;

@JGlobalMap
public class ChildAlertDto {
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getAddress() {
		return address;
	}
	public String getPhone() {
		return phone;
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
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
