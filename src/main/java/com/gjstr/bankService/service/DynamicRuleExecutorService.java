package com.gjstr.bankService.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gjstr.bankService.dto.RuleQuery;
import com.gjstr.bankService.entity.DynamicRule;
import com.gjstr.bankService.enums.ProductType;
import com.gjstr.bankService.enums.TransactionType;
import com.gjstr.bankService.repository.KnowledgeBaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleExecutorService {

    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final ObjectMapper objectMapper;

    public DynamicRuleExecutorService(KnowledgeBaseRepository knowledgeBaseRepository, ObjectMapper objectMapper) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
        this.objectMapper = objectMapper;
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

        return query.isNegate() ? !result : result; // применяем negate
    }
}
