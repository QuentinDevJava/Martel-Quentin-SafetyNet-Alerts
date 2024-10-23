package com.openclassroom.safetynet.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

public class PersonCoveredByFireStation {
	public List<PersonInfo> personInfos;
	public int adultCounts;
	public int childCounts;
	
	private final Predicate<Integer> isAdult = age -> age > 18;
	private final Predicate<Integer> isChild = age -> age <= 18;

	public PersonCoveredByFireStation(List<Person> personInfos, List<MedicalRecord> medicalRecords) {
		this.personInfos = personInfos.stream().map(p -> p.extractNameAddressAndPhone(p)).toList();
		this.adultCounts = (int) medicalRecords.stream().map(this::getAge).filter(isChild).count();
		this.childCounts = (int) medicalRecords.stream().map(this::getAge).filter(isAdult).count();
	}

	public int getAge(MedicalRecord medicalRecord) {
		String dateString = medicalRecord.birthdate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthdate = LocalDate.parse(dateString, formatter);
		LocalDate today = LocalDate.now();
		return Period.between(birthdate, today).getYears();
	}
}
