package com.streamingdemo.dto;

public record ProgressResponse(
        double percentComplete,
        boolean showUpNext
) {
}
