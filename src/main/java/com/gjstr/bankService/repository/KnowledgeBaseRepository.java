package com.gjstr.bankService.repository;

import com.gjstr.bankService.enums.ProductType;
import com.gjstr.bankService.enums.TransactionType;

import java.util.UUID;

public interface KnowledgeBaseRepository {
    boolean userOf(UUID userId, ProductType type);
    boolean activeUserOf(UUID userId, ProductType type);
    boolean compareSumWithConstant(UUID userId, ProductType type, TransactionType transactionType, String operator, int value);
    boolean compareDepositWithdraw(UUID userId, ProductType type, String operator);
}
