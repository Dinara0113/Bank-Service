package com.gjstr.bankService.service;

import com.gjstr.bankService.entity.DynamicRule;
import com.gjstr.bankService.entity.RuleStats;
import com.gjstr.bankService.repository.DynamicRuleRepository;
import com.gjstr.bankService.repository.RuleStatsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RuleStatsService {

    private final RuleStatsRepository statsRepository;
    private final DynamicRuleRepository ruleRepository;

    public RuleStatsService(RuleStatsRepository statsRepository, DynamicRuleRepository ruleRepository) {
        this.statsRepository = statsRepository;
        this.ruleRepository = ruleRepository;
    }

    public void increment(Long ruleId) {
        statsRepository.incrementRuleCounter(ruleId);
    }

    public void delete(Long ruleId) {
        statsRepository.deleteByRuleId(ruleId);
    }

    public List<RuleStats> getAllWithDefaults() {
        List<RuleStats> actualStats = statsRepository.findAll();

        // Берём ID всех правил
        List<Long> allRuleIds = StreamSupport.stream(ruleRepository.findAll().spliterator(), false)
                .map(DynamicRule::getId)
                .toList();

        // Преобразуем в финальный список: если статистики нет — создаём с count = 0
        return allRuleIds.stream()
                .map(id -> actualStats.stream()
                        .filter(stat -> stat.getRuleId().equals(id))
                        .findFirst()
                        .orElse(new RuleStats(id, 0)))
                .collect(Collectors.toList());
    }
}
