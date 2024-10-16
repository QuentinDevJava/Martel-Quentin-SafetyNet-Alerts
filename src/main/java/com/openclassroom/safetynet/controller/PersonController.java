
package com.openclassroom.safetynet.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.Person;
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
	public ResponseEntity<Person> createPerson(@Validated @RequestBody Person person) {
		log.info("POST request received for /person, adding person: {}", person);
		try {
			personService.createPerson(person);
			log.info("Person successfully created: {}", person);
			return new ResponseEntity<>(person, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error creating person: {}", person, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<Person> updatePerson(@PathVariable String firstName, @PathVariable String lastName, @Validated @RequestBody Person person) {
		log.info("PUT request received for /person/{}/{}", firstName, lastName);
		try {
			personService.updatePerson(firstName, lastName, person);
			log.info("Person successfully updated: {}", person);
			return new ResponseEntity<>(person, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error updating person with first name: {} and last name: {}", firstName, lastName, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
		log.info("DELETE request received for /person/{}/{}", firstName, lastName);

		try {
			boolean personDeleted = personService.deletePerson(firstName, lastName);

			if (Boolean.TRUE.equals(personDeleted)) {
				log.info("Person successfully deleted: firstName: {}, lastName: {}", firstName, lastName);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				log.error("Person not found: firstName: {}, lastName: {}", firstName, lastName);
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			log.error("Error deleting person with first name: {} and last name: {}", firstName, lastName, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
