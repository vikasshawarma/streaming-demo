package com.streamingdemo.dto;

import java.util.List;
import java.util.UUID;

public record PlaybackStartResponse(
        UUID sessionId,
        String titleId,
        String introUrl,
        String featureManifestUrl,
        int creditsStartSec,
        int runtimeSec,
        List<UpNextCandidateDto> upNext
) {
}
