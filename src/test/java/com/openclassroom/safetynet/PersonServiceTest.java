package com.openclassroom.safetynet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassroom.safetynet.repository.JsonRepository;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonService;

@SpringBootTest
public class PersonServiceTest {
	@Autowired
	private PersonService personService;
	@MockBean
	private FirestationService firestationService;

	@MockBean
	private JsonRepository repository;

	@MockBean
	private MedicalRecordService medicalRecordService;

}
