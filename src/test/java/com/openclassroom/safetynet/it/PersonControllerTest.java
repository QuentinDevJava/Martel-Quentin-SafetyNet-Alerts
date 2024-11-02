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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassroom.safetynet.constants.JsonFilePath;
import com.openclassroom.safetynet.model.PersonRequest;

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

	@BeforeAll
	static void setup() throws IOException {
		Files.copy(new File(JsonFilePath.JSONFILEPATH).toPath(), new File(JsonFilePath.JSONTESTFILEPATH).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		System.setProperty("test.mode", "true");
	}

	@Test
	void postPersonTest() throws Exception {
		PersonRequest person = new PersonRequest("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(person);

		this.mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isCreated());

		String firstName = "Johny";
		String lastName = "Doe";
		JsonNode personNode = searchInDataJson(firstName, lastName);

		String foundAddress = personNode.path("address").asText();
		String foundCity = personNode.path("city").asText();
		String foundZip = personNode.path("zip").asText();
		String foundPhone = personNode.path("phone").asText();
		String foundEmail = personNode.path("email").asText();

		assertThat(foundAddress).isEqualTo("1509 Culver St");
		assertThat(foundCity).isEqualTo("Culver");
		assertThat(foundZip).isEqualTo("97451");
		assertThat(foundPhone).isEqualTo("841-874-6512");
		assertThat(foundEmail).isEqualTo("jaboyd@email.com");
	}

	@Test
	void postPersonErrorTest() throws Exception {
		PersonRequest person = new PersonRequest("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", null);
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(person);
		mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());

	}

	@Test
	void putPersonTest() throws Exception {

		String firstName = "John";
		String lastName = "Boyd";
		JsonNode personNode = searchInDataJson(firstName, lastName);

		String foundCity = personNode.path("city").asText();
		assertThat(foundCity).isEqualTo("Culver");

		PersonRequest person = new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com");
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(person);

		mockMvc.perform(put("/person/John/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());

		personNode = searchInDataJson(firstName, lastName);
		foundCity = personNode.path("city").asText();
		assertThat(foundCity).isEqualTo("Paris");
	}

	@ParameterizedTest
	@MethodSource("provideInvalidPersons")
	void putPersonErrorTest(PersonRequest person) throws Exception {
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(person);
		mockMvc.perform(put("/person/John/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	static List<PersonRequest> provideInvalidPersons() {
		return Arrays.asList(new PersonRequest(null, "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com"),
				new PersonRequest("John", null, "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", null, "Paris", "97451", "841-874-6512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", null, "97451", "841-874-6512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", null, "841-874-6512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", null, "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", null),
				new PersonRequest("John", "  ", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-65124", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-8744-6512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "8414-874-6512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "8418746512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-8746512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841874-6512", "jaboyd@email.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboydemail.com"),
				new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboydemailcom"));

	}

	@Test
	void putPersonNoFoundTest() throws Exception {
		PersonRequest person = new PersonRequest("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com");
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(person);
		mockMvc.perform(put("/person/John/NoFound").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isNotFound());
	}

	@Test
	void deletePersonTest() throws Exception {
		String firstName = "Tenley";
		String lastName = "Boyd";
		JsonNode personNode = searchInDataJson(firstName, lastName);
		assertThat(personNode).isNotNull();

		mockMvc.perform(delete("/person/Tenley/Boyd")).andExpect(status().isNoContent());

		personNode = searchInDataJson(firstName, lastName);
		assertThat(personNode).isNull();

	}

	@Test
	void deletePersonNoFoundTest() throws Exception {
		mockMvc.perform(delete("/person/Tenley/nofound")).andExpect(status().isNotFound());
	}

	private JsonNode searchInDataJson(String firstName, String lastName) throws JsonProcessingException {
		StringBuilder jsonString = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(JsonFilePath.JSONTESTFILEPATH))) {
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
