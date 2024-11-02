package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a fire station with its address and station number.
 *
 * @param address The address of the fire station.
 * @param station The station number of the fire station.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "address", "station" })
public record FirestationResponse(String address, int station) {
}
