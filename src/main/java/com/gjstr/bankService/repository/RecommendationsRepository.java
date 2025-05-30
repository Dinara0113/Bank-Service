package com.gjstr.bankService.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Сумма пополнений (DEPOSIT) по типу продукта
    public int getDepositSumByProductType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(amount), 0)
            FROM transactions
            WHERE user_id = ? AND transaction_type = 'DEPOSIT' AND product_type = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType);
    }

    // Сумма списаний (WITHDRAW) по типу продукта
    public int getWithdrawalSumByProductType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(amount), 0)
            FROM transactions
            WHERE user_id = ? AND transaction_type = 'WITHDRAW' AND product_type = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType);
    }

    // Проверка, есть ли у пользователя транзакции по заданному типу продукта
    public boolean userHasProductOfType(UUID userId, String productType) {
        String sql = """
            SELECT COUNT(*)
            FROM transactions
            WHERE user_id = ? AND product_type = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType);
        return count != null && count > 0;
    }
}
