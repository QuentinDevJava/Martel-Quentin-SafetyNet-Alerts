package com.openclassroom.safetynet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.MedicalRecordNotFoundException;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.repository.DataRepository;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final DataRepository dataRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public MedicalRecordServiceImpl(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

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
			throw new MedicalRecordNotFoundException("Medical record not found for " + firstName + " " + lastName);
		}
	}

	public void deleteMedicalRecord(String firstName, String lastName) {
		MedicalRecord medicalRecord = findMedicalRecordByFullName(firstName, lastName);
		if (medicalRecord != null) {
			List<MedicalRecord> medicalRecords = getAllMedicalRecords();
			medicalRecords.remove(medicalRecord);
			saveMedicalRecords(medicalRecords);
		} else {
			throw new MedicalRecordNotFoundException("Medical record not found for " + firstName + " " + lastName);
		}
	}

	private void saveMedicalRecords(List<MedicalRecord> listOfMedicalRecords) {
		List<Object> medicalRecordData = new ArrayList<>();
		for (MedicalRecord medicalRecordObj : listOfMedicalRecords) {
			medicalRecordData.add(objectMapper.convertValue(medicalRecordObj, MedicalRecord.class));
		}
		try {
			dataRepository.saveData(TypeOfData.medicalrecords, medicalRecordData);
		} catch (IOException e) {
			logger.error("Error while saving data : {} " + e.getMessage());
		}
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
			if (medicalRecords.get(i).firstName().equals(firstName)
					&& medicalRecords.get(i).lastName().equals(lastName)) {
				return medicalRecords.get(i);
			}
		}
		return null;
	}
}
