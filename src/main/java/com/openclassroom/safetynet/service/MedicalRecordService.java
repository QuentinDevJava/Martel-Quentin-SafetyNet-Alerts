package com.openclassroom.safetynet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.MedicalRecord;
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
	 * Retrieves medical records for a list of persons.
	 *
	 * @param persons The list of persons {@link Person}.
	 * @return A list of medical records for the specified persons
	 *         {@link MedicalRecord}.
	 */
	List<MedicalRecord> getPersonMedicalRecords(List<Person> persons);

	/**
	 * Retrieves a medical record by the full name of the person.
	 *
	 * @param firstName The first name of the person.
	 * @param lastName  The last name of the person.
	 * @return The medical record for the specified person {@link MedicalRecord}.
	 */
	MedicalRecord getMedicalRecordByFullName(String firstName, String lastName);

}
