package com.openclassroom.safetynet.model;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents the medical records of people affected by flooding, grouped
 * according to the address of their fire station.
 *
 * @param medicalRecordInfo A map whose key is the address of the fire station
 *                          and whose value is the list of people's medical
 *                          records associated with this fire station
 *                          {@link MedicalRecordInfo}
 */
public record PersonFloodInfo(@NotNull @NotEmpty Map<String, @NotNull @NotEmpty List<MedicalRecordInfo>> medicalRecordInfo) {

}
