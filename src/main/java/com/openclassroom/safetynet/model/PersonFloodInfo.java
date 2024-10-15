package com.openclassroom.safetynet.model;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PersonFloodInfo(@NotNull @NotEmpty Map<String, @NotNull @NotEmpty List<MedicalRecordInfo>> medicalRecordInfo) {

}
