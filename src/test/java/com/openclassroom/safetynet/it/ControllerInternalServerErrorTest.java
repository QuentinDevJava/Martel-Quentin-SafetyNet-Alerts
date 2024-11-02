package com.openclassroom.safetynet.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.openclassroom.safetynet.constants.JsonFilePath;
import com.openclassroom.safetynet.model.FirestationRequest;
import com.openclassroom.safetynet.model.MedicalRecordResponse;
import com.openclassroom.safetynet.model.PersonResponse;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class ControllerInternalServerErrorTest {

	@Autowired
	private MockMvc mockMvc;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@BeforeAll
	static void setup() throws IOException {
		Files.copy(new File(JsonFilePath.JSONFILEPATH).toPath(), new File(JsonFilePath.JSONTESTFILEPATH).toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.delete(new File(JsonFilePath.JSONTESTFILEPATH).toPath());
		System.setProperty("test.mode", "true");
	}

	@Test
	void postFirestationErrorTest() throws Exception {
		FirestationRequest firestation = new FirestationRequest("1510 Culver St", 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(post("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isInternalServerError());

	}

	@Test
	void putFirestationErrorTest() throws Exception {
		FirestationRequest firestation = new FirestationRequest("1510 Culver St", 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(put("/firestation/112 Steppes Pl").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isInternalServerError());
	}

	@Test
	void deleteFirestationErrorTest() throws Exception {
		mockMvc.perform(delete("/firestation/1509 Culver St")).andExpect(status().isInternalServerError());
	}

	@Test
	void postMedicalRecordnErrorTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecordResponse medicalRecord = new MedicalRecordResponse("John", "Doe", "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(post("/medicalrecord").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isInternalServerError());
	}

	@Test
	void putMedicalRecordErrorTest() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecordResponse medicalRecord = new MedicalRecordResponse("Jacob", "Doe", "01/01/2014", medications, allergies);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(medicalRecord);
		mockMvc.perform(put("/medicalrecord/Jacob/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isInternalServerError());
	}

	@Test
	void deleteMedicalRecordErrorErrorTest() throws Exception {
		mockMvc.perform(delete("/medicalrecord/John/Boyd")).andExpect(status().isInternalServerError());
	}

	@Test
	void postPersonErrorTest() throws Exception {
		PersonResponse persons = new PersonResponse("Johny", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isInternalServerError());

	}

	@Test
	void putPersonErrorTest() throws Exception {
		PersonResponse persons = new PersonResponse("John", "Boyd", "1509 Culver St", "Paris", "97451", "841-874-6512", "jaboyd@email.com");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(persons);
		mockMvc.perform(put("/person/John/Boyd").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isInternalServerError());
	}

	@Test
	void deletePersonErrorErrorTest() throws Exception {
		mockMvc.perform(delete("/person/Tenley/Boyd")).andExpect(status().isInternalServerError());
	}

	@Test
	void getPersonsByStationNumberTest() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=1")).andExpect(status().isNotFound());
	}

	@Test
	void getAllChildTest() throws Exception {
		mockMvc.perform(get("/childAlert?address=1509 Culver St")).andExpect(status().isNotFound());
	}

	@Test
	void getPersonsPhoneNumbersByStationNumberTest() throws Exception {

		mockMvc.perform(get("/phoneAlert?firestation=1")).andExpect(status().isNotFound());
	}

	@Test
	void getListOfPersonsInfoAndStationNumberByAddressTest() throws Exception {
		mockMvc.perform(get("/fire?address=1509 Culver St")).andExpect(status().isNotFound());
	}

	@Test
	void getListOfPersonsInfoAndStationNumberByStationNumberTest() throws Exception {
		mockMvc.perform(get("/flood/stations?stations=3,4")).andExpect(status().isNotFound());
	}

	@Test
	void getPersonsFullInfoWithLastNameTest() throws Exception {
		mockMvc.perform(get("/personInfolastName?lastName=Boyd")).andExpect(status().isNotFound());
	}

	@Test
	void getMailByCityTest() throws Exception {
		mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isNotFound());
	}

}
