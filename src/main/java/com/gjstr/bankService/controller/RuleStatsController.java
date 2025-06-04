package com.gjstr.bankService.controller;

import com.gjstr.bankService.entity.RuleStats;
import com.gjstr.bankService.service.RuleStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleStatsController {

    private final RuleStatsService ruleStatsService;

    public RuleStatsController(RuleStatsService ruleStatsService) {
        this.ruleStatsService = ruleStatsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<RuleStats> stats = ruleStatsService.getAllWithDefaults();

        List<Map<String, Object>> responseStats = new ArrayList<>();
        for (RuleStats stat : stats) {
            Map<String, Object> ruleEntry = Map.of(
                    "rule_id", stat.getRuleId(),
                    "count", stat.getCount()
            );
            responseStats.add(ruleEntry);

        }


        Map<String, Object> response = new HashMap<>();
        response.put("stats", responseStats);
        return ResponseEntity.ok(response);
    }
}
