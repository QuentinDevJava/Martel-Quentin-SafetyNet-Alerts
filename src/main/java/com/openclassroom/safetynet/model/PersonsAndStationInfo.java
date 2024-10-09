package com.openclassroom.safetynet.model;

import java.util.List;

public record PersonsAndStationInfo(List<MedicalRecordInfo> persons, String station) {

}
