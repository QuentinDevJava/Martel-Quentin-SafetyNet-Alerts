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
import com.openclassroom.safetynet.model.MedicalRecord;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class MedicalRecordControllerTest {
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
	}

	@Test
	void postMedicalRecordnTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(post("/medicalrecord").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isCreated());
	}

	@ParameterizedTest
	@MethodSource("provideInvalidMedicalRecord")
	void postMedicalRecordErrorTest(MedicalRecord medicalRecord) throws Exception {
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(post("/medicalrecord").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	@Test
	void putMedicalRecordTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecord medicalRecord = new MedicalRecord("Jacob", "Doe", "01/01/2014", medications, allergies);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(put("/medicalrecord/Jacob/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());
	}

	@Test
	void putMedicalRecordNoFoundTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecord medicalRecord = new MedicalRecord("Jacob", "Doe", "01/01/2014", medications, allergies);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(put("/medicalrecord/Jacob/NoFound").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isNotFound());
	}

	@ParameterizedTest
	@MethodSource("provideInvalidMedicalRecord")
	void putMedicalRecordErrorTest(MedicalRecord medicalRecord) throws Exception {
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(put("/medicalrecord/Jacob/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isBadRequest());
	}

	static List<MedicalRecord> provideInvalidMedicalRecord() {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		return Arrays.asList(new MedicalRecord(null, "Doe", "01/01/2014", medications, allergies),
				new MedicalRecord("Jacob", null, "01/01/2014", medications, allergies),
				new MedicalRecord("Jacob", "Doe", null, medications, allergies),
				new MedicalRecord("Jacob", "Doe", "01/01/2014", null, allergies),
				new MedicalRecord("Jacob", "Doe", "01/01/2014", medications, null),
				new MedicalRecord(" ", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecord("Jacob", "", "01/01/2014", medications, allergies),
				new MedicalRecord("Jacob", "Doe", " ", medications, allergies));
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
