package com.openclassroom.safetynet.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.Child;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonFloodInfo;
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

	@Test
	void testGetPersonsByAddress() {
		// GIVEN
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1510 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"),
				new Person("Jack", "Din", "4345 Culver St", "Culver", "97451", "841-874-6515", "jdin@email.com"));

		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1), persons.get(2)));
		// WHEN
		List<Person> personsTest = personService.getPersonsByAddress("1509 Culver St");
		List<Person> personResult = new ArrayList<Person>();
		personResult.add(persons.get(0));

		// THEN
		assertThat(personsTest).isEqualTo(personResult);
	}

	@Test
	void testGetPhoneNumbersByStation() throws NoSuchElementException {
		int stationNumber = 1;
		List<Firestation> firestations = Arrays.asList(new Firestation("1509 Culver St", stationNumber));
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));

		when(firestationService.findFireStationByStationNumber(stationNumber)).thenReturn(firestations);
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));

		List<String> phoneNumbers = personService.getPhoneNumbersByStation(stationNumber);

		assertThat(phoneNumbers).containsExactlyInAnyOrder("841-874-6512", "841-874-6513");
	}

	@Test
	void testPersonCoveredByFirStation() { // OK
		int stationNumber = 1;
		List<Firestation> firestations = Arrays.asList(new Firestation("1509 Culver St", stationNumber));
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecord("Jane", "Doe", "01/01/2000", medications, allergies));

		when(firestationService.findFireStationByStationNumber(stationNumber)).thenReturn(firestations);
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));
		when(medicalRecordService.getPersonMedicalRecords(anyList())).thenReturn(medicalRecords);

		PersonCoveredByStation personCoveredByStationResult = new PersonCoveredByStation(persons, medicalRecords);
		PersonCoveredByStation personCoveredByStationTest = personService.personCoveredByStation(stationNumber);

		assertThat(personCoveredByStationResult).isEqualTo(personCoveredByStationTest);

	}

	@Test
	void testgetPersonsAndStationInfoByAddress() {

		int stationNumber = 1;
		String address = "1509 Culver St";

		Firestation firestations = new Firestation(address, stationNumber);
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<Person> persons = Arrays.asList(new Person("John", "Doe", address, "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", address, "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecord("Jane", "Doe", "01/01/2000", medications, allergies));

		List<MedicalRecordInfo> medicalRecordsInfo = Arrays.asList(
				new MedicalRecordInfo(persons.get(0).firstName(), persons.get(0).lastName(), persons.get(0).phone(), 10, medications, allergies),
				new MedicalRecordInfo(persons.get(1).firstName(), persons.get(1).lastName(), persons.get(1).phone(), 24, medications, allergies));

		when(firestationService.getFirestationByAddress(address)).thenReturn(firestations);
		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));
		when(medicalRecordService.getPersonAge(persons.get(0))).thenReturn(10);
		when(medicalRecordService.getPersonAge(persons.get(1))).thenReturn(24);
		when(medicalRecordService.getMedicalRecordByFullName(persons.get(0).fullName())).thenReturn(medicalRecords.get(0));
		when(medicalRecordService.getMedicalRecordByFullName(persons.get(1).fullName())).thenReturn(medicalRecords.get(1));
		when(medicalRecordService.getPersonMedicalRecords(anyList())).thenReturn(medicalRecords);

		PersonsAndStationInfo personsAndStationInfoResult = new PersonsAndStationInfo(medicalRecordsInfo, stationNumber);
		PersonsAndStationInfo personsAndStationInfoTest = personService.getPersonsAndStationInfoByAddress(address);

		assertThat(personsAndStationInfoResult).isEqualTo(personsAndStationInfoTest);

	}

	@Test
	void testPersonEmails() throws NoSuchElementException {
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"),
				new Person("Jack", "Din", "4345 Culver St", "Culver", "97451", "841-874-6515", "jdin@email.com"));
		List<String> emails = Arrays.asList("jaboyd@email.com", "jdoe@email.com", "jdin@email.com");
		PersonEmail personResult = new PersonEmail(emails);

		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1), persons.get(2)));

		PersonEmail personsTest = personService.personEmails("Culver");

		assertThat(personsTest).isEqualTo(personResult);
	}

	@Test
	void testgetchildsByAddress() throws NoSuchElementException {
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));

		List<Child> personResult = Arrays.asList(new Child("John", "Doe", "1509 Culver St", "841-874-6512", 10));

		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));
		when(medicalRecordService.getPersonAge(persons.get(0))).thenReturn(10);
		when(medicalRecordService.getPersonAge(persons.get(1))).thenReturn(24);

		List<Child> personsTest = personService.getchildsByAddress("1509 Culver St");

		assertThat(personResult).isEqualTo(personsTest);
	}

	@Test
	void testListOfPersonsByLastName() throws NoSuchElementException {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"),
				new Person("Jack", "Nanar", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecord("Jane", "Doe", "01/01/2000", medications, allergies));
		List<PersonsLastNameInfo> personsResult = Arrays.asList(
				new PersonsLastNameInfo("John", "Doe", "1509 Culver St", 10, "jaboyd@email.com", medications, allergies),
				new PersonsLastNameInfo("Jane", "Doe", "1509 Culver St", 24, "jdoe@email.com", medications, allergies));

		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1), persons.get(2)));
		when(medicalRecordService.getMedicalRecordByFullName("John Doe")).thenReturn(medicalRecords.get(0));
		when(medicalRecordService.getMedicalRecordByFullName("Jane Doe")).thenReturn(medicalRecords.get(1));
		when(medicalRecordService.getPersonAge(persons.get(0))).thenReturn(10);
		when(medicalRecordService.getPersonAge(persons.get(1))).thenReturn(24);

		List<PersonsLastNameInfo> personsTest = personService.listOfPersonsByLastName("Doe");

		assertThat(personsResult).isEqualTo(personsTest);

	}

	@Test
	void floodInfo() throws NoSuchElementException {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1650 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecordInfo> medicalRecordInfos = Arrays.asList(new MedicalRecordInfo("John", "Doe", "841-874-6512", 10, medications, allergies),
				new MedicalRecordInfo("Jane", "Doe", "841-874-6513", 24, medications, allergies));
		List<Firestation> firestations = Arrays.asList(new Firestation("1509 Culver St", 1), new Firestation("1650 Culver St", 2));
		List<MedicalRecord> medicalRecord = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecord("Jane", "Doe", "01/01/2000", medications, allergies));

		List<Integer> stationNumbers = Arrays.asList(1, 2);
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

		when(firestationService.getFirestationByListStationNumber(anyList())).thenReturn(firestations);

		when(repository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));

		when(medicalRecordService.getMedicalRecordByFullName("John Doe")).thenReturn(medicalRecord.get(0));
		when(medicalRecordService.getMedicalRecordByFullName("Jane Doe")).thenReturn(medicalRecord.get(1));

		when(medicalRecordService.getPersonAge(persons.get(0))).thenReturn(10);
		when(medicalRecordService.getPersonAge(persons.get(1))).thenReturn(24);

		PersonFloodInfo personFloodInfoTest = personService.floodInfo(stationNumbers);

		assertThat(personFloodInfoTest).isEqualTo(personFloodInfoResult);
	}

}
