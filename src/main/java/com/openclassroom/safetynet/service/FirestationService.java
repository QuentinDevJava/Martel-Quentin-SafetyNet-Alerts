package com.openclassroom.safetynet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.FirestationRequest;
import com.openclassroom.safetynet.model.FirestationResponse;
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

	private List<FirestationResponse> allFireStations() {
		return repository.loadTypeOfData(TypeOfData.FIRESTATIONS).stream()
				.map(firestationObj -> objectMapper.convertValue(firestationObj, FirestationResponse.class)).collect(Collectors.toList());
	}

	/**
	 * Creates a new fire station.
	 *
	 * @param firestation The fire station to create {@link FirestationResponse}.
	 * @return The created fire station {@link FirestationResponse}.
	 */
	public void createFirestation(FirestationResponse firestation) {
		List<FirestationResponse> firestations = allFireStations();
		firestations.add(firestation);
		saveFirestations(firestations);
		log.debug("Add firestation {} in allFireStations() : {}", firestation, firestations);
	}

	/**
	 * Updates an existing fire station.
	 *
	 * @param address     The address of the station to update.
	 * @param firestation The updated fire station {@link FirestationResponse}.
	 * @return The updated fire station {@link FirestationResponse}.
	 */
	public void updateFirestation(String address, FirestationResponse firestation) {
		FirestationResponse existingFirestation = getFirestationByAddress(address);
		log.debug("Found existing firestation: {}", existingFirestation);
		List<FirestationResponse> firestations = allFireStations();
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
		List<FirestationResponse> firestations = allFireStations();
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

	private void saveFirestations(List<FirestationResponse> firestations) {
		repository.saveData(TypeOfData.FIRESTATIONS, firestations.stream()
				.map(firestationObj -> objectMapper.convertValue(firestationObj, FirestationResponse.class)).collect(Collectors.toList()));
	}

	/**
	 * Returns the list of fire stations corresponding to the given station numbers.
	 *
	 * @param stationNumbers The station numbers.
	 * @return The corresponding fire stations {@link FirestationResponse}.
	 */
	public List<FirestationResponse> findFireStationByStationNumber(int stationNumber) {
		return allFireStations().stream().filter(f -> f.station() == stationNumber).toList();
	}

	/**
	 * Returns the list of fire stations corresponding to the given station numbers.
	 *
	 * @param stationNumbers The list of station numbers.
	 * @return The list of corresponding fire stations {@link FirestationResponse}.
	 */
	public List<FirestationResponse> getFirestationByListStationNumber(List<Integer> stationNumbers) {
		return allFireStations().stream().filter(firestation -> stationNumbers.contains(firestation.station())).toList();
	}

	/**
	 * Returns the fire station corresponding to the given address.
	 *
	 * @param address The address of the station.
	 * @return The corresponding fire station {@link FirestationResponse}.
	 */
	public FirestationResponse getFirestationByAddress(String address) {
		return allFireStations().stream().filter(firestation -> firestation.address().contains(address)).findFirst().orElse(null);
	}

	public FirestationResponse firestationRequestToFirestationResponse(FirestationRequest request) {
		return new FirestationResponse(request.address(), request.station());
	}

}
