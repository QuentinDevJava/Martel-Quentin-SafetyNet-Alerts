package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "firstName", "lastName", "address", "city", "zip", "phone", "email" })
public record Person(String firstName, String lastName, String address, String city, String zip, String phone, String email) {

}
