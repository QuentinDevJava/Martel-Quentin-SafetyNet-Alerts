package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PersonLastNameInfo(@NotNull @NotEmpty List<PersonsLastNameInfo> persons) {

}
