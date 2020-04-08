package com.safetyalert.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.safetyalert.model.FireStation;
import com.safetyalert.model.FireStationDto;
import com.safetyalert.service.IStationService;
import com.safetyalert.service.StationServiceImpl;
import com.safetyalert.util.Util;

@SpringBootTest
@AutoConfigureMockMvc
// @TestPropertySource(
// locations = "classpath:application-integrationtest.properties")
public class StationControllerTest {
	
	private static final Logger logger = LogManager.getLogger("StationControllerTest");
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IStationService stationService;
	
	private String createStation(String address, String station) throws Exception{
		System.out.println("creating ...");
		MvcResult result = this.mockMvc.perform(post("/firestation")
					.content(Util.asJsonString(new FireStationDto(null, address, station)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
		System.out.println("end post creating ...");
		String json = result.getResponse().getContentAsString();
		System.out.println("JSON="+json);
		return json;
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
	
	@Test
	public void testCreateStation() throws Exception {
		String json = createStation("address12345", "station1");
		FireStationDto station = Util.parseJsonString(json, FireStationDto.class);
		Long id = station.getId();
		FireStation found = stationService.getStationById(id);
		assertNotNull(found);
	}
	
	@Test
	public void testCreateStationSameAddress() throws Exception{
		String address = "sameAddress";
		String station = "station1";
		createStation(address, station);
		//recreate same
		String response = this.mockMvc.perform(post("/firestation")
				.content(Util.asJsonString(new FireStationDto(null, address, station)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
//				.andExpect(jsonPath("$.content", is("cannot ...")));
		assertTrue(response.startsWith("cannot create"));
	}
	
	@Test
	public void testUpdateStationById() throws Exception {
		String json = createStation("new address1234","station123");
		FireStationDto created = Util.parseJsonString(json, FireStationDto.class);
		FireStationDto updated = updateStation(new FireStationDto(created.getId(),created.getAddress(),"new Station"));
		assertEquals(created.getId(), updated.getId());
		FireStation found = stationService.getStationById(updated.getId());
		assertEquals(created.getId(), found.getId());
		assertEquals(found.getAddress(), updated.getAddress());
		assertEquals(found.getStation(), updated.getStation());
	}
	
	@Test
	public void testUpdateStationByUnknownId() throws Exception {
		FireStationDto station = new FireStationDto(99999L,"","new Station");
		String response = this.mockMvc.perform(put("/firestation")
				.content(Util.asJsonString(station))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
//				.andExpect(jsonPath("$.content", is("cannot ...")));
		assertTrue(response.startsWith("id not found"));
	}
	
	@Test
	public void testUpdateStationByAddress() throws Exception {
		String json = createStation("address123", "station1");
		FireStationDto created = Util.parseJsonString(json, FireStationDto.class);
		FireStationDto updated = updateStation(new FireStationDto(null, "address123", "station2"));
		
		assertEquals(created.getId(),updated.getId());
		
		Long id = updated.getId();
		FireStation found = stationService.getStationById(id);
		assertNotNull(found);
		assertEquals(found.getStation(),updated.getStation());
		assertEquals(found.getId(),created.getId());
		assertNotEquals(found.getStation(),created.getStation());
	}
	
	@Test
	public void testUpdateStationWithExistingAddress() throws Exception {
		String json = createStation("abc abc", "station2");
		FireStationDto created1 = Util.parseJsonString(json, FireStationDto.class);
		
		String json2 = createStation("abc 123", "station1");
		FireStationDto created2 = Util.parseJsonString(json2, FireStationDto.class);
		created2.setAddress(created1.getAddress());
		
		String response = this.mockMvc.perform(put("/firestation")
				.content(Util.asJsonString(created2))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		logger.info("#abc : "+response);
		assertTrue(response.startsWith("cannot update new station"));
	}
	
	@Test
	public void testUpdateStationWithNewAddress() throws Exception {
		String json = createStation("abc abc", "station1");
		FireStationDto created = Util.parseJsonString(json, FireStationDto.class);
		
		created.setAddress("UPDATE WITH NEW ADDRESS");
		FireStationDto updated = updateStation(created);
		assertEquals(created.getId(), updated.getId());
		assertEquals(created.getAddress(), updated.getAddress());
	}
	
	@Test
	public void testDeleteStationById() throws Exception {
		String address = "deletemyaddress";
		String station = "deletemystation";
		String json = createStation(address, station);
		FireStationDto created = Util.parseJsonString(json, FireStationDto.class);
		
		this.mockMvc.perform(delete("/firestation")
				.content(Util.asJsonString(new FireStationDto(created.getId(), null, null)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		FireStation found = stationService.getStationById(created.getId());
		assertNull(found);
	}
	
	@Test
	public void testDeleteStationByAddress() throws Exception {
		String address = "deletemyaddress1";
		String station = "deletemystation1";
		String json = createStation(address, station);
		FireStationDto created = Util.parseJsonString(json, FireStationDto.class);
		
		this.mockMvc.perform(delete("/firestation")
				.content(Util.asJsonString(new FireStationDto(null, created.getAddress(), null)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		FireStation found = stationService.getStationById(created.getId());
		assertNull(found);
	}
	
	@Test
	public void testDeleteStationByUnknownAddress() throws Exception {
		String response = this.mockMvc.perform(delete("/firestation")
				.content(Util.asJsonString(new FireStationDto(null, "dfsdfsdf", null)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		assertTrue(response.startsWith("cannot delete, no station found"));
	}
	
	@Test
	public void testDeleteStationByUnknownId() throws Exception {
		String response = this.mockMvc.perform(delete("/firestation")
				.content(Util.asJsonString(new FireStationDto(1239213L, null, null)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		assertTrue(response.startsWith("cannot delete, no station found"));
	}

}
