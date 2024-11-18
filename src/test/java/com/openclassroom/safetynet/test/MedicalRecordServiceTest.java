package com.openclassroom.safetynet.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.repository.JsonRepository;
import com.openclassroom.safetynet.service.MedicalRecordService;

@SpringBootTest
class MedicalRecordServiceTest {

	@Autowired
	private MedicalRecordService medicalRecordService;

	@MockBean
	private JsonRepository repository;

	@Test
	void testGetMedicalRecordByFullName() {

		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecord("Jane", "Doe", "01/01/2000", medications, allergies));

		MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies);

		when(repository.loadTypeOfData(TypeOfData.MEDICALRECORDS)).thenReturn(Arrays.asList(medicalRecords.get(0), medicalRecords.get(1)));
		MedicalRecord testMedicalRecord = medicalRecordService.getMedicalRecordByFullName("John Doe");

		assertThat(testMedicalRecord).isEqualTo(medicalRecord);
	}

	@Test
	void testGetPersonMedicalRecords() {

		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<Person> persons = Arrays.asList(
				new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecord("Jane", "Doe", "01/01/2000", medications, allergies));

		when(repository.loadTypeOfData(TypeOfData.MEDICALRECORDS)).thenReturn(Arrays.asList(medicalRecords.get(0), medicalRecords.get(1)));
		List<MedicalRecord> testMedicalRecord = medicalRecordService.getPersonMedicalRecords(persons);

		assertThat(testMedicalRecord).isEqualTo(medicalRecords);

	}

	@Test
	void testGetPersonAge() {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");

		LocalDate birthDate = LocalDate.now().minusYears(34);
		String birthDateString = birthDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		List<MedicalRecord> medicalRecord = Arrays.asList(new MedicalRecord("John", "Boyd", birthDateString, medications, allergies));

		when(repository.loadTypeOfData(TypeOfData.MEDICALRECORDS)).thenReturn((Arrays.asList(medicalRecord.get(0))));

		int age = medicalRecord.get(0).getAge();

		assertThat(age).isEqualTo(34);
	}
}
