package com.openclassroom.safetynet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.FloodInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonInfoWithLastName;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.service.ChildService;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.FloodService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonCoveredByStationService;
import com.openclassroom.safetynet.service.PersonService;
import com.openclassroom.safetynet.service.PersonsAndStationInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Endpoints, which enables searches
 *
 */

@RestController
@Slf4j
@RequiredArgsConstructor

public class SearchController {

	private final FirestationService firestationService;
	private final MedicalRecordService medicalRecordService;
	private final PersonService personService;
	private final PersonCoveredByStationService personCoveredByStationService;

	@GetMapping("/firestation")
	public ResponseEntity<PersonCoveredByStation> getPersonsByStationNumber(@RequestParam String stationNumber) {
		log.info("Search for people covered by the fire station N° {}.", stationNumber);
		try {
			PersonCoveredByStation personsCovered = personCoveredByStationService.findCoveredPersonsByFireStation(stationNumber);
			log.info("Successful retrieval of the list of persons : {}", personsCovered);
			return ResponseEntity.ok(personsCovered);
		} catch (Exception e) {
			log.error("Error fire station N°{} is not found.", stationNumber, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/childAlert")
	public ResponseEntity<ChildService> getAllChild(@RequestParam String address) {
		log.info("Search for children by address : {} ", address);
		try {
			List<Person> personsByAddress = personService.getPersonsByAddress(address);
			log.debug("Result of getPersonsByAddress for address {} = {} ", address, personsByAddress);
			ChildService listOfChild = new ChildService(personsByAddress, medicalRecordService, personService);
			log.info("Successful retrieval of the children's list : {}", listOfChild);
			return ResponseEntity.ok(listOfChild);
		} catch (Exception e) {
			log.error("Error children's list not found for this address : {}", address, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPersonsPhoneNumbersByStationNumber(@RequestParam("firestation") String stationNumber) {
		log.info("Search phone numbers by fire station N° {}", stationNumber);
		try {
			List<String> phoneNumbers = personService.getPhoneNumbersByStation(stationNumber);
			log.info("Successful retrieval of the phone number list : {}", phoneNumbers);
			return ResponseEntity.ok(phoneNumbers);
		} catch (Exception e) {
			log.error("Error of the phone number list for fire station N°{} is not found.", stationNumber, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/fire")
	public ResponseEntity<PersonsAndStationInfo> getListOfPersonsInfoAndStationNumberByAddress(@RequestParam String address) {
		log.info("Search for resident information and fire station number by address : {}", address);
		try {
			PersonsAndStationInfoService personsAndStationInfoService = new PersonsAndStationInfoService(personService, medicalRecordService, firestationService);
			PersonsAndStationInfo personsAndStationInfo = personsAndStationInfoService.getPersonsAndStationInfoByAddress(address);
			log.info("Successful retrieval of the list of persons, their medical records and the number of the fire station for address : {} = {}", address, personsAndStationInfo);
			return ResponseEntity.ok(personsAndStationInfo);
		} catch (Exception e) {
			log.error("Error in returning list of persons, their medical records and fire station number for address : {}.", address, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<FloodInfo> getListOfPersonsInfoAndStationNumberByStationNumber(@RequestParam("stations") List<String> stationNumber) {
		log.info("Search for resident information by list of station number : {}.", stationNumber);
		try {
			FloodService floodService = new FloodService(personService, medicalRecordService, firestationService);
			FloodInfo floodInfo = floodService.floodInfo(stationNumber);
			log.info("Successful retrieval of the list of persons and their medical records for List of station number : {} = {}", stationNumber, floodInfo);
			return ResponseEntity.ok(floodInfo);
		} catch (Exception e) {
			log.error("Error fire station N°{} is not found.", stationNumber, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/personInfolastName")
	public ResponseEntity<List<PersonsLastNameInfo>> getPersonsFullInfoWithLastName(@RequestParam String lastName) {
		log.info("Search for resident information by last name : {}.", lastName);
		try {
			List<PersonsLastNameInfo> personsLastNameInfos = personService.listOfPersonsFullInfo(lastName);
			PersonInfoWithLastName personInfoWithLastName = new PersonInfoWithLastName(personsLastNameInfos);
			log.info("Successful retrieval of list of persons and their medical records for last name : {} = {}", lastName, personInfoWithLastName);
			return ResponseEntity.ok(personsLastNameInfos);
		} catch (Exception e) {
			log.error("Error the list of persons for the last name = {} is not found.", lastName, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/communityEmail")
	public ResponseEntity<PersonEmail> getMailByCity(@RequestParam String city) {
		log.info("Search for residents' e-mail addresses by city : {}", city);
		try {
			PersonEmail communityEmail = personService.personEmails(city);
			log.info("Successful retrieval of the list of Email for city : {} = {}", city, communityEmail);
			return ResponseEntity.ok(communityEmail);
		} catch (Exception e) {
			log.error("Error the list of person's Email for city {} is not found.", city, e);
			return ResponseEntity.notFound().build();
		}
	}

}
