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

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// TODO voir s'il faut faire les étapes avec des debug et info et verifier la
	// gistion des erreurs

	private ObjectMapper objectMapper = new ObjectMapper();

	public void saveData(TypeOfData typeOfData, List<Object> data) {
		Map<String, List<Object>> jsonData = LoadData();

		switch (typeOfData) {
		case persons -> jsonData.put("persons", data);
		case firestations -> jsonData.put("firestations", data);
		case medicalrecords -> jsonData.put("medicalrecords", data);
		default -> throw new IllegalArgumentException("Error : Type of data not found");
		}
		try {
			objectMapper.writeValue(new File(JsonPath.JSONPATH), jsonData);
		} catch (IOException e) {
			throw new DataSavingException("Error saving data: " + e.getMessage());
		}
	}

	public Map<String, List<Object>> LoadData() {
		try {
			return objectMapper.readValue(new File(JsonPath.JSONPATH), new TypeReference<Map<String, List<Object>>>() {
			});
		} catch (Exception e) {
			throw new DataLoadingException("Error loading data: " + e.getMessage());
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