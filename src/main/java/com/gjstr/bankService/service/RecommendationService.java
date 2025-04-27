package com.gjstr.bankService.service;

import com.gjstr.bankService.dto.RecommendationDto;
import com.gjstr.bankService.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    // Внедряем все реализации RecommendationRuleSet
    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    // Получаем рекомендации по userId
    public List<RecommendationDto> getRecommendations(UUID userId) {
        return ruleSets.stream()
                .map(rule -> rule.evaluate(userId)) // Каждое правило возвращает Optional<RecommendationDto>
                .filter(Optional::isPresent)        // Фильтруем только успешные рекомендации
                .map(Optional::get)                 // Достаём RecommendationDto
                .collect(Collectors.toList());      // Собираем в список
    }
}
