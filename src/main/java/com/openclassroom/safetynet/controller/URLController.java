package com.openclassroom.safetynet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonService;
import com.openclassroom.safetynet.service.PersonsCoveredByFirestation;

@RestController
public class URLController {

	@Autowired
	private FirestationService firestationService;
	@Autowired
	private MedicalRecordService medicalRecordService;
	@Autowired
	private PersonService personService;

	@GetMapping("/firestation/{stationNumber}")
	public ResponseEntity<PersonsCoveredByFirestation> getPersonsByStationNumber(@PathVariable String stationNumber) {
		List<Person> persons = getPersonsByStation(stationNumber);
		PersonsCoveredByFirestation response = new PersonsCoveredByFirestation(persons, medicalRecordService);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private List<Person> getPersonsByStation(String stationNumber) {
		List<Firestation> firestation = firestationService.getFirestationByStationNumber(stationNumber);
		return personService.getPeopleByStationAddress(firestation);
	}

	@GetMapping("/childAlert/{address}")
	public ResponseEntity<PersonsCoveredByFirestation> getChildByAdress(@PathVariable String address) {
		List<Firestation> firestation = firestationService.getFirestationByStationNumber(address);
		List<Person> persons = personService.getPeopleByStationAddress(firestation);
		PersonsCoveredByFirestation response = new PersonsCoveredByFirestation(persons, medicalRecordService);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
