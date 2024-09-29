package com.openclassroom.safetynet.service;

import java.util.List;

import com.openclassroom.safetynet.model.Firestation;

public interface FirestationService {
	Firestation createFirestation(Firestation firestation);

	Firestation updateFirestation(String address, Firestation firestation);

	void deleteFirestation(String address);

	List<Firestation> getAllFirestations();

	List<Firestation> getFirestationByStationNumber(String stationNumber);

}
