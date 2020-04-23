package com.safetyalert.dto;

import java.util.List;

public class GetChildAlertByAddress {
	private List<ChildAlertDto> children;
	private List<ChildAlertDto> adults;
	
	public GetChildAlertByAddress(List<ChildAlertDto> children, List<ChildAlertDto> adults) {
		super();
		this.children = children;
		this.adults = adults;
	}
	
	public List<ChildAlertDto> getChildren() {
		return children;
	}
	public List<ChildAlertDto> getAdults() {
		return adults;
	}
	public void setChildren(List<ChildAlertDto> children) {
		this.children = children;
	}
	public void setAdults(List<ChildAlertDto> adults) {
		this.adults = adults;
	}
	@Override
	public String toString() {
		return "GetChildAlertByAddress [children=" + children + ", adults=" + adults + "]";
	}
}
