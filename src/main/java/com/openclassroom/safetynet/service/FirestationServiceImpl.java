package com.openclassroom.safetynet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.FirestationNotFoundException;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.repository.DataRepository;

@Service
public class FirestationServiceImpl implements FirestationService {

	private final DataRepository dataRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public FirestationServiceImpl(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

	public List<Firestation> getAllFirestations() {
		return dataRepository.SelectTypeOfData(TypeOfData.firestations).stream().map(firestationObj -> objectMapper.convertValue(firestationObj, Firestation.class)).collect(Collectors.toList());
	}

	public Firestation createFirestation(Firestation firestation) {
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
		dataRepository.saveData(TypeOfData.firestations, firestations.stream().map(firestationObj -> objectMapper.convertValue(firestationObj, Firestation.class)).collect(Collectors.toList()));
	}

	public List<Firestation> getFirestationByStationNumber(String stationNumber) {
		List<String> stationNumbers = List.of(stationNumber);
		return getFirestationByListStationNumber(stationNumbers);
	}

	public List<Firestation> getFirestationByListStationNumber(List<String> stationNumbers) {
		return getAllFirestations().stream().filter(firestation -> stationNumbers.contains(firestation.station())).collect(Collectors.toList());
	}

	public Firestation getFirestationByAddress(String address) {
		return getAllFirestations().stream().filter(firestation -> firestation.address().contains(address)).findFirst().orElse(null);
	}

}
