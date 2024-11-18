package com.openclassroom.safetynet.dto;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;

/**
 * Represents a child with their basic information.
 *
 * @param firstName The first name of the child.
 * @param lastName  The last name of the child.
 * @param address   The address of the child.
 * @param phone     The phone number of the child in the format "XXX-XXX-XXXX".
 * @param age       The age of the child.
 */
public record Child(String firstName, String lastName, String address, String phone, int age) {

	/**
	 * Constructs a {@code Child} instance by combining information from a
	 * {@code PersonDTO} and a {@code MedicalRecordDTO}. The child's first name,
	 * last name, address, and phone are taken from the {@code PersonDTO}, while the
	 * child's age is derived from the {@code MedicalRecordDTO}.
	 *
	 * @param person        The {@code PersonDTO} object containing the child's
	 *                      personal details.
	 * @param medicalRecord The {@code MedicalRecordDTO} object containing the
	 *                      child's age.
	 */
	public Child(Person person, MedicalRecord medicalRecord) {
		this(person.firstName(), person.lastName(), person.address(), person.phone(), medicalRecord.getAge());
	}

}
