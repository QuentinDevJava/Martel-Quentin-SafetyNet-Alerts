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
import com.openclassroom.safetynet.model.FirestationDTO;
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
	public ResponseEntity<ApiResponse> createFirestation(@Validated @RequestBody FirestationDTO firestationDTO) throws URISyntaxException {
		log.info("POST request received for /firestation, adding firestation: {}", firestationDTO);
		firestationService.createFirestation(firestationDTO);
		log.info("Firestation successfully created: {}", firestationDTO);
		String str = "/firestation";
		URI uri = new URI(str);
		return ResponseEntity.created(uri).body(new ApiResponse(201));
	}

	@PutMapping("/{address}")
	public ResponseEntity<ApiResponse> updateFirestation(@PathVariable String address, @Validated @RequestBody FirestationDTO firestationDTO) {
		log.info("PUT request received for /firestation/{} updating firestation: {}", address, firestationDTO);
		firestationService.updateFirestation(address, firestationDTO);
		log.info("Firestation successfully updated: {}", firestationDTO);
		return ResponseEntity.ok(new ApiResponse(200));
	}

	@DeleteMapping("/{addressOrNum}")
	public ResponseEntity<Void> deleteFirestation(@PathVariable String addressOrNum) {
		log.info("DELETE request received for /firestation/{}", addressOrNum);
		Boolean firestationsDeleted = firestationService.deleteFirestation(addressOrNum);
		if (Boolean.TRUE.equals(firestationsDeleted)) {
			log.info("Firestation successfully deleted: {}", addressOrNum);
			return ResponseEntity.noContent().build();
		} else {
			log.error("Firestation not found: address: {}", addressOrNum);
			return ResponseEntity.notFound().build();
		}

	}

}