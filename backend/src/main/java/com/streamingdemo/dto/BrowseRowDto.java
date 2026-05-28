package com.streamingdemo.dto;

import java.util.List;

public record BrowseRowDto(
        String id,
        String label,
        List<TitleSummaryDto> titles
) {
}
