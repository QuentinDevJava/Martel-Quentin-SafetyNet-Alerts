package com.openclassroom.safetynet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class SearchControllerTest {

	@Autowired
	private MockMvc mockMvc;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	void getPersonsByStationNumberTest() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=1")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void getAllChildTest() throws Exception {
		mockMvc.perform(get("/childAlert?address=1509 Culver St")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void getPersonsPhoneNumbersByStationNumberTest() throws Exception {

		mockMvc.perform(get("/phoneAlert?firestation=1")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void getListOfPersonsInfoAndStationNumberByAddressTest() throws Exception {
		mockMvc.perform(get("/fire?address=1509 Culver St")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void getListOfPersonsInfoAndStationNumberByStationNumberTest() throws Exception {
		mockMvc.perform(get("/flood/stations?stations=3,4")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void getPersonsFullInfoWithLastNameTest() throws Exception {
		mockMvc.perform(get("/personInfolastName?lastName=Boyd")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void getMailByCityTest() throws Exception {
		mockMvc.perform(get("/communityEmail?city=Culver")).andDo(print()).andExpect(status().isOk());
	}

}
