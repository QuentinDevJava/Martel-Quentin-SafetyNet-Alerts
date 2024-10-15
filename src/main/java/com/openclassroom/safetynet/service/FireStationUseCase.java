package com.openclassroom.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.safetynet.constants.TypeOfData;
import com.openclassroom.safetynet.model.Firestation;
import com.openclassroom.safetynet.repository.JsonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FireStationUseCase {

    private final JsonRepository repository;
    private final ObjectMapper mapper;

    public List<Firestation> findAllByStationNumber(int stationNumber) {
        return allFireStations()
                .stream()
                .filter(f -> matchStationNumber(stationNumber, f.station()))
                .toList();
    }

    private static boolean matchStationNumber(int stationNumber, String station) {
        return stationNumber == Integer.parseInt(station);
    }

    private List<Firestation> allFireStations() {
        return repository.selectTypeOfData(TypeOfData.FIRESTATIONS)
                .stream()
                .map(o -> mapper.convertValue(o, Firestation.class))
                .toList();
    }
}
