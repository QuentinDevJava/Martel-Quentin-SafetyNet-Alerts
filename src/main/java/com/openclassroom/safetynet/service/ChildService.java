package com.openclassroom.safetynet.service;

import java.util.ArrayList;
import java.util.List;

import com.openclassroom.safetynet.model.ChildInfo;
import com.openclassroom.safetynet.model.Person;

public class ChildService {

	private List<ChildInfo> childInfo = new ArrayList<>();

	public ChildService(List<Person> personsByAddress, MedicalRecordService medicalRecordService, PersonService personService) {
		this.childInfo = new ArrayList<>();
		for (Person person : personsByAddress) {
			if (personService.getPersonAge(person, medicalRecordService) < 18) {
				ChildInfo child = personService.extractChildInfo(person, medicalRecordService, personService);
				childInfo.add(child);
			}
		}

	}

	@Override
	public String toString() {
		return "ListOfChild [childInfo=" + childInfo + "]";
	}

	public List<ChildInfo> getChildInfo() {
		return childInfo;
	}
}