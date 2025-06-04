package com.gjstr.bankService.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gjstr.bankService.dto.RuleQuery;
import com.gjstr.bankService.entity.DynamicRule;
import com.gjstr.bankService.entity.User;
import com.gjstr.bankService.enums.ProductType;
import com.gjstr.bankService.enums.TransactionType;
import com.gjstr.bankService.repository.KnowledgeBaseRepository;
import com.gjstr.bankService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DynamicRuleExecutorService {

    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final ObjectMapper objectMapper;
    private final RuleStatsService ruleStatsService;
    private final UserRepository userRepository;

    public DynamicRuleExecutorService(KnowledgeBaseRepository knowledgeBaseRepository,
                                      ObjectMapper objectMapper,
                                      RuleStatsService ruleStatsService,
                                      UserRepository userRepository) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
        this.objectMapper = objectMapper;
        this.ruleStatsService = ruleStatsService;
        this.userRepository = userRepository;
    }

    public boolean evaluateRule(DynamicRule rule, UUID userId) {
        try {
            System.out.println("Проверка правила для продукта: " + rule.getProductName());
            System.out.println("JSON rule: " + rule.getRule());

            List<RuleQuery> ruleQueries = objectMapper.readValue(rule.getRule(), new TypeReference<>() {});
            for (RuleQuery query : ruleQueries) {
                boolean result = evaluateQuery(query, userId);
                System.out.println("➡ Выполняется запрос: " + query.getQuery() + ", результат: " + result);

                if (!result) {
                    System.out.println(" Правило не выполнено -> продукт не будет показан");
                    return false;
                }
            }

            System.out.println("Все правила выполнены -> продукт будет рекомендован");

            ruleStatsService.increment(rule.getId());
            return true;

        } catch (Exception e) {
            System.err.println(" Ошибка при выполнении правила: " + rule.getRule());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при выполнении правила: " + rule.getRule(), e);
        }
    }

    private boolean evaluateQuery(RuleQuery query, UUID userId) {
        boolean result;

        switch (query.getQuery()) {
            case "USER_OF" -> result = knowledgeBaseRepository.userOf(userId, ProductType.valueOf(query.getArguments().get(0)));

            case "ACTIVE_USER_OF" -> result = knowledgeBaseRepository.activeUserOf(userId, ProductType.valueOf(query.getArguments().get(0)));

            case "TRANSACTION_SUM_COMPARE" -> result = knowledgeBaseRepository.compareSumWithConstant(
                    userId,
                    ProductType.valueOf(query.getArguments().get(0)),
                    TransactionType.valueOf(query.getArguments().get(1)),
                    query.getArguments().get(2),
                    Integer.parseInt(query.getArguments().get(3))
            );

            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> result = knowledgeBaseRepository.compareDepositWithdraw(
                    userId,
                    ProductType.valueOf(query.getArguments().get(0)),
                    query.getArguments().get(1)
            );

            default -> throw new IllegalArgumentException("Неизвестный тип запроса: " + query.getQuery());
        }

        return query.isNegate() ? !result : result;
    }

    public UUID getUserIdByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(User::getId).orElse(null);
    }
}
