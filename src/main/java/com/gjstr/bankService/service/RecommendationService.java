package com.gjstr.bankService.service;

import com.gjstr.bankService.dto.RecommendationDto;
import com.gjstr.bankService.entity.DynamicRule;
import com.gjstr.bankService.entity.User;
import com.gjstr.bankService.repository.DynamicRuleRepository;
import com.gjstr.bankService.repository.UserRepository;
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
    private final UserRepository userRepository;


    public RecommendationService(
            List<RecommendationRuleSet> ruleSets,
            DynamicRuleRepository dynamicRuleRepository,
            DynamicRuleExecutorService dynamicRuleExecutorService,
            UserRepository userRepository
    ) {
        this.ruleSets = ruleSets;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleExecutorService = dynamicRuleExecutorService;
        this.userRepository = userRepository;
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

        // Объединяем оба списка и возвращаем
        fixedRecommendations.addAll(dynamicRecommendations);
        return fixedRecommendations;
    }
    public String getRecommendationTextForTelegram(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return "Пользователь с именем " + username + " не найден.";
        }

        UUID userId = userOpt.get().getId();
        List<RecommendationDto> recommendations = getRecommendations(userId);

        if (recommendations.isEmpty()) {
            return "Здравствуйте, " + username + "!\n\nУ вас пока нет новых рекомендаций.";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Здравствуйте, ").append(username).append("!\n\n");
        builder.append("🎯 Новые продукты для вас:\n");

        for (RecommendationDto rec : recommendations) {
            builder.append("• ").append(rec.getText()).append("\n");
        }

        return builder.toString();
    }



}
