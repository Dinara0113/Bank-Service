package com.gjstr.bankService.service;

import com.gjstr.bankService.entity.DynamicRule;
import com.gjstr.bankService.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис CRUD-операций с DynamicRule и очисткой статистики при удалении.
 */
@Service
public class DynamicRuleService {
    private final DynamicRuleRepository repository;
    private DynamicRuleService ruleStatsService;

    public DynamicRuleService(DynamicRuleRepository repository) {
        this.repository = repository;
    }

    public Iterable<DynamicRule> getAllRules() {
        return repository.findAll();
    }

    public Optional<DynamicRule> getRuleById(Long id) {
        return repository.findById(id);
    }

    public DynamicRule createRule(DynamicRule rule) {
        return repository.save(rule);
    }

    public DynamicRule updateRule(Long id, DynamicRule rule) {
        rule.setId(id);
        return repository.save(rule);
    }

    public void deleteRule(Long id) {
        ruleStatsService.deleteRule(id);
    }
}
