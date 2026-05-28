package com.streamingdemo.web;

import com.streamingdemo.config.UserIdFilter;
import com.streamingdemo.dto.RecommendationDto;
import com.streamingdemo.dto.TitleDetailDto;
import com.streamingdemo.service.CatalogService;
import com.streamingdemo.service.RecommendationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/titles")
public class TitleController {

    private final CatalogService catalogService;
    private final RecommendationService recommendationService;

    public TitleController(CatalogService catalogService, RecommendationService recommendationService) {
        this.catalogService = catalogService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{id}")
    public TitleDetailDto getTitle(@PathVariable String id) {
        return catalogService.getTitle(id);
    }

    @GetMapping("/{id}/up-next")
    public List<RecommendationDto> upNext(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestParam(defaultValue = "5") int limit) {
        String userId = (String) request.getAttribute(UserIdFilter.USER_ID_ATTR);
        return recommendationService.getUpNext(userId, id, limit);
    }
}
