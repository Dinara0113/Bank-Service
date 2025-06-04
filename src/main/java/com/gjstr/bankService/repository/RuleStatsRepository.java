package com.gjstr.bankService.repository;

import com.gjstr.bankService.entity.RuleStats;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface RuleStatsRepository {
    void incrementRuleCounter(Long ruleId);
    void deleteByRuleId(Long ruleId);
    List<RuleStats> findAll();
    Optional<RuleStats> findByRuleId(Long ruleId);
}
