package com.openclassroom.safetynet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.PersonNotFoundException;
import com.openclassroom.safetynet.model.ChildInfo;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.repository.JsonRepository;

@Service
public class PersonServiceImpl implements PersonService {

	private final JsonRepository repository;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final MedicalRecordService medicalRecordService;
	private final FirestationService firestationService;

	public PersonServiceImpl(JsonRepository dataRepository, FirestationService firestationService, MedicalRecordService medicalRecordService) {
		this.repository = dataRepository;
		this.medicalRecordService = medicalRecordService;
		this.firestationService = firestationService;
	}

	public List<Person> allPersons() {
		List<Object> personData = repository.loadTypeOfData(TypeOfData.PERSONS);
		List<Person> persons = new ArrayList<>();
		for (Object personObj : personData) {
			persons.add(objectMapper.convertValue(personObj, Person.class));
		}
		return persons;
	}

	public Person createPerson(Person person) {
		for (String field : new String[] { person.firstName(), person.firstName(), person.address(), person.city(), person.zip(), person.phone(), person.email() }) {
			if (Optional.ofNullable(field).map(StringUtils::isBlank).orElse(true)) {
				throw new IllegalArgumentException("All fields of the person must be filled.");
			}
		}
		List<Person> persons = allPersons();
		persons.add(person);
		savePersons(persons);
		return person;
	}

	public Person updatePerson(String firstName, String lastName, Person person) {
		List<Person> persons = allPersons();
		for (int i = 0; i < persons.size(); i++) {
			if (persons.get(i).firstName().equals(firstName) && persons.get(i).lastName().equals(lastName)) {
				persons.set(i, person);
				savePersons(persons);
				return person;
			}
		}
		throw new PersonNotFoundException("Person with first name '" + firstName + "' and last name '" + lastName + "' not found.");
	}

	public boolean deletePerson(String firstName, String lastName) {
		List<Person> persons = allPersons();
		boolean personDeleted = persons.removeIf(p -> p.firstName().equals(firstName) && p.lastName().equals(lastName));
		if (personDeleted) {
			savePersons(persons);
		}
		return personDeleted;
	}

	private void savePersons(List<Person> listOfPersons) {
		List<Object> personData = new ArrayList<>();
		for (Person personObj : listOfPersons) {
			personData.add(objectMapper.convertValue(personObj, Person.class));
		}
		repository.saveData(TypeOfData.PERSONS, personData);
	}

	public List<Person> getPersonsByAddress(String address) {
		return allPersons().stream().filter(person -> person.address().equals(address)).collect(Collectors.toList());
	}

	public PersonsLastNameInfo extractNameAddressAgeEmailInfo(Person person, MedicalRecord medicalRecord) {
		return new PersonsLastNameInfo(person.firstName(), person.lastName(), person.address(), getPersonAge(person, medicalRecordService), person.email(), medicalRecord.medications(), medicalRecord.allergies());
	}

	public PersonEmail personEmails(String city) {

		List<String> emails = allPersons().stream().filter(person -> person.city().equals(city)).map(Person::email).toList();
		return new PersonEmail(emails);
	}

	public PersonInfo extractNameAddressPhoneInfo(Person person) {
		return new PersonInfo(person.firstName(), person.lastName(), person.address(), person.phone());

	}

	public List<PersonInfo> extractPersonInfos(List<Person> persons) {
		return persons.stream().map(this::extractNameAddressPhoneInfo).toList();
	}

	public ChildInfo extractChildInfo(Person person, MedicalRecordService medicalRecordService, PersonService personService) {
		return new ChildInfo(person.firstName(), person.lastName(), person.address(), person.phone(), personService.getPersonAge(person, medicalRecordService));
	}

	public int CountsNumberOfChildrenAndAdults(List<Person> persons, Predicate<Integer> predicate) {
		return (int) persons.stream().map(person -> getPersonAge(person, medicalRecordService)).filter(predicate).count();
	}

	public int getPersonAge(Person person, MedicalRecordService medicalRecordService) {
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFullName(person.firstName(), person.lastName());
		if (medicalRecord != null) {
			String dateString = medicalRecord.birthdate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate birthdate = LocalDate.parse(dateString, formatter);
			LocalDate today = LocalDate.now();
			return Period.between(birthdate, today).getYears();
		}
		return 0;
	}

	public List<Person> getPersonsByStationAddress(List<Firestation> firestation) {
		List<Person> persons = allPersons();
		return firestation.stream().flatMap(f -> persons.stream().filter(person -> person.address().equals(f.address())).toList().stream()).toList();
	}

	public List<String> getPhoneNumbersByStation(String stationNumber) {
		return getPersonsByStation(stationNumber).stream().map(Person::phone).toList();
	}

	public List<Person> getPersonsByStation(String stationNumber) {
		List<Firestation> firestation = firestationService.findAllByStationNumber(stationNumber);
		return getPersonsByStationAddress(firestation);
	}

}
