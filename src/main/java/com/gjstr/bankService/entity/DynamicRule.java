package com.gjstr.bankService.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("DYNAMIC_RULES")
public class DynamicRule {

    @Id
    @Column("ID")
    private Long id;

    @Column("PRODUCT_ID")
    private String productId;

    @Column("PRODUCT_NAME")
    private String productName;

    @Column("PRODUCT_TEXT")
    private String productText;

    @Column("RULE")
    private String rule;

    // --- Конструкторы ---

    public DynamicRule() {
    }

    public DynamicRule(Long id, String productId, String productName, String productText, String rule) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
        this.rule = rule;
    }

    // --- Геттеры и сеттеры ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    // --- toString() ---

    @Override
    public String toString() {
        return "DynamicRule{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productText='" + productText + '\'' +
                ", rule='" + rule + '\'' +
                '}';
    }
}
