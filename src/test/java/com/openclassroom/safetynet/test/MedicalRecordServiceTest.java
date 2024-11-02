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
import com.openclassroom.safetynet.model.MedicalRecordDTO;
import com.openclassroom.safetynet.model.PersonDTO;
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
		List<MedicalRecordDTO> medicalRecords = Arrays.asList(new MedicalRecordDTO("John", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecordDTO("Jane", "Doe", "01/01/2000", medications, allergies));

		MedicalRecordDTO medicalRecord = new MedicalRecordDTO("John", "Doe", "01/01/2014", medications, allergies);

		when(repository.loadTypeOfData(TypeOfData.MEDICALRECORDS)).thenReturn(Arrays.asList(medicalRecords.get(0), medicalRecords.get(1)));
		MedicalRecordDTO testMedicalRecord = medicalRecordService.getMedicalRecordByFullName("John Doe");

		assertThat(testMedicalRecord).isEqualTo(medicalRecord);
	}

	@Test
	void testGetPersonMedicalRecords() {

		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<PersonDTO> persons = Arrays.asList(
				new PersonDTO("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new PersonDTO("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecordDTO> medicalRecords = Arrays.asList(new MedicalRecordDTO("John", "Doe", "01/01/2014", medications, allergies),
				new MedicalRecordDTO("Jane", "Doe", "01/01/2000", medications, allergies));

		when(repository.loadTypeOfData(TypeOfData.MEDICALRECORDS)).thenReturn(Arrays.asList(medicalRecords.get(0), medicalRecords.get(1)));
		List<MedicalRecordDTO> testMedicalRecord = medicalRecordService.getPersonMedicalRecords(persons);

		assertThat(testMedicalRecord).isEqualTo(medicalRecords);

	}

	@Test
	void testGetPersonAge() {
		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");

		LocalDate birthDate = LocalDate.now().minusYears(34);
		String birthDateString = birthDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		List<MedicalRecordDTO> medicalRecord = Arrays.asList(new MedicalRecordDTO("John", "Boyd", birthDateString, medications, allergies));

		when(repository.loadTypeOfData(TypeOfData.MEDICALRECORDS)).thenReturn((Arrays.asList(medicalRecord.get(0))));

		int age = medicalRecord.get(0).getAge();

		assertThat(age).isEqualTo(34);
	}
}
