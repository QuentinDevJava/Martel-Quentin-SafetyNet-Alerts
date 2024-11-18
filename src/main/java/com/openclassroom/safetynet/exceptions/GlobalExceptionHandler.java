package com.openclassroom.safetynet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.openclassroom.safetynet.dto.ApiResponse;

/**
 * Global exception handler for REST controller methods.
 *
 * This class provides centralized handling of common exceptions, ensuring
 * consistent error responses are returned to clients.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles validation errors that occur during request parameter binding.
	 *
	 * @param ex The {@link MethodArgumentNotValidException} thrown during
	 *           validation.
	 * @return A {@link ResponseEntity} with a {@link HttpStatus#BAD_REQUEST} status
	 *         code and an {@link ApiResponse} object containing the error message.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(400, ex.getMessage()));
	}

	/**
	 * Handles validation errors that occur during request parameter binding.
	 *
	 * @param ex The {@link MethodArgumentNotValidException} thrown during
	 *           validation.
	 * @return A {@link ResponseEntity} with a {@link HttpStatus#BAD_REQUEST} status
	 *         code and an {@link ApiResponse} object containing the error message.
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse> handleValidationExceptions(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(404, ex.getMessage()));
	}
}
