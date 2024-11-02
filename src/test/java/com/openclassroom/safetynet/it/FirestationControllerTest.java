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
import com.openclassroom.safetynet.constants.JsonFilePath;
import com.openclassroom.safetynet.model.FirestationRequest;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class FirestationControllerTest {

	@Autowired
	private MockMvc mockMvc;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	@BeforeAll
	static void setup() throws IOException {
		Files.copy(new File(JsonFilePath.JSONFILEPATH).toPath(), new File(JsonFilePath.JSONTESTFILEPATH).toPath(), StandardCopyOption.REPLACE_EXISTING);
		System.setProperty("test.mode", "true");
	}

	@Test
	void postFirestationTest() throws Exception {
		FirestationRequest firestation = new FirestationRequest("1510 Culver St", 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(post("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isCreated());

	}

	@Test
	void postFirestationErrorTest() throws Exception {
		FirestationRequest firestation = new FirestationRequest(null, 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(post("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	@Test
	void putFirestationTest() throws Exception {
		FirestationRequest firestation = new FirestationRequest("1510 Culver St", 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(put("/firestation/112 Steppes Pl").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());
	}

	@Test
	void putFirestationErrorTest() {
		FirestationRequest firestation = new FirestationRequest(null, 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson;
		try {
			requestJson = ow.writeValueAsString(firestation);

			mockMvc.perform(put("/firestation/112 Steppes Pl").contentType(APPLICATION_JSON_UTF8).content(requestJson))
					.andExpect(status().isBadRequest());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
