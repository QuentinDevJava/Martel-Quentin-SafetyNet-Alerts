package com.openclassroom.safetynet.model;

import java.util.List;

public record MedicalRecordInfo(String firstName, String lastName, String phone, int age, List<String> medications,
		List<String> allergies) {
}
