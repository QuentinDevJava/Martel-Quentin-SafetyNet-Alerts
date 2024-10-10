package com.openclassroom.safetynet.model;

import java.util.List;

public record PersonsLastNameInfo(String firstName, String lastName, String address, int age, String email, List<String> medications, List<String> allergies) {

}
