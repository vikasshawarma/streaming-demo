package com.streamingdemo.dto;

import com.streamingdemo.domain.IntroPolicy;
import com.streamingdemo.domain.TitleType;

import java.util.List;

public record TitleDetailDto(
        String id,
        TitleType type,
        String name,
        String description,
        int runtimeSec,
        String posterUrl,
        String previewManifestUrl,
        String featureManifestUrl,
        String introUrl,
        IntroPolicy introPolicy,
        int creditsStartSec,
        List<String> genres,
        String collectionId
) {
}
