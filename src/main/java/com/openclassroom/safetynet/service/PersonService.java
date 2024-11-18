package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.dto.Child;
import com.openclassroom.safetynet.dto.MedicalRecordInfo;
import com.openclassroom.safetynet.dto.PersonCoveredByStation;
import com.openclassroom.safetynet.dto.PersonFloodInfo;
import com.openclassroom.safetynet.dto.PersonsAndStationInfo;
import com.openclassroom.safetynet.dto.PersonsLastNameInfo;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
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
	private String unknownStationNumber = "Unknown station number: ";
	private final ObjectMapper objectMapper;
	private final JsonRepository repository;
	private final MedicalRecordService medicalRecordService;
	private final FirestationService firestationService;

	/**
	 * Creates a new Person.
	 *
	 * @param person The person to create {@link Person}.
	 * 
	 */
	public void createPerson(Person person) {
		List<Person> persons = allPersons();
		persons.add(person);
		savePersons(persons);
		log.debug("Add person {} in allPersons() : {}", person, persons);
	}

	/**
	 * Updates an existing person.
	 *
	 * @param firstName The first name of the person to update.
	 * @param lastName  The last name of the person to update.
	 * @param person    The updated person {@link Person}.
	 */
	public void updatePerson(String firstName, String lastName, Person person) {
		String fullName = firstName + " " + lastName;
		Person existingPerson = getPersonByFullName(fullName);
		if (existingPerson == null) {
			log.error("Unknown person: {}", fullName);
			throw new IllegalArgumentException("Unknown person: " + fullName);
		}
		List<Person> persons = allPersons();
		log.debug("Found existing person: {}", existingPerson);
		persons.set(persons.indexOf(existingPerson), person);
		savePersons(persons);
		log.debug("Updated person list: {}", persons);
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

	/**
	 * Returns a list of people covered by a given fire station.
	 *
	 * @param stationNumber The fire station number.
	 * @return A list of {@link PersonCoveredByStation} representing the people
	 *         covered by the station.
	 */
	public PersonCoveredByStation personCoveredByStation(int stationNumber) {
		List<Firestation> firestations = firestationService.findFireStationByStationNumber(stationNumber);
		log.debug("Result of findFireStationByStationNumber for stationNumber {} = {}", stationNumber, firestations);
		if (firestations.isEmpty()) {
			log.error("Unknown station number: {}", stationNumber);
			throw new IllegalArgumentException(unknownStationNumber + stationNumber);
		}
		List<Person> personByStation = getPersonsByStationAddress(firestations);
		log.debug("Result of getPersonsByStationAddress for firestations found in findFireStationByStationNumber : {}", personByStation);

		List<MedicalRecord> medicalRecords = medicalRecordService.getPersonMedicalRecords(personByStation);
		log.debug("Result of getPersonMedicalRecords for persons found in getPersonsByStationAddress : {}", medicalRecords);

		return new PersonCoveredByStation(personByStation, medicalRecords);
	}

	/**
	 * Returns information about the people and fire station associated with a given
	 * address.
	 *
	 * @param address The address to search for. The address is used to query both
	 *                the list of persons and the fire station.
	 * @return A {@link PersonsAndStationInfo} object containing information about
	 *         the people at the given address and the fire station responsible for
	 *         that address.
	 * @throws IllegalArgumentException If no persons are found at the given
	 *                                  address, an exception is thrown.
	 */
	public PersonsAndStationInfo getPersonsAndStationInfoByAddress(String address) {
		List<Person> persons = getPersonsByAddress(address);
		log.debug("Result of getPersonsByAddress for address {} = {} ", address, persons);
		if ((persons).isEmpty()) {
			log.error("Unknown address: {}", address);
			throw new IllegalArgumentException("Unknown address: " + address);
		}
		List<MedicalRecordInfo> medicalRecordInfos = persons.stream()
				.map(p -> new MedicalRecordInfo(p, medicalRecordService.getMedicalRecordByFullName(p.fullName()))).toList();
		log.debug("Result of getMedicalRecordInfosByPersons for persons found in getPersonsByAddress : {}", medicalRecordInfos);
		Firestation firestation = firestationService.getFirestationByAddress(address);
		log.debug("Result of getFirestationByAddress the fire station number associated with address : {} = {} ", address, firestation.station());
		return new PersonsAndStationInfo(medicalRecordInfos, firestation.station());
	}

	/**
	 * Retrieves email addresses of persons residing in a specific city.
	 *
	 * @param city The city to retrieve email addresses for .
	 * @return A list of String containing the extracted email addresses.
	 * @throws IllegalArgumentException If no persons are found at the given city,
	 *                                  an exception is thrown.
	 */
	public List<String> personEmails(String city) {
		List<Person> persons = allPersons();
		List<Person> matchingPersons = persons.stream().filter(person -> person.city().equals(city)).toList();
		log.debug("Result of matchingPersons for city {} = {} ", city, matchingPersons);
		if (matchingPersons.isEmpty()) {
			throw new IllegalArgumentException("Unknown city: " + city);
		}
		return matchingPersons.stream().map(Person::email).toList();
	}

	/**
	 * Retrieves a list of Child objects from a list of persons.
	 *
	 * @param address The list of persons to extract child information from
	 *                {@link Person}.
	 * @return A list of Child objects containing the extracted child information
	 *         {@link Child}.
	 * @throws IllegalArgumentException If no persons are found at the given
	 *                                  address, an exception is thrown.
	 */
	public List<Child> getChildsByAddress(String address) {
		List<Person> personsByAddress = getPersonsByAddress(address);
		log.debug("Result of getPersonsByAddress for address {} = {} ", address, personsByAddress);
		if (personsByAddress.isEmpty()) {
			log.error("Unknown address: {}", address);
			throw new IllegalArgumentException("Unknown address: " + address);
		}
		return personsByAddress.stream().filter(person -> medicalRecordService.getMedicalRecordByFullName(person.fullName()).isChild())
				.map(p -> new Child(p, medicalRecordService.getMedicalRecordByFullName(p.fullName()))).toList();
	}

	/**
	 * Retrieves persons by address.
	 *
	 * @param address The address to retrieve persons for.
	 * @return A list of persons residing at the specified address
	 *         {@link Person}.
	 */
	public List<Person> getPersonsByAddress(String address) {
		return allPersons().stream().filter(person -> person.address().equals(address)).toList();
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

	/**
	 * Retrieves a list of PersonsLastNameInfo objects for persons with the given
	 * last name.
	 *
	 * @param lastName The last name of the persons to retrieve.
	 * @return A list of PersonsLastNameInfo objects containing the extracted
	 *         information {@link PersonsLastNameInfo}.
	 * @throws IllegalArgumentException If no persons are found at the given
	 *                                  lastName, an exception is thrown.
	 */
	public List<PersonsLastNameInfo> listOfPersonsByLastName(String lastName) {
		List<Person> persons = allPersons();
		List<Person> matchingPersons = persons.stream().filter(person -> person.lastName().equals(lastName)).toList();
		log.debug("Result of matchingPersons for  last name {} = {} ", lastName, matchingPersons);
		if (matchingPersons.isEmpty()) {
			throw new IllegalArgumentException("Unknown last name: " + lastName);
		}
		return matchingPersons.stream()
				.map(person -> new PersonsLastNameInfo(person, medicalRecordService.getMedicalRecordByFullName(person.fullName()))).toList();
	}

	/**
	 * Returns information about people affected by a flood, based on the fire
	 * station numbers.
	 *
	 * @param stationNumber A list of fire station numbers.
	 * @return A {@link PersonFloodInfo} object containing information about the
	 *         people affected by the flood.
	 * @throws IllegalArgumentException If no firestations are found at the given
	 *                                  stationNumber list, an exception is thrown.
	 */
	public PersonFloodInfo floodInfo(List<Integer> stationNumber) {
		List<Firestation> firestations = firestationService.getFirestationByListStationNumber(stationNumber);
		log.debug("Result of getFirestationByListStationNumber for station number {} = {} ", stationNumber, firestations);
		if (firestations.isEmpty()) {
			log.error("Unknown station number: {}", stationNumber);
			throw new IllegalArgumentException(unknownStationNumber + stationNumber);
		}
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
			List<Person> persons = new ArrayList<>(getPersonsByAddress(firestation.address()));
			List<MedicalRecordInfo> medicalRecordInfos = getMedicalRecordInfosByListPersons(persons);
			medicalRecordsByAddress.put(firestation.address(), medicalRecordInfos);
		}
		return medicalRecordsByAddress;
	}

	private List<Person> allPersons() {
		return repository.loadTypeOfData(TypeOfData.PERSONS).stream().map(p -> objectMapper.convertValue(p, Person.class)).filter(Objects::nonNull)
				.map(Person.class::cast).collect((Collectors.toCollection(ArrayList::new)));
	}

	private void savePersons(List<Person> persons) {
		repository.saveData(TypeOfData.PERSONS,
				persons.stream().map(personsObj -> objectMapper.convertValue(personsObj, Person.class)).collect(Collectors.toList()));
	}

	private List<MedicalRecordInfo> getMedicalRecordInfosByListPersons(List<Person> persons) {
		return persons.stream().map(this::getMedicalRecordInfosByPerson).toList();
	}

	private MedicalRecordInfo getMedicalRecordInfosByPerson(Person person) {
		return new MedicalRecordInfo(person, medicalRecordService.getMedicalRecordByFullName(person.fullName()));

	}

	private Person getPersonByFullName(String fullName) {
		return allPersons().stream().filter(p -> p.fullName().equals(fullName)).findFirst().orElse(null);
	}

	private List<Person> getPersonsByStation(int stationNumber) {
		List<Firestation> firestation = firestationService.findFireStationByStationNumber(stationNumber);
		log.debug("Result of findFireStationByStationNumber for stationNumber {} = {} ", stationNumber, firestation);
		if (firestation.isEmpty()) {
			log.error("Unknown station number : {}", stationNumber);
			throw new IllegalArgumentException(unknownStationNumber + stationNumber);
		}
		return getPersonsByStationAddress(firestation);
	}

	private List<Person> getPersonsByStationAddress(List<Firestation> firestation) {
		List<Person> persons = allPersons();
		return firestation.stream().flatMap(f -> persons.stream().filter(person -> person.address().equals(f.address())).toList().stream()).toList();
	}

}
