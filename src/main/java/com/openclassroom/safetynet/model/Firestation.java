package com.openclassroom.safetynet.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({ "address", "station" })
public record Firestation(@NotBlank String address, @NotBlank String station) {
}
