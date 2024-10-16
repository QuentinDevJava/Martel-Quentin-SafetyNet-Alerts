package com.openclassroom.safetynet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonFloodInfo;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;
import com.openclassroom.safetynet.repository.JsonRepository;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonService;
import com.openclassroom.safetynet.service.SearchService;

@Disabled
@SpringBootTest
public class SearchServiceImplTest {
	@Autowired
	private SearchService searchService;

	@MockBean
	private JsonRepository repository;
	@MockBean
	private PersonService personService;

	@MockBean
	private FirestationService firestationService;

	@MockBean
	private MedicalRecordService medicalRecordService;

	@Test
	void getPersonsAndStationInfoByAddress() {

		PersonsAndStationInfo stationInfoResult = searchService.getPersonsAndStationInfoByAddress(null);
	}

	@Test
	void floodInfo() {

		PersonFloodInfo personFloodInfoResult = searchService.floodInfo(null);
	}

	@Test
	void findCoveredPersonsByFireStation() {

		PersonCoveredByStation personCoveredByStationResult = searchService.findCoveredPersonsByFireStation(null);

	}

}
