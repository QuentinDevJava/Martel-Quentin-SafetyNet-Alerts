package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;

import com.openclassroom.safetynet.model.ChildInfo;
import com.openclassroom.safetynet.model.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChildService {

	private List<ChildInfo> childInfo = new ArrayList<>();

	public ChildService(List<Person> personsByAddress, MedicalRecordService medicalRecordService, PersonService personService) {
		this.childInfo = personsByAddress.stream().filter(person -> personService.getPersonAge(person, medicalRecordService) < 18)
				.map(person -> personService.extractChildInfo(person, medicalRecordService, personService)).toList();
	}

	@Override
	public String toString() {
		return "ListOfChild [childInfo=" + childInfo + "]";
	}

	public List<ChildInfo> getChildInfo() {
		return childInfo;
	}
}