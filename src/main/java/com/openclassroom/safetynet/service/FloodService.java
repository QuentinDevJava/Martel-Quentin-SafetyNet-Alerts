package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.FloodInfo;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FloodService {
	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;
	private final FirestationService firestationService;

	public Map<String, List<MedicalRecordInfo>> listOfPersonsByAddressByStationNumber(List<Firestation> firestations) {
		Map<String, List<MedicalRecordInfo>> medicalRecordsByAddress = new HashMap<>();
		for (Firestation firestation : firestations) {
			List<Person> persons = new ArrayList<>();
			persons.addAll(personService.getPersonsByAddress(firestation.address()));
			List<MedicalRecordInfo> medicalRecordInfos = persons.stream().map(person -> medicalRecordService.getMedicalRecordInfosByPerson(person, medicalRecordService, personService)).toList();
			medicalRecordsByAddress.put(firestation.address(), medicalRecordInfos);
		}
		return medicalRecordsByAddress;
	}

	public FloodInfo floodInfo(List<String> stationNumber) {
		List<Firestation> firestations = firestationService.getFirestationByListStationNumber(stationNumber);
		Map<String, List<MedicalRecordInfo>> medicalRecordsByAddress = listOfPersonsByAddressByStationNumber(firestations);

		return new FloodInfo(medicalRecordsByAddress);
	}
}
