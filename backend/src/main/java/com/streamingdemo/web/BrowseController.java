package com.streamingdemo.web;

import com.streamingdemo.dto.BrowseResponseDto;
import com.streamingdemo.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/browse")
public class BrowseController {

    private final CatalogService catalogService;

    public BrowseController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/rows")
    public BrowseResponseDto rows() {
        return catalogService.getBrowseRows();
    }
}
