package com.openclassroom.safetynet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "lastName", "birthdate", "medications", "allergies" })
public record MedicalRecord(String firstName, String lastName, String birthdate, List<String> medications,
		List<String> allergies) {
}
