package com.openclassroom.safetynet.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.JsonPath;
import com.openclassroom.safetynet.constants.TypeOfData;

public class DataRepository {

	private ObjectMapper objectMapper = new ObjectMapper();

	public DataRepository() {
		this.objectMapper = new ObjectMapper();
	}

	public void saveData(TypeOfData typeOfData, List<Object> data)
			throws StreamWriteException, DatabindException, IOException {
		Map<String, List<Object>> jsonData = LoadData();

		switch (typeOfData) {
		case persons -> jsonData.put("persons", data);
		case firestations -> jsonData.put("firestations", data);
		case medicalrecords -> jsonData.put("medicalrecords", data);
		default -> throw new IllegalArgumentException("Error : Type of data not found");
		}
		objectMapper.writeValue(new File(JsonPath.JSONPATH), jsonData);
	}

	public Map<String, List<Object>> LoadData() {
		try {
			Map<String, List<Object>> jsonData = objectMapper.readValue(new File(JsonPath.JSONPATH),
					new TypeReference<Map<String, List<Object>>>() {
					});
			return jsonData;
		} catch (Exception e) {
			System.err.println("Error loading data : " + e.getMessage());
			return null;
		}
	}

	public List<Object> SelectTypeOfData(TypeOfData typeOfData) {
		Map<String, List<Object>> data = LoadData();

		return switch (typeOfData) {
		case persons -> data.getOrDefault("persons", new ArrayList<>());
		case firestations -> data.getOrDefault("firestations", new ArrayList<>());
		case medicalrecords -> data.getOrDefault("medicalrecords", new ArrayList<>());
		default -> throw new IllegalArgumentException("Error : Type of data no found");
		};
	}

}