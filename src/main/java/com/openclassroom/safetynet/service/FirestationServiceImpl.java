package com.openclassroom.safetynet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.DataRepository;
import com.openclassroom.safetynet.model.Firestation;

@Service
public class FirestationServiceImpl implements FirestationService {

	private final DataRepository dataRepository = new DataRepository();

	public List<Firestation> getAllFirestations() {
		List<Object> firesationData = dataRepository.SelectTypeOfData(TypeOfData.firestations);
		List<Firestation> firesations = new ArrayList<>();
		for (Object firesationObj : firesationData) {
			firesations.add(new ObjectMapper().convertValue(firesationObj, Firestation.class));
		}
		return firesations;
	}

	public Firestation createFirestation(Firestation firestation) {
		List<Firestation> firestations = getAllFirestations();
		firestations.add(firestation);
		saveFirestations(firestations);
		return firestation;
	}

	public Firestation updateFirestation(String address, Firestation firestation) {
		List<Firestation> firestations = getAllFirestations();
		for (int i = 0; i < firestations.size(); i++) {
			if (firestations.get(i).getAddress().equals(address)) {
				firestations.set(i, firestation);
				saveFirestations(firestations);
				return firestation;
			}
		}
		return null;
	}

	public void deleteFirestation(String address) {
		List<Firestation> firestations = getAllFirestations();
		firestations.removeIf(f -> f.getAddress().equals(address));
		saveFirestations(firestations);
	}

	private void saveFirestations(List<Firestation> listOfFirestations) {
		List<Object> firestationData = new ArrayList<>();
		for (Firestation firestationObj : listOfFirestations) {
			firestationData.add(new ObjectMapper().convertValue(firestationObj, Firestation.class));
		}
		try {
			dataRepository.saveData(TypeOfData.firestations, firestationData);
		} catch (IOException e) {
			System.err.println("Erreur lors de la suavegarde des données : " + e.getMessage());
		}
	}

}
