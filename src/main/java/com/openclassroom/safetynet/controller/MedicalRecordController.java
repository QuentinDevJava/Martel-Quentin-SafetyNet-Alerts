package com.openclassroom.safetynet.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.MedicalRecordRequest;
import com.openclassroom.safetynet.model.MedicalRecordResponse;
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
	public ResponseEntity<MedicalRecordResponse> createMedicalRecord(@Validated @RequestBody MedicalRecordRequest medicalRecordRequest) {
		log.info("POST request received for /medicalrecord, adding medical record: {}", medicalRecordRequest);
		try {
			MedicalRecordResponse medicalRecordResponse = medicalRecordService.medicalRecordRequestToMedicalRecordResponse(medicalRecordRequest);
			medicalRecordService.createMedicalRecord(medicalRecordResponse);

			log.info("Medical record successfully created: {}", medicalRecordResponse);
			URI uri = new URI("/medicalrecord");
			return ResponseEntity.created(uri).body(medicalRecordResponse);
		} catch (Exception e) {
			log.error("Error creating medical record: {}", medicalRecordRequest, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<MedicalRecordResponse> updateMedicalRecord(@PathVariable String firstName, @PathVariable String lastName,
			@Validated @RequestBody MedicalRecordRequest medicalRecordRequest) {
		log.info("PUT request received for /medicalrecord/{}/{} updating medical record: {}", firstName, lastName, medicalRecordRequest);
		try {
			MedicalRecordResponse medicalRecordResponse = medicalRecordService.medicalRecordRequestToMedicalRecordResponse(medicalRecordRequest);

			medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecordResponse);
			log.info("Medical record successfully updated: {}", medicalRecordResponse);
			return ResponseEntity.ok(medicalRecordResponse);
		} catch (Exception e) {
			log.error("Error updating medical record with first name: {} and last name: {}", firstName, lastName, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
		log.info("DELETE request received for /medicalrecord/{}/{}", firstName, lastName);
		try {
			Boolean medicalRecordDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
			if (Boolean.TRUE.equals(medicalRecordDeleted)) {
				log.info("Medical record successfully deleted: {} {}", firstName, lastName);
				return ResponseEntity.noContent().build();
			} else {
				log.error("Medical record not found: firstName: {}, lastName: {}", firstName, lastName);
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			log.error("Error deleting medical record with first name: {} and last name: {}", firstName, lastName, e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
