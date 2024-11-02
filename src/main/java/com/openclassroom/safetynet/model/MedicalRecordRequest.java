package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicalRecordRequest(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String birthdate,
		@NotNull List<String> medications, @NotNull List<String> allergies) {

}
