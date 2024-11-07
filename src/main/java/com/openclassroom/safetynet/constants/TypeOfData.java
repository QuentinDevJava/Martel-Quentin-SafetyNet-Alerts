package com.openclassroom.safetynet.constants;

/**
 * Enum representing different types of data handled in the system.
 * 
 * Each type of data is associated with a corresponding JSON key.
 * 
 */
public enum TypeOfData {
	/**
	 * Represents the "persons" data type.
	 */
	PERSONS("persons"),

	/**
	 * Represents the "firestations" data type.
	 */
	FIRESTATIONS("firestations"),

	/**
	 * Represents the "medicalrecords" data type.
	 */
	MEDICALRECORDS("medicalrecords");
	;

	private final String jsonKey;

	TypeOfData(String jsonKey) {
		this.jsonKey = jsonKey;
	}

	/**
	 * Returns the JSON key associated with this data type.
	 *
	 * @return The JSON key.
	 */
	public String getJsonKey() {
		return jsonKey;
	}

}