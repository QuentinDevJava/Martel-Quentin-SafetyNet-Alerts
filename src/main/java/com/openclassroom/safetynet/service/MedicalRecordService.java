package com.openclassroom.safetynet.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.CreateMedicalRecordRequest;
import com.openclassroom.safetynet.model.MedicalRecordResponse;
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

	private List<MedicalRecordResponse> allMedicalRecords() {
		return repository.loadTypeOfData(TypeOfData.MEDICALRECORDS).stream()
				.map(medicalRecordObj -> objectMapper.convertValue(medicalRecordObj, MedicalRecordResponse.class)).collect(Collectors.toList());
	}

	/**
	 * Creates a new medical record.
	 *
	 * @param medicalRecord The medical record to create
	 *                      {@link MedicalRecordResponse}.
	 * @return The created medical record {@link MedicalRecordResponse}.
	 */
	public void createMedicalRecord(MedicalRecordResponse medicalRecord) {
		List<MedicalRecordResponse> medicalRecords = allMedicalRecords();
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
	 * @param medicalRecord The updated medical record
	 *                      {@link MedicalRecordResponse}.
	 * @return The updated medical record {@link MedicalRecordResponse}.
	 */
	public void updateMedicalRecord(String firstName, String lastName, MedicalRecordResponse medicalRecord) {
		String fullName = firstName + " " + lastName;
		MedicalRecordResponse existingMedicalRecord = getMedicalRecordByFullName(fullName);
		log.debug("Found existing medical record for: {} = {}", fullName, existingMedicalRecord);
		List<MedicalRecordResponse> medicalRecords = allMedicalRecords();
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
	public Boolean deleteMedicalRecord(String firstName, String lastName) {
		String fullName = firstName + " " + lastName;
		List<MedicalRecordResponse> medicalRecords = allMedicalRecords();
		boolean medicalRecordDeleted = medicalRecords.removeIf(m -> m.fullName().equals(fullName));
		if (medicalRecordDeleted) {
			saveMedicalRecords(medicalRecords);
			log.debug("Medical record deleted successfully for {}.", fullName);
		}
		return medicalRecordDeleted;
	}

	private void saveMedicalRecords(List<MedicalRecordResponse> medicalRecords) {

		repository.saveData(TypeOfData.MEDICALRECORDS, medicalRecords.stream()
				.map(medicalRecordsObj -> objectMapper.convertValue(medicalRecordsObj, MedicalRecordResponse.class)).collect(Collectors.toList()));
	}

	/**
	 * Retrieves a medical record by the full name of the person.
	 *
	 * @param firstName The first name of the person.
	 * @param lastName  The last name of the person.
	 * @return The medical record for the specified person
	 *         {@link MedicalRecordResponse}.
	 */
	public MedicalRecordResponse getMedicalRecordByFullName(String fullName) {
		return allMedicalRecords().stream().filter(medicalRecord -> medicalRecord.fullName().equals(fullName)).findFirst().orElse(null);
	}

	/**
	 * Retrieves medical records for a list of persons.
	 *
	 * @param persons The list of persons {@link Person}.
	 * @return A list of medical records for the specified persons
	 *         {@link MedicalRecordResponse}.
	 */
	public List<MedicalRecordResponse> getPersonMedicalRecords(List<Person> persons) {
		return persons.stream().map(p -> getMedicalRecordByFullName(p.fullName())).filter(Objects::nonNull).toList();
	}

	/**
	 * Calculates the age of a person based on their birthdate.
	 *
	 * @param person The person to calculate the age for {@link Person}.
	 * @return The age of the person.
	 */
	public int getAge(Person person) {
		MedicalRecordResponse medicalRecord = getMedicalRecordByFullName(person.fullName());
		if (medicalRecord != null) {
			return medicalRecord.getAge();
		}
		return -1;
	}

	public MedicalRecordResponse medicalRecordRequestToMedicalRecordResponse(CreateMedicalRecordRequest request) {
		return new MedicalRecordResponse(request.firstName(), request.lastName(), request.birthdate(), request.medications(), request.allergies());
	}

}
