package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;

public class PersonsInfoWithLastNameService {
	private MedicalRecordService medicalRecordService;
	private PersonService personService;

	public PersonsInfoWithLastNameService(PersonService personService, MedicalRecordService medicalRecordService) {
		this.personService = personService;
		this.medicalRecordService = medicalRecordService;
	}

	public List<PersonsLastNameInfo> listOfPersonsFullInfo(String lastName) {
		List<Person> persons = personService.allPersons();
		List<PersonsLastNameInfo> personsFullInfos = new ArrayList<>();
		for (Person person : persons) {
			if (person.lastName().equals(lastName)) {
				MedicalRecord medicalRecord = medicalRecordService.findPersonsMedicalRecords(lastName);
				personsFullInfos.add(personService.extractNameAddressAgeEmailInfo(person, medicalRecord));
			}
		}
		return personsFullInfos;

	}
}
