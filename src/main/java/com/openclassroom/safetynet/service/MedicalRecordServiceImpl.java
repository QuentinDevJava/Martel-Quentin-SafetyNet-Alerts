package com.openclassroom.safetynet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.DataRepository;
import com.openclassroom.safetynet.model.MedicalRecord;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

	private final DataRepository dataRepository = new DataRepository();

	public List<MedicalRecord> getAllMedicalRecords() {
		List<Object> medicalRecordData = dataRepository.SelectTypeOfData(TypeOfData.medicalrecords);
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		for (Object medicalRecordObj : medicalRecordData) {
			medicalRecords.add(new ObjectMapper().convertValue(medicalRecordObj, MedicalRecord.class));
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
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();
		for (int i = 0; i < medicalRecords.size(); i++) {
			if (medicalRecords.get(i).firstName().equals(firstName)
					&& medicalRecords.get(i).lastName().equals(lastName)) {
				medicalRecords.set(i, medicalRecord);
				saveMedicalRecords(medicalRecords);
				return medicalRecord;
			}
		}
		return null;
	}

	public void deleteMedicalRecord(String firstName, String lastName) {
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();
		medicalRecords.removeIf(mr -> mr.firstName().equals(firstName) && mr.lastName().equals(lastName));
		saveMedicalRecords(medicalRecords);
	}

	private void saveMedicalRecords(List<MedicalRecord> listOfMedicalRecords) {
		List<Object> medicalRecordData = new ArrayList<>();
		for (MedicalRecord medicalRecordObj : listOfMedicalRecords) {
			medicalRecordData.add(new ObjectMapper().convertValue(medicalRecordObj, MedicalRecord.class));
		}
		try {
			dataRepository.saveData(TypeOfData.medicalrecords, medicalRecordData);
		} catch (IOException e) {
			System.err.println("Erreur lors de la suavegarde des données : " + e.getMessage());
		}
	}

}
