package com.openclassroom.safetynet.dto;

import java.util.List;
import java.util.Map;

/**
 * Represents the medical records of people affected by flooding, grouped
 * according to the address of their fire station.
 *
 * @param medicalRecordInfo A map whose key is the address of the fire station
 *                          and whose value is the list of people's medical
 *                          records associated with this fire station
 *                          {@link MedicalRecordInfo}
 */
public record PersonFloodInfo(Map<String, List<MedicalRecordInfo>> medicalRecordInfo) {

}
