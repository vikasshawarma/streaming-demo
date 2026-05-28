package com.streamingdemo.web;

import com.streamingdemo.config.UserIdFilter;
import com.streamingdemo.dto.PlaybackStartRequest;
import com.streamingdemo.dto.PlaybackStartResponse;
import com.streamingdemo.service.PlaybackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/playback")
public class PlaybackController {

    private final PlaybackService playbackService;

    public PlaybackController(PlaybackService playbackService) {
        this.playbackService = playbackService;
    }

    @PostMapping("/start")
    public PlaybackStartResponse start(
            HttpServletRequest request,
            @Valid @RequestBody PlaybackStartRequest body) {
        String userId = (String) request.getAttribute(UserIdFilter.USER_ID_ATTR);
        return playbackService.startPlayback(userId, body);
    }
}
