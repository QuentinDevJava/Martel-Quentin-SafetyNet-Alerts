package com.openclassroom.safetynet.model;

import com.openclassroom.safetynet.service.PersonService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents a child with their basic information.
 *
 * @param firstName The first name of the child.
 * @param lastName  The last name of the child.
 * @param address   The address of the child.
 * @param phone     The phone number of the child in the format "XXX-XXX-XXXX".
 * @param age       The age of the child.
 */
public record Child(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String address,
		@NotBlank @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$") String phone, @Min(0) int age) {

	public Child(Person person, PersonService personService) {
		this(person.firstName(), person.lastName(), person.address(), person.phone(), personService.getPersonAge(person));
	}

}
