package com.openclassroom.safetynet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonFloodInfo;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;
import com.openclassroom.safetynet.repository.JsonRepository;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonService;
import com.openclassroom.safetynet.service.SearchService;

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
	void testGetPersonsAndStationInfoByAddress() {

		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecordInfo> medicalRecordInfos = Arrays.asList(new MedicalRecordInfo("John", "Doe", "841-874-6512", 10, medications, allergies),
				new MedicalRecordInfo("Jane", "Doe", "841-874-6513", 24, medications, allergies));
		Firestation firestationResult = new Firestation("1509 Culver St", "1");

		when(personService.getPersonsByAddress(anyString())).thenReturn(persons);
		when(personService.getMedicalRecordInfosByListPersons(anyList())).thenReturn(medicalRecordInfos);
		when(firestationService.getFirestationByAddress(anyString())).thenReturn(firestationResult);

		PersonsAndStationInfo personsAndStationInfo = new PersonsAndStationInfo(medicalRecordInfos, firestationResult.station());
		PersonsAndStationInfo stationInfoTest = searchService.getPersonsAndStationInfoByAddress("1509 Culver St");

		assertThat(personsAndStationInfo).isEqualTo(stationInfoTest);
	}

	@Test
	void floodInfo() {
		// TODO a refactor
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1650 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecordInfo> medicalRecordInfos = Arrays.asList(new MedicalRecordInfo("John", "Doe", "841-874-6512", 10, medications, allergies),
				new MedicalRecordInfo("Jane", "Doe", "841-874-6513", 24, medications, allergies));
		List<Firestation> firestations = Arrays.asList(new Firestation("1509 Culver St", "1"), new Firestation("1650 Culver St", "2"));
		List<String> stationNumbers = Arrays.asList("1", "2");
		Map<String, List<MedicalRecordInfo>> mapResult = new HashMap<>();
		List<MedicalRecordInfo> medicalRecordInfos1 = new ArrayList<>();
		medicalRecordInfos1.add(medicalRecordInfos.get(0));
		List<MedicalRecordInfo> medicalRecordInfos2 = new ArrayList<>();
		medicalRecordInfos2.add(medicalRecordInfos.get(1));

		mapResult.put("1509 Culver St", medicalRecordInfos1);
		mapResult.put("1650 Culver St", medicalRecordInfos2);

		List<Person> persons1 = new ArrayList<>();
		persons1.add(persons.get(0));
		List<Person> persons2 = new ArrayList<>();
		persons2.add(persons.get(1));

		PersonFloodInfo personFloodInfoResult = new PersonFloodInfo(mapResult);
		when(personService.getPersonsByAddress("1509 Culver St")).thenReturn(persons1);
		when(personService.getPersonsByAddress("1650 Culver St")).thenReturn(persons2);
		when(firestationService.getFirestationByListStationNumber(anyList())).thenReturn(firestations);
		when(personService.getMedicalRecordInfosByPerson(persons.get(0))).thenReturn(medicalRecordInfos.get(0));
		when(personService.getMedicalRecordInfosByPerson(persons.get(1))).thenReturn(medicalRecordInfos.get(1));

		PersonFloodInfo personFloodInfoTest = searchService.floodInfo(stationNumbers);

		assertThat(personFloodInfoTest).isEqualTo(personFloodInfoResult);
	}

	@Disabled
	@Test
	void findCoveredPersonsByFireStation() {

		PersonCoveredByStation personCoveredByStationResult = searchService.findCoveredPersonsByFireStation(null);

	}

}
