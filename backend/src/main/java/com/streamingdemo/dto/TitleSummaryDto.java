package com.streamingdemo.dto;

import com.streamingdemo.domain.IntroPolicy;
import com.streamingdemo.domain.TitleType;

public record TitleSummaryDto(
        String id,
        TitleType type,
        String name,
        String description,
        int runtimeSec,
        String posterUrl,
        String previewManifestUrl,
        IntroPolicy introPolicy,
        int creditsStartSec
) {
}
