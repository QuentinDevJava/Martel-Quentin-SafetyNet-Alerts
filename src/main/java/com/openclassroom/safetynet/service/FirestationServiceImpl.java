package com.openclassroom.safetynet.service;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirestationServiceImpl implements FirestationService {

	private final JsonRepository repository;
	private final ObjectMapper objectMapper;

	private List<Firestation> allFireStations() {
		return repository.loadTypeOfData(TypeOfData.FIRESTATIONS).stream().map(firestationObj -> objectMapper.convertValue(firestationObj, Firestation.class)).collect(Collectors.toList());
	}

	@Override
	public Firestation createFirestation(Firestation firestation) {
		for (String field : new String[] { firestation.station(), firestation.address() }) {
			if (Optional.ofNullable(field).map(StringUtils::isBlank).orElse(true)) {
				throw new IllegalArgumentException("All fields of the person must be filled.");
			}
		}
		List<Firestation> firestations = allFireStations();
		firestations.add(firestation);
		saveFirestations(firestations);
		return firestation;
	}

	@Override
	public Firestation updateFirestation(String address, Firestation firestation) {
		Firestation existingFirestation = getFirestationByAddress(address);
		if (existingFirestation != null) {
			List<Firestation> firestations = allFireStations();
			firestations.set(firestations.indexOf(existingFirestation), firestation);
			saveFirestations(firestations);
			return firestation;
		} else {
			throw new FirestationNotFoundException("Fire station not found for address: " + address);
		}
	}

	@Override
	public Boolean deleteFirestation(String address) {
		List<Firestation> firestations = allFireStations();
		boolean firestationsDeleted = firestations.removeIf(f -> f.address().equals(address));
		if (firestationsDeleted) {
			saveFirestations(firestations);
			return true;
		} else {
			throw new FirestationNotFoundException("Fire station not found for address: " + address);
		}
	}

	private void saveFirestations(List<Firestation> firestations) {
		repository.saveData(TypeOfData.FIRESTATIONS, firestations.stream().map(firestationObj -> objectMapper.convertValue(firestationObj, Firestation.class)).collect(Collectors.toList()));
	}

	@Override
	public List<Firestation> findFireStationByStationNumber(String stationNumber) {
		return allFireStations().stream().filter(f -> f.station().equals(stationNumber)).toList();

	}

	@Override
	public List<Firestation> getFirestationByListStationNumber(List<String> stationNumbers) {
		return allFireStations().stream().filter(firestation -> stationNumbers.contains(firestation.station())).toList();
	}

	@Override
	public Firestation getFirestationByAddress(String address) {
		return allFireStations().stream().filter(firestation -> firestation.address().contains(address)).findFirst().orElse(null);
	}

}
