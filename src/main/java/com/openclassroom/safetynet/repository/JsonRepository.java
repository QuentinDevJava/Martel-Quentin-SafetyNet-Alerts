package com.openclassroom.safetynet.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.JsonFilePath;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.DataLoadingException;
import com.openclassroom.safetynet.exceptions.DataSavingException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JsonRepository {

	private final ObjectMapper objectMapper;

	public void saveData(TypeOfData typeOfData, List<Object> data) {
		Map<String, List<Object>> jsonData = loadJsonAllData();
		jsonData.put(typeOfData.getJsonKey(), data);
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(getJsonFilePath()), jsonData);
		} catch (IOException e) {
			throw new DataSavingException("Error saving data: " + e.getMessage());
		}
	}

	public Map<String, List<Object>> loadJsonAllData() {
		try {
			return objectMapper.readValue(new File(getJsonFilePath()), new TypeReference<Map<String, List<Object>>>() {
			});
		} catch (Exception e) {
			throw new DataLoadingException("Error loading data: " + e.getMessage());
		}
	}

	public List<Object> loadTypeOfData(TypeOfData typeOfData) {
		Map<String, List<Object>> data = loadJsonAllData();
		return data.getOrDefault(typeOfData.getJsonKey(), new ArrayList<>());
	}

	private String getJsonFilePath() {
		if (System.getProperty("test.mode") != null && System.getProperty("test.mode").equals("true")) {
			return JsonFilePath.JSONTESTFILEPATH;
		} else {
			return JsonFilePath.JSONFILEPATH;
		}
	}
}