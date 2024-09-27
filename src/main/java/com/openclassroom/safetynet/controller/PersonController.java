//package com.openclassroom.safetynet.controller;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.openclassroom.safetynet.model.Person;
//import com.openclassroom.safetynet.service.PersonService;
//
//@RestController
//@RequestMapping("/person")
//public class PersonController {
//
//	private final PersonService personService;
package com.openclassroom.safetynet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

	private final Logger logger = LoggerFactory.getLogger(PersonController.class);

	private final PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping
	public ResponseEntity<List<Person>> getAllPersons() {
		logger.info("Requête GET /person reçue.");
		List<Person> persons = personService.getAllPersons();
		return new ResponseEntity<>(persons, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Person> addPerson(@RequestBody Person person) {
		logger.info("Requête POST /person reçue ajout de la personne : {}", person);
		personService.createPerson(person);
		return new ResponseEntity<>(person, HttpStatus.CREATED);
	}

	@PutMapping("/{firstName}/{lastName}")
	public ResponseEntity<Person> updatePerson(@PathVariable String firstName, @PathVariable String lastName,
			@RequestBody Person person) {
		logger.info("Requête PUT //{}/{} reçue, elle sera remplacée avec la personne : {}", firstName, lastName,
				person);
		personService.updatePerson(firstName, lastName, person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}

	@DeleteMapping("/{firstName}/{lastName}")
	public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
		logger.info("Requête DELETE /person/{}/{} reçue, elle sera supprimée", firstName, lastName);
		personService.deletePerson(firstName, lastName);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
