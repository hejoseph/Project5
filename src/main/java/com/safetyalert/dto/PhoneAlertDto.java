package com.safetyalert.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;

@JGlobalMap
public class PhoneAlertDto {
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
