package com.gjstr.bankService.rules;

import com.gjstr.bankService.dto.RecommendationDto;
import com.gjstr.bankService.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500Rule implements RecommendationRuleSet{
    private final RecommendationsRepository repository;

    public Invest500Rule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        // Здесь будут проверки по условиям правила
        return Optional.empty();
    }
}
