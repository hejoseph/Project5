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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.safetyalert.model.Person;
import com.safetyalert.service.IPersonService;
import com.safetyalert.util.Util;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
	private static final Logger logger = LogManager.getLogger("PersonControllerTest");
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private IPersonService personService;
	
	@Test
	public void testCreatePerson() throws Exception{
		String json = "{\"firstName\": \"Person1\",\"lastName\": \"Person1\",\"address\": \"1509 Culverss Sts\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		Person created = createPerson(person);
		Person found = personService.getPersonById(created.getId());
		assertNotNull(found);
	}
	
	private Person createPerson(Person person) throws Exception {
		logger.info("creating ...");
		MvcResult result = this.mockMvc.perform(post("/person")
					.content(Util.asJsonString(person))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
		logger.info("end post creating ...");
		String json = result.getResponse().getContentAsString();
		return Util.parseJsonString(json, Person.class);
	}
	
	@Test
	public void testCreateSamePerson() throws Exception{
		String json = "{\"firstName\": \"Person2\",\"lastName\": \"Person2\",\"address\": \"1509 Culverss Sts\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		createPerson(person);
		String response = this.mockMvc.perform(post("/person")
				.content(Util.asJsonString(person))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		assertTrue(response.startsWith("cannot create"));
	}
	
	
	@Test
	public void testUpdatePersonByFirstAndLastName() throws Exception{
		String json = "{\"firstName\": \"Person3\",\"lastName\": \"Person3\",\"address\": \"1509 Culverss Sts\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		Person created = createPerson(person);
		person.setCity("Hello");
		Person updated = updatePerson(person);
		Person found = personService.getPersonById(created.getId());
		assertNotNull(created);
		assertNotNull(updated);
		assertNotNull(found);
		assertEquals(created.getId(), updated.getId());
		assertEquals(person.getCity(), found.getCity());
	}
	
	@Test
	public void testUpdatePersonById() throws Exception{
		String json = "{\"firstName\": \"PersonId3\",\"lastName\": \"PersonId3\",\"address\": \"1509 Culverss Sts\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		Person created = createPerson(person);
		created.setCity("Hello");
		Person updated = updatePerson(created);
		Person found = personService.getPersonById(created.getId());
		assertNotNull(created);
		assertNotNull(updated);
		assertNotNull(found);
		assertEquals(found.getId(), updated.getId());
		assertNotEquals(person.getCity(), found.getCity());
		assertEquals(created.getCity(), found.getCity());
		assertNotEquals(person.getCity(), found.getCity());
	}
	
	@Test
	public void testUpdatePersonWithExistingName() throws Exception{
		String json = "{\"firstName\": \"PersonSameName\",\"lastName\": \"PersonId3\",\"address\": \"1509 Culverss Sts\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		Person created1 = createPerson(person);
		person.setFirstName("AnotherName");
		Person created2 = createPerson(person);
		created2.setFirstName(created1.getFirstName());
		Person updated = updatePerson(created2);
		assertNull(updated);
		Person found = personService.getPersonById(created2.getId());
		assertEquals(found.getFirstName(),person.getFirstName());
	}
	
	@Test
	public void testUpdatePersonWithNewName() throws Exception{
		String json = "{\"firstName\": \"PersonSameName1\",\"lastName\": \"PersonId3\",\"address\": \"1509 Culverss Sts\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		Person created1 = createPerson(person);
		created1.setFirstName("AnotherName2");
		Person updated = updatePerson(created1);
		assertNotNull(updated);
		Person found = personService.getPersonById(created1.getId());
		assertEquals(found.getFirstName(),created1.getFirstName());
	}
	
	private Person updatePerson(Person person) throws Exception {
		MvcResult result = this.mockMvc.perform(put("/person")
				.content(Util.asJsonString(person))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String json = result.getResponse().getContentAsString();
		return Util.parseJsonString(json, Person.class);
	}
	
	@Test
	public void testDeletePersonById() throws Exception{
		String json = "{\"firstName\": \"Person4\",\"lastName\": \"Person4\",\"address\": \"1509 Culverss Sts\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		Person created = createPerson(person);
		Person found = personService.getPersonById(created.getId());
		assertNotNull(found);
		deletePerson(created);
		found = personService.getPersonById(created.getId());
		assertNull(found);
	}
	
	@Test
	public void testDeletePersonByFirstAndLastName() throws Exception{
		String json = "{\"firstName\": \"DeletePerson\",\"lastName\": \"Person4\",\"address\": \"1509 Culverss Sts\",\"city\": \"Culverss\",\"zip\": \"97451\",\"phone\": \"841-874-6512\",\"email\": \"jaboyd@email.com\",\"age\": 36}";
		Person person = Util.parseJsonString(json, Person.class);
		Person created = createPerson(person);
		Person found = personService.getPersonById(created.getId());
		assertNotNull(found);
		deletePerson(person);
		found = personService.getPersonByFirstAndLastName(person.getFirstName(),person.getLastName());
		assertNull(found);
	}
	
	private void deletePerson(Person person) throws Exception {
		this.mockMvc.perform(delete("/person")
				.content(Util.asJsonString(person))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	
	
	
}
