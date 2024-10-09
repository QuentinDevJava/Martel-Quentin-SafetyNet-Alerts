package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;

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
		List<Object> medicalRecordData = dataRepository.SelectTypeOfData(TypeOfData.medicalrecords);
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		for (Object medicalRecordObj : medicalRecordData) {
			medicalRecords.add(objectMapper.convertValue(medicalRecordObj, MedicalRecord.class));
		}
		return medicalRecords;
	}

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
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
		dataRepository.saveData(TypeOfData.medicalrecords, medicalRecordData);
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
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();
		for (int i = 0; i < medicalRecords.size(); i++) {
			if (medicalRecords.get(i).firstName().equals(firstName) && medicalRecords.get(i).lastName().equals(lastName)) {
				return medicalRecords.get(i);
			}
		}
		return null;
	}

	public MedicalRecord findMedicalRecordByLastName(String lastName) {
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();
		for (int i = 0; i < medicalRecords.size(); i++) {
			if (medicalRecords.get(i).lastName().equals(lastName)) {
				return medicalRecords.get(i);
			}
		}
		return null;
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
