package com.openclassroom.safetynet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.safetynet.dto.Child;
import com.openclassroom.safetynet.dto.PersonCoveredByStation;
import com.openclassroom.safetynet.dto.PersonFloodInfo;
import com.openclassroom.safetynet.dto.PersonsAndStationInfo;
import com.openclassroom.safetynet.dto.PersonsLastNameInfo;
import com.openclassroom.safetynet.service.PersonService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Endpoints, which enables searches This controller provides various endpoints
 * to retrieve information about persons, their medical records, fire stations,
 * and community-related data.
 *
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchController {

	private final PersonService personService;

	/**
	 * Retrieves all persons covered by a given fire station number.
	 *
	 * @param stationNumber The station number to search for.
	 * @return A {@link ResponseEntity} containing a {@link PersonCoveredByStation}
	 *         object, which includes the list of persons covered by the specified
	 *         fire station.
	 */
	@GetMapping("/firestation")
	public ResponseEntity<PersonCoveredByStation> getPersonsByStationNumber(@RequestParam @Validated @Positive int stationNumber) {
		log.info("Search for people covered by the fire station N° {}.", stationNumber);
		PersonCoveredByStation personsCovered = personService.personCoveredByStation(stationNumber);
		log.info("Successful retrieval of the list of persons : {}", personsCovered);
		return ResponseEntity.ok(personsCovered);
	}

	/**
	 * Retrieves a list of children living at a specific address.
	 *
	 * @param address The address to search for children.
	 * @return A {@link ResponseEntity} containing a list of {@link Child} objects
	 *         representing the children living at the specified address.
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<List<Child>> getAllChild(@RequestParam @Validated @NotBlank String address) {
		log.info("Search for children by address : {} ", address);
		List<Child> childs = personService.getChildsByAddress(address);
		log.info("Successful retrieval of the children's list : {}", childs);
		return ResponseEntity.ok(childs);
	}

	/**
	 * Retrieves a list of phone numbers of persons covered by a specific fire
	 * station number.
	 *
	 * @param stationNumber The fire station number to search for.
	 * @return A {@link ResponseEntity} containing a list of phone numbers of
	 *         persons covered by the specified fire station.
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPersonsPhoneNumbersByStationNumber(
			@RequestParam("firestation") @Validated @NotNull @Min(1) int stationNumber) {
		log.info("Search phone numbers by fire station N° {}", stationNumber);
		List<String> phoneNumbers = personService.getPhoneNumbersByStation(stationNumber);
		log.info("Successful retrieval of the phone number list : {}", phoneNumbers);
		return ResponseEntity.ok(phoneNumbers);
	}

	/**
	 * Retrieves resident information and the fire station number associated with a
	 * given address.
	 *
	 * @param address The address to search for.
	 * @return A {@link ResponseEntity} containing a {@link PersonsAndStationInfo}
	 *         object, which includes both the persons' information and the
	 *         associated fire station number.
	 */
	@GetMapping("/fire")
	public ResponseEntity<PersonsAndStationInfo> getListOfPersonsInfoAndStationNumberByAddress(@RequestParam @Validated @NotBlank String address) {
		log.info("Search for resident information and fire station number by address : {}", address);
		PersonsAndStationInfo personsAndStationInfo = personService.getPersonsAndStationInfoByAddress(address);
		log.info("Successful retrieval of the list of persons, their medical records and the number of the fire station for address : {} = {}",
				address, personsAndStationInfo);
		return ResponseEntity.ok(personsAndStationInfo);
	}

	/**
	 * Retrieves resident information for multiple fire stations based on a list of
	 * station numbers.
	 *
	 * @param stationNumber The list of fire station numbers to search for.
	 * @return A {@link ResponseEntity} containing a {@link PersonFloodInfo} object,
	 *         which includes the list of persons and their medical records for the
	 *         given stations.
	 */
	@GetMapping("/flood/stations")
	public ResponseEntity<PersonFloodInfo> getListOfPersonsInfoAndStationNumberByStationNumber(
			@RequestParam("stations") @Validated List<@Positive Integer> stationNumber) {
		log.info("Search for resident information by list of station number : {}.", stationNumber);
		PersonFloodInfo floodInfo = personService.floodInfo(stationNumber);
		log.info("Successful retrieval of the list of persons and their medical records for List of station number : {} = {}", stationNumber,
				floodInfo);
		return ResponseEntity.ok(floodInfo);
	}

	/**
	 * Retrieves full resident information by last name.
	 *
	 * @param lastName The last name of the resident(s) to search for.
	 * @return A {@link ResponseEntity} containing a list of
	 *         {@link PersonsLastNameInfo} objects, which includes full information
	 *         and medical records for residents with the specified last name.
	 */
	@GetMapping("/personInfolastName")
	public ResponseEntity<List<PersonsLastNameInfo>> getPersonsFullInfoWithLastName(@RequestParam @Validated @NotBlank String lastName) {
		log.info("Search for resident information by last name : {}.", lastName);
		List<PersonsLastNameInfo> personsLastNameInfos = personService.listOfPersonsByLastName(lastName);
		log.info("Successful retrieval of list of persons and their medical records for last name : {} = {}", lastName, personsLastNameInfos);
		return ResponseEntity.ok(personsLastNameInfos);
	}

	/**
	 * Retrieves a list of community email addresses for residents of a specific
	 * city.
	 *
	 * @param city The city to search for.
	 * @return A {@link ResponseEntity} containing a list of email addresses of the
	 *         residents in the specified city.
	 */
	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getMailByCity(@RequestParam @Validated @NotBlank String city) {
		log.info("Search for residents' e-mail addresses by city : {}", city);
		List<String> communityEmail = personService.personEmails(city);
		log.info("Successful retrieval of the list of Email for city : {} = {}", city, communityEmail);
		return ResponseEntity.ok(communityEmail);
	}

}
