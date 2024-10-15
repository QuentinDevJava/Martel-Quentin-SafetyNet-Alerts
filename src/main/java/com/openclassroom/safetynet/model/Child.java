package com.openclassroom.safetynet.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Child(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String address, @NotBlank @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$") String phone, @Min(0) int age) {
}
