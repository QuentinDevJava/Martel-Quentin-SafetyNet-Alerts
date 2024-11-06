package com.openclassroom.safetynet.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.ApiResponse;
import com.openclassroom.safetynet.model.MedicalRecordDTO;
import com.openclassroom.safetynet.service.MedicalRecordService;

import jakarta.validation.constraints.NotBlank;
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
	public ResponseEntity<ApiResponse> createMedicalRecord(@Validated @RequestBody MedicalRecordDTO medicalRecordDTO) throws URISyntaxException {
		log.info("POST request received for /medicalrecord, adding medical record: {}", medicalRecordDTO);
		medicalRecordService.createMedicalRecord(medicalRecordDTO);
		log.info("Medical record successfully created: {}", medicalRecordDTO);
		String str = "/medicalrecord";
		URI uri = new URI(str);
		return ResponseEntity.created(uri).body(new ApiResponse(201));
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<ApiResponse> updateMedicalRecord(@PathVariable @Validated @NotBlank String firstName,
			@PathVariable @Validated @NotBlank String lastName, @Validated @RequestBody MedicalRecordDTO medicalRecordDTO) {
		log.info("PUT request received for /medicalrecord/{}/{} updating medical record: {}", firstName, lastName, medicalRecordDTO);
		medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecordDTO);
		log.info("Medical record successfully updated: {}", medicalRecordDTO);
		return ResponseEntity.ok(new ApiResponse(200));
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable @Validated @NotBlank String firstName,
			@PathVariable @Validated @NotBlank String lastName) {
		log.info("DELETE request received for /medicalrecord/{}/{}", firstName, lastName);
		Boolean medicalRecordDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		if (Boolean.TRUE.equals(medicalRecordDeleted)) {
			log.info("Medical record successfully deleted: {} {}", firstName, lastName);
			return ResponseEntity.noContent().build();
		} else {
			log.error("Medical record not found: firstName: {}, lastName: {}", firstName, lastName);
			return ResponseEntity.notFound().build();
		}
	}
}
