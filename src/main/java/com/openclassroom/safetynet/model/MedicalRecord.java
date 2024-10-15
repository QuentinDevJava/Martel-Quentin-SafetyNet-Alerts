package com.openclassroom.safetynet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "lastName", "birthdate", "medications", "allergies" })
public record MedicalRecord(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String birthdate, @NotNull List<String> medications, @NotNull List<String> allergies) {

}
