package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;
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

	/**
	 * Creates a new medical record.
	 *
	 * @param medicalRecord The medical record to create {@link MedicalRecord}.
	 */
	public void createMedicalRecord(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = allMedicalRecords();
		medicalRecords.add(medicalRecord);
		saveMedicalRecords(medicalRecords);
		log.debug("Add medicalRecord {} in allMedicalRecords() : {}", medicalRecord, medicalRecords);
	}

	/**
	 * Updates an existing medical record.
	 *
	 * @param firstName     The first name of the person whose medical record to
	 *                      update.
	 * @param lastName      The last name of the person whose medical record to
	 *                      update.
	 * @param medicalRecord The updated medical record {@link MedicalRecord}.
	 */
	public void updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecord) {
		String fullName = firstName + " " + lastName;
		MedicalRecord existingMedicalRecord = getMedicalRecordByFullName(fullName);
		if (existingMedicalRecord == null) {
			log.error("Unknown person: {}", fullName);
			throw new IllegalArgumentException("Unknown person: " + fullName);
		}
		log.debug("Found existing medical record for: {} = {}", fullName, existingMedicalRecord);
		List<MedicalRecord> medicalRecords = allMedicalRecords();
		medicalRecords.set(medicalRecords.indexOf(existingMedicalRecord), medicalRecord);
		saveMedicalRecords(medicalRecords);
		log.debug("Updated medical record list with {} = {}", medicalRecord, medicalRecords);
	}

	/**
	 * Deletes a medical record by the full name of the person.
	 *
	 * @param firstName The first name of the person.
	 * @param lastName  The last name of the person.
	 * @return True if the medical record was deleted successfully, false otherwise.
	 */
	public boolean deleteMedicalRecord(String firstName, String lastName) {
		String fullName = firstName + " " + lastName;
		List<MedicalRecord> medicalRecords = allMedicalRecords();
		boolean medicalRecordDeleted = medicalRecords.removeIf(m -> m.fullName().equals(fullName));
		if (medicalRecordDeleted) {
			saveMedicalRecords(medicalRecords);
			log.debug("Medical record deleted successfully for {}.", fullName);
		}
		return medicalRecordDeleted;
	}

	/**
	 * Retrieves a medical record by the full name of the person.
	 *
	 * @param fullName The first name and the last name of the person.
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

	private List<MedicalRecord> allMedicalRecords() {
		return repository.loadTypeOfData(TypeOfData.MEDICALRECORDS).stream()
				.map(medicalRecordObj -> objectMapper.convertValue(medicalRecordObj, MedicalRecord.class))
				.collect((Collectors.toCollection(ArrayList::new)));
	}

	private void saveMedicalRecords(List<MedicalRecord> medicalRecords) {

		repository.saveData(TypeOfData.MEDICALRECORDS, medicalRecords.stream()
				.map(medicalRecordsObj -> objectMapper.convertValue(medicalRecordsObj, MedicalRecord.class)).collect(Collectors.toList()));
	}

}
