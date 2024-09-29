
package com.openclassroom.safetynet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping
	public ResponseEntity<List<Person>> getAllPersons() {
		logger.info("GET request received for /person.");
		List<Person> persons = personService.getAllPersons();
		logger.info("Successfully retrieved {} persons.", persons.size());
		return new ResponseEntity<>(persons, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Person> addPerson(@Validated @RequestBody Person person) {
		logger.info("POST request received for /person, adding person: {}", person);
		try {
			personService.createPerson(person);
			logger.info("Person successfully created: {}", person);
			return new ResponseEntity<>(person, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating person: {}", person, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<Person> updatePerson(@PathVariable String firstName, @PathVariable String lastName,
			@RequestBody Person person) {
		logger.info("PUT request received for /person/{}/{}", firstName, lastName);
		logger.info("Updating person with details: {}", person);
		try {
			personService.updatePerson(firstName, lastName, person);
			logger.info("Person successfully updated: {}", person);
			return new ResponseEntity<>(person, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error updating person with first name: {} and last name: {}", firstName, lastName, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
		logger.info("DELETE request received for /person/{}/{}", firstName, lastName);

		try {
			boolean personDeleted = personService.deletePerson(firstName, lastName);

			if (personDeleted) {
				logger.info("Person successfully deleted: firstName: {}, lastName: {}", firstName, lastName);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				logger.error("Person not found: firstName: {}, lastName: {}", firstName, lastName);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			logger.error("Error deleting person with first name: {} and last name: {}", firstName, lastName, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
