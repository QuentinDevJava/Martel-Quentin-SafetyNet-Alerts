package com.openclassroom.safetynet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.FirestationNotFoundException;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.repository.DataRepository;

@Service
public class FirestationServiceImpl implements FirestationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final DataRepository dataRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public FirestationServiceImpl(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

	public List<Firestation> getAllFirestations() {
		List<Object> firesationData = dataRepository.SelectTypeOfData(TypeOfData.firestations);
		List<Firestation> firesations = new ArrayList<>();
		for (Object firesationObj : firesationData) {
			firesations.add(objectMapper.convertValue(firesationObj, Firestation.class));
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
		Firestation existingFirestation = findFirestationByAddress(address);
		if (existingFirestation != null) {
			List<Firestation> firestations = getAllFirestations();
			firestations.set(firestations.indexOf(existingFirestation), firestation);
			saveFirestations(firestations);
			return firestation;
		} else {
			throw new FirestationNotFoundException("Firestation not found for address: " + address);
		}
	}

	public void deleteFirestation(String address) {
		Firestation firestation = findFirestationByAddress(address);
		if (firestation != null) {
			List<Firestation> firestations = getAllFirestations();
			firestations.remove(firestation);
			saveFirestations(firestations);
		} else {
			throw new FirestationNotFoundException("Firestation not found for address: " + address);
		}
	}

	private void saveFirestations(List<Firestation> listOfFirestations) {
		List<Object> firestationData = new ArrayList<>();
		for (Firestation firestationObj : listOfFirestations) {
			firestationData.add(objectMapper.convertValue(firestationObj, Firestation.class));
		}
		try {
			dataRepository.saveData(TypeOfData.firestations, firestationData);
		} catch (IOException e) {
			logger.error("Error while saving data : {} " + e.getMessage());
		}
	}

	public List<Firestation> getFirestationByStationNumber(String stationNumber) {
		List<Firestation> firestations = getAllFirestations();
		List<Firestation> matchingFirestations = new ArrayList<>();

		for (Firestation firestation : firestations) {
			if (firestation.station().equals(stationNumber)) {
				matchingFirestations.add(firestation);
			}
		}

		return matchingFirestations;
	}

	private Firestation findFirestationByAddress(String address) {
		List<Firestation> firestations = getAllFirestations();
		for (int i = 0; i < firestations.size(); i++) {
			if (firestations.get(i).address().equals(address)) {
				return firestations.get(i);
			}
		}
		return null;
	}
}
