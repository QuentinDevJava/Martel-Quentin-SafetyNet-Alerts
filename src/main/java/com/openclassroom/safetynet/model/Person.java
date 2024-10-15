package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonPropertyOrder({ "firstName", "lastName", "address", "city", "zip", "phone", "email" })
public record Person(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String address, @NotBlank String city, @NotBlank String zip, @NotBlank @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$") String phone,
		@Email String email) {
}
