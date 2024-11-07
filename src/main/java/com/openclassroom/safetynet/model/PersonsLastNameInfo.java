package com.openclassroom.safetynet.model;

import java.util.List;

/**
 * Represents a person with their basic information.
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

	/**
	 * Constructs a {@link PersonsLastNameInfo} instance using the provided
	 * {@link PersonDTO} and {@link MedicalRecordDTO}.
	 * 
	 * @param personDto        The {@link PersonDTO} object containing the person's
	 *                         basic information.
	 * @param medicalRecordDto The {@link MedicalRecordDTO} object containing the
	 *                         person's medical information.
	 */
	public PersonsLastNameInfo(PersonDTO personDto, MedicalRecordDTO medicalRecordDto) {
		this(personDto.firstName(), personDto.lastName(), personDto.address(), medicalRecordDto.getAge(), personDto.email(),
				medicalRecordDto.medications(), medicalRecordDto.allergies());

	}

}
