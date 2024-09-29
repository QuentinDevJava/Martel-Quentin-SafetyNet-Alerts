package com.openclassroom.safetynet.service;

import java.util.List;

import com.openclassroom.safetynet.model.MedicalRecord;

public interface MedicalRecordService {
	MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);

	MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecord);

	MedicalRecord getMedicalRecordByFullName(String firstName, String lastName);

	void deleteMedicalRecord(String firstName, String lastName);

	List<MedicalRecord> getAllMedicalRecords();
}
