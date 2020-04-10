package com.safetyalert.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import com.safetyalert.dto.FloodStationDto;
import com.safetyalert.model.MedicalRecordDto;
import com.safetyalert.model.Person;

public class Util {
	
	private static final Logger logger = LogManager.getLogger("Util");
	
	public static <D,S> D copyObject(S from, Class<D> destClass) {
		JMapperAPI simpleApi = new JMapperAPI().add(JMapperAPI.mappedClass(destClass));
		JMapper simpleMapper = new JMapper(destClass, from.getClass(), simpleApi);
		return (D) simpleMapper.getDestination(from);
	}
	
	public static <D,S> List<D> copyListObject(List<S> fromList, Class<D> destClass) {
		if(fromList.size()==0) {
			return null;
		}
		try {
			JMapperAPI simpleApi = new JMapperAPI().add(JMapperAPI.mappedClass(destClass));
			JMapper simpleMapper = new JMapper(destClass, fromList.get(0).getClass(), simpleApi);
			List<D> result = new ArrayList<D>();
			for(S from : fromList) {
				result.add((D) simpleMapper.getDestination(from));
			}
			return result;
		}catch(Exception e) {
			logger.error("cannot copy list object",e);
		}
		return null;
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	    	logger.error("ERROR", e);
	    	return null;
	    }
	}
	
	public static <T> T parseJsonString(String json, Class<T> destClass){
		try {
	        return new ObjectMapper().readValue(json, destClass);
	    } catch (Exception e) {
	    	logger.error("ERROR", e);
	    	return null;
	    }
	}
	
	/**
	 * 
	 * @param birthDate
	 *            in format like 12/31/1994
	 * @return
	 */
	public static int calculteAge(String birthDate) {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate localDate = LocalDate.parse(birthDate, formatter);
		return Period.between(localDate, today).getYears();
	}

	public static List<FloodStationDto> copyToFloodStation(List<Person> temp) {
		List<FloodStationDto> result = new ArrayList<FloodStationDto>();
		for(Person person : temp) {
			FloodStationDto station = new FloodStationDto();
			station.setAge(person.getAge());
			station.setLastName(person.getLastName());
			com.safetyalert.dto.MedicalDto dto = Util.copyObject(person.getMedicalRecord(), com.safetyalert.dto.MedicalDto.class);
			station.setMedicalRecord(dto);
			station.setPhone(person.getPhone());
			result.add(station);
		}
		return result;
	}
	
}
