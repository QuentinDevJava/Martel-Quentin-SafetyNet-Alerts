package com.openclassroom.safetynet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
public class MedicalRecordServiceImplTest {

	@Autowired
	private MedicalRecordService medicalService;

	@MockBean
	private JsonRepository repository;

	@Test
	void testGetMedicalRecordByFullName() {

		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies), new MedicalRecord("Jane", "Doe", "01/01/2000", medications, allergies));

		MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies);

		when(repository.loadTypeOfData(TypeOfData.MEDICALRECORDS)).thenReturn(Arrays.asList(medicalRecords.get(0), medicalRecords.get(1)));
		MedicalRecord testMedicalRecord = medicalService.getMedicalRecordByFullName("John", "Doe");

		assertThat(testMedicalRecord).isEqualTo(medicalRecord);
	}

	@Test
	void testGetPersonMedicalRecords() {

		List<String> medications = Arrays.asList("aznol:350mg", "hydrapermazol:100mg");
		List<String> allergies = Arrays.asList("nillacilan");
		List<Person> persons = Arrays.asList(new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
				new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6513", "jdoe@email.com"));
		List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/2014", medications, allergies), new MedicalRecord("Jane", "Doe", "01/01/2000", medications, allergies));

		when(repository.loadTypeOfData(TypeOfData.MEDICALRECORDS)).thenReturn(Arrays.asList(medicalRecords.get(0), medicalRecords.get(1)));
		List<MedicalRecord> testMedicalRecord = medicalService.getPersonMedicalRecords(persons);

		assertThat(testMedicalRecord).isEqualTo(medicalRecords);

	}

}
