package com.openclassroom.safetynet.model;

public record ApiResponse(int status, String message) {

	public ApiResponse(int status) {
		this(status, getStatusMessage(status));
	}

	public ApiResponse(String message) {
		this(400, message);
	}

	private static String getStatusMessage(int status) {
		return switch (status) {
		case 200 -> "Successfully";
		case 201 -> "Created";
		default -> throw new IllegalArgumentException("Unexpected value: " + status);
		};
	}
}
