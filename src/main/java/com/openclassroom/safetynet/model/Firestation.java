package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Represents a fire station with its address and station number.
 *
 * @param address The address of the fire station.
 * @param station The station number of the fire station.
 */
@JsonPropertyOrder({ "address", "station" })
public record Firestation(@NotBlank String address, @Positive int station) {
}
