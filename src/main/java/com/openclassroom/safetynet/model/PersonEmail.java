package com.openclassroom.safetynet.model;

import java.util.List;

/**
 * Represents a list of email addresses.
 *
 * @param email A list of email addresses.
 */
public record PersonEmail(List<String> email) {
}
