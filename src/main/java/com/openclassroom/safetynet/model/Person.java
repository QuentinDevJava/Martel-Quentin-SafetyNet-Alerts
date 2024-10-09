package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "lastName", "address", "city", "zip", "phone", "email" })
public record Person(String firstName, String lastName, String address, String city, String zip, String phone, String email) {
}
