package com.openclassroom.safetynet.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;

public class PersonsAndStationInfoService {
	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;
	private final FirestationService firestationService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public PersonsAndStationInfoService(PersonService personService, MedicalRecordService medicalRecordService, FirestationService firestationService) {
		this.personService = personService;
		this.medicalRecordService = medicalRecordService;
		this.firestationService = firestationService;
	}

	public PersonsAndStationInfo getPersonsAndStationInfoByAddress(String address) {
		List<Person> persons = personService.getPersonsByAddress(address);
		logger.debug("Result of getPersonsByAddress for address {} = {} ", address, persons);
		List<MedicalRecordInfo> medicalRecordInfos = medicalRecordService.getMedicalRecordInfosByListPersons(persons, medicalRecordService, personService);
		logger.debug("Result of getMedicalRecordInfosByPersons for persons found in getPersonsByAddress : {}", medicalRecordInfos);
		Firestation firestation = firestationService.getFirestationByAddress(address);
		logger.debug("Result of getFirestationByAddress the fire station number associated with address : {} = {} ", address, firestation.station());
		return new PersonsAndStationInfo(medicalRecordInfos, firestation.station());
	}
}