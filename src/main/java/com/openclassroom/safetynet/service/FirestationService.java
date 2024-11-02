package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.FirestationDTO;
import com.openclassroom.safetynet.repository.JsonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service defining the operations for managing fire stations.
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class FirestationService {

	private final JsonRepository repository;
	private final ObjectMapper objectMapper;

	/**
	 * Creates a new fire station.
	 *
	 * @param firestation The fire station to create {@link FirestationDTO}.
	 * @return The created fire station {@link FirestationDTO}.
	 */
	public void createFirestation(FirestationDTO firestation) {
		List<FirestationDTO> firestations = allFireStations();
		firestations.add(firestation);
		saveFirestations(firestations);
		log.debug("Add firestation {} in allFireStations() : {}", firestation, firestations);
	}

	/**
	 * Updates an existing fire station.
	 *
	 * @param address     The address of the station to update.
	 * @param firestation The updated fire station {@link FirestationDTO}.
	 * @return The updated fire station {@link FirestationDTO}.
	 */
	public void updateFirestation(String address, FirestationDTO firestation) {
		FirestationDTO existingFirestation = getFirestationByAddress(address);
		log.debug("Found existing firestation: {}", existingFirestation);
		List<FirestationDTO> firestations = allFireStations();
		firestations.set(firestations.indexOf(existingFirestation), firestation);
		log.debug("Updated firestation list: {}", firestations);
		saveFirestations(firestations);
	}

	/**
	 * Deletes an existing fire station.
	 *
	 * @param address The address of the station to delete.
	 * @return True if the fire station was deleted successfully, false otherwise.
	 */
	public Boolean deleteFirestation(String address) {
		List<FirestationDTO> firestations = allFireStations();
		boolean firestationsDeleted;
		if (address.length() <= 2) {
			int stationNumber = Integer.parseInt(address);
			firestationsDeleted = firestations.removeIf(f -> f.station() == stationNumber);
		} else {
			firestationsDeleted = firestations.removeIf(f -> f.address().equals(address));
		}
		if (firestationsDeleted) {
			saveFirestations(firestations);
			log.debug("Fire station deleted successfully for {}.", address);
		}
		return firestationsDeleted;
	}

	/**
	 * Returns the list of fire stations corresponding to the given station numbers.
	 *
	 * @param stationNumbers The station numbers.
	 * @return The corresponding fire stations {@link FirestationDTO}.
	 */
	public List<FirestationDTO> findFireStationByStationNumber(int stationNumber) {
		return allFireStations().stream().filter(f -> f.station() == stationNumber).toList();
	}

	/**
	 * Returns the list of fire stations corresponding to the given station numbers.
	 *
	 * @param stationNumbers The list of station numbers.
	 * @return The list of corresponding fire stations {@link FirestationDTO}.
	 */
	public List<FirestationDTO> getFirestationByListStationNumber(List<Integer> stationNumbers) {
		return allFireStations().stream().filter(firestation -> stationNumbers.contains(firestation.station())).toList();
	}

	/**
	 * Returns the fire station corresponding to the given address.
	 *
	 * @param address The address of the station.
	 * @return The corresponding fire station {@link FirestationDTO}.
	 */
	public FirestationDTO getFirestationByAddress(String address) {
		return allFireStations().stream().filter(firestation -> firestation.address().contains(address)).findFirst().orElse(null);
	}

	private List<FirestationDTO> allFireStations() {
		return repository.loadTypeOfData(TypeOfData.FIRESTATIONS).stream()
				.map(firestationObj -> objectMapper.convertValue(firestationObj, FirestationDTO.class))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private void saveFirestations(List<FirestationDTO> firestations) {
		repository.saveData(TypeOfData.FIRESTATIONS, firestations.stream()
				.map(firestationObj -> objectMapper.convertValue(firestationObj, FirestationDTO.class)).collect(Collectors.toList()));
	}
}
