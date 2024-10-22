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
import java.util.Arrays;
import java.util.List;

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
import com.openclassroom.safetynet.model.MedicalRecord;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class MedicalRecordControllerTest {
	@Autowired
	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@BeforeAll
	static void setup() throws IOException {
		Files.copy(new File(JsonPath.JSONFILEPATH).toPath(), new File(JsonPath.JSONTESTFILEPATH).toPath(), StandardCopyOption.REPLACE_EXISTING);
		System.setProperty("test.mode", "true");
	}

	@Test
	void postMedicalRecordnTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(post("/medicalrecord").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isCreated());
	}

	@Test
	void postMedicalRecordErrorTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecord medicalRecord = new MedicalRecord("John", null, "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(post("/medicalrecord").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	@Test
	void putMedicalRecordTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecord medicalRecord = new MedicalRecord("Jacob", "Doe", "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(put("/medicalrecord/Jacob/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());
	}

	@Test
	void putMedicalRecordErrorTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecord medicalRecord = new MedicalRecord("Jacob", null, "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(put("/medicalrecord/Jacob/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	@Test
	void deleteMedicalRecordErrorTest() throws Exception {
		mockMvc.perform(delete("/medicalrecord/John/Boyd")).andExpect(status().isNoContent());
	}

	@Test
	void deleteMedicalRecordNoFoundTest() throws Exception {
		mockMvc.perform(delete("/medicalrecord/John/nofound")).andExpect(status().isNotFound());
	}

}
