package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.FirestationNotFoundException;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.repository.JsonRepository;

@Service
public class FirestationServiceImpl implements FirestationService {

	private final JsonRepository jsonRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public FirestationServiceImpl(JsonRepository jsonRepository) {
		this.jsonRepository = jsonRepository;
	}

	public List<Firestation> getAllFirestations() {
		return jsonRepository.selectTypeOfData(TypeOfData.FIRESTATIONS).stream().map(firestationObj -> objectMapper.convertValue(firestationObj, Firestation.class)).collect(Collectors.toList());
	}

	public Firestation createFirestation(Firestation firestation) {
		for (String field : new String[] { firestation.station(), firestation.address() }) {
			if (Optional.ofNullable(field).map(StringUtils::isBlank).orElse(true)) {
				throw new IllegalArgumentException("All fields of the person must be filled.");
			}
		}
		List<Firestation> firestations = getAllFirestations();
		firestations.add(firestation);
		saveFirestations(firestations);
		return firestation;
	}

	public Firestation updateFirestation(String address, Firestation firestation) {
		Firestation existingFirestation = getFirestationByAddress(address);
		if (existingFirestation != null) {
			List<Firestation> firestations = getAllFirestations();
			firestations.set(firestations.indexOf(existingFirestation), firestation);
			saveFirestations(firestations);
			return firestation;
		} else {
			throw new FirestationNotFoundException("Fire station not found for address: " + address);
		}
	}

	public void deleteFirestation(String address) {
		List<Firestation> firestations = getAllFirestations();
		Firestation firestation = getFirestationByAddress(address);
		if (firestation != null) {
			firestations.remove(firestation);
			saveFirestations(firestations);
		} else {
			throw new FirestationNotFoundException("Fire station not found for address: " + address);
		}
	}

	private void saveFirestations(List<Firestation> firestations) {
		jsonRepository.saveData(TypeOfData.FIRESTATIONS, firestations.stream().map(firestationObj -> objectMapper.convertValue(firestationObj, Firestation.class)).collect(Collectors.toList()));
	}

	public List<Firestation> getFirestationByStationNumber(String stationNumber) {
		List<Firestation> firestations = getAllFirestations();
		List<Firestation> matchingFirestations = new ArrayList<>();
		for (Firestation firestation : firestations) {
			if (firestation.station().equals(stationNumber)) {
				matchingFirestations.add(firestation);
			}
		}
		if (matchingFirestations.isEmpty()) {
			throw new FirestationNotFoundException("Fire station N° {} not found" + stationNumber);
		}

		return matchingFirestations;
	}

	public List<Firestation> getFirestationByListStationNumber(List<String> stationNumbers) {
		return getAllFirestations().stream().filter(firestation -> stationNumbers.contains(firestation.station())).toList();
	}

	public Firestation getFirestationByAddress(String address) {
		return getAllFirestations().stream().filter(firestation -> firestation.address().contains(address)).findFirst().orElse(null);
	}

}
