package com.streamingdemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProgressRequest(
        @NotNull UUID sessionId,
        @NotBlank String titleId,
        double positionSec,
        double durationSec,
        boolean completed
) {
}
