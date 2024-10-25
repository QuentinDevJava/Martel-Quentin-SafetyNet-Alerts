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
	public ResponseEntity<Firestation> createFirestation(@Validated @RequestBody Firestation firestation) {
		log.info("POST request received for /firestation, adding firestation: {}", firestation);
		try {
			firestationService.createFirestation(firestation);
			log.info("Firestation successfully created: {}", firestation);
			URI uri = new URI("/firestation");
			return ResponseEntity.created(uri).body(firestation);

		} catch (Exception e) {
			log.error("Error creating firestation: {}", firestation, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping("/{address}")
	public ResponseEntity<Firestation> updateFirestation(@PathVariable String address, @Validated @RequestBody Firestation firestation) {
		log.info("PUT request received for /firestation/{} updating firestation: {}", address, firestation);
		try {
			firestationService.updateFirestation(address, firestation);
			log.info("Firestation successfully updated: {}", firestation);
			return ResponseEntity.ok(firestation);
		} catch (Exception e) {
			log.error("Error updating firestation with address: {}", address, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@DeleteMapping("/{addressOrNum}")
	public ResponseEntity<Void> deleteFirestation(@PathVariable String addressOrNum) {
		log.info("DELETE request received for /firestation/{}", addressOrNum);
		try {
			Boolean firestationsDeleted = firestationService.deleteFirestation(addressOrNum);
			if (Boolean.TRUE.equals(firestationsDeleted)) {
				log.info("Firestation successfully deleted: {}", addressOrNum);
				return ResponseEntity.noContent().build();
			} else {
				log.error("Firestation not found: address: {}", addressOrNum);
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			log.error("Error deleting firestation with address: {}", addressOrNum, e);
			return ResponseEntity.internalServerError().build();
		}
	}

}