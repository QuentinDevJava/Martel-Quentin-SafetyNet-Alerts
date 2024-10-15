package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonsLastNameInfo(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String address, @Min(0) int age, @Email String email, @NotNull List<String> medications,
		@NotNull List<String> allergies) {

}
