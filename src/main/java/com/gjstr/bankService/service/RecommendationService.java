package com.gjstr.bankService.service;

import com.gjstr.bankService.dto.RecommendationDto;
import com.gjstr.bankService.entity.DynamicRule;
import com.gjstr.bankService.repository.DynamicRuleRepository;
import com.gjstr.bankService.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleExecutorService dynamicRuleExecutorService;

    public RecommendationService(List<RecommendationRuleSet> ruleSets,
                                 DynamicRuleRepository dynamicRuleRepository,
                                 DynamicRuleExecutorService dynamicRuleExecutorService) {
        this.ruleSets = ruleSets;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleExecutorService = dynamicRuleExecutorService;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        // Получаем фиксированные рекомендации
        List<RecommendationDto> fixedRecommendations = ruleSets.stream()
                .map(rule -> rule.evaluate(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // Получаем динамические рекомендации (те, чьи правила выполнились)
        List<RecommendationDto> dynamicRecommendations =
                StreamSupport.stream(dynamicRuleRepository.findAll().spliterator(), false)
                        .filter(rule -> dynamicRuleExecutorService.evaluateRule(rule, userId))
                        .map(rule -> new RecommendationDto(
                                UUID.fromString(rule.getProductId()),
                                rule.getProductName(),
                                rule.getProductText()
                        ))
                        .collect(Collectors.toList());

        // 🔹 Объединяем оба списка и возвращаем
        fixedRecommendations.addAll(dynamicRecommendations);
        return fixedRecommendations;
    }
}
