package com.openclassroom.safetynet.constants;

public enum TypeOfData {
	PERSONS("persons"), FIRESTATIONS("firestations"), MEDICALRECORDS("medicalrecords");

	private final String jsonKey;

	TypeOfData(String jsonKey) {
		this.jsonKey = jsonKey;
	}

	public String getJsonKey() {
		return jsonKey;
	}

}