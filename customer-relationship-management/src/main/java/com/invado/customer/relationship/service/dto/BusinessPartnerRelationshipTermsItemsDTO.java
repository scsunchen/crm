package com.invado.customer.relationship.service.dto;

import com.invado.core.domain.Article;
import com.invado.customer.relationship.domain.BusinessPartnerRelationshipTerms;

import java.math.BigDecimal;

/**
 * Created by nikola on 24.07.2015.
 */
public class BusinessPartnerRelationshipTermsItemsDTO {
    private Integer businessPartnerRelationshipTermsId;
    private Integer ordinal;
    private String articleCode;
    private String articleName;
    private BigDecimal totalAmount;
    private BigDecimal totalQuantity;
    private BigDecimal rebate;
    private Long businessPartnerRelationshipTermsVersion;

    public Integer getBusinessPartnerRelationshipTermsId() {
        return businessPartnerRelationshipTermsId;
    }

    public void setBusinessPartnerRelationshipTermsId(Integer businessPartnerRelationshipTermsId) {
        this.businessPartnerRelationshipTermsId = businessPartnerRelationshipTermsId;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public Long getBusinessPartnerRelationshipTermsVersion() {
        return businessPartnerRelationshipTermsVersion;
    }

    public void setBusinessPartnerRelationshipTermsVersion(Long businessPartnerRelationshipTermsVersion) {
        this.businessPartnerRelationshipTermsVersion = businessPartnerRelationshipTermsVersion;
    }
}
