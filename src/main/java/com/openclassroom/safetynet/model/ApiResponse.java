package com.openclassroom.safetynet.model;

public record ApiResponse(int status, String message) {

	public ApiResponse(int status) {
		this(status, getStatusMessage(status));
	}

	private static String getStatusMessage(int status) {
		return switch (status) {
		case 200 -> "Successfully";
		case 201 -> "Created";
		default -> throw new IllegalArgumentException("Unexpected value: " + status);
		};
	}
}
