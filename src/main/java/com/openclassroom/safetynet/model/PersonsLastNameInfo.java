package com.openclassroom.safetynet.model;

import java.util.List;

public record PersonsLastNameInfo(String lastName, String firstName, String address, int age, String email, List<String> medications, List<String> allergies) {

}
