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

    public int getDepositSumByProductType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND t."TYPE" = 'DEPOSIT' AND p.type = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType);
    }

    public int getWithdrawalSumByProductType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND t."TYPE" = 'WITHDRAWAL' AND p.type = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType);
    }

    public boolean userHasProductOfType(UUID userId, String productType) {
        String sql = """
            SELECT COUNT(*)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType);
        return count != null && count > 0;
    }

}
