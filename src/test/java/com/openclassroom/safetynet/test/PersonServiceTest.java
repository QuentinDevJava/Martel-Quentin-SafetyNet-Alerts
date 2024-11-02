package com.openclassroom.safetynet.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.Child;
import com.openclassroom.safetynet.model.FirestationResponse;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.MedicalRecordResponse;
import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonFloodInfo;
import com.openclassroom.safetynet.model.PersonResponse;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.repository.JsonRepository;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonService;

@SpringBootTest
class PersonServiceTest {
	@Autowired
	private PersonService personService;

	@MockBean
	private FirestationService firestationService;

	@MockBean
	private MedicalRecordService medicalRecordService;

	@MockBean
	private JsonRepository repository;

	private String address;
	private int stationNumber;
	private List<FirestationResponse> firestationsN1;

	private PersonResponse johnDoe;
	private PersonResponse janeDoe;
	private PersonResponse jackDin;

	private List<String> medications;
	private List<String> allergies;

	private MedicalRecordResponse johnDoeMedicalRecordResponse;
	private MedicalRecordResponse janeDoeMedicalRecordResponse;
	private MedicalRecordResponse jackDinMedicalRecordResponse;

	@BeforeEach
	void setup() {

		stationNumber = 1;
		address = "1509 Culver St";
		firestationsN1 = Arrays.asList(new FirestationResponse(address, stationNumber));

		johnDoe = new PersonResponse("John", "Doe", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		janeDoe = new PersonResponse("Jane", "Doe", address, "Culver", "97451", "841-874-6513", "jdoe@email.com");
		jackDin = new PersonResponse("Jack", "Din", "4345 Culver St", "Culver", "97451", "841-874-6515", "jdin@email.com");

		medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		allergies = Arrays.asList("nillacilan");

		johnDoeMedicalRecordResponse = new MedicalRecordResponse("John", "Doe", "01/01/2014", medications, allergies);
		janeDoeMedicalRecordResponse = new MedicalRecordResponse("Jane", "Doe", "01/01/2000", medications, allergies);
		jackDinMedicalRecordResponse = new MedicalRecordResponse("Jack", "Din", "01/01/2000", medications, allergies);
	}

	@Test
	void shouldReturnPersonsMatchingGivenAddress() {
		// GIVEN
		List<PersonResponse> persons = Arrays.asList(johnDoe, janeDoe, jackDin);
		List<PersonResponse> expectedPersons = Arrays.asList(johnDoe, janeDoe);

		// WHEN
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1), persons.get(2)));

		// THEN
		List<PersonResponse> personsTest = personService.getPersonsByAddress(address);

		assertThat(expectedPersons).isEqualTo(personsTest);
	}

	@Test
	void shouldReturnPhoneNumbersForPersonsInGivenStation() {
		// GIVEN
		List<PersonResponse> persons = Arrays.asList(johnDoe, janeDoe);
		List<String> expectedPhoneNumbers = Arrays.asList("841-874-6512", "841-874-6513");

		// WHEN
		when(firestationService.findFireStationByStationNumber(stationNumber)).thenReturn(firestationsN1);
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));

		// THEN
		List<String> phoneNumbers = personService.getPhoneNumbersByStation(stationNumber);

		assertEquals(expectedPhoneNumbers.size(), phoneNumbers.size());
		for (int i = 0; i < expectedPhoneNumbers.size(); i++) {
			assertEquals(expectedPhoneNumbers.get(i), phoneNumbers.get(i));
		}
	}

	@Test
	void shouldReturnPersonsAndMedicalRecordsForGivenStationNumber() {
		// GIVEN
		List<PersonResponse> persons = Arrays.asList(johnDoe, janeDoe);
		List<MedicalRecordResponse> medicalRecords = Arrays.asList(johnDoeMedicalRecordResponse, janeDoeMedicalRecordResponse);
		PersonCoveredByStation expectedPersonCoveredByStationResult = new PersonCoveredByStation(persons, medicalRecords);

		// WHEN
		when(firestationService.findFireStationByStationNumber(stationNumber)).thenReturn(firestationsN1);
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));
		when(medicalRecordService.getPersonMedicalRecords(anyList())).thenReturn(medicalRecords);

		// THEN
		PersonCoveredByStation personCoveredByStationTest = personService.personCoveredByStation(stationNumber);

		assertThat(expectedPersonCoveredByStationResult).isEqualTo(personCoveredByStationTest);

	}

	@Test
	void shouldReturnPersonsAndStationInfoForGivenAddress() {
		// GIVEN
		List<PersonResponse> persons = Arrays.asList(johnDoe, janeDoe);
		List<MedicalRecordResponse> medicalRecords = Arrays.asList(johnDoeMedicalRecordResponse, janeDoeMedicalRecordResponse);
		List<MedicalRecordInfo> expectedMedicalRecordInfos = Arrays.asList(
				new MedicalRecordInfo(johnDoe.firstName(), johnDoe.lastName(), johnDoe.phone(), 10, medications, allergies),
				new MedicalRecordInfo(janeDoe.firstName(), janeDoe.lastName(), janeDoe.phone(), 24, medications, allergies));
		PersonsAndStationInfo expectedPersonsAndStationInfoResult = new PersonsAndStationInfo(expectedMedicalRecordInfos, stationNumber);

		// WHEN
		when(firestationService.getFirestationByAddress(address)).thenReturn(firestationsN1.get(0));
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));
		when(medicalRecordService.getAge(johnDoe)).thenReturn(10);
		when(medicalRecordService.getAge(janeDoe)).thenReturn(24);
		when(medicalRecordService.getMedicalRecordByFullName(johnDoe.fullName())).thenReturn(medicalRecords.get(0));
		when(medicalRecordService.getMedicalRecordByFullName(janeDoe.fullName())).thenReturn(medicalRecords.get(1));

		// THEN
		PersonsAndStationInfo personsAndStationInfoTest = personService.getPersonsAndStationInfoByAddress(address);

		assertThat(expectedPersonsAndStationInfoResult).isEqualTo(personsAndStationInfoTest);
	}

	@Test
	void shouldReturnListOfEmailsForPersonsInGivenCity() {
		// GIVEN
		List<PersonResponse> persons = Arrays.asList(johnDoe, janeDoe, jackDin);
		List<String> emails = Arrays.asList("jaboyd@email.com", "jdoe@email.com", "jdin@email.com");
		PersonEmail expectedPersonEmailResult = new PersonEmail(emails);
		// WHEN
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1), persons.get(2)));
		// THEN
		PersonEmail personEmailTest = personService.personEmails("Culver");

		assertThat(personEmailTest).isEqualTo(expectedPersonEmailResult);
	}

	@Test
	void shouldReturnListOfChildrenForGivenAddress() {
		// GIVEN
		List<PersonResponse> persons = Arrays.asList(johnDoe, janeDoe);
		List<Child> expectedChildrenResult = Arrays.asList(new Child("John", "Doe", "1509 Culver St", "841-874-6512", 10));

		// WHEN
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));
		when(medicalRecordService.getAge(johnDoe)).thenReturn(10);
		when(medicalRecordService.getAge(janeDoe)).thenReturn(24);
		when(medicalRecordService.getMedicalRecordByFullName(johnDoe.fullName())).thenReturn(johnDoeMedicalRecordResponse);
		when(medicalRecordService.getMedicalRecordByFullName(janeDoe.fullName())).thenReturn(janeDoeMedicalRecordResponse);

		// THEN
		List<Child> childrenTest = personService.getchildsByAddress("1509 Culver St");

		assertEquals(expectedChildrenResult.size(), childrenTest.size());
		for (int i = 0; i < expectedChildrenResult.size(); i++) {
			assertEquals(expectedChildrenResult.get(i), childrenTest.get(i));
		}
	}

	@Test
	void shouldReturnListOfPersonsWithMatchingLastName() {
		// GIVEN
		List<PersonResponse> persons = Arrays.asList(johnDoe, janeDoe, jackDin);
		List<PersonsLastNameInfo> expectedPersonsResult = Arrays.asList(
				new PersonsLastNameInfo("John", "Doe", "1509 Culver St", 10, "jaboyd@email.com", medications, allergies),
				new PersonsLastNameInfo("Jane", "Doe", "1509 Culver St", 24, "jdoe@email.com", medications, allergies));

		// WHEN
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1), persons.get(2)));
		when(medicalRecordService.getMedicalRecordByFullName(johnDoe.fullName())).thenReturn(johnDoeMedicalRecordResponse);
		when(medicalRecordService.getMedicalRecordByFullName(janeDoe.fullName())).thenReturn(janeDoeMedicalRecordResponse);
		when(medicalRecordService.getAge(johnDoe)).thenReturn(10);
		when(medicalRecordService.getAge(janeDoe)).thenReturn(24);

		// THEN
		List<PersonsLastNameInfo> personsTest = personService.listOfPersonsByLastName("Doe");

		assertEquals(expectedPersonsResult.size(), personsTest.size());
		for (int i = 0; i < expectedPersonsResult.size(); i++) {
			assertEquals(expectedPersonsResult.get(i), personsTest.get(i));
		}
	}

	@Test
	void floodInfoTest() {
		// GIVEN
		List<PersonResponse> persons = Arrays.asList(johnDoe, jackDin);

		MedicalRecordInfo johnDoeMedicalRecord = new MedicalRecordInfo("John", "Doe", "841-874-6512", 10, medications, allergies);
		MedicalRecordInfo jackDinMedicalRecord = new MedicalRecordInfo("Jack", "Din", "841-874-6515", 24, medications, allergies);

		List<FirestationResponse> firestationsResult = Arrays.asList(firestationsN1.get(0), new FirestationResponse("4345 Culver St", 2));

		List<Integer> stationNumbers = Arrays.asList(1, 2);

		Map<String, List<MedicalRecordInfo>> expectedMapResult = new HashMap<>();
		expectedMapResult.put("1509 Culver St", List.of(johnDoeMedicalRecord));
		expectedMapResult.put("4345 Culver St", List.of(jackDinMedicalRecord));
		PersonFloodInfo personFloodInfoResult = new PersonFloodInfo(expectedMapResult);

		// WHEN
		when(firestationService.getFirestationByListStationNumber(anyList())).thenReturn(firestationsResult);
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));
		when(medicalRecordService.getMedicalRecordByFullName(johnDoe.fullName())).thenReturn(johnDoeMedicalRecordResponse);
		when(medicalRecordService.getMedicalRecordByFullName(jackDin.fullName())).thenReturn(jackDinMedicalRecordResponse);
		when(medicalRecordService.getAge(johnDoe)).thenReturn(10);
		when(medicalRecordService.getAge(janeDoe)).thenReturn(24);

		// THEN
		PersonFloodInfo personFloodInfoTest = personService.floodInfo(stationNumbers);

		assertThat(personFloodInfoTest).isEqualTo(personFloodInfoResult);
	}

}
