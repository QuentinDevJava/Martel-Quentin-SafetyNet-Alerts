package com.openclassroom.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.MedicalRecord;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.repository.JsonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalRecordUseCase {

    private final JsonRepository repository;
    private final ObjectMapper mapper;

    public List<MedicalRecord> findPersonsMedicalRecords(List<Person> persons) {
        return persons.stream()
                .map(p -> getMedicalRecordByFullName(p.firstName(), p.lastName()))
                .filter(Objects::nonNull)
                .toList();
    }

    private MedicalRecord getMedicalRecordByFullName(String firstName, String lastName) {
        return allMedicalRecords().stream()
                .filter(m -> m.firstName().equals(firstName) && m.lastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    private List<MedicalRecord> allMedicalRecords() {
        return repository.selectTypeOfData(TypeOfData.MEDICALRECORDS)
                .stream()
                .map(m -> mapper.convertValue(m, MedicalRecord.class))
                .toList();
    }
}
