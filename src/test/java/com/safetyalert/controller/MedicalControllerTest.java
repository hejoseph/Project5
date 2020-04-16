package com.safetyalert.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.safetyalert.model.MedicalRecord;
import com.safetyalert.service.IMedicalService;
import com.safetyalert.util.Util;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalControllerTest {
	private static final Logger logger = LogManager.getLogger("MedicalControllerTest");
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private IMedicalService medicalService;
	
	private MedicalRecord createMedical(MedicalRecord record) throws Exception {
		System.out.println("creating ...");
		MvcResult result = this.mockMvc.perform(post("/medicalRecord")
					.content(Util.asJsonString(record))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
//					.andExpect(jsonPath("$.id").exists())
//					.andExpect(jsonPath("$.firstName").exists())
//					.andExpect(jsonPath("$.lastName").exists())
//					.andExpect(jsonPath("$.birthdate").exists())
//					.andExpect(jsonPath("$.medications").exists())
//					.andExpect(jsonPath("$.allergies").exists())
					.andReturn();
		System.out.println("end post creating ...");
		String json = result.getResponse().getContentAsString();
		return Util.parseJsonString(json, MedicalRecord.class);
	}
	
	@Test
	public void testCreateMedical() throws Exception {
		MedicalRecord record = new MedicalRecord(null, "hello", "world", "31/01/1994", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		try {
			createMedical(record);
		}catch(Exception e) {
			logger.info("ERROR1",e);
		}
	}
	
	@Test
	public void testCreateMedicalForSamePerson() throws Exception {
		MedicalRecord record = new MedicalRecord(null, "hello2", "world2", "31/01/1994", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		createMedical(record);
		String response = this.mockMvc.perform(post("/medicalRecord")
				.content(Util.asJsonString(record))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
//				.andExpect(jsonPath("$.content", is("cannot ...")));
		assertTrue(response.startsWith("cannot create"));
	}
	
	private MedicalRecord updateMedical(MedicalRecord record) throws Exception {
		MvcResult result = this.mockMvc.perform(put("/medicalRecord")
				.content(Util.asJsonString(record))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.id").exists())
				.andReturn();
		String json = result.getResponse().getContentAsString();
		return Util.parseJsonString(json, MedicalRecord.class);
	}
	
	@Test
	public void testUpdateMedicalById() throws Exception {
		MedicalRecord record = new MedicalRecord(null, "hello3", "world3", "31/01/1994", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		MedicalRecord created = createMedical(record);
		created.setBirthdate("01/01/1995");
		MedicalRecord updated = updateMedical(created);
		assertEquals(created.getId(),updated.getId());
		assertEquals(created.getBirthdate(),updated.getBirthdate());
	}
	
	@Test
	public void testUpdateMedicalByIdWithExistingPersonName() throws Exception {
		MedicalRecord record = new MedicalRecord(null, "hello33", "world33", "31/01/1994", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		MedicalRecord created = createMedical(record);
		
		record.setFirstName("anothername");
		MedicalRecord created2 = createMedical(record);
		created2.setFirstName(created.getFirstName());
		
		String response = this.mockMvc.perform(put("/medicalRecord")
				.content(Util.asJsonString(created2))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse().getContentAsString();
		assertTrue(response.contains("cannot update medical"));
	}
	
	@Test
	public void testUpdateMedicalByIdWithNewPersonName() throws Exception {
		MedicalRecord record = new MedicalRecord(null, "hello334", "world33", "31/01/1994", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		MedicalRecord created = createMedical(record);
		created.setFirstName("anothername");
		MedicalRecord updated = updateMedical(created);
		MedicalRecord found = medicalService.getMedicalById(created.getId());
		assertNotEquals(found.getFirstName(),created.getFirstName());
	}
	
	@Test
	public void testUpdateMedicalByUnknownId() throws Exception {
		MedicalRecord record = new MedicalRecord(123432345L, "hello3", "world3", "31/01/1994", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		
		String response = this.mockMvc.perform(put("/medicalRecord")
				.content(Util.asJsonString(record))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse().getContentAsString();
		assertTrue(response.contains("not found"));
	}
	
	@Test
	public void testUpdateMedicalByFirstAndLastName() throws Exception {
		MedicalRecord record = new MedicalRecord(null, "hello4", "world4", "31/01/1994", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		MedicalRecord created = createMedical(record);
		
		MedicalRecord newRecord = new MedicalRecord(null, "hello4", "world4", "01/01/1995", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		MedicalRecord updated = updateMedical(newRecord);
		assertEquals(created.getId(),updated.getId());
		assertNotEquals(created.getBirthdate(),updated.getBirthdate());
	}
	
	private void deleteMedical(MedicalRecord record) throws Exception {
		this.mockMvc.perform(delete("/medicalRecord")
				.content(Util.asJsonString(record))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	@Test
	public void testDeleteMedicalById() throws Exception {
		MedicalRecord record = new MedicalRecord(null, "hello5", "world5", "31/01/1994", 
				Arrays.asList("medications"), 
				Arrays.asList("medications"));
		MedicalRecord created = createMedical(record);
		MedicalRecord found = medicalService.getMedicalById(created.getId());
		assertNotNull(found);
		assertNotNull(created.getId());
		deleteMedical(created);
		found = medicalService.getMedicalById(created.getId());
		assertNull(found);
	}
	
//	@Test
//	public void testDeleteMedicalByFirstAndLastName() {
//		
//	}
	
}
