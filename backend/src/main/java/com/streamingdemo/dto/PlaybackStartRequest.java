package com.streamingdemo.dto;

import jakarta.validation.constraints.NotBlank;

public record PlaybackStartRequest(
        @NotBlank String titleId,
        boolean skipIntro,
        String device
) {
}
