package com.openclassroom.safetynet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;

/**
 * Interface defining the operations for managing medical records.
 */
@Service
public interface MedicalRecordService {

	/**
	 * Creates a new medical record.
	 *
	 * @param medicalRecord The medical record to create {@link MedicalRecord}.
	 * @return The created medical record {@link MedicalRecord}.
	 */
	MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);

	/**
	 * Updates an existing medical record.
	 *
	 * @param firstName     The first name of the person whose medical record to
	 *                      update.
	 * @param lastName      The last name of the person whose medical record to
	 *                      update.
	 * @param medicalRecord The updated medical record {@link MedicalRecord}.
	 * @return The updated medical record {@link MedicalRecord}.
	 */
	MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecord);

	/**
	 * Deletes a medical record by the full name of the person.
	 *
	 * @param firstName The first name of the person.
	 * @param lastName  The last name of the person.
	 * @return True if the medical record was deleted successfully, false otherwise.
	 */
	Boolean deleteMedicalRecord(String firstName, String lastName);

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
	 * Retrieves medical records for a list of persons.
	 *
	 * @param persons The list of persons {@link Person}.
	 * @return A list of medical records for the specified persons
	 *         {@link MedicalRecord}.
	 */
	List<MedicalRecord> getPersonMedicalRecords(List<Person> persons);

	/**
	 * Retrieves basic information from the medical record of a person.
	 *
	 * @param person The person whose medical record to extract information from
	 *               {@link Person}.
	 * @return A MedicalRecordInfo object containing basic information from the
	 *         medical record {@link MedicalRecordInfo}.
	 */
	MedicalRecordInfo getMedicalRecordInfosByPerson(Person person);

	/**
	 * Retrieves a medical record by the full name of the person.
	 *
	 * @param firstName The first name of the person.
	 * @param lastName  The last name of the person.
	 * @return The medical record for the specified person {@link MedicalRecord}.
	 */
	MedicalRecord getMedicalRecordByFullName(String firstName, String lastName);

	/**
	 * Finds a medical record by the last name of the person.
	 *
	 * @param lastName The last name of the person.
	 * @return The medical record for the specified person {@link MedicalRecord}.
	 */
	MedicalRecord findPersonsMedicalRecords(String lastName);

}
