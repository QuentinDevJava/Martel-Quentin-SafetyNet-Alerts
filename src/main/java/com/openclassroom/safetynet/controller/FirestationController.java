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

import com.openclassroom.safetynet.model.FirestationRequest;
import com.openclassroom.safetynet.model.FirestationResponse;
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
	public ResponseEntity<FirestationResponse> createFirestation(@Validated @RequestBody FirestationRequest firestationRequest) {
		log.info("POST request received for /firestation, adding firestation: {}", firestationRequest);
		try {

			FirestationResponse firestationResponse = firestationService.firestationRequestToFirestationResponse(firestationRequest);

			firestationService.createFirestation(firestationResponse);
			log.info("Firestation successfully created: {}", firestationResponse);
			URI uri = new URI("/firestation");
			return ResponseEntity.created(uri).body(firestationResponse);

		} catch (Exception e) {
			log.error("Error creating firestation: {}", firestationRequest, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping("/{address}")
	public ResponseEntity<FirestationResponse> updateFirestation(@PathVariable String address,
			@Validated @RequestBody FirestationRequest firestationRequest) {
		log.info("PUT request received for /firestation/{} updating firestation: {}", address, firestationRequest);
		try {
			FirestationResponse firestationResponse = firestationService.firestationRequestToFirestationResponse(firestationRequest);

			firestationService.updateFirestation(address, firestationResponse);
			log.info("Firestation successfully updated: {}", firestationResponse);
			return ResponseEntity.ok(firestationResponse);
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