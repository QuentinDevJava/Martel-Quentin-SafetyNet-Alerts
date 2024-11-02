
package com.openclassroom.safetynet.controller;

import java.net.URI;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.PersonRequest;
import com.openclassroom.safetynet.model.PersonResponse;
import com.openclassroom.safetynet.service.PersonService;

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
public class PersonController {

	private final PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@PostMapping
	public ResponseEntity<PersonResponse> createPerson(@Validated @RequestBody PersonRequest personRequest) {
		log.info("POST request received for /person, adding person: {}", personRequest);
		try {
			PersonResponse personResponse = personService.personRequestToPersonResponse(personRequest);
			personService.createPerson(personResponse);
			log.info("Person successfully created: {}", personResponse);
			URI uri = new URI("/person");
			return ResponseEntity.created(uri).body(personResponse);
		} catch (Exception e) {
			log.error("Error creating person: {}", personRequest, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<PersonResponse> updatePerson(@PathVariable String firstName, @PathVariable String lastName,
			@Validated @RequestBody PersonRequest personRequest) {
		log.info("PUT request received for /person/{}/{} updating person : {}", firstName, lastName, personRequest);
		try {
			PersonResponse personResponse = personService.personRequestToPersonResponse(personRequest);
			personService.updatePerson(firstName, lastName, personResponse);
			log.info("Person successfully updated: {}", personResponse);
			return ResponseEntity.ok(personResponse);
		} catch (NoSuchElementException e) {
			log.error("Error the people with full name : {} {} cannot be found.", firstName, lastName, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error updating person with first name: {} and last name: {}", firstName, lastName, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
		log.info("DELETE request received for /person/{}/{}", firstName, lastName);

		try {
			boolean personDeleted = personService.deletePerson(firstName, lastName);

			if (Boolean.TRUE.equals(personDeleted)) {
				log.info("Person successfully deleted: firstName: {}, lastName: {}", firstName, lastName);
				return ResponseEntity.noContent().build();
			} else {
				log.error("Person not found: firstName: {}, lastName: {}", firstName, lastName);
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			log.error("Error deleting person with first name: {} and last name: {}", firstName, lastName, e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
