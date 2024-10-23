package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a list of email addresses.
 *
 * @param email A list of email addresses.
 */
public record PersonEmail(@NotNull @Email List<String> email) {
}
