package com.openclassroom.safetynet.model;

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

	public Child(PersonResponse person, MedicalRecordResponse medicalRecord) {
		this(person.firstName(), person.lastName(), person.address(), person.phone(), medicalRecord.getAge());
	}

}
