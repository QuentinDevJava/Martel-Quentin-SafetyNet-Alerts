package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Represents a person with their basic information.
 *
 * @param firstName The first name of the person.
 * @param lastName  The last name of the person.
 * @param address   The address of the person.
 * @param city      The city of the person.
 * @param zip       The zip code of the person.
 * @param phone     The phone number of the person in the format "XXX-XXX-XXXX".
 * @param email     The email address of the person.
 */
@JsonPropertyOrder({ "firstName", "lastName", "address", "city", "zip", "phone", "email" })
public record Person(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String address, @NotBlank String city, @NotBlank String zip, @NotBlank @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$") String phone,
		@NotNull @Email String email) {
	public String fullName() {
		return firstName + " " + lastName;
	}
}
