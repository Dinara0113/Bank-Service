package com.gjstr.bankService.repository;

import com.gjstr.bankService.entity.RuleStats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RuleStatsRepositoryImpl implements RuleStatsRepository{
    private final JdbcTemplate jdbcTemplate;

    public RuleStatsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void incrementRuleCounter(Long ruleId) {
        String updateSql = "UPDATE rule_stats SET count = count + 1 WHERE rule_id = ?";
        int updated = jdbcTemplate.update(updateSql, ruleId);
        if (updated == 0) {
            String insertSql = "INSERT INTO rule_stats (rule_id, count) VALUES (?, 1)";
            jdbcTemplate.update(insertSql, ruleId);
        }
    }

    @Override
    public void deleteByRuleId(Long ruleId) {
        jdbcTemplate.update("DELETE FROM rule_stats WHERE rule_id = ?", ruleId);
    }

    @Override
    public List<RuleStats> findAll() {
        return jdbcTemplate.query(
                "SELECT rule_id, count FROM rule_stats",
                (rs, rowNum) -> new RuleStats(rs.getLong("rule_id"), rs.getInt("count"))
        );
    }

    @Override
    public Optional<RuleStats> findByRuleId(Long ruleId) {
        List<RuleStats> list = jdbcTemplate.query(
                "SELECT rule_id, count FROM rule_stats WHERE rule_id = ?",
                (rs, rowNum) -> new RuleStats(rs.getLong("rule_id"), rs.getInt("count")),
                ruleId
        );
        return list.stream().findFirst();
    }
}
