package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonInfo;

public class PersonCoveredByStationDTO {

	private final PersonService personService;
	private final List<PersonInfo> personsInfo;
	private final int adultCount;
	private final int childCount;
	private static final Predicate<Integer> IS_ADULT = age -> age > 18;
	private static final Predicate<Integer> IS_CHILD = age -> age <= 18;

	public PersonCoveredByStationDTO(List<Person> persons, PersonService personService) {
		this.personService = personService;
		this.personsInfo = new ArrayList<>();
		for (Person person : persons) {
			this.personsInfo.add(personService.extractNameAddressPhoneInfo(person));
		}
		this.adultCount = personService.CountsNumberOfChildrenAndAdults(persons, IS_ADULT);
		this.childCount = personService.CountsNumberOfChildrenAndAdults(persons, IS_CHILD);
	}

	@Override
	public String toString() {
		return "PersonsCoveredByFirestation [personsInfo=" + personsInfo + ", adultCount=" + adultCount + ", childCount=" + childCount + "]";
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
