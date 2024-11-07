package com.openclassroom.safetynet.model;

import java.util.List;

/**
 * Represents information about a list of persons and their associated fire
 * station.
 *
 * @param personsMedicalRecords A list of medical records containing information
 *                              about each person {@link MedicalRecordInfo}.
 * @param stationNumber         The station number of the fire station.
 */
public record PersonsAndStationInfo(List<MedicalRecordInfo> personsMedicalRecords, int stationNumber) {

}
