package com.openclassroom.safetynet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.Child;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.repository.JsonRepository;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonServiceImpl;

@SpringBootTest
public class PersonServiceImplTest {
	@Autowired
	private PersonServiceImpl personServiceImpl;

	@MockBean
	private FirestationService firestationService;

	@MockBean
	private MedicalRecordService medicalRecordService;

	@MockBean
	private JsonRepository dataRepository;

	@Test
	void testCalculateAgeCount() {
		// GIVEN
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		final Predicate<Integer> IS_ADULT = age -> age > 18;
		final Predicate<Integer> IS_CHILD = age -> age <= 18;
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		// WHEN
		when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(new MedicalRecord("John", "Doe", "01/01/1980", medications, allergies)); // Age > 18
		when(medicalRecordService.getMedicalRecordByFullName("Jane", "Doe")).thenReturn(new MedicalRecord("Jane", "Doe", "01/01/2014", medications, allergies)); // Age < 18

		// TODO
		// int countAdult = personServiceImpl.CountsNumberOfChildrenAndAdults(persons,
		// IS_ADULT);
		// int countChild = personServiceImpl.CountsNumberOfChildrenAndAdults(persons,
		// IS_CHILD);

		// THEN
		// assertThat(countAdult).isEqualTo(1);
		// assertThat(countChild).isEqualTo(1);
	}

	@Test
	void testGetPersonsByAddress() {
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"), new Person("Jack", "Din", "4345 Culver St", "Culver", "97451", "841-874-6515", "jdin@email.com"));

		when(dataRepository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1), persons.get(2)));

		List<Person> personsTest = personServiceImpl.getPersonsByAddress("1509 Culver St");
		List<Person> personResult = new ArrayList<Person>();
		personResult.add(persons.get(0));
		personResult.add(persons.get(1));

		assertThat(personsTest).isEqualTo(personResult);
	}

	@Test
	void testPersonEmails() {
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"), new Person("Jack", "Din", "4345 Culver St", "Culver", "97451", "841-874-6515", "jdin@email.com"));
		List<String> emails = Arrays.asList("jaboyd@email.com", "jdoe@email.com", "jdin@email.com");
		PersonEmail Result = new PersonEmail(emails);

		when(dataRepository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1), persons.get(2)));

		PersonEmail personsTest = personServiceImpl.personEmails("Culver");

		assertThat(personsTest).isEqualTo(Result);
	}

	@Test
	void testExtractNameAddressPhoneInfo() {
		Person persons = new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		PersonInfo personResult = new PersonInfo("John", "Doe", "1509 Culver St", "841-874-6512");

		PersonInfo personsTest = personServiceImpl.extractNameAddressPhoneInfo(persons);

		assertThat(personsTest).isEqualTo(personResult);
	}

	@Test
	void TestExtractNameAddressAgeEmailInfo() {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		MedicalRecord medicalrecord = new MedicalRecord("John", "Doe", "01/01/1980", medications, allergies);
		Person persons = new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		PersonsLastNameInfo ptest = new PersonsLastNameInfo("John", "Doe", "1509 Culver St", 44, "jaboyd@email.com", medications, allergies);

		when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(new MedicalRecord("John", "Doe", "01/01/1980", medications, allergies));

		PersonsLastNameInfo p = personServiceImpl.extractNameAddressAgeEmailInfo(persons, medicalrecord);

		assertThat(p).isEqualTo(ptest);
	}

	@Test
	void testExtractChildInfo() {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		Person persons = new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		Child personResult = new Child("John", "Doe", "1509 Culver St", "841-874-6512", 10);

		when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies));

		Child personsTest = personServiceImpl.extractChildInfo(persons);

		assertThat(personsTest).isEqualTo(personResult);
	}

	@Test
	void testGetPersonAge() {
		// Arrange
		Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");

		LocalDate birthDate = LocalDate.now().minusYears(34);
		String birthDateString = birthDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		MedicalRecord medicalRecord = new MedicalRecord("John", "Boyd", birthDateString, medications, allergies);

		when(medicalRecordService.getMedicalRecordByFullName("John", "Boyd")).thenReturn(medicalRecord);

		// Act
		int age = personServiceImpl.getPersonAge(person);

		// Assert
		assertThat(age).isEqualTo(34);
	}

	@Test
	void testGetPersonAgeNoMedicalRecord() {
		// Arrange
		Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");

		MedicalRecord medicalRecord = null;

		when(medicalRecordService.getMedicalRecordByFullName("John", "Boyd")).thenReturn(medicalRecord);

		// Act
		int age = personServiceImpl.getPersonAge(person);

		// Assert
		assertThat(age).isZero();
	}

	@Test
	void testGetPersonsByStationAddress() {
		List<Firestation> firestations = Arrays.asList(new Firestation("1509 Culver St", "1"), new Firestation("2", "1604 Culver St"));

		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));

		when(dataRepository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));

		List<Person> personsTests = personServiceImpl.getPersonsByStationAddress(firestations);

		assertThat(personsTests).isEqualTo(persons);
	}

	@Test
	void testGetPhoneNumbersByStation() {
		String stationNumber = "1";
		List<Firestation> firestations = Arrays.asList(new Firestation("1509 Culver St", stationNumber));
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));

		when(firestationService.findAllByStationNumber(stationNumber)).thenReturn(firestations);
		when(dataRepository.loadTypeOfData(TypeOfData.PERSONS)).thenReturn(Arrays.asList(persons.get(0), persons.get(1)));

		List<String> phoneNumbers = personServiceImpl.getPhoneNumbersByStation(stationNumber);

		assertThat(phoneNumbers).containsExactlyInAnyOrder("841-874-6512", "841-874-6513");

		verify(firestationService, times(1)).findAllByStationNumber(stationNumber);
		verify(dataRepository, times(1)).loadTypeOfData(TypeOfData.PERSONS);
	}

	@Disabled
	@Test
	void testGetPersonsByStation() {

	}
}
