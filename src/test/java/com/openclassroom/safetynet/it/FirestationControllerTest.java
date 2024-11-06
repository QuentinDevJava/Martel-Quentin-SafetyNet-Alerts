package com.openclassroom.safetynet.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassroom.safetynet.constants.JsonDataFilePath;
import com.openclassroom.safetynet.model.FirestationDTO;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class FirestationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	@BeforeAll
	static void setup() throws IOException {
		Files.copy(new File(JsonDataFilePath.JSONFILEPATH).toPath(), new File(JsonDataFilePath.JSONTESTFILEPATH).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		System.setProperty("test.mode", "true");
	}

	@Test
	void postFirestationTest() throws Exception {
		FirestationDTO firestation = new FirestationDTO("1510 Culver St", 10);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(post("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isCreated());

	}

	@ParameterizedTest
	@MethodSource("provideInvalidFirestations")
	void postFirestationErrorTest(FirestationDTO firestation) throws Exception {
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(post("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	@Test
	void putFirestationTest() throws Exception {
		FirestationDTO firestation = new FirestationDTO("1510 Culver St", 10);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(put("/firestation/112 Steppes Pl").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());
	}

	@Test
	void putFirestationNoFoundTest() throws Exception {
		FirestationDTO firestation = new FirestationDTO("1510 Culver St", 10);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(put("/firestation/NoFound").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isNotFound());
	}

	@ParameterizedTest
	@MethodSource("provideInvalidFirestations")
	void putFirestationErrorTest(FirestationDTO firestation) throws Exception {
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(put("/firestation/112 Steppes Pl").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isBadRequest());
	}

	static List<FirestationDTO> provideInvalidFirestations() {
		return Arrays.asList(new FirestationDTO(null, 10), new FirestationDTO("112 Steppes Pl", 0), new FirestationDTO(" ", 10));

	}

	@Test
	void deleteFirestationTest() throws Exception {
		mockMvc.perform(delete("/firestation/1509 Culver St")).andExpect(status().isNoContent());
	}

	@Test
	void deleteFirestationStationNumberTest() throws Exception {
		mockMvc.perform(delete("/firestation/1")).andExpect(status().isNoContent());
	}

	@Test
	void deleteFirestationNoFoundTest() throws Exception {
		mockMvc.perform(delete("/firestation/Nofound")).andExpect(status().isNotFound());
	}
}
