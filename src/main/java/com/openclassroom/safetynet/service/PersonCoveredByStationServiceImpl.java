package com.openclassroom.safetynet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public abstract class PersonCoveredByStationServiceImpl implements SearchControllerService {

	private final FirestationService fireStationService;
	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;

	private final Predicate<Integer> IS_ADULT = age -> age > 18;
	private final Predicate<Integer> IS_CHILD = age -> age <= 18;

	@Override
	public PersonCoveredByStation findCoveredPersonsByFireStation(String stationNumber) {
		log.info("Finding covered persons by fire station {}", stationNumber);
		List<Firestation> firestations = fireStationService.findAllByStationNumber(stationNumber);
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