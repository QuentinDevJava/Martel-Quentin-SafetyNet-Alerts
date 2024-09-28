package com.openclassroom.safetynet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.DataRepository;
import com.openclassroom.safetynet.model.Person;

@Service
public class PersonServiceImpl implements PersonService {

	private final DataRepository dataRepository = new DataRepository();

	public List<Person> getAllPersons() {
		List<Object> personData = dataRepository.SelectTypeOfData(TypeOfData.persons);
		List<Person> persons = new ArrayList<>();
		for (Object personObj : personData) {
			persons.add(new ObjectMapper().convertValue(personObj, Person.class));
		}
		return persons;
	}

	public Person createPerson(Person person) {
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
				break;
			}
		}
		savePersons(persons);
		return person;
	}

	public void deletePerson(String firstName, String lastName) {
		List<Person> persons = getAllPersons();
		persons.removeIf(p -> p.firstName().equals(firstName) && p.lastName().equals(lastName));
		savePersons(persons);
	}

	private void savePersons(List<Person> listOfPersons) {
		List<Object> personData = new ArrayList<>();
		for (Person personObj : listOfPersons) {
			personData.add(new ObjectMapper().convertValue(personObj, Person.class));
		}
		try {
			dataRepository.saveData(TypeOfData.persons, personData);
		} catch (IOException e) {
			System.err.println("Erreur lors de la suavegarde des données : " + e.getMessage());
		}
	}
}
