package com.openclassroom.safetynet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.service.FirestationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j

//@RequestMapping("/firestation")

public class FirestationController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final FirestationService firestationService;

	public FirestationController(FirestationService firestationService) {
		this.firestationService = firestationService;
	}

	// @GetMapping("/firestation")
	public ResponseEntity<List<Firestation>> getAllFirestations() {

		logger.info("GET request received for /firestation.");
		List<Firestation> firestations = firestationService.getAllFirestations();
		logger.info("Successfully retrieved {} firestations.", firestations.size());
		return new ResponseEntity<>(firestations, HttpStatus.OK);
	}

	@PostMapping("/firestation")
	public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
		logger.info("POST request received for /firestation, adding firestation: {}", firestation);
		try {
			Firestation createdFirestation = firestationService.createFirestation(firestation);
			logger.info("Firestation successfully created: {}", createdFirestation);
			return new ResponseEntity<>(createdFirestation, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating firestation: {}", firestation, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/firestation/{address}")
	public ResponseEntity<Firestation> updateFirestation(@PathVariable String address, @RequestBody Firestation firestation) {
		logger.info("PUT request received for /firestation/{} updating firestation: {}", address, firestation);
		try {
			Firestation updatedFirestation = firestationService.updateFirestation(address, firestation);
			logger.info("Firestation successfully updated: {}", updatedFirestation);
			return new ResponseEntity<>(updatedFirestation, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error updating firestation with address: {}", address, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/firestation/{address}")
	public ResponseEntity<Void> deleteFirestation(@PathVariable String address) {
		logger.info("DELETE request received for /firestation/{}", address);
		try {
			firestationService.deleteFirestation(address);
			logger.info("Firestation successfully deleted: {}", address);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Error deleting firestation with address: {}", address, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}