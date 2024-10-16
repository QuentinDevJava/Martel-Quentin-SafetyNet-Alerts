package com.openclassroom.safetynet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonFloodInfo;
import com.openclassroom.safetynet.model.PersonInfo;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchImpl implements SearchService {

	private final FirestationService firestationService;
	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;

	private final Predicate<Integer> IS_ADULT = age -> age > 18;
	private final Predicate<Integer> IS_CHILD = age -> age <= 18;

	@Override
	public PersonsAndStationInfo getPersonsAndStationInfoByAddress(String address) {
		List<Person> persons = personService.getPersonsByAddress(address);
		log.debug("Result of getPersonsByAddress for address {} = {} ", address, persons);
		List<MedicalRecordInfo> medicalRecordInfos = personService.getMedicalRecordInfosByListPersons(persons);
		log.debug("Result of getMedicalRecordInfosByPersons for persons found in getPersonsByAddress : {}", medicalRecordInfos);
		Firestation firestation = firestationService.getFirestationByAddress(address);
		log.debug("Result of getFirestationByAddress the fire station number associated with address : {} = {} ", address, firestation.station());
		return new PersonsAndStationInfo(medicalRecordInfos, firestation.station());
	}

	@Override
	public PersonFloodInfo floodInfo(List<String> stationNumber) {
		List<Firestation> firestations = firestationService.getFirestationByListStationNumber(stationNumber);
		Map<String, List<MedicalRecordInfo>> medicalRecordsByAddress = listOfPersonsByAddressByStationNumber(firestations);

		return new PersonFloodInfo(medicalRecordsByAddress);
	}

	private Map<String, List<MedicalRecordInfo>> listOfPersonsByAddressByStationNumber(List<Firestation> firestations) {
		Map<String, List<MedicalRecordInfo>> medicalRecordsByAddress = new HashMap<>();
		for (Firestation firestation : firestations) {
			List<Person> persons = new ArrayList<>();
			persons.addAll(personService.getPersonsByAddress(firestation.address()));
			List<MedicalRecordInfo> medicalRecordInfos = persons.stream().map(personService::getMedicalRecordInfosByPerson).toList();
			medicalRecordsByAddress.put(firestation.address(), medicalRecordInfos);
		}
		return medicalRecordsByAddress;
	}

	@Override
	public PersonCoveredByStation findCoveredPersonsByFireStation(String stationNumber) {
		List<Firestation> firestations = firestationService.findAllByStationNumber(stationNumber);
		List<Person> personByStation = personService.getPersonsByStationAddress(firestations);
		List<PersonInfo> personInfos = personService.extractPersonInfos(personByStation);
		List<MedicalRecord> medicalRecords = medicalRecordService.getPersonMedicalRecords(personByStation);
		int adultCount = countAdults(medicalRecords);
		int childCount = countChildren(medicalRecords);
		return new PersonCoveredByStation(personInfos, adultCount, childCount);
	}

	private int countChildren(List<MedicalRecord> medicalRecords) {
		return (int) medicalRecords.stream().map(this::calculateAge).filter(IS_CHILD).count();
	}

	private int countAdults(List<MedicalRecord> medicalRecords) {
		return (int) medicalRecords.stream().map(this::calculateAge).filter(IS_ADULT).count();
	}

	private int calculateAge(MedicalRecord medicalRecord) {
		String dateString = medicalRecord.birthdate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthdate = LocalDate.parse(dateString, formatter);
		LocalDate today = LocalDate.now();
		return Period.between(birthdate, today).getYears();
	}
}
