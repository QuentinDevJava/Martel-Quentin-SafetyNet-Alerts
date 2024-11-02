package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
@JsonIgnoreProperties(value = { "fullName" }, ignoreUnknown = false)
@JsonPropertyOrder({ "firstName", "lastName", "address", "city", "zip", "phone", "email" })
public record PersonResponse(String firstName, String lastName, String address, String city, String zip, String phone, String email) {

	public String fullName() {
		return firstName + " " + lastName;
	}
}
