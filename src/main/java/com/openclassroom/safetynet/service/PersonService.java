package com.openclassroom.safetynet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.Child;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;

/**
 * Interface defining the operations for managing persons.
 */
@Service
public interface PersonService {

	/**
	 * Creates a new person.
	 *
	 * @param person The person to create {@link Person}.
	 * @return The created person {@link Person}.
	 */
	Person createPerson(Person person);

	/**
	 * Updates an existing person.
	 *
	 * @param firstName The first name of the person to update.
	 * @param lastName  The last name of the person to update.
	 * @param person    The updated person {@link Person}.
	 * @return The updated person {@link Person}.
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
	 * Retrieves persons associated with a specific fire station.
	 *
	 * @param firestation The fire station to retrieve persons for
	 *                    {@link Firestation}.
	 * @return A list of persons associated with the specified fire station
	 *         {@link Person}.
	 */
	List<Person> getPersonsByStationAddress(List<Firestation> firestation);

	/**
	 * Retrieves persons by address.
	 *
	 * @param address The address to retrieve persons for.
	 * @return A list of persons residing at the specified address {@link Person}.
	 */
	List<Person> getPersonsByAddress(String address);

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
	 * @param person The person to calculate the age for {@link Person}.
	 * @return The age of the person.
	 */
	int getPersonAge(Person person);

	/**
	 * Extracts basic information from a person, including name, address, and phone
	 * number.
	 *
	 * @param List of person The persons to extract information from {@link Person}.
	 * @return A list of PersonInfo object containing the extracted information
	 *         {@link PersonInfo}.
	 */
	List<PersonInfo> extractPersonInfos(List<Person> persons);

	/**
	 * Extracts name, address, age, and email information from a person.
	 *
	 * @param person        The person to extract information from {@link Person}.
	 * @param medicalRecord The medical record of the person {@link MedicalRecord}.
	 * @return A PersonsLastNameInfo object containing the extracted information
	 *         {@link PersonsLastNameInfo}.
	 */
	PersonsLastNameInfo extractNameAddressAgeEmailInfo(Person person, MedicalRecord medicalRecord);

	/**
	 * Retrieves email addresses of persons residing in a specific city.
	 *
	 * @param city The city to retrieve email addresses for .
	 * @return A list of PersonEmail objects containing the extracted email
	 *         addresses {@link PersonEmail}.
	 */
	PersonEmail personEmails(String city);

	/**
	 * Retrieves a list of PersonsLastNameInfo objects for persons with the given
	 * last name.
	 *
	 * @param lastName The last name of the persons to retrieve.
	 * @return A list of PersonsLastNameInfo objects containing the extracted
	 *         information {@link PersonsLastNameInfo}.
	 */
	List<PersonsLastNameInfo> listOfPersonsByLastName(String lastName);

	/**
	 * Retrieves a list of Child objects from a list of persons.
	 *
	 * @param personsByAddress The list of persons to extract child information from
	 *                         {@link Person}.
	 * @return A list of Child objects containing the extracted child information
	 *         {@link Child}.
	 */
	List<Child> listOfChild(List<Person> personsByAddress);

	/**
	 * Extracts basic information from a person's medical record.
	 *
	 * @param person        The person whose medical record to extract information
	 *                      from {@link Person}.
	 * @param medicalRecord The medical record of the person {@link MedicalRecord}.
	 * @return A MedicalRecordInfo object containing basic information from the
	 *         medical record {@link MedicalRecordInfo}.
	 */
	MedicalRecordInfo extractBasicInfo(Person person, MedicalRecord medicalRecord);

	/**
	 * Retrieves basic information from the medical records of a list of persons.
	 *
	 * @param persons The list of persons whose medical records to extract
	 *                information from {@link Person}.
	 * @return A list of MedicalRecordInfo objects containing basic information from
	 *         the medical records {@link MedicalRecordInfo}.
	 */
	List<MedicalRecordInfo> getMedicalRecordInfosByListPersons(List<Person> persons);

	/**
	 * Retrieves basic information from the medical record of a person.
	 *
	 * @param person The person whose medical record to extract information from
	 *               {@link Person}.
	 * @return A MedicalRecordInfo object containing basic information from the
	 *         medical record {@link MedicalRecordInfo}.
	 */
	MedicalRecordInfo getMedicalRecordInfosByPerson(Person person);
}
