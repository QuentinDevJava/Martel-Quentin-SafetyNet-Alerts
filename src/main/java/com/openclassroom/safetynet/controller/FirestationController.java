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

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.service.FirestationService;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

	private final FirestationService firestationService;

	public FirestationController(FirestationService firestationService) {
		this.firestationService = firestationService;
	}

	@PostMapping
	public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
		Firestation createdFirestation = firestationService.createFirestation(firestation);
		return new ResponseEntity<>(createdFirestation, HttpStatus.CREATED);
	}

	@PutMapping("/{address}")
	public ResponseEntity<Firestation> updateFirestation(@PathVariable String address,
			@RequestBody Firestation firestation) {
		Firestation updatedFirestation = firestationService.updateFirestation(address, firestation);
		return new ResponseEntity<>(updatedFirestation, HttpStatus.OK);
	}

	@DeleteMapping("/{address}")
	public ResponseEntity<Void> deleteFirestation(@PathVariable String address) {
		firestationService.deleteFirestation(address);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping
	public ResponseEntity<List<Firestation>> getAllFirestations() {
		List<Firestation> firestations = firestationService.getAllFirestations();
		return new ResponseEntity<>(firestations, HttpStatus.OK);
	}
}
