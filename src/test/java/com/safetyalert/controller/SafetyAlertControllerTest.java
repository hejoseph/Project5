package com.safetyalert.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SafetyAlertControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testFireStationEndpoint() throws Exception {
		this.mockMvc.perform(get("/firestation?stationNumber=1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.personDetails").exists())
			.andExpect(jsonPath("$.personDetails", hasSize(6)))
			.andExpect(jsonPath("$.personDetails[0].firstName").exists())
			.andExpect(jsonPath("$.personDetails[0].lastName").exists())
			.andExpect(jsonPath("$.personDetails[0].address").exists())
			.andExpect(jsonPath("$.personDetails[0].phone").exists())
			.andExpect(jsonPath("$.count.child").exists())
			.andExpect(jsonPath("$.count.child").value("1"))
			.andExpect(jsonPath("$.count.adult").exists())
			.andExpect(jsonPath("$.count.adult").value("5"));
	}
	
	@Test
	public void testChildAlertEndpoint() throws Exception {
		this.mockMvc.perform(get("/childAlert?address=1509 Culver St"))
		.andDo(print())
		.andExpect(status().isOk())
//		.andExpect(jsonPath("$.children", hasSize(3)))
//		.andExpect(jsonPath("$.children[2].address").value("1509 Culver St"))
		.andExpect(jsonPath("$.adults", hasSize(3)));
//		.andExpect(jsonPath("$.adults[2].address").value("1509 Culver St"));
	}
	
	@Test
	public void testPhoneAlertEndpoint() throws Exception {
		this.mockMvc.perform(get("/phoneAlert?firestation=1"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.phones[*]", containsInAnyOrder( "841-874-6512", "841-874-8547", "841-874-7462", "841-874-7784")));
	}
	
	@Test
	public void testFireEndpoint() throws Exception {
		this.mockMvc.perform(get("/fire?address=1509 Culver St"))
		.andDo(print())
		.andExpect(status().isOk());
//		.andExpect(jsonPath("$", hasSize(6)));
//		.andExpect(jsonPath("$[0].fireStation.address").value("1509 Culver St"));
	}
	
	@Test
	public void testFloodStationsEndpoint() throws Exception {
		this.mockMvc.perform(get("/flood/stations?stations=1,2"))
		.andDo(print())
		.andExpect(status().isOk());
//		.andExpect(jsonPath("$['908 73rd St'][0].fireStation.station").value("1"))
//		.andExpect(jsonPath("$['951 LoneTree Rd'][0].fireStation.station").value("2"));
	}
	
	@Test
	public void testPersonInfoEndpoint() throws Exception {
		this.mockMvc.perform(get("/personInfo?firstName=John&lastName=Boyd"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(6)))
//		.andExpect(jsonPath("$[0].firstName").value("John"))
		.andExpect(jsonPath("$[0].lastName").value("Boyd"))
//		.andExpect(jsonPath("$[1].firstName").value("Jacob"))
		.andExpect(jsonPath("$[1].lastName").value("Boyd"));
	}
	
	@Test
	public void testCommunityEmailEndpoint() throws Exception {
		this.mockMvc.perform(get("/communityEmail?city=Culver"))
		.andDo(print())
		.andExpect(status().isOk());
//		.andExpect(jsonPath("$", hasSize(24)));
//		.andExpect(jsonPath("$[0].city").value("Culver"));
	}

}
