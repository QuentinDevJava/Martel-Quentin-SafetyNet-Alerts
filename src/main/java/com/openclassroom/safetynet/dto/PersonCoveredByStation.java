package com.openclassroom.safetynet.dto;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;

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
	 * Constructs a {@link PersonCoveredByStation} from a list of {@link Person}
	 * and {@link MedicalRecord}.
	 *
	 * This constructor takes two lists: a list of persons ({@link Person}) and a
	 * list of medical records ({@link MedicalRecord}). It maps each person into
	 * a {@link PersonInfo} object, and counts the number of adults and children by
	 * inspecting the medical records.
	 * 
	 *
	 * @param persons        A list of {@link Person} representing people covered
	 *                       by the station.
	 * @param medicalRecords A list of {@link MedicalRecord} representing the
	 *                       medical records for each person.
	 */
	public PersonCoveredByStation(List<Person> persons, List<MedicalRecord> medicalRecords) {
		this(persons.stream().map(PersonInfo::new).toList(), (int) medicalRecords.stream().filter(MedicalRecord::isAdult).count(),
				(int) medicalRecords.stream().filter(MedicalRecord::isChild).count());
	}
}
