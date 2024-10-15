package com.openclassroom.safetynet.model;

import java.util.List;

public record PersonCoveredByStation(List<PersonInfo> personInfos, int adultCount, int childCount) {

}
