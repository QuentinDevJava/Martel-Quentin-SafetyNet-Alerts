package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
public record PersonsLastNameInfo(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String address, @Min(0) int age,
		@Email String email, @NotNull List<String> medications, @NotNull List<String> allergies) {

	public PersonsLastNameInfo(Person person, MedicalRecordResponse medicalRecord) {
		this(person.firstName(), person.lastName(), person.address(), medicalRecord.getAge(), person.email(), medicalRecord.medications(),
				medicalRecord.allergies());

	}

}
