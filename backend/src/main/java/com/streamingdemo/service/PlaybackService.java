package com.streamingdemo.service;

import com.streamingdemo.domain.IntroPolicy;
import com.streamingdemo.domain.PlaybackSession;
import com.streamingdemo.domain.Title;
import com.streamingdemo.dto.PlaybackStartRequest;
import com.streamingdemo.dto.PlaybackStartResponse;
import com.streamingdemo.repository.PlaybackSessionRepository;
import com.streamingdemo.repository.TitleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class PlaybackService {

    private final TitleRepository titleRepository;
    private final PlaybackSessionRepository sessionRepository;
    private final MediaUrlService mediaUrlService;
    private final RecommendationService recommendationService;

    public PlaybackService(
            TitleRepository titleRepository,
            PlaybackSessionRepository sessionRepository,
            MediaUrlService mediaUrlService,
            RecommendationService recommendationService) {
        this.titleRepository = titleRepository;
        this.sessionRepository = sessionRepository;
        this.mediaUrlService = mediaUrlService;
        this.recommendationService = recommendationService;
    }

    @Transactional
    public PlaybackStartResponse startPlayback(String userId, PlaybackStartRequest request) {
        Title title = titleRepository.findById(request.titleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Title not found"));

        UUID sessionId = UUID.randomUUID();
        PlaybackSession session = new PlaybackSession();
        session.setId(sessionId);
        session.setUserId(userId);
        session.setTitleId(title.getId());
        session.setStartedAt(Instant.now());
        session.setDevice(request.device());
        sessionRepository.save(session);

        String introUrl = null;
        if (shouldPlayIntro(request.skipIntro(), title)) {
            introUrl = mediaUrlService.resolveIntroUrl(title);
        }

        return new PlaybackStartResponse(
                sessionId,
                title.getId(),
                introUrl,
                mediaUrlService.resolveFeatureUrl(title),
                title.getCreditsStartSec(),
                title.getRuntimeSec(),
                recommendationService.getUpNextCandidates(userId, title.getId(), 5));
    }

    private boolean shouldPlayIntro(boolean skipIntroRequested, Title title) {
        if (skipIntroRequested || title.getIntroPolicy() == IntroPolicy.SKIP) {
            return false;
        }
        return title.getIntroPolicy() == IntroPolicy.ALWAYS
                || title.getIntroPolicy() == IntroPolicy.FIRST_TIME;
    }
}
