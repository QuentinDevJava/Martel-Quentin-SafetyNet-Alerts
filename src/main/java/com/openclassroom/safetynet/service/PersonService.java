package com.openclassroom.safetynet.service;

import java.util.List;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.Person;

public interface PersonService {

	Person createPerson(Person person);

	Person updatePerson(String firstName, String lastName, Person person);

	boolean deletePerson(String firstName, String lastName);

	List<Person> getAllPersons();

	List<Person> getPeopleByStationAddress(List<Firestation> firestation);
}
