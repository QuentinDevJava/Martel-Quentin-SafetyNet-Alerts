package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.repository.JsonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service defining the operations for managing medical records.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalRecordService {

	private final JsonRepository repository;
	private final ObjectMapper objectMapper;

	private List<MedicalRecord> allMedicalRecords() {
		return repository.loadTypeOfData(TypeOfData.MEDICALRECORDS).stream().map(medicalRecordObj -> objectMapper.convertValue(medicalRecordObj, MedicalRecord.class)).collect(Collectors.toList());
	}

	/**
	 * Creates a new medical record.
	 *
	 * @param medicalRecord The medical record to create {@link MedicalRecord}.
	 * @return The created medical record {@link MedicalRecord}.
	 */
	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = allMedicalRecords();
		medicalRecords.add(medicalRecord);
		saveMedicalRecords(medicalRecords);
		log.debug("Add medicalRecord {} in allMedicalRecords() : {}", medicalRecord, medicalRecords);
		return medicalRecord;
	}

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
	public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecord) {
		String fullName = firstName + " " + lastName;
		MedicalRecord existingMedicalRecord = getMedicalRecordByFullName(fullName);
		if (existingMedicalRecord != null) {
			log.debug("Found existing medical record for: {} = {}", fullName, existingMedicalRecord);
			List<MedicalRecord> medicalRecords = allMedicalRecords();
			medicalRecords.set(medicalRecords.indexOf(existingMedicalRecord), medicalRecord);
			saveMedicalRecords(medicalRecords);
			log.debug("Updated medical record list with {} = {}", medicalRecord, medicalRecords);
			return medicalRecord;
		} else {
			throw new NoSuchElementException("Medical record not updated beacause is not found for " + firstName + " " + lastName);
		}
	}

	/**
	 * Deletes a medical record by the full name of the person.
	 *
	 * @param firstName The first name of the person.
	 * @param lastName  The last name of the person.
	 * @return True if the medical record was deleted successfully, false otherwise.
	 */
	public Boolean deleteMedicalRecord(String firstName, String lastName) {
		String fullName = firstName + " " + lastName;
		List<MedicalRecord> medicalRecords = allMedicalRecords();
		boolean medicalRecordDeleted = medicalRecords.removeIf(m -> m.fullName().equals(fullName));
		if (medicalRecordDeleted) {
			saveMedicalRecords(medicalRecords);
			log.debug("Medical record deleted successfully for {}.", fullName);
		}
		return medicalRecordDeleted;
	}

	private void saveMedicalRecords(List<MedicalRecord> listOfMedicalRecords) {
		List<Object> medicalRecordData = new ArrayList<>();
		for (MedicalRecord medicalRecordObj : listOfMedicalRecords) {
			medicalRecordData.add(objectMapper.convertValue(medicalRecordObj, MedicalRecord.class));
		}
		log.debug("Saving medical records to repository: {}", medicalRecordData);
		repository.saveData(TypeOfData.MEDICALRECORDS, medicalRecordData);
	}

	/**
	 * Retrieves a medical record by the full name of the person.
	 *
	 * @param firstName The first name of the person.
	 * @param lastName  The last name of the person.
	 * @return The medical record for the specified person {@link MedicalRecord}.
	 */
	public MedicalRecord getMedicalRecordByFullName(String fullName) {
		return allMedicalRecords().stream().filter(medicalRecord -> medicalRecord.fullName().equals(fullName)).findFirst().orElse(null);
	}

	/**
	 * Retrieves medical records for a list of persons.
	 *
	 * @param persons The list of persons {@link Person}.
	 * @return A list of medical records for the specified persons
	 *         {@link MedicalRecord}.
	 */
	public List<MedicalRecord> getPersonMedicalRecords(List<Person> persons) {
		return persons.stream().map(p -> getMedicalRecordByFullName(p.fullName())).filter(Objects::nonNull).toList();
	}

}
