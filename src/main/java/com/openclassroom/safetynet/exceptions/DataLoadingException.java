package com.openclassroom.safetynet.exceptions;

/**
 * Exception thrown when there is an error during data loading or processing.
 *
 * This exception is used to indicate issues when loading or processing data,
 * such as when a file cannot be read, data is malformed, or there are other
 * errors during data initialization.
 * 
 */
public class DataLoadingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code DataLoadingException} with the specified detail
	 * message.
	 * 
	 * The detail message is saved for later retrieval by the {@link #getMessage()}
	 * method.
	 * 
	 *
	 * @param message The detail message (which can later be retrieved by the
	 *                {@link #getMessage()} method).
	 */
	public DataLoadingException(String message) {
		super(message);
	}
}