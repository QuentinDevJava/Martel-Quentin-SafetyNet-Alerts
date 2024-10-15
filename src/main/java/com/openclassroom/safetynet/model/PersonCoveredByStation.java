package com.openclassroom.safetynet.model;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PersonCoveredByStation(@NotNull @NotEmpty List<PersonInfo> personInfos, @Min(0) int adultCount, @Min(0) int childCount) {

}
