package com.openclassroom.safetynet.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class PersonsAndStationInfoServiceImpl implements SearchControllerService {
	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;
	private final FirestationService firestationService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public PersonsAndStationInfo getPersonsAndStationInfoByAddress(String address) {
		List<Person> persons = personService.getPersonsByAddress(address);
		logger.debug("Result of getPersonsByAddress for address {} = {} ", address, persons);
		List<MedicalRecordInfo> medicalRecordInfos = medicalRecordService.getMedicalRecordInfosByListPersons(persons);
		logger.debug("Result of getMedicalRecordInfosByPersons for persons found in getPersonsByAddress : {}", medicalRecordInfos);
		Firestation firestation = firestationService.getFirestationByAddress(address);
		logger.debug("Result of getFirestationByAddress the fire station number associated with address : {} = {} ", address, firestation.station());
		return new PersonsAndStationInfo(medicalRecordInfos, firestation.station());
	}
}