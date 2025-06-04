package com.gjstr.bankService.repository;

import com.gjstr.bankService.entity.DynamicRule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicRuleRepository extends CrudRepository<DynamicRule, Long> {
}
