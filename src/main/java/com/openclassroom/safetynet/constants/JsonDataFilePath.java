package com.openclassroom.safetynet.constants;

/**
 * Utility class that holds the file paths to JSON data files used in the
 * application.
 * 
 */
public class JsonDataFilePath {

	private JsonDataFilePath() {
		super();
	}

	/**
	 * The file path to the main JSON data file in the application's resources
	 * folder.
	 * 
	 * This is the default data file used for reading and writing application data
	 * in JSON format.
	 * 
	 */
	public static final String JSONFILEPATH = "src/main/resources/data.json";

	/**
	 * The file path to the test JSON data file located in the test resources
	 * folder.
	 * 
	 * This file is typically used for unit or integration tests that require a
	 * test-specific JSON data file.
	 * 
	 */
	public static final String JSONTESTFILEPATH = "src/test/resources/dataTest.json";
}
