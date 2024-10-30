package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Represents a medical record for a person.
 *
 * @param firstName   The first name of the person.
 * @param lastName    The last name of the person.
 * @param birthdate   The birthdate of the person.
 * @param medications A list of medications the person is taking.
 * @param allergies   A list of allergies the person has.
 */
@JsonIgnoreProperties(value = {"age", "child","adult"}, ignoreUnknown = false) 
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"firstName", "lastName", "birthdate", "medications", "allergies"})
public record MedicalRecordResponse(String firstName, String lastName, String birthdate, List<String> medications,
                            List<String> allergies) {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public String fullName() {
        return firstName + " " + lastName;
    }
   public int getAge() {
        LocalDate birthdate = LocalDate.parse(this.birthdate, FORMATTER);
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears();
    }

    public boolean isChild() {
        return this.getAge() <= 18;
    }

    public boolean isAdult() {
        return this.getAge() > 18;
    }


}
