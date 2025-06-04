package com.gjstr.bankService.controller;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final Cache<String, Boolean> userOfCache;
    private final BuildProperties buildProperties;

    public ManagementController(Cache<String, Boolean> userOfCache, BuildProperties buildProperties) {
        this.userOfCache = userOfCache;
        this.buildProperties = buildProperties;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        userOfCache.invalidateAll();
        System.out.println("✅ Кеш успешно очищен");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/build-info") //
    public Map<String, String> getBuildInfo() {
        return Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        );
    }

}
