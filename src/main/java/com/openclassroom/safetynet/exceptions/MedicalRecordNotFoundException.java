package com.openclassroom.safetynet.exceptions;

public class MedicalRecordNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MedicalRecordNotFoundException(String message) {
		super(message);
	}
}