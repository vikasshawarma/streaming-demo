package com.streamingdemo.dto;

import java.util.List;

public record BrowseResponseDto(
        TitleSummaryDto hero,
        List<BrowseRowDto> rows
) {
}
