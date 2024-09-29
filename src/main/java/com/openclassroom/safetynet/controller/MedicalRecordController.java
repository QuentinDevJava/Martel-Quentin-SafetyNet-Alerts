package com.openclassroom.safetynet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.service.MedicalRecordService;

@RestController
@RequestMapping("/medicalrecord")
public class MedicalRecordController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final MedicalRecordService medicalRecordService;

	public MedicalRecordController(MedicalRecordService medicalRecordService) {
		this.medicalRecordService = medicalRecordService;
	}

	@GetMapping
	public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
		logger.info("GET request received for /medicalrecord.");
		List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
		logger.info("Successfully retrieved {} medical records.", medicalRecords.size());
		return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		logger.info("POST request received for /medicalrecord, adding medical record: {}", medicalRecord);
		try {
			MedicalRecord createdMedicalRecord = medicalRecordService.createMedicalRecord(medicalRecord);
			logger.info("Medical record successfully created: {}", createdMedicalRecord);
			return new ResponseEntity<>(createdMedicalRecord, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating medical record: {}", medicalRecord, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstName,
			@PathVariable String lastName, @RequestBody MedicalRecord medicalRecord) {
		logger.info("PUT request received for /medicalrecord/{}/{} updating medical record: {}", firstName, lastName,
				medicalRecord);
		try {
			MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(firstName, lastName,
					medicalRecord);
			logger.info("Medical record successfully updated: {}", updatedMedicalRecord);
			return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error updating medical record with first name: {} and last name: {}", firstName, lastName, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
		logger.info("DELETE request received for /medicalrecord/{}/{}", firstName, lastName);
		try {
			medicalRecordService.deleteMedicalRecord(firstName, lastName);
			logger.info("Medical record successfully deleted: {} {}", firstName, lastName);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Error deleting medical record with first name: {} and last name: {}", firstName, lastName, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
