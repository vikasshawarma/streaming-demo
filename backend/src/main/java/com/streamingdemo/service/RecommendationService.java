package com.streamingdemo.service;

import com.streamingdemo.domain.Title;
import com.streamingdemo.domain.WatchProgress;
import com.streamingdemo.dto.RecommendationDto;
import com.streamingdemo.dto.UpNextCandidateDto;
import com.streamingdemo.repository.TitleRepository;
import com.streamingdemo.repository.WatchProgressRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final TitleRepository titleRepository;
    private final WatchProgressRepository watchProgressRepository;
    private final MediaUrlService mediaUrlService;

    public RecommendationService(
            TitleRepository titleRepository,
            WatchProgressRepository watchProgressRepository,
            MediaUrlService mediaUrlService) {
        this.titleRepository = titleRepository;
        this.watchProgressRepository = watchProgressRepository;
        this.mediaUrlService = mediaUrlService;
    }

    public List<RecommendationDto> getUpNext(String userId, String titleId, int limit) {
        return scoreCandidates(userId, titleId).stream()
                .limit(limit)
                .map(c -> new RecommendationDto(
                        c.id(),
                        c.name(),
                        c.posterUrl(),
                        c.previewManifestUrl(),
                        c.reason(),
                        c.score()))
                .toList();
    }

    public List<UpNextCandidateDto> getUpNextCandidates(String userId, String titleId, int limit) {
        return scoreCandidates(userId, titleId).stream().limit(limit).toList();
    }

    private List<UpNextCandidateDto> scoreCandidates(String userId, String titleId) {
        Title current = titleRepository.findById(titleId).orElseThrow();
        Set<String> watched = watchProgressRepository.findByUserId(userId).stream()
                .filter(WatchProgress::isCompleted)
                .map(WatchProgress::getTitleId)
                .collect(Collectors.toCollection(HashSet::new));
        watched.add(titleId);

        return titleRepository.findAll().stream()
                .filter(t -> !watched.contains(t.getId()))
                .map(t -> scoreTitle(current, t))
                .sorted(Comparator.comparingDouble(UpNextCandidateDto::score).reversed())
                .toList();
    }

    private UpNextCandidateDto scoreTitle(Title current, Title candidate) {
        double score = candidate.getPopularity();
        String reason = "Popular on Streaming Demo";

        Set<String> currentGenres = new HashSet<>(current.getGenres());
        long genreOverlap = candidate.getGenres().stream().filter(currentGenres::contains).count();
        if (genreOverlap > 0) {
            score += 50 * genreOverlap;
            reason = "Because you watched " + current.getName();
        }

        if (current.getCollectionId() != null
                && current.getCollectionId().equals(candidate.getCollectionId())) {
            score += 80;
            reason = "More from " + formatCollection(current.getCollectionId());
        }

        return new UpNextCandidateDto(
                candidate.getId(),
                candidate.getName(),
                mediaUrlService.resolvePosterUrl(candidate),
                mediaUrlService.resolvePreviewUrl(candidate),
                reason,
                score);
    }

    private String formatCollection(String collectionId) {
        return collectionId.replace('-', ' ');
    }
}
