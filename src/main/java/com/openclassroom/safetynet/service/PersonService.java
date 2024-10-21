package com.openclassroom.safetynet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.Child;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonFloodInfo;
import com.openclassroom.safetynet.model.PersonInfo;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.repository.JsonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service defining the operations for managing persons.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
	private final ObjectMapper objectMapper;
	private final JsonRepository repository;
	private final MedicalRecordService medicalRecordService;
	private final FirestationService firestationService;
	private final Predicate<Integer> isAdult = age -> age > 18;
	private final Predicate<Integer> isChild = age -> age <= 18;

	private List<Person> allPersons() {
		List<Object> personData = repository.loadTypeOfData(TypeOfData.PERSONS);
		List<Person> persons = new ArrayList<>();
		for (Object personObj : personData) {
			persons.add(objectMapper.convertValue(personObj, Person.class));
		}
		return persons;
	}

	/**
	 * Interface defining the operations for managing persons.
	 */
	public Person createPerson(Person person) {
		List<Person> persons = allPersons();
		persons.add(person);
		savePersons(persons);
		log.debug("Add person {} in allPersons() : {}", person, persons);
		return person;
	}

	/**
	 * Updates an existing person.
	 *
	 * @param firstName The first name of the person to update.
	 * @param lastName  The last name of the person to update.
	 * @param person    The updated person {@link Person}.
	 * @return The updated person {@link Person}.
	 */
	public Person updatePerson(String firstName, String lastName, Person person) {
		String fullName = firstName + " " + lastName;
		Person existingPerson = getPersonByFullName(fullName);
		if (existingPerson != null) {
			List<Person> persons = allPersons();
			log.debug("Found existing person: {}", existingPerson);
			persons.set(persons.indexOf(existingPerson), person);
			savePersons(persons);
			log.debug("Updated person list: {}", persons);
			return person;
		} else {
			throw new NoSuchElementException("Person with first name '" + firstName + "' and last name '" + lastName + "' not found.");
		}
	}

	private Person getPersonByFullName(String fullName) {
		return allPersons().stream().filter(p -> p.fullName().equals(fullName)).findFirst().orElse(null);
	}

	/**
	 * Deletes a person by the full name.
	 *
	 * @param firstName The first name of the person to delete.
	 * @param lastName  The last name of the person to delete.
	 * @return True if the person was deleted successfully, false otherwise.
	 */
	public boolean deletePerson(String firstName, String lastName) {
		String fullName = firstName + " " + lastName;
		List<Person> persons = allPersons();
		boolean personDeleted = persons.removeIf(p -> p.fullName().equals(fullName));
		if (personDeleted) {
			savePersons(persons);
			log.debug("Person {} deleted successfully.", fullName);
		}
		return personDeleted;
	}

	private void savePersons(List<Person> listOfPersons) {
		List<Object> personData = new ArrayList<>();
		for (Person personObj : listOfPersons) {
			personData.add(objectMapper.convertValue(personObj, Person.class));
		}
		log.debug("Saving persons to repository: {}", personData);
		repository.saveData(TypeOfData.PERSONS, personData);
	}

	/**
	 * Returns a list of people covered by a given fire station.
	 *
	 * @param stationNumber The fire station number.
	 * @return A list of {@link PersonCoveredByStation} representing the people
	 *         covered by the station.
	 */
	public PersonCoveredByStation findCoveredPersonsByFireStation(int stationNumber) {
		List<Firestation> firestations = firestationService.findFireStationByStationNumber(stationNumber);
		log.debug("Result of findFireStationByStationNumber for stationNumber {} = {}", stationNumber, firestations);
		List<Person> personByStation = getPersonsByStationAddress(firestations);
		log.debug("Result of getPersonsByStationAddress for firestations found in findFireStationByStationNumber : {}", personByStation);
		List<PersonInfo> personInfos = extractPersonInfos(personByStation);
		log.debug("Result of extractPersonInfos for persons found in getPersonsByStationAddress : {}", personInfos);
		List<MedicalRecord> medicalRecords = medicalRecordService.getPersonMedicalRecords(personByStation);
		log.debug("Result of getPersonMedicalRecords for persons found in getPersonsByStationAddress : {}", medicalRecords);
		int adultCount = countAdults(medicalRecords);
		log.debug("Result of countAdults for medicalRecords found in getPersonMedicalRecords : {}", adultCount);
		int childCount = countChildren(medicalRecords);
		log.debug("Result of countChildren for medicalRecords found in getPersonMedicalRecords : {}", childCount);
		return new PersonCoveredByStation(personInfos, adultCount, childCount);
	}

	private int countChildren(List<MedicalRecord> medicalRecords) {
		return (int) medicalRecords.stream().map(this::calculateAge).filter(isChild).count();
	}

	private int countAdults(List<MedicalRecord> medicalRecords) {
		return (int) medicalRecords.stream().map(this::calculateAge).filter(isAdult).count();
	}

	/**
	 * Calculates the age of a person based on their birthdate.
	 *
	 * @param person The person to calculate the age for {@link Person}.
	 * @return The age of the person.
	 */
	public int getPersonAge(Person person) {
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFullName(person.fullName());
		if (medicalRecord != null) {
			return calculateAge(medicalRecord);
		}
		return -1;
	}

	private int calculateAge(MedicalRecord medicalRecord) {
		String dateString = medicalRecord.birthdate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthdate = LocalDate.parse(dateString, formatter);
		LocalDate today = LocalDate.now();
		return Period.between(birthdate, today).getYears();
	}

	/**
	 * Returns information about the people and fire station associated with a given
	 * address.
	 *
	 * @param address The address to search for.
	 * @return A {@link PersonsAndStationInfo} object containing information about
	 *         the people and the station.
	 */
	public PersonsAndStationInfo getPersonsAndStationInfoByAddress(String address) {
		List<Person> persons = getPersonsByAddress(address);
		log.debug("Result of getPersonsByAddress for address {} = {} ", address, persons);
		List<MedicalRecordInfo> medicalRecordInfos = getMedicalRecordInfosByListPersons(persons);
		log.debug("Result of getMedicalRecordInfosByPersons for persons found in getPersonsByAddress : {}", medicalRecordInfos);
		Firestation firestation = firestationService.getFirestationByAddress(address);
		log.debug("Result of getFirestationByAddress the fire station number associated with address : {} = {} ", address, firestation.station());
		return new PersonsAndStationInfo(medicalRecordInfos, firestation.station());
	}

	/**
	 * Retrieves email addresses of persons residing in a specific city.
	 *
	 * @param city The city to retrieve email addresses for .
	 * @return A list of PersonEmail objects containing the extracted email
	 *         addresses {@link PersonEmail}.
	 */
	public PersonEmail personEmails(String city) {
		List<String> emails = allPersons().stream().filter(person -> person.city().equals(city)).map(Person::email).toList();
		return new PersonEmail(emails);
	}

	/**
	 * Extracts basic information from a person, including name, address, and phone
	 * number.
	 *
	 * @param List of person The persons to extract information from {@link Person}.
	 * @return A list of PersonInfo object containing the extracted information
	 *         {@link PersonInfo}.
	 */
	public List<PersonInfo> extractPersonInfos(List<Person> persons) {
		return persons.stream().map(this::extractNameAddressAndPhone).toList();
	}

	private PersonInfo extractNameAddressAndPhone(Person person) {
		return new PersonInfo(person.firstName(), person.lastName(), person.address(), person.phone());
	}

	/**
	 * Extracts name, address, age, and email information from a person.
	 *
	 * @param person        The person to extract information from {@link Person}.
	 * @param medicalRecord The medical record of the person {@link MedicalRecord}.
	 * @return A PersonsLastNameInfo object containing the extracted information
	 *         {@link PersonsLastNameInfo}.
	 */
	public PersonsLastNameInfo extractNameAddressAgeEmailInfo(Person person, MedicalRecord medicalRecord) {
		return new PersonsLastNameInfo(person.firstName(), person.lastName(), person.address(), getPersonAge(person), person.email(), medicalRecord.medications(), medicalRecord.allergies());
	}

	/**
	 * Retrieves a list of Child objects from a list of persons.
	 *
	 * @param personsByAddress The list of persons to extract child information from
	 *                         {@link Person}.
	 * @return A list of Child objects containing the extracted child information
	 *         {@link Child}.
	 */
	public List<Child> listOfChild(String address) {
		List<Person> personsByAddress = getPersonsByAddress(address);
		log.debug("Result of getPersonsByAddress for address {} = {} ", address, personsByAddress);
		return personsByAddress.stream().filter(this::isChild).map(this::extractChildInfo).toList();
	}

	private Child extractChildInfo(Person person) {
		return new Child(person.firstName(), person.lastName(), person.address(), person.phone(), getPersonAge(person));
	}

	/**
	 * Retrieves persons by address.
	 *
	 * @param address The address to retrieve persons for.
	 * @return A list of persons residing at the specified address {@link Person}.
	 */
	public List<Person> getPersonsByAddress(String address) {
		return allPersons().stream().filter(person -> person.address().equals(address)).collect(Collectors.toList());
	}

	/**
	 * Retrieves persons associated with a specific fire station.
	 *
	 * @param firestation The fire station to retrieve persons for
	 *                    {@link Firestation}.
	 * @return A list of persons associated with the specified fire station
	 *         {@link Person}.
	 */
	public List<Person> getPersonsByStationAddress(List<Firestation> firestation) {
		List<Person> persons = allPersons();
		return firestation.stream().flatMap(f -> persons.stream().filter(person -> person.address().equals(f.address())).toList().stream()).toList();
	}

	/**
	 * Retrieves phone numbers of persons covered by a specific station.
	 *
	 * @param stationNumber The station number to retrieve phone numbers for.
	 * @return A list of phone numbers of persons covered by the specified station.
	 */
	public List<String> getPhoneNumbersByStation(int stationNumber) {
		return getPersonsByStation(stationNumber).stream().map(Person::phone).toList();
	}

	private List<Person> getPersonsByStation(int stationNumber) {
		List<Firestation> firestation = firestationService.findFireStationByStationNumber(stationNumber);
		return getPersonsByStationAddress(firestation);
	}

	/**
	 * Retrieves a list of PersonsLastNameInfo objects for persons with the given
	 * last name.
	 *
	 * @param lastName The last name of the persons to retrieve.
	 * @return A list of PersonsLastNameInfo objects containing the extracted
	 *         information {@link PersonsLastNameInfo}.
	 */
	public List<PersonsLastNameInfo> listOfPersonsByLastName(String lastName) {
		List<Person> persons = allPersons();
		List<PersonsLastNameInfo> listOfPersonsByLastName = new ArrayList<>();
		for (Person person : persons) {
			if (person.lastName().equals(lastName)) {
				MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFullName(person.fullName());
				listOfPersonsByLastName.add(extractNameAddressAgeEmailInfo(person, medicalRecord));
			}
		}
		return listOfPersonsByLastName;

	}

	private boolean isChild(Person person) {
		return getPersonAge(person) < 18;
	}

	/**
	 * Extracts basic information from a person's medical record.
	 *
	 * @param person        The person whose medical record to extract information
	 *                      from {@link Person}.
	 * @param medicalRecord The medical record of the person {@link MedicalRecord}.
	 * @return A MedicalRecordInfo object containing basic information from the
	 *         medical record {@link MedicalRecordInfo}.
	 */
	public MedicalRecordInfo extractBasicInfo(Person person, MedicalRecord medicalRecord) {
		return new MedicalRecordInfo(person.firstName(), person.lastName(), person.phone(), getPersonAge(person), medicalRecord.medications(), medicalRecord.allergies());
	}

	private MedicalRecordInfo getMedicalRecordInfo(Person person) {
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFullName(person.fullName());
		return extractBasicInfo(person, medicalRecord);
	}

	/**
	 * Retrieves basic information from the medical records of a list of persons.
	 *
	 * @param persons The list of persons whose medical records to extract
	 *                information from {@link Person}.
	 * @return A list of MedicalRecordInfo objects containing basic information from
	 *         the medical records {@link MedicalRecordInfo}.
	 */
	public List<MedicalRecordInfo> getMedicalRecordInfosByListPersons(List<Person> persons) {
		List<MedicalRecordInfo> medicalRecords = new ArrayList<>();
		for (Person person : persons) {
			medicalRecords.add(getMedicalRecordInfosByPerson(person));
		}
		return medicalRecords;

		// persons.stream().map(this::getMedicalRecordInfo).collect(Collectors.toList());
	}

	/**
	 * Retrieves basic information from the medical record of a person.
	 *
	 * @param person The person whose medical record to extract information from
	 *               {@link Person}.
	 * @return A MedicalRecordInfo object containing basic information from the
	 *         medical record {@link MedicalRecordInfo}.
	 */
	public MedicalRecordInfo getMedicalRecordInfosByPerson(Person person) {
		return getMedicalRecordInfo(person);
	}

	/**
	 * Returns information about people affected by a flood, based on the fire
	 * station numbers.
	 *
	 * @param stationNumber A list of fire station numbers.
	 * @return A {@link PersonFloodInfo} object containing information about the
	 *         people affected by the flood.
	 */
	public PersonFloodInfo floodInfo(List<Integer> stationNumber) {
		List<Firestation> firestations = firestationService.getFirestationByListStationNumber(stationNumber);
		Map<String, List<MedicalRecordInfo>> medicalRecordsByAddress = listOfPersonsByAddressByStationNumber(firestations);

		return new PersonFloodInfo(medicalRecordsByAddress);
	}

	/**
	 * Returns a map of fire station addresses to lists of medical record
	 * information for people located at those addresses.
	 *
	 * @param firestations A list of fire stations {@link Firestation}.
	 * @return A map where keys are addresses and values are lists of medical record
	 *         information for people at those addresses.
	 */
	public Map<String, List<MedicalRecordInfo>> listOfPersonsByAddressByStationNumber(List<Firestation> firestations) {
		Map<String, List<MedicalRecordInfo>> medicalRecordsByAddress = new HashMap<>();
		for (Firestation firestation : firestations) {
			List<Person> persons = new ArrayList<>();
			persons.addAll(getPersonsByAddress(firestation.address()));

			List<MedicalRecordInfo> medicalRecordInfos = getMedicalRecordInfosByListPersons(persons);
			medicalRecordsByAddress.put(firestation.address(), medicalRecordInfos);
		}
		return medicalRecordsByAddress;
	}
}
