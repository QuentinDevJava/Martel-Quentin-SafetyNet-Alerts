package com.openclassroom.safetynet.service;

import java.util.List;
import java.util.function.Predicate;

import com.openclassroom.safetynet.model.ChildInfo;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;

public interface PersonService {

	/**
	 * Creates a new person.
	 *
	 * @param person The person to create.
	 * @return The created person.
	 */
	Person createPerson(Person person);

	/**
	 * Updates an existing person.
	 *
	 * @param firstName The first name of the person to update.
	 * @param lastName  The last name of the person to update.
	 * @param person    The updated person.
	 * @return The updated person.
	 */
	Person updatePerson(String firstName, String lastName, Person person);

	/**
	 * Deletes a person by the full name.
	 *
	 * @param firstName The first name of the person to delete.
	 * @param lastName  The last name of the person to delete.
	 * @return True if the person was deleted successfully, false otherwise.
	 */
	boolean deletePerson(String firstName, String lastName);

	/**
	 * Retrieves all persons.
	 *
	 * @return A list of all persons.
	 */
	List<Person> allPersons();

	/**
	 * Retrieves persons associated with a specific fire station.
	 *
	 * @param firestation The fire station to retrieve persons for.
	 * @return A list of persons associated with the specified fire station.
	 */
	List<Person> getPersonsByStationAddress(List<Firestation> firestation);

	/**
	 * Retrieves persons by address.
	 *
	 * @param address The address to retrieve persons for.
	 * @return A list of persons residing at the specified address.
	 */
	List<Person> getPersonsByAddress(String address);

	/**
	 * Retrieves persons by station number.
	 *
	 * @param stationNumber The station number to retrieve persons for.
	 * @return A list of persons covered by the specified station.
	 */
	List<Person> getPersonsByStation(String stationNumber);

	/**
	 * Retrieves phone numbers of persons covered by a specific station.
	 *
	 * @param stationNumber The station number to retrieve phone numbers for.
	 * @return A list of phone numbers of persons covered by the specified station.
	 */
	List<String> getPhoneNumbersByStation(String stationNumber);

	/**
	 * Calculates the age of a person based on their birthdate.
	 *
	 * @param person               The person to calculate the age for.
	 * @param medicalRecordService The medical record service.
	 * @return The age of the person.
	 */
	int getPersonAge(Person person, MedicalRecordService medicalRecordService);

	/**
	 * Extracts basic information from a person, including name, address, and phone
	 * number.
	 *
	 * @param person The person to extract information from.
	 * @return A PersonInfo object containing the extracted information.
	 */
	PersonInfo extractNameAddressPhoneInfo(Person person);

	/**
	 * Extracts basic information from a person, including name, address, and phone
	 * number.
	 *
	 * @param List of person The persons to extract information from.
	 * @return A list of PersonInfo object containing the extracted information.
	 */
	List<PersonInfo> extractPersonInfos(List<Person> persons);

	/**
	 * Extracts child information from a person.
	 *
	 * @param person               The person to extract information from.
	 * @param medicalRecordService The medical record service.
	 * @param personService        The person service.
	 * @return A ChildInfo object containing the extracted child information.
	 */
	ChildInfo extractChildInfo(Person person, MedicalRecordService medicalRecordService, PersonService personService);

	/**
	 * Extracts name, address, age, and email information from a person.
	 *
	 * @param person        The person to extract information from.
	 * @param medicalRecord The medical record of the person.
	 * @return A PersonsLastNameInfo object containing the extracted information.
	 */
	PersonsLastNameInfo extractNameAddressAgeEmailInfo(Person person, MedicalRecord medicalRecord);

	/**
	 * Retrieves email addresses of persons residing in a specific city.
	 *
	 * @param city The city to retrieve email addresses for.
	 * @return A list of PersonEmail objects containing the extracted email
	 *         addresses.
	 */
	PersonEmail personEmails(String city);

	int CountsNumberOfChildrenAndAdults(List<Person> persons, Predicate<Integer> predicate);
}
