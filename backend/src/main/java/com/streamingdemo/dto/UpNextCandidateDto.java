package com.streamingdemo.dto;

public record UpNextCandidateDto(
        String id,
        String name,
        String posterUrl,
        String previewManifestUrl,
        String reason,
        double score
) {
}
