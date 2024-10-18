package com.openclassroom.safetynet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassroom.safetynet.model.Person;

import lombok.RequiredArgsConstructor;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class MedicalRecordControllerTest {

	@Autowired
	private MockMvc mockMvc;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	@Order(1)
	void postPersonTest() throws Exception {
		Person persons = new Person("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isCreated());

	}

	@Test
	@Order(2)
	void postPersonErrorTest() throws Exception {
		Person persons = new Person("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", null);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isBadRequest());

	}

	@Test
	@Order(3)
	void putPersonTest() throws Exception {
		Person persons = new Person("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(put("/person/Johny/Doe").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@Order(4)
	void putPersonErrorTest() throws Exception {
		Person persons = new Person("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", null);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(put("/person/Johny/Doe").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(5)
	void deletePersonErrorTest() throws Exception {
		mockMvc.perform(delete("/person/Johny/Doe")).andDo(print()).andExpect(status().isNoContent());
	}

	@Test
	@Order(6)
	void deletePersonNoFoundTest() throws Exception {
		mockMvc.perform(delete("/person/Johny/nofound")).andDo(print()).andExpect(status().isNotFound());
	}

}
