package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MedicalRecordInfo(@NotBlank String firstName, @NotBlank String lastName, @NotBlank @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$") String phone, @Min(0) int age, @NotNull List<String> medications,
		@NotNull List<String> allergies) {
}
