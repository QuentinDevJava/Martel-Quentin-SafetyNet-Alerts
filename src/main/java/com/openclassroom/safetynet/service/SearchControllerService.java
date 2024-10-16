package com.openclassroom.safetynet.service;

import java.util.List;

import com.openclassroom.safetynet.model.PersonCoveredByStation;
import com.openclassroom.safetynet.model.PersonFloodInfo;
import com.openclassroom.safetynet.model.PersonsAndStationInfo;

/**
 * Interface defining the operations for retrieving information about people for
 * SearchController.
 */
public interface SearchControllerService {

	/**
	 * Returns a list of people covered by a given fire station.
	 *
	 * @param stationNumber The fire station number.
	 * @return A list of {@link PersonCoveredByStation} representing the people
	 *         covered by the station.
	 */
	PersonCoveredByStation findCoveredPersonsByFireStation(String stationNumber);

	/**
	 * Returns information about the people and fire station associated with a given
	 * address.
	 *
	 * @param address The address to search for.
	 * @return A {@link PersonsAndStationInfo} object containing information about
	 *         the people and the station.
	 */
	PersonsAndStationInfo getPersonsAndStationInfoByAddress(String address);

	/**
	 * Returns information about people affected by a flood, based on the fire
	 * station numbers.
	 *
	 * @param stationNumber A list of fire station numbers.
	 * @return A {@link PersonFloodInfo} object containing information about the
	 *         people affected by the flood.
	 */
	PersonFloodInfo floodInfo(List<String> stationNumber);
}