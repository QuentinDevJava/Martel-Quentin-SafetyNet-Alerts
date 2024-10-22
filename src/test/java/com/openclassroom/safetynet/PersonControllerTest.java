package com.openclassroom.safetynet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassroom.safetynet.constants.JsonPath;
import com.openclassroom.safetynet.model.Person;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@BeforeAll
	static void setup() throws IOException {
		Files.copy(new File(JsonPath.JSONFILEPATH).toPath(), new File(JsonPath.JSONTESTFILEPATH).toPath(), StandardCopyOption.REPLACE_EXISTING);
		System.setProperty("test.mode", "true");
	}

	@Test
	void postPersonTest() throws Exception {
		Person persons = new Person("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isCreated());

	}

	@Test
	void postPersonErrorTest() throws Exception {
		Person persons = new Person("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", null);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());

	}

	@Test
	void putPersonTest() throws Exception {
		Person persons = new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(put("/person/John/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());
	}

	@Test
	void putPersonErrorTest() throws Exception {
		Person persons = new Person("Johny", "Doe", "1509 Culver St", "Paris", "97451", "841-874-6512", null);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(put("/person/Johny/Doe").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	@Test
	void deletePersonErrorTest() throws Exception {
		mockMvc.perform(delete("/person/Tenley/Boyd")).andExpect(status().isNoContent());
	}

	@Test
	void deletePersonNoFoundTest() throws Exception {
		mockMvc.perform(delete("/person/Tenley/nofound")).andExpect(status().isNotFound());
	}

}
