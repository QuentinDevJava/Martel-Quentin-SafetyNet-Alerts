package com.openclassroom.safetynet.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.service.MedicalRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for managing medical records data.
 *
 * This controller provides endpoints for creating, updating, and deleting
 * medical records.
 */
@RestController
@Slf4j
@RequestMapping("/medicalrecord")
@RequiredArgsConstructor
public class MedicalRecordController {

	private final MedicalRecordService medicalRecordService;

	@PostMapping
	public ResponseEntity<MedicalRecord> createMedicalRecord(@Validated @RequestBody MedicalRecord medicalRecord) {
		log.info("POST request received for /medicalrecord, adding medical record: {}", medicalRecord);
		try {
			MedicalRecord createdMedicalRecord = medicalRecordService.createMedicalRecord(medicalRecord);
			log.info("Medical record successfully created: {}", createdMedicalRecord);
			return new ResponseEntity<>(createdMedicalRecord, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error creating medical record: {}", medicalRecord, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstName, @PathVariable String lastName, @Validated @RequestBody MedicalRecord medicalRecord) {
		log.info("PUT request received for /medicalrecord/{}/{} updating medical record: {}", firstName, lastName, medicalRecord);
		try {
			MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecord);
			log.info("Medical record successfully updated: {}", updatedMedicalRecord);
			return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error updating medical record with first name: {} and last name: {}", firstName, lastName, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
		log.info("DELETE request received for /medicalrecord/{}/{}", firstName, lastName);
		try {
			Boolean medicalRecordDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
			if (Boolean.TRUE.equals(medicalRecordDeleted)) {
				log.info("Medical record successfully deleted: {} {}", firstName, lastName);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				log.error("Medical record not found: firstName: {}, lastName: {}", firstName, lastName);
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			log.error("Error deleting medical record with first name: {} and last name: {}", firstName, lastName, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
