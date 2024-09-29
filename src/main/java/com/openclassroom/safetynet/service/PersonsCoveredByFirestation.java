package com.openclassroom.safetynet.service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonInfo;

public class PersonsCoveredByFirestation {
	private final List<PersonInfo> personsInfo;
	private final int adultCount;
	private final int childCount;
	private static final Predicate<Integer> IS_ADULT = age -> age > 18;
	private static final Predicate<Integer> IS_CHILD = age -> age <= 18;

	public PersonsCoveredByFirestation(List<Person> persons, MedicalRecordService medicalRecordService) {
		this.personsInfo = persons.stream().map(PersonInfo::extractBasicInfo).collect(Collectors.toList());

		this.adultCount = calculateAgeCount(persons, medicalRecordService, IS_ADULT);
		this.childCount = calculateAgeCount(persons, medicalRecordService, IS_CHILD);
	}

	private int calculateAgeCount(List<Person> persons, MedicalRecordService medicalRecordService,
			Predicate<Integer> predicate) {
		return (int) persons.stream().map(person -> Person.getPersonAge(person, medicalRecordService)).filter(predicate)
				.count();
	}

	public List<PersonInfo> getPersonsInfo() {
		return personsInfo;
	}

	public int getAdultCount() {
		return adultCount;
	}

	public int getChildCount() {
		return childCount;
	}
}
