package com.openclassroom.safetynet.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents information about persons covered by a fire station, including
 * adult and child counts.
 *
 * @param personInfos A list of basic information about each person covered by
 *                    the station {@link PersonInfo}.
 * @param adultCount  The number of adults covered by the station.
 * @param childCount  The number of children covered by the station.
 */
public record PersonCoveredByStation(List<PersonInfo> personInfos, int adultCounts, int childCounts) {

    static  Predicate<Integer> isAdult = age -> age > 18;
    static  Predicate<Integer> isChild = age -> age <= 18;

     public static PersonCoveredByStation of(List<Person> persons, List<MedicalRecord> medicalRecords) {
        List<PersonInfo> personInfos = persons.stream().map(PersonInfo::new).toList();
        int adultCounts = (int) medicalRecords.stream().map(PersonCoveredByStation::getAge).filter(isAdult).count();
        int childCounts = (int) medicalRecords.stream().map(PersonCoveredByStation::getAge).filter(isChild).count();
        return new PersonCoveredByStation(personInfos, adultCounts, childCounts);
    }

    public static int getAge(MedicalRecord medicalRecord) {
        String dateString = medicalRecord.birthdate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthdate = LocalDate.parse(dateString, formatter);
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears();
    }
}
