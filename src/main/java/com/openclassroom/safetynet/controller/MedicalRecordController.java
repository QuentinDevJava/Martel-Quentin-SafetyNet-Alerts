package com.openclassroom.safetynet.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
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
import com.openclassroom.safetynet.model.MedicalRecord;
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

	/**
	 * Creates a new medical record.
	 * 
	 * This method processes the incoming POST request to add a new medical record
	 * to the system. It validates the provided {@link MedicalRecord} and calls the
	 * service layer to persist the medical record. Upon successful creation, it
	 * returns a response with a 201 status and the location of the new resource.
	 *
	 * @param medicalRecordDTO The {@link MedicalRecord} containing the details of
	 *                         the medical record to be created.
	 * @return A {@link ResponseEntity} with a status of 201 (Created) and the URI
	 *         of the newly created medical record.
	 * @throws URISyntaxException If the URI for the created resource cannot be
	 *                            constructed.
	 */
	@PostMapping
	public ResponseEntity<ApiResponse> createMedicalRecord(@Validated @RequestBody MedicalRecord medicalRecordDTO) throws URISyntaxException {
		log.info("POST request received for /medicalrecord, adding medical record: {}", medicalRecordDTO);
		medicalRecordService.createMedicalRecord(medicalRecordDTO);
		log.info("Medical record successfully created: {}", medicalRecordDTO);
		String str = "/medicalrecord";
		URI uri = new URI(str);
		return ResponseEntity.created(uri).body(new ApiResponse(201));
	}

	/**
	 * Updates an existing medical record.
	 * 
	 * This method processes the incoming PUT request to update an existing medical
	 * record based on the provided firstName and lastName. It validates the
	 * provided {@link MedicalRecord} and calls the service layer to update the
	 * medical record details. Upon successful update, it returns a response with a
	 * 200 status indicating the medical record was updated successfully.
	 * 
	 *
	 * @param firstName        The firstName of the person medical record to update.
	 * @param lastName         The lastName of the person medical record to update.
	 * @param medicalRecordDTO The {@link MedicalRecord} containing the new medical
	 *                         record details.
	 * @return A {@link ResponseEntity} with a status of 200 (OK) indicating the
	 *         medical record was updated.
	 */
	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<ApiResponse> updateMedicalRecord(@PathVariable @Validated @NotBlank String firstName,
			@PathVariable @Validated @NotBlank String lastName, @Validated @RequestBody MedicalRecord medicalRecordDTO) {
		log.info("PUT request received for /medicalrecord/{}/{} updating medical record: {}", firstName, lastName, medicalRecordDTO);
		medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecordDTO);
		log.info("Medical record successfully updated: {}", medicalRecordDTO);
		return ResponseEntity.ok(new ApiResponse(200));
	}

	/**
	 * Deletes an existing medical record.
	 * 
	 * This method processes the incoming DELETE request to remove a medical record
	 * from the system based on the provided firstName and lastName. It calls the
	 * service layer to delete the medical record and returns a response indicating
	 * whether the deletion was successful.
	 * 
	 *
	 * @param firstName The firstName of the person medical record to delete.
	 * @param lastName  The lastName of the person medical record to delete.
	 * @return A {@link ResponseEntity} with either a 204 (No Content) status if the
	 *         deletion was successful, or a 404 (Not Found) status if the medical
	 *         record was not found.
	 */
	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<ApiResponse> deleteMedicalRecord(@PathVariable @Validated @NotBlank String firstName,
			@PathVariable @Validated @NotBlank String lastName) {
		log.info("DELETE request received for /medicalrecord/{}/{}", firstName, lastName);
		Boolean medicalRecordDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		if (Boolean.TRUE.equals(medicalRecordDeleted)) {
			log.info("Medical record successfully deleted: {} {}", firstName, lastName);
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new ApiResponse(204, "The medical record with firstName: " + firstName + " and lastName: " + lastName + " is delete"));
		} else {
			log.error("Medical record not found: firstName: {}, lastName: {}", firstName, lastName);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(404, "medical record not found: firstName: " + firstName + ", lastName: " + lastName));
		}
	}
}
