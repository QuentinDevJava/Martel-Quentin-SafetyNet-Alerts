package com.openclassroom.safetynet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.PersonNotFoundException;
import com.openclassroom.safetynet.model.Child;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.repository.JsonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	private final JsonRepository repository;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final MedicalRecordService medicalRecordService;
	private final FirestationService firestationService;

	private List<Person> allPersons() {
		List<Object> personData = repository.loadTypeOfData(TypeOfData.PERSONS);
		List<Person> persons = new ArrayList<>();
		for (Object personObj : personData) {
			persons.add(objectMapper.convertValue(personObj, Person.class));
		}
		return persons;
	}

	@Override
	public Person createPerson(Person person) {
		List<Person> persons = allPersons();
		persons.add(person);
		savePersons(persons);
		return person;
	}

	@Override
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

	@Override
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

	@Override
	public List<Person> getPersonsByAddress(String address) {
		return allPersons().stream().filter(person -> person.address().equals(address)).collect(Collectors.toList());
	}

	@Override
	public PersonsLastNameInfo extractNameAddressAgeEmailInfo(Person person, MedicalRecord medicalRecord) {
		return new PersonsLastNameInfo(person.firstName(), person.lastName(), person.address(), getPersonAge(person), person.email(), medicalRecord.medications(), medicalRecord.allergies());
	}

	@Override
	public PersonEmail personEmails(String city) {
		List<String> emails = allPersons().stream().filter(person -> person.city().equals(city)).map(Person::email).toList();
		return new PersonEmail(emails);
	}

	@Override
	public List<PersonInfo> extractPersonInfos(List<Person> persons) {
		return persons.stream().map(this::extractNameAddressAndPhone).toList();
	}

	private PersonInfo extractNameAddressAndPhone(Person person) {
		return new PersonInfo(person.firstName(), person.lastName(), person.address(), person.phone());
	}

	@Override
	public List<Child> listOfChild(List<Person> personsByAddress) {
		return personsByAddress.stream().filter(this::isChild).map(this::extractChildInfo).collect(Collectors.toList());
	}

	private Child extractChildInfo(Person person) {
		return new Child(person.firstName(), person.lastName(), person.address(), person.phone(), getPersonAge(person));
	}

	@Override
	public int getPersonAge(Person person) {
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFullName(person.firstName(), person.lastName());
		if (medicalRecord != null) {
			String dateString = medicalRecord.birthdate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate birthdate = LocalDate.parse(dateString, formatter);
			LocalDate today = LocalDate.now();
			return Period.between(birthdate, today).getYears();
		}
		return -1;
	}

	@Override
	public List<Person> getPersonsByStationAddress(List<Firestation> firestation) {
		List<Person> persons = allPersons();
		return firestation.stream().flatMap(f -> persons.stream().filter(person -> person.address().equals(f.address())).toList().stream()).toList();
	}

	@Override
	public List<String> getPhoneNumbersByStation(String stationNumber) {
		return getPersonsByStation(stationNumber).stream().map(Person::phone).toList();
	}

	private List<Person> getPersonsByStation(String stationNumber) {
		List<Firestation> firestation = firestationService.findAllByStationNumber(stationNumber);
		return getPersonsByStationAddress(firestation);
	}

	@Override
	public List<PersonsLastNameInfo> listOfPersonsByLastName(String lastName) {
		List<Person> persons = allPersons();
		List<PersonsLastNameInfo> listOfPersonsByLastName = new ArrayList<>();
		for (Person person : persons) {
			if (person.lastName().equals(lastName)) {
				MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFullName(person.firstName(), person.lastName());
				listOfPersonsByLastName.add(extractNameAddressAgeEmailInfo(person, medicalRecord));
			}
		}
		return listOfPersonsByLastName;

	}

	private boolean isChild(Person person) {
		return getPersonAge(person) < 18;
	}

	@Override
	public MedicalRecordInfo extractBasicInfo(Person person, MedicalRecord medicalRecord) {
		return new MedicalRecordInfo(person.firstName(), person.lastName(), person.phone(), getPersonAge(person), medicalRecord.medications(), medicalRecord.allergies());
	}

	private MedicalRecordInfo getMedicalRecordInfo(Person person) {
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFullName(person.firstName(), person.lastName());
		return extractBasicInfo(person, medicalRecord);
	}

	@Override
	public List<MedicalRecordInfo> getMedicalRecordInfosByListPersons(List<Person> persons) {
		return persons.stream().map(this::getMedicalRecordInfo).collect(Collectors.toList());
	}

	@Override
	public MedicalRecordInfo getMedicalRecordInfosByPerson(Person person) {
		return getMedicalRecordInfo(person);
	}
}
