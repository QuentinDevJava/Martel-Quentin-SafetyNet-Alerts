package com.openclassroom.safetynet.model;

import java.util.List;
import java.util.Map;

public record FloodInfo(Map<String, List<MedicalRecordInfo>> medicalRecordInfo) {

}
