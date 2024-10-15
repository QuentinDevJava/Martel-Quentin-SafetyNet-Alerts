package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.MedicalRecordNotFoundException;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.MedicalRecordInfo;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.repository.JsonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

	private final JsonRepository repository;
	private final ObjectMapper objectMapper;

	private List<MedicalRecord> allMedicalRecords() {
		return repository.loadTypeOfData(TypeOfData.MEDICALRECORDS).stream().map(medicalRecordObj -> objectMapper.convertValue(medicalRecordObj, MedicalRecord.class)).collect(Collectors.toList());
	}

	@Override
	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		for (String field : new String[] { medicalRecord.firstName(), medicalRecord.firstName(), medicalRecord.birthdate() }) {
			if (Optional.ofNullable(field).map(StringUtils::isBlank).orElse(true)) {
				throw new IllegalArgumentException("All fields of the person must be filled.");
			}
		}
		List<MedicalRecord> medicalRecords = allMedicalRecords();
		medicalRecords.add(medicalRecord);
		saveMedicalRecords(medicalRecords);
		return medicalRecord;
	}

	@Override
	public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecord) {
		MedicalRecord existingMedicalRecord = getMedicalRecordByFullName(firstName, lastName);
		if (existingMedicalRecord != null) {
			List<MedicalRecord> medicalRecords = allMedicalRecords();
			medicalRecords.set(medicalRecords.indexOf(existingMedicalRecord), medicalRecord);
			saveMedicalRecords(medicalRecords);
			return medicalRecord;
		} else {
			throw new MedicalRecordNotFoundException("Medical record not updated beacause is not found for " + firstName + " " + lastName);
		}
	}

	@Override
	public Boolean deleteMedicalRecord(String firstName, String lastName) {
		List<MedicalRecord> medicalRecords = allMedicalRecords();
		boolean medicalRecordDeleted = medicalRecords.removeIf(m -> m.firstName().equals(firstName) && m.lastName().equals(lastName));
		if (medicalRecordDeleted) {
			saveMedicalRecords(medicalRecords);
			return true;
		} else {
			throw new MedicalRecordNotFoundException("Medical record not deleted beacause is not found for " + firstName + " " + lastName);
		}
	}

	private void saveMedicalRecords(List<MedicalRecord> listOfMedicalRecords) {
		List<Object> medicalRecordData = new ArrayList<>();
		for (MedicalRecord medicalRecordObj : listOfMedicalRecords) {
			medicalRecordData.add(objectMapper.convertValue(medicalRecordObj, MedicalRecord.class));
		}
		repository.saveData(TypeOfData.MEDICALRECORDS, medicalRecordData);
	}

	@Override
	public MedicalRecord getMedicalRecordByFullName(String firstName, String lastName) {
		return allMedicalRecords().stream().filter(medicalRecord -> medicalRecord.firstName().equals(firstName) && medicalRecord.lastName().equals(lastName)).findFirst().orElse(null);
	}

	@Override
	public MedicalRecord findPersonsMedicalRecords(String lastName) {
		return allMedicalRecords().stream().filter(medicalRecord -> medicalRecord.lastName().equals(lastName)).findFirst().orElse(null);
	}

	@Override
	public List<MedicalRecord> getPersonMedicalRecords(List<Person> persons) {
		return persons.stream().map(p -> getMedicalRecordByFullName(p.firstName(), p.lastName())).filter(Objects::nonNull).toList();
	}

	@Override
	public List<MedicalRecordInfo> getMedicalRecordInfosByListPersons(List<Person> persons, MedicalRecordService medicalRecordService, PersonService personService) {
		List<MedicalRecordInfo> medicalRecordInfos = new ArrayList<>();
		for (Person person : persons) {
			medicalRecordInfos.add(extractBasicInfo(person, getMedicalRecordByFullName(person.firstName(), person.lastName()), medicalRecordService, personService));
		}
		return medicalRecordInfos;
	}

	@Override
	public MedicalRecordInfo extractBasicInfo(Person person, MedicalRecord medicalRecord, MedicalRecordService medicalRecordService, PersonService personService) {
		return new MedicalRecordInfo(person.firstName(), person.lastName(), person.phone(), personService.getPersonAge(person, medicalRecordService), medicalRecord.medications(), medicalRecord.allergies());

	}

	@Override
	public MedicalRecordInfo getMedicalRecordInfosByPerson(Person person, MedicalRecordService medicalRecordService, PersonService personService) {
		return extractBasicInfo(person, getMedicalRecordByFullName(person.firstName(), person.lastName()), medicalRecordService, personService);
	}

}
