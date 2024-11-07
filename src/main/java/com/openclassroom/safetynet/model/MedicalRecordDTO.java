package com.openclassroom.safetynet.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents medical record information for a person.
 *
 * @param firstName   The first name of the person.
 * @param lastName    The last name of the person.
 * @param birthdate   The birthdate of the person.
 * @param medications A list of medications the person is taking.
 * @param allergies   A list of allergies the person has.
 */

@JsonIgnoreProperties(value = { "age", "child", "adult" }, ignoreUnknown = false)
@JsonPropertyOrder({ "firstName", "lastName", "birthdate", "medications", "allergies" })
public record MedicalRecordDTO(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String birthdate, @NotNull List<String> medications,
		@NotNull List<String> allergies) {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	/**
	 * Returns the full name of the person by combining the first name and last
	 * name.
	 *
	 * @return A string representing the full name of the person, combining the
	 *         first and last names.
	 */

	public String fullName() {
		return firstName + " " + lastName;
	}

	/**
	 * Calculates the person's age based on the provided birthdate.
	 * 
	 * The birthdate is parsed from the string value of {@code birthdate}
	 * ("format:"MM/dd/yyyy").
	 *
	 * @return The person's age in years.
	 */

	public int getAge() {
		LocalDate birthdate = LocalDate.parse(this.birthdate, FORMATTER);
		LocalDate today = LocalDate.now();
		return Period.between(birthdate, today).getYears();
	}

	/**
	 * Checks if the person is a child (under 18 years old).
	 *
	 * @return True if the person is a child, false otherwise.
	 */
	public boolean isChild() {
		return this.getAge() <= 18;
	}

	/**
	 * Checks if the person is an adult (over 18 years old).
	 *
	 * @return True if the person is an adult, false otherwise.
	 */
	public boolean isAdult() {
		return this.getAge() > 18;
	}

}
