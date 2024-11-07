package com.openclassroom.safetynet.model;

/**
 * Represents an API response with a status code and a message.
 *
 * This record provides a convenient way to encapsulate API responses and
 * automatically generate a message based on the status code.
 *
 * @param status  The HTTP status code of the response.
 * @param message The message associated with the response.
 */
public record ApiResponse(int status, String message) {

	/**
	 * Constructs an ApiResponse with a given status code and a default message
	 * based on the status code.
	 *
	 * @param status The HTTP status code of the response.
	 */
	public ApiResponse(int status) {
		this(status, getStatusMessage(status));
	}

	/**
	 * Returns a message based on the given status code.
	 *
	 * @param status The HTTP status code.
	 * @return The message corresponding to the status code.
	 * @throws IllegalArgumentException if the status code is not recognized.
	 */
	private static String getStatusMessage(int status) {
		return switch (status) {
		case 200 -> "Successfully";
		case 201 -> "Created";
		default -> throw new IllegalArgumentException("Unexpected value: " + status);
		};
	}
}
