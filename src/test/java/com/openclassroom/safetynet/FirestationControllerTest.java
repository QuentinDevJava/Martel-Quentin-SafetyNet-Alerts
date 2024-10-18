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
import com.openclassroom.safetynet.model.Firestation;

import lombok.RequiredArgsConstructor;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class FirestationControllerTest {

	@Autowired
	private MockMvc mockMvc;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	@Order(1)
	void postFirestationTest() throws Exception {
		Firestation firestation = new Firestation("1509 Culver St", 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(post("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isCreated());

	}

	@Test
	@Order(2)
	void postFirestationErrorTest() throws Exception {
		Firestation firestation = new Firestation(null, 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(post("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isBadRequest());

	}

	@Test
	@Order(3)
	void putFirestationTest() throws Exception {
		Firestation firestation = new Firestation("1509 Culver St", 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(put("/firestation/1509 Culver St").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@Order(4)
	void putFirestationErrorTest() throws Exception {
		Firestation firestation = new Firestation(null, 10);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(firestation);
		mockMvc.perform(put("/firestation/1509 Culver St").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(5)
	void deleteFirestationErrorTest() throws Exception {
		mockMvc.perform(delete("/firestation/1509 Culver St")).andDo(print()).andExpect(status().isNoContent());
	}

	@Test
	@Order(6)
	void deleteFirestationNoFoundTest() throws Exception {
		mockMvc.perform(delete("/firestation/Nofound")).andDo(print()).andExpect(status().isNotFound());
	}

}
