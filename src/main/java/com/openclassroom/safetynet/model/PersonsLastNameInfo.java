package com.openclassroom.safetynet.model;

import java.util.List;

/**
 * Represents information about a person.
 *
 * @param firstName   The first name of the person.
 * @param lastName    The last name of the person.
 * @param address     The address of the person.
 * @param age         The age of the person.
 * @param email       The email address of the person.
 * @param medications A list of medications the person is taking.
 * @param allergies   A list of allergies the person has.
 */
public record PersonsLastNameInfo(String firstName, String lastName, String address, int age, String email, List<String> medications,
		List<String> allergies) {

	public PersonsLastNameInfo(PersonResponse person, MedicalRecordResponse medicalRecord) {
		this(person.firstName(), person.lastName(), person.address(), medicalRecord.getAge(), person.email(), medicalRecord.medications(),
				medicalRecord.allergies());

	}

}
