package com.streamingdemo.service;

import com.streamingdemo.config.AppProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Component
public class MediaUrlSigner {

    private final AppProperties appProperties;

    public MediaUrlSigner(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String sign(String rawUrl) {
        if (rawUrl.startsWith("https://devstreaming-cdn.apple.com")) {
            return rawUrl;
        }
        long exp = Instant.now().getEpochSecond() + appProperties.media().urlTtlSeconds();
        String pathToSign = rawUrl + (rawUrl.contains("?") ? "&" : "?") + "exp=" + exp;
        String sig = hmac(pathToSign);
        return pathToSign + "&sig=" + URLEncoder.encode(sig, StandardCharsets.UTF_8);
    }

    public boolean verify(String url, long exp, String sig) {
        if (Instant.now().getEpochSecond() > exp) {
            return false;
        }
        String base = url.substring(0, url.indexOf("?exp="));
        String pathToSign = base + "?exp=" + exp;
        if (url.contains("&sig=")) {
            pathToSign = url.substring(0, url.indexOf("&sig="));
        }
        return hmac(pathToSign).equals(sig);
    }

    private String hmac(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    appProperties.media().signingSecret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Signing failed", e);
        }
    }

    @SuppressWarnings("unused")
    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
