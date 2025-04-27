package com.gjstr.bankService.rules;


import com.gjstr.bankService.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> evaluate(UUID userId);
}
