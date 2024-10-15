package com.openclassroom.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.model.Person;
import com.openclassroom.safetynet.repository.JsonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonUseCase {

    private final FireStationUseCase fireStationService;
    private final JsonRepository repository;
    private final ObjectMapper mapper;
    private final MedicalRecordUseCase medicalRecordService;

    public List<PersonCoveredByStationDTO> findCoveredPersonsByFireStation(int stationNumber) {
        log.info("Finding covered persons by fire station {}", stationNumber);
        List<Firestation> firestations = fireStationService.findAllByStationNumber(stationNumber);
        List<Person> persons = allPersons();
        List<Person> personByStation = firestations.stream()
                .flatMap(f -> persons.stream().filter(p -> p.address().equals(f.address())).toList().stream()).toList();
        return null;
    }

    private List<Person> allPersons() {
        return repository.selectTypeOfData(TypeOfData.PERSONS)
                .stream()
                .map(p -> mapper.convertValue(p, Person.class))
                .toList();
    }
}
