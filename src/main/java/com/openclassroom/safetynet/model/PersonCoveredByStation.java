package com.openclassroom.safetynet.model;

import java.util.List;

/**
 * Represents information about persons covered by a fire station, including
 * adult and child counts.
 *
 * @param personInfos A list of basic information about each person covered by
 *                    the station {@link PersonInfo}.
 * @param adultCount  The number of adults covered by the station.
 * @param childCount  The number of children covered by the station.
 */
public record PersonCoveredByStation(List<PersonInfo> personInfos, int adultCount, int childCount) {

}
