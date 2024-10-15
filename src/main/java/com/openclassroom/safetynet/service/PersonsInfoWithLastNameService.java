package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonsInfoWithLastNameService {
	private final MedicalRecordService medicalRecordService;
	private final PersonService personService;

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
