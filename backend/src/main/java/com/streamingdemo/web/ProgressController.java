package com.streamingdemo.web;

import com.streamingdemo.config.UserIdFilter;
import com.streamingdemo.dto.ProgressRequest;
import com.streamingdemo.dto.ProgressResponse;
import com.streamingdemo.service.ProgressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PutMapping
    public ProgressResponse update(HttpServletRequest request, @Valid @RequestBody ProgressRequest body) {
        String userId = (String) request.getAttribute(UserIdFilter.USER_ID_ATTR);
        return progressService.updateProgress(userId, body);
    }
}
