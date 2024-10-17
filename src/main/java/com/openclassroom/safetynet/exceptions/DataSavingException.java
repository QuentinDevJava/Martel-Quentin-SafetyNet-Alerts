package com.openclassroom.safetynet.exceptions;

public class DataSavingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataSavingException(String message) {
		super(message);
	}
}