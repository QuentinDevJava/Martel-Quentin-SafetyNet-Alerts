package com.openclassroom.safetynet.model;

public record PersonInfo(String firstName, String lastName, String address, String phone) {

	public static PersonInfo extractBasicInfo(Person person) {
		return new PersonInfo(person.firstName(), person.lastName(), person.address(), person.phone());
	}
}