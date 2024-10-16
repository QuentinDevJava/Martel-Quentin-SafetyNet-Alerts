package com.openclassroom.safetynet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents basic information about a person.
 *
 * @param firstName The first name of the person.
 * @param lastName  The last name of the person.
 * @param address   The address of the person.
 * @param phone     The phone number of the person in the format "XXX-XXX-XXXX".
 */
public record PersonInfo(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String address, @NotBlank @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$") String phone) {

}