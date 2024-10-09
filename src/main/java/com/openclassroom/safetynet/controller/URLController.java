package com.openclassroom.safetynet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.model.FloodInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonEmail;
import com.openclassroom.safetynet.model.PersonInfoWithLastName;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;
import com.openclassroom.safetynet.model.PersonsLastNameInfo;
import com.openclassroom.safetynet.service.ChildService;
import com.openclassroom.safetynet.service.FirestationService;
import com.openclassroom.safetynet.service.FloodService;
import com.openclassroom.safetynet.service.MedicalRecordService;
import com.openclassroom.safetynet.service.PersonService;
import com.openclassroom.safetynet.service.PersonsAndStationInfoService;
import com.openclassroom.safetynet.service.PersonsCoveredByFirestationService;
import com.openclassroom.safetynet.service.PersonsInfoWithLastNameService;

@RestController
public class URLController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final FirestationService firestationService;
	private final MedicalRecordService medicalRecordService;
	private PersonService personService;

	URLController(PersonService personService, MedicalRecordService medicalRecordService, FirestationService firestationService) {
		this.personService = personService;
		this.medicalRecordService = medicalRecordService;
		this.firestationService = firestationService;

	}

	@GetMapping("/firestation/{stationNumber}")
	public ResponseEntity<PersonsCoveredByFirestationService> getPersonsByStationNumber(@PathVariable String stationNumber) {
		logger.info("GET request received for /firestation/{}.", stationNumber);
		try {
			List<Person> persons = personService.getPersonsByStation(stationNumber);
			logger.debug("Result of getPersonsByStation for fire station N°{} = {} ", stationNumber, persons);
			PersonsCoveredByFirestationService personsCoveredByFirestation = new PersonsCoveredByFirestationService(personService, persons, medicalRecordService);
			logger.info("Successful retrieval of the list of persons : {}", personsCoveredByFirestation);
			return new ResponseEntity<>(personsCoveredByFirestation, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fire station N°{} is not found.", stationNumber, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/childAlert")
	public ResponseEntity<ChildService> getAllChild(@RequestParam String address) {
		logger.info("GET request received for /childAlert?address={}.", address);
		try {
			List<Person> personsByAddress = personService.getPersonsByAddress(address);
			logger.debug("Result of getPersonsByAddress for address {} = {} ", address, personsByAddress);
			ChildService listOfChild = new ChildService(personsByAddress, medicalRecordService, personService);
			logger.info("Successful retrieval of the children's list : {}", listOfChild);
			return new ResponseEntity<>(listOfChild, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error children's list not found for this address : {}", address, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPersonsPhoneNumbersByStationNumber(@RequestParam("firestation") String stationNumber) {
		logger.info("GET request received for /phoneAlert?firestation={}.", stationNumber);
		try {
			List<String> phoneNumbers = personService.getPhoneNumbersByStation(stationNumber);
			logger.info("Successful retrieval of the phone number list : {}", phoneNumbers);
			return new ResponseEntity<>(phoneNumbers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error of the phone number list for fire station N°{} is not found.", stationNumber, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/fire")
	public ResponseEntity<PersonsAndStationInfo> getListOfPersonsInfoAndStationNumberByAddress(@RequestParam String address) {
		logger.info("GET request received for /fire?address={}.", address);
		try {
			PersonsAndStationInfoService personsAndStationInfoService = new PersonsAndStationInfoService(personService, medicalRecordService, firestationService);
			PersonsAndStationInfo personsAndStationInfo = personsAndStationInfoService.getPersonsAndStationInfoByAddress(address);
			logger.info("Successful retrieval of the list of persons, their medical records and the number of the fire station for address : {} = {}", address, personsAndStationInfo);
			return new ResponseEntity<>(personsAndStationInfo, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error in returning list of persons, their medical records and fire station number for address : {}.", address, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<FloodInfo> getListOfPersonsInfoAndStationNumberByStationNumber(@RequestParam("stations") List<String> stationNumber) {
		logger.info("GET request received for /flood/stations?stations={}.", stationNumber);
		try {
			FloodService floodService = new FloodService(personService, medicalRecordService, firestationService);
			FloodInfo floodInfo = floodService.floodInfo(stationNumber);
			logger.info("Successful retrieval of the list of persons and their medical records for List of station number : {} = {}", stationNumber, floodInfo);
			return new ResponseEntity<>(floodInfo, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fire station N°{} is not found.", stationNumber, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/personInfolastName")
	public ResponseEntity<PersonInfoWithLastName> getPersonsFullInfoWithLastName(@RequestParam String lastName) {
		logger.info("GET request received for /personInfolastName?lastName={}.", lastName);
		try {
			PersonsInfoWithLastNameService listOfPersonsInfoWithLastName = new PersonsInfoWithLastNameService(personService, medicalRecordService);
			List<PersonsLastNameInfo> personsLastNameInfos = listOfPersonsInfoWithLastName.listOfPersonsFullInfo(lastName);
			PersonInfoWithLastName personInfoWithLastName = new PersonInfoWithLastName(personsLastNameInfos);
			logger.info("Successful retrieval of list of persons and their medical records for last name : {} = {}", lastName, personInfoWithLastName);
			return new ResponseEntity<>(personInfoWithLastName, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error the list of persons for the last name = {} is not found.", lastName, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/communityEmail")
	public ResponseEntity<PersonEmail> getMailByCity(@RequestParam String city) {
		logger.info("GET request received for /communityEmail?city={}.", city);
		try {
			PersonEmail communityEmail = personService.personEmails(city);
			logger.info("Successful retrieval of the list of Email for city : {} = {}", city, communityEmail);
			return new ResponseEntity<>(communityEmail, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error the list of person's Email for city {} is not found.", city, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
