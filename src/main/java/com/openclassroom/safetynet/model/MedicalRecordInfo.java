package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Represents medical record information for a person.
 *
 * @param firstName   The first name of the person.
 * @param lastName    The last name of the person.
 * @param phone       The phone number of the person in the format
 *                    "XXX-XXX-XXXX".
 * @param age         The age of the person.
 * @param medications A list of medications the person is taking.
 * @param allergies   A list of allergies the person has.
 */
public record MedicalRecordInfo(@NotBlank String firstName, @NotBlank String lastName, @NotBlank @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$") String phone, @Min(0) int age, @NotNull List<String> medications,
		@NotNull List<String> allergies) {
}
