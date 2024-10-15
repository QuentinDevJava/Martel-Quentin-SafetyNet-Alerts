package com.openclassroom.safetynet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassroom.safetynet.model.Firestation;

@Service
public interface FirestationService {

	/**
	 * Creates a new fire station.
	 *
	 * @param firestation The fire station to create.
	 * @return The created fire station.
	 */
	Firestation createFirestation(Firestation firestation);

	/**
	 * Updates an existing fire station.
	 *
	 * @param address     The address of the station to update.
	 * @param firestation The updated fire station.
	 * @return The updated fire station.
	 */
	Firestation updateFirestation(String address, Firestation firestation);

	/**
	 * Deletes an existing fire station.
	 *
	 * @param address The address of the station to delete.
	 * @return True if the fire station was deleted successfully, false otherwise.
	 */
	Boolean deleteFirestation(String address);

	/**
	 * Returns the list of fire stations corresponding to the given station numbers.
	 *
	 * @param stationNumbers The station numbers.
	 * @return The corresponding fire stations.
	 */
	List<Firestation> findAllByStationNumber(String stationNumber);

	/**
	 * Returns the list of fire stations corresponding to the given station numbers.
	 *
	 * @param stationNumbers The list of station numbers.
	 * @return The list of corresponding fire stations.
	 */
	List<Firestation> getFirestationByListStationNumber(List<String> stationNumbers);

	/**
	 * Returns the fire station corresponding to the given address.
	 *
	 * @param address The address of the station.
	 * @return The corresponding fire station.
	 */
	Firestation getFirestationByAddress(String address);
}
