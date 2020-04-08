package com.safetyalert.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.safetyalert.model.FireStationDto;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.Person;
import com.safetyalert.service.IMedicalService;
import com.safetyalert.service.IPersonService;
import com.safetyalert.service.IStationService;
import com.safetyalert.util.Util;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonStationMedicalTest {
	private static final Logger logger = LogManager.getLogger("PersonStationMedicalTest");
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private IPersonService personService;
	
	@Autowired
	private IMedicalService medicalService;
	
	@Autowired
	private IStationService stationService;
	
	
	private FireStationDto createStation(String address, String station) throws Exception{
		System.out.println("creating ...");
		MvcResult result = this.mockMvc.perform(post("/firestation")
					.content(Util.asJsonString(new FireStationDto(null, address, station)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
		System.out.println("end post creating ...");
		String json = result.getResponse().getContentAsString();
		System.out.println("JSON="+json);
		return Util.parseJsonString(json, FireStationDto.class);
	}
	
	private FireStationDto updateStation(FireStationDto station) throws Exception {
		MvcResult result = this.mockMvc.perform(put("/firestation")
				.content(Util.asJsonString(station))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.address").exists())
				.andExpect(jsonPath("$.station").exists())
				.andReturn();
		String json = result.getResponse().getContentAsString();
		return Util.parseJsonString(json, FireStationDto.class);
	}
	
	private void deleteStation(FireStationDto station) throws Exception {
		this.mockMvc.perform(delete("/firestation")
				.content(Util.asJsonString(station))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
	}
	
	private MedicalRecord createMedical(MedicalRecord record) throws Exception {
		System.out.println("creating ...");
		MvcResult result = this.mockMvc.perform(post("/medicalRecord")
					.content(Util.asJsonString(record))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
		System.out.println("end post creating ...");
		String json = result.getResponse().getContentAsString();
		return Util.parseJsonString(json, MedicalRecord.class);
	}
	
	private MedicalRecord updateMedical(MedicalRecord record) throws Exception {
		MvcResult result = this.mockMvc.perform(put("/medicalRecord")
				.content(Util.asJsonString(record))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String json = result.getResponse().getContentAsString();
		return Util.parseJsonString(json, MedicalRecord.class);
	}
	
	private void deleteMedical(MedicalRecord record) throws Exception {
		this.mockMvc.perform(delete("/medicalRecord")
				.content(Util.asJsonString(record))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	private Person createPerson(Person person) throws Exception {
		System.out.println("creating ...");
		MvcResult result = this.mockMvc.perform(post("/person")
					.content(Util.asJsonString(person))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
		System.out.println("end post creating ...");
		String json = result.getResponse().getContentAsString();
		System.out.println("JSON="+json);
		return Util.parseJsonString(json, Person.class);
	}
	
	private Person updatePerson(Person person) throws Exception {
		MvcResult result = this.mockMvc.perform(put("/person")
				.content(Util.asJsonString(person))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.id").exists())
				.andReturn();
		String json = result.getResponse().getContentAsString();
		return Util.parseJsonString(json, Person.class);
	}
	
	private void deletePerson(Person person) throws Exception {
		this.mockMvc.perform(delete("/person")
				.content(Util.asJsonString(person))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	
	@Test
	public void integrationTest() throws Exception {
		String json = "{\"firstName\": \"Joseph\",\"lastName\": \"He\",\"address\": \"myNewAddress\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		Person personCreated = createPerson(person);
		Person found = personService.getPersonById(personCreated.getId());
		assertNull(found.getMedicalRecord());
		
		MedicalRecord record = new MedicalRecord(null, "Joseph", "He", "01/01/1994", 
				Arrays.asList("rien"), 
				Arrays.asList("rien"));
		MedicalRecord medicalCreated = createMedical(record);
		found = personService.getPersonById(personCreated.getId());
		assertNotNull(found.getMedicalRecord());
		
		FireStationDto stationCreated = createStation("myNewAddress", "station1");
		found = personService.getPersonById(personCreated.getId());
		assertNotNull(found.getFireStation());
	}
	
}
