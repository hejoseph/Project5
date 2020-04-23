package com.safetyalert.dto;

import java.util.List;
import java.util.Map;

public class GetPersonByStationNumber {
	private List<StationNumberDto> personDetails;
	private Map<String, String> count;

	public GetPersonByStationNumber(List<StationNumberDto> personDetails, Map<String, String> count) {
		super();
		this.personDetails = personDetails;
		this.count = count;
	}

	public List<StationNumberDto> getPersonDetails() {
		return personDetails;
	}

	public Map<String, String> getCount() {
		return count;
	}

	public void setPersonDetails(List<StationNumberDto> personDetails) {
		this.personDetails = personDetails;
	}

	public void setCount(Map<String, String> count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "GetPersonByStationNumber [personDetails=" + personDetails + ", count=" + count + "]";
	}

}
