package com.openclassroom.safetynet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.FirestationNotFoundException;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.repository.JsonRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service defining the operations for managing fire stations.
 */

@Service
@RequiredArgsConstructor
public class FirestationService {

	private final JsonRepository repository;
	private final ObjectMapper objectMapper;

	private List<Firestation> allFireStations() {
		return repository.loadTypeOfData(TypeOfData.FIRESTATIONS).stream().map(firestationObj -> objectMapper.convertValue(firestationObj, Firestation.class)).collect(Collectors.toList());
	}

	/**
	 * Creates a new fire station.
	 *
	 * @param firestation The fire station to create {@link Firestation}.
	 * @return The created fire station {@link Firestation}.
	 */
	public Firestation createFirestation(Firestation firestation) {

		List<Firestation> firestations = allFireStations();
		firestations.add(firestation);
		saveFirestations(firestations);
		return firestation;
	}

	/**
	 * Updates an existing fire station.
	 *
	 * @param address     The address of the station to update.
	 * @param firestation The updated fire station {@link Firestation}.
	 * @return The updated fire station {@link Firestation}.
	 */
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

	/**
	 * Deletes an existing fire station.
	 *
	 * @param address The address of the station to delete.
	 * @return True if the fire station was deleted successfully, false otherwise.
	 */
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

	/**
	 * Returns the list of fire stations corresponding to the given station numbers.
	 *
	 * @param stationNumbers The station numbers.
	 * @return The corresponding fire stations {@link Firestation}.
	 */
	public List<Firestation> findFireStationByStationNumber(int stationNumber) {
		return allFireStations().stream().filter(f -> f.stationNumber() == stationNumber) // Use getStationNumber() and compare with ==
				.toList();
	}

	/**
	 * Returns the list of fire stations corresponding to the given station numbers.
	 *
	 * @param stationNumbers The list of station numbers.
	 * @return The list of corresponding fire stations {@link Firestation}.
	 */
	public List<Firestation> getFirestationByListStationNumber(List<Integer> stationNumbers) {
		return allFireStations().stream().filter(firestation -> stationNumbers.contains(firestation.stationNumber())).toList();
	}

	/**
	 * Returns the fire station corresponding to the given address.
	 *
	 * @param address The address of the station.
	 * @return The corresponding fire station {@link Firestation}.
	 */
	public Firestation getFirestationByAddress(String address) {
		return allFireStations().stream().filter(firestation -> firestation.address().contains(address)).findFirst().orElse(null);
	}

}
