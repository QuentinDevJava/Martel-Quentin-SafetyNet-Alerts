package com.openclassroom.safetynet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

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
import com.openclassroom.safetynet.model.MedicalRecord;

import lombok.RequiredArgsConstructor;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class MedicalRecordControllerTest {
	@Autowired
	private MockMvc mockMvc;

	private MedicalRecord medicalRecord;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	@Order(1)
	void postMedicalRecordnTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		medicalRecord = new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(post("/medicalrecord").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isCreated());
	}

	@Test
	@Order(2)
	void postMedicalRecordErrorTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		medicalRecord = new MedicalRecord("John", null, "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(post("/medicalrecord").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(3)
	void putMedicalRecordTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		medicalRecord = new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(put("/medicalrecord/John/Doe").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@Order(4)
	void putMedicalRecordErrorTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		medicalRecord = new MedicalRecord("John", null, "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(put("/medicalrecord/John/Doe").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(5)
	void deleteMedicalRecordErrorTest() throws Exception {
		mockMvc.perform(delete("/medicalrecord/John/Doe")).andDo(print()).andExpect(status().isNoContent());
	}

	@Test
	@Order(6)
	void deleteMedicalRecordNoFoundTest() throws Exception {
		mockMvc.perform(delete("/medicalrecord/John/nofound")).andDo(print()).andExpect(status().isNotFound());
	}

}
