package com.openclassroom.safetynet.dto;

import com.openclassroom.safetynet.model.PersonDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents a person with their basic information.
 *
 * @param firstName The first name of the person.
 * @param lastName  The last name of the person.
 * @param address   The address of the person.
 * @param phone     The phone number of the person in the format "XXX-XXX-XXXX".
 */
public record PersonInfo(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String address,
		@NotBlank @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$") String phone) {

	/**
	 * Constructs a {@link PersonInfo} from a {@link PersonDTO}.
	 *
	 * @param personDto A list of {@link PersonDTO} representing people covered by
	 *                  the station.
	 */
	public PersonInfo(PersonDTO personDto) {
		this(personDto.firstName(), personDto.lastName(), personDto.address(), personDto.phone());
	}
}