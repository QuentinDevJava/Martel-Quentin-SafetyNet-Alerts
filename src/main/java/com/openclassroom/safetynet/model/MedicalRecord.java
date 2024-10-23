package com.openclassroom.safetynet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a medical record for a person.
 *
 * @param firstName   The first name of the person.
 * @param lastName    The last name of the person.
 * @param birthdate   The birthdate of the person.
 * @param medications A list of medications the person is taking.
 * @param allergies   A list of allergies the person has.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "lastName", "birthdate", "medications", "allergies" })
public record MedicalRecord(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String birthdate, @NotNull List<String> medications, @NotNull List<String> allergies) {
	public String fullName() {
		return firstName + " " + lastName;
	}
}
