package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents information about persons covered by a fire station, including
 * adult and child counts.
 *
 * @param personInfos A list of basic information about each person covered by
 *                    the station {@link PersonInfo}.
 * @param adultCount  The number of adults covered by the station.
 * @param childCount  The number of children covered by the station.
 */
public record PersonCoveredByStation(@NotNull @NotEmpty List<PersonInfo> personInfos, @Min(0) int adultCount, @Min(0) int childCount) {

}
