package com.streamingdemo.dto;

public record RecommendationDto(
        String id,
        String name,
        String posterUrl,
        String previewManifestUrl,
        String reason,
        double score
) {
}
