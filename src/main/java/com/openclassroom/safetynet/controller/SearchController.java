package com.openclassroom.safetynet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.Child;
import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonFloodInfo;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.service.PersonService;

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

	private final PersonService personService;

	@GetMapping("/firestation")
	public ResponseEntity<PersonCoveredByStation> getPersonsByStationNumber(@RequestParam int stationNumber) {
		log.info("Search for people covered by the fire station N° {}.", stationNumber);
		try {
			PersonCoveredByStation personsCovered = personService.findCoveredPersonsByFireStation(stationNumber);
			log.info("Successful retrieval of the list of persons : {}", personsCovered);
			return ResponseEntity.ok(personsCovered);
		} catch (Exception e) {
			log.error("Error fire station N°{} is not found.", stationNumber, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/childAlert")
	public ResponseEntity<List<Child>> getAllChild(@RequestParam String address) {
		log.info("Search for children by address : {} ", address);
		try {
			List<Child> listOfChilds = personService.listOfChild(address);
			log.info("Successful retrieval of the children's list : {}", listOfChilds);
			return ResponseEntity.ok(listOfChilds);
		} catch (Exception e) {
			log.error("Error children's list not found for this address : {}", address, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPersonsPhoneNumbersByStationNumber(@RequestParam("firestation") int stationNumber) {
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
			PersonsAndStationInfo personsAndStationInfo = personService.getPersonsAndStationInfoByAddress(address);
			log.info("Successful retrieval of the list of persons, their medical records and the number of the fire station for address : {} = {}", address, personsAndStationInfo);
			return ResponseEntity.ok(personsAndStationInfo);
		} catch (Exception e) {
			log.error("Error in returning list of persons, their medical records and fire station number for address : {}.", address, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<PersonFloodInfo> getListOfPersonsInfoAndStationNumberByStationNumber(@RequestParam("stations") List<Integer> stationNumber) {
		log.info("Search for resident information by list of station number : {}.", stationNumber);
		try {
			PersonFloodInfo floodInfo = personService.floodInfo(stationNumber);
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
			List<PersonsLastNameInfo> personsLastNameInfos = personService.listOfPersonsByLastName(lastName);
			log.info("Successful retrieval of list of persons and their medical records for last name : {} = {}", lastName, personsLastNameInfos);
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
