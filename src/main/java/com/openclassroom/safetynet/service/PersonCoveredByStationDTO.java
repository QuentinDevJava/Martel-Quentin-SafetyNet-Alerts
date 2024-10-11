package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonInfo;

public class PersonCoveredByStationDTO {

	private final List<PersonInfo> personsInfo = new ArrayList<>();
	private static final Predicate<Integer> IS_ADULT = age -> age > 18;
	private static final Predicate<Integer> IS_CHILD = age -> age <= 18;

	public PersonCoveredByStationDTO(List<Person> persons, List<MedicalRecord> medicalRecords) {
		// faire une verification ici pour etre sur qu'il sagit de la bonne personne
	}

	public List<PersonInfo> getPersonsInfo() {
		return personsInfo;
	}

}
