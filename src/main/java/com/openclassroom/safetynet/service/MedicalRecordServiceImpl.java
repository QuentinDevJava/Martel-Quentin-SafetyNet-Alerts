package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.MedicalRecordNotFoundException;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.repository.DataRepository;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

	private final DataRepository dataRepository = new DataRepository();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public List<MedicalRecord> getAllMedicalRecords() {
		return dataRepository.selectTypeOfData(TypeOfData.MEDICALRECORDS).stream().map(medicalRecordObj -> objectMapper.convertValue(medicalRecordObj, MedicalRecord.class)).toList();
	}

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		for (String field : new String[] { medicalRecord.firstName(), medicalRecord.firstName(), medicalRecord.birthdate() }) {
			if (Optional.ofNullable(field).map(StringUtils::isBlank).orElse(true)) {
				throw new IllegalArgumentException("All fields of the person must be filled.");
			}
		}
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();
		medicalRecords.add(medicalRecord);
		saveMedicalRecords(medicalRecords);
		return medicalRecord;
	}

	public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecord) {
		MedicalRecord existingMedicalRecord = findMedicalRecordByFullName(firstName, lastName);
		if (existingMedicalRecord != null) {
			List<MedicalRecord> medicalRecords = getAllMedicalRecords();
			medicalRecords.set(medicalRecords.indexOf(existingMedicalRecord), medicalRecord);
			saveMedicalRecords(medicalRecords);
			return medicalRecord;
		} else {
			throw new MedicalRecordNotFoundException("Medical record not updated beacause is not found for " + firstName + " " + lastName);
		}
	}

	public void deleteMedicalRecord(String firstName, String lastName) {
		MedicalRecord medicalRecord = findMedicalRecordByFullName(firstName, lastName);
		if (medicalRecord != null) {
			List<MedicalRecord> medicalRecords = getAllMedicalRecords();
			medicalRecords.remove(medicalRecord);
			saveMedicalRecords(medicalRecords);
		} else {
			throw new MedicalRecordNotFoundException("Medical record not deleted beacause is not found for " + firstName + " " + lastName);
		}
	}

	private void saveMedicalRecords(List<MedicalRecord> listOfMedicalRecords) {
		List<Object> medicalRecordData = new ArrayList<>();
		for (MedicalRecord medicalRecordObj : listOfMedicalRecords) {
			medicalRecordData.add(objectMapper.convertValue(medicalRecordObj, MedicalRecord.class));
		}
		dataRepository.saveData(TypeOfData.MEDICALRECORDS, medicalRecordData);
	}

	public MedicalRecord getMedicalRecordByFullName(String firstName, String lastName) {
		MedicalRecord medicalRecord = findMedicalRecordByFullName(firstName, lastName);
		if (medicalRecord != null) {
			return medicalRecord;
		} else {
			throw new MedicalRecordNotFoundException("Medical record not found for " + firstName + " " + lastName);
		}
	}

	private MedicalRecord findMedicalRecordByFullName(String firstName, String lastName) {
		return getAllMedicalRecords().stream().filter(medicalRecord -> medicalRecord.firstName().equals(firstName) && medicalRecord.lastName().equals(lastName)).findFirst().orElse(null);
	}

	public MedicalRecord findMedicalRecordByLastName(String lastName) {
		return getAllMedicalRecords().stream().filter(medicalRecord -> medicalRecord.lastName().equals(lastName)).findFirst().orElse(null);
	}

	public List<MedicalRecordInfo> getMedicalRecordInfosByListPersons(List<Person> persons, MedicalRecordService medicalRecordService, PersonService personService) {
		List<MedicalRecordInfo> medicalRecordInfos = new ArrayList<>();
		for (Person person : persons) {
			medicalRecordInfos.add(extractBasicInfo(person, getMedicalRecordByFullName(person.firstName(), person.lastName()), medicalRecordService, personService));
		}
		return medicalRecordInfos;
	}

	public MedicalRecordInfo extractBasicInfo(Person person, MedicalRecord medicalRecord, MedicalRecordService medicalRecordService, PersonService personService) {
		return new MedicalRecordInfo(person.firstName(), person.lastName(), person.phone(), personService.getPersonAge(person, medicalRecordService), medicalRecord.medications(), medicalRecord.allergies());

	}

	public MedicalRecordInfo getMedicalRecordInfosByPerson(Person person, MedicalRecordService medicalRecordService, PersonService personService) {
		return extractBasicInfo(person, getMedicalRecordByFullName(person.firstName(), person.lastName()), medicalRecordService, personService);
	}

}
