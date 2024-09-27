package com.openclassroom.safetynet.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "lastName", "birthdate", "medications", "allergies" })
public class MedicalRecord {

	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("birthdate")
	private String birthdate;
	@JsonProperty("medications")
	private List<String> medications;
	@JsonProperty("allergies")
	private List<String> allergies;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public List<String> getMedications() {
		return medications;
	}

	public void setMedications(List<String> medications) {
		this.medications = medications;
	}

	public List<String> getAllergies() {
		return allergies;
	}

	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MedicalRecord that = (MedicalRecord) o;
		return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName)
				&& Objects.equals(birthdate, that.birthdate) && Objects.equals(medications, that.medications)
				&& Objects.equals(allergies, that.allergies);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, birthdate, medications, allergies);
	}

	@Override
	public String toString() {
		return "MedicalRecord{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", birthdate='"
				+ birthdate + '\'' + ", medications=" + medications + ", allergies=" + allergies + '}';
	}
}
