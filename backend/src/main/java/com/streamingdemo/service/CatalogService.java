package com.streamingdemo.service;

import com.streamingdemo.domain.Title;
import com.streamingdemo.dto.BrowseResponseDto;
import com.streamingdemo.dto.BrowseRowDto;
import com.streamingdemo.dto.TitleDetailDto;
import com.streamingdemo.dto.TitleSummaryDto;
import com.streamingdemo.repository.TitleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CatalogService {

    private static final Map<String, String> ROW_LABELS = Map.of(
            "trending", "Trending Now",
            "action", "Action & Adventure",
            "family", "Family Favorites"
    );

    private final TitleRepository titleRepository;
    private final MediaUrlService mediaUrlService;

    public CatalogService(TitleRepository titleRepository, MediaUrlService mediaUrlService) {
        this.titleRepository = titleRepository;
        this.mediaUrlService = mediaUrlService;
    }

    public BrowseResponseDto getBrowseRows() {
        List<Title> all = titleRepository.findAllByOrderByPopularityDesc();
        if (all.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catalog not seeded");
        }

        TitleSummaryDto hero = toSummary(all.getFirst());

        Map<String, List<Title>> byRow = new LinkedHashMap<>();
        for (Title title : all) {
            byRow.computeIfAbsent(title.getBrowseRowId(), k -> new java.util.ArrayList<>()).add(title);
        }

        List<BrowseRowDto> rows = byRow.entrySet().stream()
                .map(e -> new BrowseRowDto(
                        e.getKey(),
                        ROW_LABELS.getOrDefault(e.getKey(), e.getKey()),
                        e.getValue().stream().map(this::toSummary).toList()))
                .toList();

        return new BrowseResponseDto(hero, rows);
    }

    public TitleDetailDto getTitle(String id) {
        Title title = titleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Title not found"));
        return toDetail(title);
    }

    public TitleSummaryDto toSummary(Title title) {
        return new TitleSummaryDto(
                title.getId(),
                title.getType(),
                title.getName(),
                title.getDescription(),
                title.getRuntimeSec(),
                mediaUrlService.resolvePosterUrl(title),
                mediaUrlService.resolvePreviewUrl(title),
                title.getIntroPolicy(),
                title.getCreditsStartSec());
    }

    public TitleDetailDto toDetail(Title title) {
        return new TitleDetailDto(
                title.getId(),
                title.getType(),
                title.getName(),
                title.getDescription(),
                title.getRuntimeSec(),
                mediaUrlService.resolvePosterUrl(title),
                mediaUrlService.resolvePreviewUrl(title),
                mediaUrlService.resolveFeatureUrl(title),
                mediaUrlService.resolveIntroUrl(title),
                title.getIntroPolicy(),
                title.getCreditsStartSec(),
                title.getGenres(),
                title.getCollectionId());
    }
}
