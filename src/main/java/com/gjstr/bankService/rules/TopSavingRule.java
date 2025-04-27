package com.gjstr.bankService.rules;

import com.gjstr.bankService.dto.RecommendationDto;
import com.gjstr.bankService.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRule implements RecommendationRuleSet{
    private final RecommendationsRepository repository;

    public TopSavingRule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasDebit = repository.userHasProductOfType(userId, "DEBIT");
        int debitDeposit = repository.getDepositSumByProductType(userId, "DEBIT");
        int savingDeposit = repository.getDepositSumByProductType(userId, "SAVING");
        int debitWithdrawal = repository.getWithdrawalSumByProductType(userId, "DEBIT");

        boolean depositCondition = debitDeposit >= 50_000 || savingDeposit >= 50_000;
        boolean debitPositiveBalance = debitDeposit > debitWithdrawal;

        if (hasDebit && depositCondition && debitPositiveBalance) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                    "Top Saving",
                    "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем! Прозрачность и безопасность гарантированы."
            ));
        }

        return Optional.empty();
    }
}
