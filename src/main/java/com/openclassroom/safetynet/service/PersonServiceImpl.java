package com.openclassroom.safetynet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.PersonNotFoundException;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.repository.DataRepository;

@Service
public class PersonServiceImpl implements PersonService {

	private final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
	private final DataRepository dataRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public PersonServiceImpl(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

	public List<Person> getAllPersons() {
		List<Object> personData = dataRepository.SelectTypeOfData(TypeOfData.persons);
		List<Person> persons = new ArrayList<>();
		for (Object personObj : personData) {
			persons.add(objectMapper.convertValue(personObj, Person.class));
		}
		return persons;
	}

	public Person createPerson(Person person) {
		for (String field : new String[] { person.firstName(), person.firstName(), person.address(), person.city(),
				person.zip(), person.phone(), person.email() }) {
			if (Optional.ofNullable(field).map(StringUtils::isBlank).orElse(true)) {
				throw new IllegalArgumentException("All fields of the person must be filled.");
			}
		}
		List<Person> persons = getAllPersons();
		persons.add(person);
		savePersons(persons);
		return person;
	}

	public Person updatePerson(String firstName, String lastName, Person person) {
		List<Person> persons = getAllPersons();
		for (int i = 0; i < persons.size(); i++) {
			if (persons.get(i).firstName().equals(firstName) && persons.get(i).lastName().equals(lastName)) {
				persons.set(i, person);
				savePersons(persons);
				return person;
			}
		}
		throw new PersonNotFoundException(
				"Person with first name '" + firstName + "' and last name '" + lastName + "' not found.");
	}

	public boolean deletePerson(String firstName, String lastName) {
		List<Person> persons = getAllPersons();
		boolean personDeleted = persons.removeIf(p -> p.firstName().equals(firstName) && p.lastName().equals(lastName));
		if (personDeleted) {
			savePersons(persons);
			return true;
		} else {
			throw new PersonNotFoundException(
					"Person with first name '" + firstName + "' and last name '" + lastName + "' not found.");
		}
	}

	private void savePersons(List<Person> listOfPersons) {
		List<Object> personData = new ArrayList<>();
		for (Person personObj : listOfPersons) {
			personData.add(objectMapper.convertValue(personObj, Person.class));
		}
		try {
			dataRepository.saveData(TypeOfData.persons, personData);
		} catch (IOException e) {
			logger.error("Error saving data: {} ", e.getMessage());
		}
	}

	public List<Person> getPeopleByStationAddress(List<Firestation> firestation) {
		List<Person> persons = getAllPersons();
		List<Person> matchingPersons = new ArrayList<>();

		for (Firestation f : firestation) {
			matchingPersons.addAll(persons.stream().filter(person -> person.address().equals(f.address()))
					.collect(Collectors.toList()));
		}
		return matchingPersons;
	}
}
