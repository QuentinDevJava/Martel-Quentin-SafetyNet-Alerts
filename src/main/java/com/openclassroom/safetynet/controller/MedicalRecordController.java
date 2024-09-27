package com.openclassroom.safetynet.controller;

import java.util.List;

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

	private final MedicalRecordService medicalRecordService;

	public MedicalRecordController(MedicalRecordService medicalRecordService) {
		this.medicalRecordService = medicalRecordService;
	}

	@GetMapping
	public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
		List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
		return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		MedicalRecord createdMedicalRecord = medicalRecordService.createMedicalRecord(medicalRecord);
		return new ResponseEntity<>(createdMedicalRecord, HttpStatus.CREATED);
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstName,
			@PathVariable String lastName, @RequestBody MedicalRecord medicalRecord) {
		MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(firstName, lastName,
				medicalRecord);
		return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
		medicalRecordService.deleteMedicalRecord(firstName, lastName);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
