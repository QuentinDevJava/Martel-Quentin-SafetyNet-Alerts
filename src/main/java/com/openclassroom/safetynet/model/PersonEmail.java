package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record PersonEmail(@NotNull @NotNull List<String> email) {
}
