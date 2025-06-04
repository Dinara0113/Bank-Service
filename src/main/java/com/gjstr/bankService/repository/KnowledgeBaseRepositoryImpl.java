package com.gjstr.bankService.repository;

import com.gjstr.bankService.enums.ProductType;
import com.gjstr.bankService.enums.TransactionType;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class KnowledgeBaseRepositoryImpl implements KnowledgeBaseRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Cache<String, Boolean> userOfCache;

    public KnowledgeBaseRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                                       Cache<String, Boolean> userOfCache) {
        this.jdbcTemplate = jdbcTemplate;
        this.userOfCache = userOfCache;
    }

    @Override
    public boolean userOf(UUID userId, ProductType type) {
        String key = userId + ":" + type;

        // Сначала пробуем достать результат из кеша
        Boolean cached = userOfCache.getIfPresent(key);
        if (cached != null) return cached;

        String sql = """
            SELECT COUNT(*) > 0
            FROM transactions
            WHERE user_id = :userId AND product_type = :type
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("type", type.name());

        boolean result = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
        userOfCache.put(key, result); // Кладём результат в кеш
        return result;
    }



    @Override
    public boolean activeUserOf(UUID userId, ProductType type) {
        String sql = """
        SELECT COUNT(*) > 0
        FROM transactions
        WHERE user_id = :userId
        AND product_type = :type
        AND transaction_type = 'WITHDRAW'
        AND created_at >= DATEADD('DAY', -30, CURRENT_TIMESTAMP)
    """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("type", type.name());

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }


    @Override
    public boolean compareSumWithConstant(UUID userId, ProductType type, TransactionType transactionType, String operator, int value) {
        String sql = """
            SELECT SUM(amount)
            FROM transactions
            WHERE user_id = :userId
            AND product_type = :type
            AND transaction_type = :transactionType
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("type", type.name())
                .addValue("transactionType", transactionType.name());

        Integer sum = jdbcTemplate.queryForObject(sql, params, Integer.class);
        if (sum == null) return false;

        return switch (operator) {
            case ">" -> sum > value;
            case ">=" -> sum >= value;
            case "<" -> sum < value;
            case "<=" -> sum <= value;
            case "=" -> sum == value;
            default -> throw new IllegalArgumentException("Неверный оператор: " + operator);
        };
    }

    @Override
    public boolean compareDepositWithdraw(UUID userId, ProductType type, String operator) {
        String sql = """
            SELECT
                SUM(CASE WHEN transaction_type = 'DEPOSIT' THEN amount ELSE 0 END) AS deposits,
                SUM(CASE WHEN transaction_type = 'WITHDRAW' THEN amount ELSE 0 END) AS withdrawals
            FROM transactions
            WHERE user_id = :userId
            AND product_type = :type
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("type", type.name());

        return Boolean.TRUE.equals(jdbcTemplate.query(sql, params, rs -> {
            if (rs.next()) {
                int deposits = rs.getInt("deposits");
                int withdrawals = rs.getInt("withdrawals");
                return switch (operator) {
                    case ">" -> deposits > withdrawals;
                    case ">=" -> deposits >= withdrawals;
                    case "<" -> deposits < withdrawals;
                    case "<=" -> deposits <= withdrawals;
                    case "=" -> deposits == withdrawals;
                    default -> throw new IllegalArgumentException("Неверный оператор: " + operator);
                };
            }
            return false;
        }));
    }
}


