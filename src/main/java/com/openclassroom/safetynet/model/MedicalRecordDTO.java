package com.openclassroom.safetynet.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(value = { "age", "child", "adult" }, ignoreUnknown = false)

@JsonPropertyOrder({ "firstName", "lastName", "birthdate", "medications", "allergies" })
public record MedicalRecordDTO(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String birthdate, @NotNull List<String> medications,
		@NotNull List<String> allergies) {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	public String fullName() {
		return firstName + " " + lastName;
	}

	public int getAge() {
		LocalDate birthdate = LocalDate.parse(this.birthdate, FORMATTER);
		LocalDate today = LocalDate.now();
		return Period.between(birthdate, today).getYears();
	}

	public boolean isChild() {
		return this.getAge() <= 18;
	}

	public boolean isAdult() {
		return this.getAge() > 18;
	}

}
