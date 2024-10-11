package com.openclassroom.safetynet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.FloodInfo;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonInfo;
import com.openclassroom.safetynet.model.PersonInfoWithLastName;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.service.ChildService;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.FloodService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonCoveredByStationDTO;
import com.openclassroom.safetynet.service.PersonService;
import com.openclassroom.safetynet.service.PersonsAndStationInfoService;
import com.openclassroom.safetynet.service.PersonsInfoWithLastNameService;

/**
 * Endpoints, which enables searches
 *
 */

@RestController
//@RequiredArgsConstructor
public class SearchController {

	// TODO SLF4J
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final FirestationService firestationService;
	private final MedicalRecordService medicalRecordService;
	private final PersonService personService;

	SearchController(PersonService personService, MedicalRecordService medicalRecordService, FirestationService firestationService) {
		this.personService = personService;
		this.medicalRecordService = medicalRecordService;
		this.firestationService = firestationService;

	}

	@GetMapping("/firestation")
	public ResponseEntity<PersonCoveredByStationDTO> getPersonsByStationNumber(@RequestParam String stationNumber) {
		logger.info("Search for people covered by the fire station N° {}.", stationNumber);
		try {
			List<Person> persons = personService.getPersonsByStation(stationNumber);
			logger.debug("Result of getPersonsByStation for fire station N°{} = {} ", stationNumber, persons);
			List<MedicalRecord> medicalRecords = medicalRecordService.getPersonMedicalRecords(persons);
			List<PersonInfo> personInfos = personService.extractPersonInfos(persons);
			PersonCoveredByStationDTO personsCovered = new PersonCoveredByStationDTO(personInfos, medicalRecords);
			// TODO decompte des adultes et enfants
			logger.info("Successful retrieval of the list of persons : {}", personsCovered);
			return ResponseEntity.ok(personsCovered);
		} catch (Exception e) {
			logger.error("Error fire station N°{} is not found.", stationNumber, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/childAlert")
	public ResponseEntity<ChildService> getAllChild(@RequestParam String address) {
		logger.info("Search for children by address : {} ", address);
		try {
			List<Person> personsByAddress = personService.getPersonsByAddress(address);
			logger.debug("Result of getPersonsByAddress for address {} = {} ", address, personsByAddress);
			ChildService listOfChild = new ChildService(personsByAddress, medicalRecordService, personService);
			logger.info("Successful retrieval of the children's list : {}", listOfChild);
			return ResponseEntity.ok(listOfChild);
		} catch (Exception e) {
			logger.error("Error children's list not found for this address : {}", address, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPersonsPhoneNumbersByStationNumber(@RequestParam("firestation") String stationNumber) {
		logger.info("Search phone numbers by fire station N° {}", stationNumber);
		try {
			List<String> phoneNumbers = personService.getPhoneNumbersByStation(stationNumber);
			logger.info("Successful retrieval of the phone number list : {}", phoneNumbers);
			return ResponseEntity.ok(phoneNumbers);
		} catch (Exception e) {
			logger.error("Error of the phone number list for fire station N°{} is not found.", stationNumber, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/fire")
	public ResponseEntity<PersonsAndStationInfo> getListOfPersonsInfoAndStationNumberByAddress(@RequestParam String address) {
		logger.info("Search for resident information and fire station number by address : {}", address);
		try {
			PersonsAndStationInfoService personsAndStationInfoService = new PersonsAndStationInfoService(personService, medicalRecordService, firestationService);
			PersonsAndStationInfo personsAndStationInfo = personsAndStationInfoService.getPersonsAndStationInfoByAddress(address);
			logger.info("Successful retrieval of the list of persons, their medical records and the number of the fire station for address : {} = {}", address, personsAndStationInfo);
			return ResponseEntity.ok(personsAndStationInfo);
		} catch (Exception e) {
			logger.error("Error in returning list of persons, their medical records and fire station number for address : {}.", address, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<FloodInfo> getListOfPersonsInfoAndStationNumberByStationNumber(@RequestParam("stations") List<String> stationNumber) {
		logger.info("Search for resident information by list of station number : {}.", stationNumber);
		try {
			FloodService floodService = new FloodService(personService, medicalRecordService, firestationService);
			FloodInfo floodInfo = floodService.floodInfo(stationNumber);
			logger.info("Successful retrieval of the list of persons and their medical records for List of station number : {} = {}", stationNumber, floodInfo);
			return ResponseEntity.ok(floodInfo);
		} catch (Exception e) {
			logger.error("Error fire station N°{} is not found.", stationNumber, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/personInfolastName")
	public ResponseEntity<PersonInfoWithLastName> getPersonsFullInfoWithLastName(@RequestParam String lastName) {
		logger.info("Search for resident information by last name : {}.", lastName);
		try {
			PersonsInfoWithLastNameService listOfPersonsInfoWithLastName = new PersonsInfoWithLastNameService(personService, medicalRecordService);
			List<PersonsLastNameInfo> personsLastNameInfos = listOfPersonsInfoWithLastName.listOfPersonsFullInfo(lastName);
			PersonInfoWithLastName personInfoWithLastName = new PersonInfoWithLastName(personsLastNameInfos);
			logger.info("Successful retrieval of list of persons and their medical records for last name : {} = {}", lastName, personInfoWithLastName);
			return ResponseEntity.ok(personInfoWithLastName);
		} catch (Exception e) {
			logger.error("Error the list of persons for the last name = {} is not found.", lastName, e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/communityEmail")
	public ResponseEntity<PersonEmail> getMailByCity(@RequestParam String city) {
		logger.info("Search for residents' e-mail addresses by city : {}", city);
		try {
			PersonEmail communityEmail = personService.personEmails(city);
			logger.info("Successful retrieval of the list of Email for city : {} = {}", city, communityEmail);
			return ResponseEntity.ok(communityEmail);
		} catch (Exception e) {
			logger.error("Error the list of person's Email for city {} is not found.", city, e);
			return ResponseEntity.notFound().build();
		}
	}

}
