package com.openclassroom.safetynet.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.JsonDataFilePath;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.DataLoadingException;
import com.openclassroom.safetynet.exceptions.DataSavingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Repository for managing JSON data.
 *
 * This class provides methods for saving, loading, and retrieving data from a
 * JSON file.
 */
@Repository
@RequiredArgsConstructor
public class JsonRepository {

	private final ObjectMapper objectMapper;
	private final Environment environment;

	/**
	 * Saves data to the JSON file.
	 *
	 * @param typeOfData The type of data to save. This is used to determine the
	 *                   correct key in the JSON file.
	 * @param data       The list of data objects to save.
	 * @throws DataSavingException If an error occurs while saving the data to the
	 *                             JSON file.
	 */
	public void saveData(TypeOfData typeOfData, List<Object> data) {
		Map<String, List<Object>> jsonData = loadJsonAllData();
		jsonData.put(typeOfData.getJsonKey(), data);
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(getJsonFilePath()), jsonData);
		} catch (IOException e) {
			throw new DataSavingException("Error saving data: " + e.getMessage());
		}
	}

	/**
	 * Loads all data from the JSON file.
	 *
	 * @return A map containing all data from the JSON file.
	 * @throws DataLoadingException If an error occurs while loading the data from
	 *                              the JSON file.
	 */
	public Map<String, List<Object>> loadJsonAllData() {
		try {
			return objectMapper.readValue(new File(getJsonFilePath()), new TypeReference<Map<String, List<Object>>>() {
			});
		} catch (Exception e) {
			throw new DataLoadingException("Error loading data: " + e.getMessage());
		}
	}

	/**
	 * Returns the path to the JSON file.
	 *
	 * The path is determined based on whether the "test.mode" system property is
	 * set to "true". If it is, the test file path is used; otherwise, the regular
	 * file path is used.
	 * 
	 * @param typeOfData The type of data to load.
	 * @return The path to the JSON file.
	 */
	public List<Object> loadTypeOfData(TypeOfData typeOfData) {
		Map<String, List<Object>> data = loadJsonAllData();
		return data.getOrDefault(typeOfData.getJsonKey(), new ArrayList<>());
	}

	private String getJsonFilePath() {
		if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
			return JsonDataFilePath.JSONTESTFILEPATH.toString();
		} else {
			return JsonDataFilePath.JSONFILEPATH.toString();
		}
	}
}