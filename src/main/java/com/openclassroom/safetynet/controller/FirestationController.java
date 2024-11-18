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

import com.openclassroom.safetynet.dto.ApiResponse;
import com.openclassroom.safetynet.model.FirestationDTO;
import com.openclassroom.safetynet.service.FirestationService;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for managing fire station data.
 *
 * This controller provides endpoints for creating, updating, and deleting fire
 * station.
 */

@RestController
@Slf4j
@RequestMapping("/firestation")
@RequiredArgsConstructor

public class FirestationController {
	private final FirestationService firestationService;

	/**
	 * Creates a new firestation.
	 * 
	 * This method processes the incoming POST request to add a new firestation to
	 * the system. It validates the provided {@link FirestationDTO} and calls the
	 * service layer to persist the firestation. Upon successful creation, it
	 * returns a response with a 201 status and the location of the new resource.
	 *
	 * @param firestationDTO The {@link FirestationDTO} containing the details of
	 *                       the firestation to be created.
	 * @return A {@link ResponseEntity} with a status of 201 (Created) and the URI
	 *         of the newly created firestation.
	 * @throws URISyntaxException If the URI for the created resource cannot be
	 *                            constructed.
	 */
	@PostMapping()
	public ResponseEntity<ApiResponse> createFirestation(@Validated @RequestBody FirestationDTO firestationDTO) throws URISyntaxException {
		log.info("POST request received for /firestation, adding firestation: {}", firestationDTO);
		firestationService.createFirestation(firestationDTO);
		log.info("Firestation successfully created: {}", firestationDTO);
		String str = "/firestation";
		URI uri = new URI(str);
		return ResponseEntity.created(uri).body(new ApiResponse(201));
	}

	/**
	 * Updates an existing firestation.
	 * 
	 * This method processes the incoming PUT request to update an existing
	 * firestation based on the provided address. It validates the provided
	 * {@link FirestationDTO} and calls the service layer to update the firestation
	 * details. Upon successful update, it returns a response with a 200 status
	 * indicating the firestation was updated successfully.
	 * 
	 *
	 * @param address        The address of the firestation to update.
	 * @param firestationDTO The {@link FirestationDTO} containing the new
	 *                       firestation details.
	 * @return A {@link ResponseEntity} with a status of 200 (OK) indicating the
	 *         firestation was updated.
	 */
	@PutMapping("/{address}")
	public ResponseEntity<ApiResponse> updateFirestation(@PathVariable @Validated @NotBlank String address,
			@Validated @RequestBody FirestationDTO firestationDTO) {
		log.info("PUT request received for /firestation/{} updating firestation: {}", address, firestationDTO);
		firestationService.updateFirestation(address, firestationDTO);
		log.info("Firestation successfully updated: {}", firestationDTO);
		return ResponseEntity.ok(new ApiResponse(200));
	}

	/**
	 * Deletes an existing firestation.
	 * 
	 * This method processes the incoming DELETE request to remove a firestation
	 * from the system based on the provided address or number. It calls the service
	 * layer to delete the firestation and returns a response indicating whether the
	 * deletion was successful.
	 * 
	 *
	 * @param addressOrNum The address or station number of the firestation to
	 *                     delete.
	 * @return A {@link ResponseEntity} with either a 204 (No Content) status if the
	 *         deletion was successful, or a 404 (Not Found) status if the
	 *         firestation was not found.
	 */
	@DeleteMapping("/{addressOrNum}")
	public ResponseEntity<Void> deleteFirestation(@PathVariable @Validated @NotBlank String addressOrNum) {
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