package com.openclassroom.safetynet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.service.FirestationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/firestation")
@RequiredArgsConstructor

public class FirestationController {
	private final FirestationService firestationService;

	@PostMapping()
	public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
		log.info("POST request received for /firestation, adding firestation: {}", firestation);
		try {
			Firestation createdFirestation = firestationService.createFirestation(firestation);
			log.info("Firestation successfully created: {}", createdFirestation);
			return new ResponseEntity<>(createdFirestation, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error creating firestation: {}", firestation, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{address}")
	public ResponseEntity<Firestation> updateFirestation(@PathVariable String address, @RequestBody Firestation firestation) {
		log.info("PUT request received for /firestation/{} updating firestation: {}", address, firestation);
		try {
			Firestation updatedFirestation = firestationService.updateFirestation(address, firestation);
			log.info("Firestation successfully updated: {}", updatedFirestation);
			return new ResponseEntity<>(updatedFirestation, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error updating firestation with address: {}", address, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{address}")
	public ResponseEntity<Void> deleteFirestation(@PathVariable String address) {
		log.info("DELETE request received for /firestation/{}", address);
		try {
			Boolean firestationsDeleted = firestationService.deleteFirestation(address);
			if (Boolean.TRUE.equals(firestationsDeleted)) {
				log.info("Firestation successfully deleted: {}", address);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				log.error("Firestation not found: address: {}", address);
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			log.error("Error deleting firestation with address: {}", address, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}