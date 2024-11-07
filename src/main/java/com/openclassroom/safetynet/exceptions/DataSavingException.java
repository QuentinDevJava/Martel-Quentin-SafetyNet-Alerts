package com.openclassroom.safetynet.exceptions;

/**
 * Exception thrown when there is an error during data saving or processing.
 * 
 * This exception is used to indicate issues when saving or persisting data,
 * such as when a file cannot be written, data cannot be saved to a database, or
 * other errors occur during data saving operations.
 * 
 */
public class DataSavingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code DataSavingException} with the specified detail
	 * message.
	 * 
	 * The detail message is saved for later retrieval by the {@link #getMessage()}
	 * method.
	 * 
	 *
	 * @param message The detail message (which can later be retrieved by the
	 *                {@link #getMessage()} method).
	 */
	public DataSavingException(String message) {
		super(message);
	}
}