package com.gjstr.bankService.rules;

import com.gjstr.bankService.dto.RecommendationDto;
import com.gjstr.bankService.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRule implements RecommendationRuleSet{
    private final RecommendationsRepository repository;

    public SimpleCreditRule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasCredit = repository.userHasProductOfType(userId, "CREDIT");
        int debitDeposit = repository.getDepositSumByProductType(userId, "DEBIT");
        int debitWithdrawal = repository.getWithdrawalSumByProductType(userId, "DEBIT");

        boolean debitPositiveBalance = debitDeposit > debitWithdrawal;
        boolean debitSpentEnough = debitWithdrawal > 100_000;

        if (!hasCredit && debitPositiveBalance && debitSpentEnough) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"),
                    "Простой кредит",
                    "Откройте мир выгодных кредитов с нами! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту. Быстрое рассмотрение заявки и удобное оформление через мобильное приложение!"
            ));
        }

        return Optional.empty();
    }
}
