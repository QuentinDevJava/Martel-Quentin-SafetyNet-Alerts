package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.FloodInfo;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;

public class FloodService {
	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;
	private final FirestationService firestationService;
	private Map<String, List<MedicalRecordInfo>> resultByAddress = new HashMap<>();

	public FloodService(PersonService personService, MedicalRecordService medicalRecordService, FirestationService firestationService) {
		this.personService = personService;
		this.medicalRecordService = medicalRecordService;
		this.firestationService = firestationService;
	}

	public Map<String, List<MedicalRecordInfo>> listOfPersonsByAddressByStationNumber(List<Firestation> firestations) {
		for (Firestation firestation : firestations) {
			List<Person> persons = new ArrayList<>();
			persons.addAll(personService.getPersonsByAddress(firestation.address()));
			List<MedicalRecordInfo> medicalRecordInfo = new ArrayList<>();
			for (Person person : persons) {
				medicalRecordInfo.add(medicalRecordService.getMedicalRecordInfosByPerson(person, medicalRecordService, personService));
			}
			resultByAddress.put(firestation.address(), medicalRecordInfo);

		}
		return resultByAddress;
	}

	public FloodInfo floodInfo(List<String> stationNumber) {
		List<Firestation> firestations = firestationService.getFirestationByListStationNumber(stationNumber);
		Map<String, List<MedicalRecordInfo>> ResultMapOfmedicalRecordInfo = listOfPersonsByAddressByStationNumber(firestations);

		return new FloodInfo(ResultMapOfmedicalRecordInfo);
	}
}
