package com.gjstr.bankService.entity;

public class RuleStats {
    private Long ruleId;
    private int count;

    public RuleStats() {}

    public RuleStats(Long ruleId, int count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

