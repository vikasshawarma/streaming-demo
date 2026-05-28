package com.streamingdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Cors cors,
        Media media,
        Fallback fallback
) {
    public record Cors(List<String> allowedOrigins) {
    }

    public record Media(String baseUrl, String signingSecret, long urlTtlSeconds) {
    }

    public record Fallback(String featureManifest, String previewManifest, String introUrl) {
    }
}
