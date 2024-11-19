package com.openclassroom.safetynet.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassroom.safetynet.constants.JsonDataFilePath;
import com.openclassroom.safetynet.model.Person;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private Person johnBoyd;
	private Person johnDoe;
	private String address;

	@BeforeAll
	static void setupBeforeAll() throws IOException {
		Files.copy(new File(JsonDataFilePath.JSONFILEPATH).toPath(), new File(JsonDataFilePath.JSONTESTFILEPATH).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
	}

	@BeforeEach
	void setup() {
		address = "1509 Culver St";
		johnDoe = new Person("John", "Doe", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		johnBoyd = new Person("John", "Boyd", address, "Paris", "97451", "841-874-6512", "jaboyd@email.com");

	}

	@Test
	void postPersonTest() throws Exception {

		JsonNode personNode = searchPersonInDataJson(johnDoe.firstName(), johnDoe.lastName());
		assertThat(personNode).isNull();

		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(johnDoe);
		this.mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isCreated());

		JsonNode johnDoeNode = searchPersonInDataJson(johnDoe.firstName(), johnDoe.lastName());

		String foundAddress = johnDoeNode.path("address").asText();
		String foundCity = johnDoeNode.path("city").asText();
		String foundZip = johnDoeNode.path("zip").asText();
		String foundPhone = johnDoeNode.path("phone").asText();
		String foundEmail = johnDoeNode.path("email").asText();

		assertThat(foundAddress).isEqualTo(johnDoe.address());
		assertThat(foundCity).isEqualTo(johnDoe.city());
		assertThat(foundZip).isEqualTo(johnDoe.zip());
		assertThat(foundPhone).isEqualTo(johnDoe.phone());
		assertThat(foundEmail).isEqualTo(johnDoe.email());
	}

	@ParameterizedTest
	@MethodSource("provideInvalidPersons")
	void postPersonErrorTest(Person person) throws Exception {
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(person);
		mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());

	}

	@Test
	void putPersonTest() throws Exception {

		JsonNode personNode = searchPersonInDataJson(johnBoyd.firstName(), johnBoyd.lastName());
		String foundCity = personNode.path("city").asText();
		assertThat(foundCity).isEqualTo("Culver");

		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(johnBoyd);

		mockMvc.perform(put("/person/John/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());

		personNode = searchPersonInDataJson(johnBoyd.firstName(), johnBoyd.lastName());
		foundCity = personNode.path("city").asText();
		assertThat(foundCity).isEqualTo("Paris");
	}

	@ParameterizedTest
	@MethodSource("provideInvalidPersons")
	void putPersonErrorTest(Person person) throws Exception {
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(person);
		mockMvc.perform(put("/person/John/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	static List<Person> provideInvalidPersons() {
		return Arrays.asList(new Person(null, "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("John", null, "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("John", "Boyd", null, "Paris", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", null, "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", null, "841-874-6512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", null, "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", null),
				new Person("John", "  ", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-65124", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-8744-6512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "8414-874-6512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "8418746512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-8746512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "841874-6512", "jaboyd@email.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboydemail.com"),
				new Person("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboydemailcom"));

	}

	@Test
	void putPersonNoFoundTest() throws Exception {
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(johnBoyd);
		mockMvc.perform(put("/person/John/NoFound").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isNotFound());
	}

	@Test
	void deletePersonTest() throws Exception {
		String firstName = "Tenley";
		String lastName = "Boyd";
		JsonNode personNode = searchPersonInDataJson(firstName, lastName);
		assertThat(personNode).isNotNull();

		mockMvc.perform(delete("/person/Tenley/Boyd")).andExpect(status().isNoContent());

		personNode = searchPersonInDataJson(firstName, lastName);
		assertThat(personNode).isNull();

	}

	@Test
	void deletePersonNoFoundTest() throws Exception {
		mockMvc.perform(delete("/person/Tenley/nofound")).andExpect(status().isNotFound());
	}

	private JsonNode searchPersonInDataJson(String firstName, String lastName) throws JsonProcessingException {
		StringBuilder jsonString = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(JsonDataFilePath.JSONTESTFILEPATH))) {
			String line;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonNode rootNode = mapper.readTree(jsonString.toString());

		JsonNode personNode = null;
		for (JsonNode node : rootNode.path("persons")) {
			if (node.path("firstName").asText().equals(firstName) && node.path("lastName").asText().equals(lastName)) {
				personNode = node;
				break;
			}
		}
		return personNode;
	}

}
