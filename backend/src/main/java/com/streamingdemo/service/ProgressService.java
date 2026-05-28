package com.streamingdemo.service;

import com.streamingdemo.domain.WatchProgress;
import com.streamingdemo.domain.WatchProgressId;
import com.streamingdemo.dto.ProgressRequest;
import com.streamingdemo.dto.ProgressResponse;
import com.streamingdemo.repository.TitleRepository;
import com.streamingdemo.repository.WatchProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ProgressService {

    private static final double UP_NEXT_THRESHOLD = 0.90;

    private final WatchProgressRepository watchProgressRepository;
    private final TitleRepository titleRepository;

    public ProgressService(WatchProgressRepository watchProgressRepository, TitleRepository titleRepository) {
        this.watchProgressRepository = watchProgressRepository;
        this.titleRepository = titleRepository;
    }

    @Transactional
    public ProgressResponse updateProgress(String userId, ProgressRequest request) {
        WatchProgress progress = watchProgressRepository
                .findById(new WatchProgressId(userId, request.titleId()))
                .orElseGet(WatchProgress::new);

        progress.setUserId(userId);
        progress.setTitleId(request.titleId());
        progress.setPositionSec(request.positionSec());
        progress.setDurationSec(request.durationSec());
        progress.setCompleted(request.completed());
        progress.setUpdatedAt(Instant.now());
        watchProgressRepository.save(progress);

        double percent = request.durationSec() > 0
                ? request.positionSec() / request.durationSec()
                : 0;

        int creditsStart = titleRepository.findById(request.titleId())
                .map(t -> t.getCreditsStartSec())
                .orElse((int) (request.durationSec() * UP_NEXT_THRESHOLD));

        boolean showUpNext = request.completed()
                || request.positionSec() >= creditsStart
                || percent >= UP_NEXT_THRESHOLD;

        return new ProgressResponse(percent, showUpNext);
    }
}
