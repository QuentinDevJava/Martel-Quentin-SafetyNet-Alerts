
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
import com.openclassroom.safetynet.model.PersonDTO;
import com.openclassroom.safetynet.service.PersonService;

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

	@PostMapping
	public ResponseEntity<ApiResponse> createPerson(@Validated @RequestBody PersonDTO personDTO) throws URISyntaxException {
		log.info("POST request received for /person, adding person: {}", personDTO);
		personService.createPerson(personDTO);
		log.info("Person successfully created: {}", personDTO);
		String str = "/person";
		URI uri = new URI(str);
		return ResponseEntity.created(uri).body(new ApiResponse(201));
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<ApiResponse> updatePerson(@PathVariable String firstName, @PathVariable String lastName,
			@Validated @RequestBody PersonDTO personDTO) {
		log.info("PUT request received for /person/{}/{} updating person : {}", firstName, lastName, personDTO);
		personService.updatePerson(firstName, lastName, personDTO);
		log.info("Person successfully updated: {}", personDTO);
		return ResponseEntity.ok(new ApiResponse(200));
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
		log.info("DELETE request received for /person/{}/{}", firstName, lastName);
		boolean personDeleted = personService.deletePerson(firstName, lastName);
		if (Boolean.TRUE.equals(personDeleted)) {
			log.info("Person successfully deleted: firstName: {}, lastName: {}", firstName, lastName);
			return ResponseEntity.noContent().build();
		} else {
			log.error("Person not found: firstName: {}, lastName: {}", firstName, lastName);
			return ResponseEntity.notFound().build();
		}
	}
}
