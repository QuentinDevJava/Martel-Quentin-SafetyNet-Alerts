package com.openclassroom.safetynet.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.PersonInfo;

public class PersonCoveredByStationDTO {

	private final Map<PersonInfo, MedicalRecord> personInfoAndMedicalRecords;

	// il manquera le decompte des enfants et des adultes

	public PersonCoveredByStationDTO(List<PersonInfo> personInfos, List<MedicalRecord> medicalRecords) {
		this.personInfoAndMedicalRecords = matchPersonInfoAndMedicalRecords(personInfos, medicalRecords);
	}

	// faire une verification ici pour etre sur qu'il sagit de la bonne personne
	public Map<PersonInfo, MedicalRecord> matchPersonInfoAndMedicalRecords(List<PersonInfo> personInfos, List<MedicalRecord> medicalRecords) {
		Map<PersonInfo, MedicalRecord> result = new HashMap<PersonInfo, MedicalRecord>();
		for (PersonInfo personInfo : personInfos) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (personInfo.firstName().equals(medicalRecord.firstName()) && personInfo.lastName().equals(medicalRecord.lastName())) {
					result.put(personInfo, medicalRecord);
				}
			}
		}
		return result;
	}

	public Map<PersonInfo, MedicalRecord> getPersonInfoAndMedicalRecords() {
		return personInfoAndMedicalRecords;
	}
}
