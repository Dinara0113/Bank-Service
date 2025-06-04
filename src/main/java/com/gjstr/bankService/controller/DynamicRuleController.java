package com.gjstr.bankService.controller;

import com.gjstr.bankService.entity.DynamicRule;
import com.gjstr.bankService.service.DynamicRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dynamic-rules")
public class DynamicRuleController {
    private final DynamicRuleService service;

    public DynamicRuleController(DynamicRuleService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<DynamicRule> getAllRules() {
        return service.getAllRules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DynamicRule> getRuleById(@PathVariable Long id) {
        return service.getRuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DynamicRule createRule(@RequestBody DynamicRule rule) {
        return service.createRule(rule);
    }

    @PutMapping("/{id}")
    public DynamicRule updateRule(@PathVariable Long id, @RequestBody DynamicRule rule) {
        return service.updateRule(id, rule);
    }

    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable Long id) {
        service.deleteRule(id);
    }
}
