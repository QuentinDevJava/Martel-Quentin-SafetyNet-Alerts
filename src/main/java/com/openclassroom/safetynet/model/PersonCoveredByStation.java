package com.openclassroom.safetynet.model;

import java.util.List;

/**
 * Represents information about persons covered by a fire station, including
 * adult and child counts.
 *
 * @param personInfos A list of basic information about each person covered by
 *                    the station {@link PersonInfo}.
 * @param adultCounts The number of adults covered by the station.
 * @param childCounts The number of children covered by the station.
 */
public record PersonCoveredByStation(List<PersonInfo> personInfos, int adultCounts, int childCounts) {

	/**
	 * Constructs a {@link PersonCoveredByStation} from a list of {@link PersonDTO}
	 * and {@link MedicalRecordDTO}.
	 *
	 * This constructor takes two lists: a list of persons ({@link PersonDTO}) and a
	 * list of medical records ({@link MedicalRecordDTO}). It maps each person into
	 * a {@link PersonInfo} object, and counts the number of adults and children by
	 * inspecting the medical records.
	 * 
	 *
	 * @param persons        A list of {@link PersonDTO} representing people covered
	 *                       by the station.
	 * @param medicalRecords A list of {@link MedicalRecordDTO} representing the
	 *                       medical records for each person.
	 */
	public PersonCoveredByStation(List<PersonDTO> persons, List<MedicalRecordDTO> medicalRecords) {
		this(persons.stream().map(PersonInfo::new).toList(), (int) medicalRecords.stream().filter(MedicalRecordDTO::isAdult).count(),
				(int) medicalRecords.stream().filter(MedicalRecordDTO::isChild).count());
	}
}
