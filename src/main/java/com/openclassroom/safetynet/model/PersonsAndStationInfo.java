package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Represents information about a list of persons and their associated fire
 * station.
 *
 * @param persons A list of medical records containing information about each
 *                person {@link MedicalRecordInfo}.
 * @param station The station number of the fire station.
 */
public record PersonsAndStationInfo(@NotNull @NotEmpty List<MedicalRecordInfo> persons, @NotNull @Positive int stationNumber) {

}
