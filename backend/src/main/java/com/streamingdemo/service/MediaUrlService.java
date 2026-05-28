package com.streamingdemo.service;

import com.streamingdemo.config.AppProperties;
import com.streamingdemo.domain.Title;
import org.springframework.stereotype.Service;

@Service
public class MediaUrlService {

    private final AppProperties appProperties;
    private final MediaUrlSigner signer;

    public MediaUrlService(AppProperties appProperties, MediaUrlSigner signer) {
        this.appProperties = appProperties;
        this.signer = signer;
    }

    public String resolvePosterUrl(Title title) {
        return signer.sign(appProperties.media().baseUrl() + title.getPosterPath());
    }

    public String resolvePreviewUrl(Title title) {
        String path = title.getPreviewManifestPath();
        if (path.startsWith("http")) {
            return signer.sign(path);
        }
        return signer.sign(appProperties.media().baseUrl() + path);
    }

    public String resolveFeatureUrl(Title title) {
        String path = title.getFeatureManifestPath();
        if (path.startsWith("http")) {
            return signer.sign(path);
        }
        return signer.sign(appProperties.media().baseUrl() + path);
    }

    public String resolveIntroUrl(Title title) {
        String path = title.getIntroPath();
        if (path.startsWith("http")) {
            return signer.sign(path);
        }
        return signer.sign(appProperties.media().baseUrl() + path);
    }

    public String fallbackFeatureUrl() {
        return signer.sign(appProperties.fallback().featureManifest());
    }

    public String fallbackPreviewUrl() {
        return signer.sign(appProperties.fallback().previewManifest());
    }

    public String fallbackIntroUrl() {
        return signer.sign(appProperties.fallback().introUrl());
    }
}
