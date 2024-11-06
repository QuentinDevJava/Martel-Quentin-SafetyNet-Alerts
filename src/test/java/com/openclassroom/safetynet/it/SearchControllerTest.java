package com.openclassroom.safetynet.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.jayway.jsonpath.JsonPath;
import com.openclassroom.safetynet.constants.JsonDataFilePath;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class SearchControllerTest {

	@BeforeAll
	static void setup() throws IOException {
		Files.copy(new File(JsonDataFilePath.JSONFILEPATH).toPath(), new File(JsonDataFilePath.JSONTESTFILEPATH).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		System.setProperty("test.mode", "true");
	}

	@Autowired
	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	@Test
	void getPersonsByStationNumberBadRequestTest() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=0")).andExpect(status().isBadRequest());
	}

	@Test
	void getAllChildBadRequestTest() throws Exception {
		mockMvc.perform(get("/childAlert?address=")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/childAlert?address= ")).andExpect(status().isBadRequest());
	}

	@Test
	void getPersonsPhoneNumbersByStationNumberBadRequestTest() throws Exception {
		mockMvc.perform(get("/phoneAlert?firestation=0")).andExpect(status().isBadRequest());
	}

	@Test
	void getListOfPersonsInfoAndStationNumberByAddressBadRequestTest() throws Exception {
		mockMvc.perform(get("/fire?address=")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/fire?address= ")).andExpect(status().isBadRequest());
	}

	@Test
	void getListOfPersonsInfoAndStationNumberByStationNumberBadRequestTest() throws Exception {
		mockMvc.perform(get("/flood/stations?stations=0,0")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/flood/stations?stations=-1,1")).andExpect(status().isBadRequest());
	}

	@Test
	void getPersonsFullInfoWithLastNameBadRequestTest() throws Exception {
		mockMvc.perform(get("/personInfolastName?lastName=")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/personInfolastName?lastName= ")).andExpect(status().isBadRequest());
	}

	@Test
	void getMailByCityBadRequestTest() throws Exception {
		mockMvc.perform(get("/communityEmail?city=")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/communityEmail?city= ")).andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@MethodSource("testCases")
	void searchControllerSuccessTest(String endpoint, String queryParam, String queryParamValue) throws Exception {
		mockMvc.perform(get(endpoint).param(queryParam, queryParamValue)).andExpect(status().isOk());
	}

	static Object[][] testCases() {
		return new Object[][] { { "/communityEmail", "city", "Culver" }, { "/personInfolastName", "lastName", "Boyd" },
				{ "/flood/stations", "stations", "3,4,5" }, { "/fire", "address", "1509 Culver St" }, { "/phoneAlert", "firestation", "1" },
				{ "/childAlert", "address", "1509 Culver St" }, { "/firestation", "stationNumber", "1" } };
	}

	@ParameterizedTest
	@MethodSource("notFoundTestCases")
	void searchControllerNotFoundTest(String endpoint, String queryParam, String queryParamValue, String expectedMessage) throws Exception {
		ResultActions resultActions = mockMvc.perform(get(endpoint).param(queryParam, queryParamValue)).andExpect(status().isNotFound());

		String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
		Object status = JsonPath.read(contentAsString, "$.status");
		Object message = JsonPath.read(contentAsString, "$.message");

		assertThat(status).isEqualTo(404);
		assertThat(message).isEqualTo(expectedMessage);
	}

	static Object[][] notFoundTestCases() {
		return new Object[][] { { "/communityEmail", "city", "NoFoundCity", "Unknown city: NoFoundCity" },
				{ "/personInfolastName", "lastName", "NoFound", "Unknown last name: NoFound" },
				{ "/flood/stations", "stations", "3000,4000,5000", "Unknown station number: [3000, 4000, 5000]" },
				{ "/fire", "address", "NoFound", "Unknown address: NoFound" },
				{ "/phoneAlert", "firestation", "1000", "Unknown station number: 1000" },
				{ "/childAlert", "address", "NoFound", "Unknown address: NoFound" },
				{ "/firestation", "stationNumber", "1000", "Unknown station number: 1000" } };
	}

}
