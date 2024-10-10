package com.openclassroom.safetynet.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.JsonPath;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.exceptions.DataLoadingException;
import com.openclassroom.safetynet.exceptions.DataSavingException;

@Repository
public class DataRepository {

	private ObjectMapper objectMapper = new ObjectMapper();

	public void saveData(TypeOfData typeOfData, List<Object> data) {
		Map<String, List<Object>> jsonData = loadJsonData();

		switch (typeOfData) {
		case PERSONS -> jsonData.put("persons", data);
		case FIRESTATIONS -> jsonData.put("firestations", data);
		case MEDICALRECORDS -> jsonData.put("medicalrecords", data);
		default -> throw new IllegalArgumentException("Error : Type of data not found");
		}
		try {
			objectMapper.writeValue(new File(JsonPath.JSONFILEPATH), jsonData);
		} catch (IOException e) {
			throw new DataSavingException("Error saving data: " + e.getMessage());
		}
	}

	public Map<String, List<Object>> loadJsonData() {
		try {
			return objectMapper.readValue(new File(JsonPath.JSONFILEPATH), new TypeReference<Map<String, List<Object>>>() {
			});
		} catch (Exception e) {
			throw new DataLoadingException("Error loading data: " + e.getMessage());
		}
	}

	public List<Object> selectTypeOfData(TypeOfData typeOfData) {
		Map<String, List<Object>> data = loadJsonData();

		return switch (typeOfData) {
		case PERSONS -> data.getOrDefault("persons", new ArrayList<>());
		case FIRESTATIONS -> data.getOrDefault("firestations", new ArrayList<>());
		case MEDICALRECORDS -> data.getOrDefault("medicalrecords", new ArrayList<>());
		default -> throw new IllegalArgumentException("Error : Type of data no found");
		};
	}

}