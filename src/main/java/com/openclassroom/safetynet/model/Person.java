package com.openclassroom.safetynet.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.openclassroom.safetynet.service.MedicalRecordService;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "lastName", "address", "city", "zip", "phone", "email" })
public record Person( String firstName, String lastName, String address, String city, String zip, String phone,
		String email) {

	public static int getPersonAge(Person person, MedicalRecordService medicalRecordService) {
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFullName(person.firstName(),
				person.lastName());
		if (medicalRecord != null) {
			String dateString = medicalRecord.birthdate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate birthdate = LocalDate.parse(dateString, formatter);
			LocalDate today = LocalDate.now();
			return Period.between(birthdate, today).getYears();
		}
		return 0;
	}

}
