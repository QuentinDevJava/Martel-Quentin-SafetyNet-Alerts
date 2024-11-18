
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
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.service.PersonService;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for managing persons data.
 *
 * This controller provides endpoints for creating, updating, and deleting
 * person records.
 */
@RestController
@RequestMapping("/person")
@Slf4j
@RequiredArgsConstructor
public class PersonController {

	private final PersonService personService;

	/**
	 * Creates a new person.
	 * 
	 * This method processes the incoming POST request to add a new person to the
	 * system. It validates the provided {@link Person} and calls the service
	 * layer to persist the person. Upon successful creation, it returns a response
	 * with a 201 status and the location of the new resource.
	 *
	 * @param personDTO The {@link Person} containing the details of the person
	 *                  to be created.
	 * @return A {@link ResponseEntity} with a status of 201 (Created) and the URI
	 *         of the newly created person.
	 * @throws URISyntaxException If the URI for the created resource cannot be
	 *                            constructed.
	 */
	@PostMapping
	public ResponseEntity<ApiResponse> createPerson(@Validated @RequestBody Person personDTO) throws URISyntaxException {
		log.info("POST request received for /person, adding person: {}", personDTO);
		personService.createPerson(personDTO);
		log.info("Person successfully created: {}", personDTO);
		String str = "/person";
		URI uri = new URI(str);
		return ResponseEntity.created(uri).body(new ApiResponse(201));
	}

	/**
	 * Updates an existing person.
	 * 
	 * This method processes the incoming PUT request to update an existing person
	 * based on the provided firstName and lastName. It validates the provided
	 * {@link Person} and calls the service layer to update the person details.
	 * Upon successful update, it returns a response with a 200 status indicating
	 * the person was updated successfully.
	 * 
	 *
	 * @param firstName The firstName of the person to update.
	 * @param lastName  The lastName of the person to update.
	 * @param personDTO The {@link Person} containing the new person details.
	 * @return A {@link ResponseEntity} with a status of 200 (OK) indicating the
	 *         person was updated.
	 */

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<ApiResponse> updatePerson(@PathVariable @Validated @NotBlank String firstName,
			@PathVariable @Validated @NotBlank String lastName, @RequestBody @Validated Person personDTO) {
		log.info("PUT request received for /person/{}/{} updating person : {}", firstName, lastName, personDTO);
		personService.updatePerson(firstName, lastName, personDTO);
		log.info("Person successfully updated: {}", personDTO);
		return ResponseEntity.ok(new ApiResponse(200));

	}

	/**
	 * Deletes an existing person.
	 * 
	 * This method processes the incoming DELETE request to remove a person from the
	 * system based on the provided firstName and lastName. It calls the service
	 * layer to delete the person and returns a response indicating whether the
	 * deletion was successful.
	 * 
	 *
	 * @param firstName The firstName of the person person to delete.
	 * @param lastName  The lastName of the person person to delete.
	 * @return A {@link ResponseEntity} with either a 204 (No Content) status if the
	 *         deletion was successful, or a 404 (Not Found) status if the medical
	 *         record was not found.
	 */
	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<ApiResponse> deletePerson(@PathVariable @Validated @NotBlank String firstName,
			@PathVariable @Validated @NotBlank String lastName) {
		log.info("DELETE request received for /person/{}/{}", firstName, lastName);
		boolean personDeleted = personService.deletePerson(firstName, lastName);
		if (Boolean.TRUE.equals(personDeleted)) {
			log.info("Person successfully deleted: firstName: {}, lastName: {}", firstName, lastName);
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new ApiResponse(204, "The person with firstName: " + firstName + " and lastName: " + lastName + " is delete"));
		} else {
			log.error("Person not found: firstName: {}, lastName: {}", firstName, lastName);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(404, "Person not found: firstName: " + firstName + ", lastName: " + lastName));
		}
	}
}
