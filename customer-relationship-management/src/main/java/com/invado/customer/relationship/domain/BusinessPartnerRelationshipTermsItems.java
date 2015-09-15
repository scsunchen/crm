package com.invado.customer.relationship.domain;

import com.invado.core.domain.Article;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by nikola on 11.07.2015.
 */
@Entity
@Table(name = "CRM_BUSINESS_TERMS_ITEMS", schema = "devel")
public class BusinessPartnerRelationshipTermsItems implements Serializable, Comparable<BusinessPartnerRelationshipTermsItems>{

    @Id
    @NotNull(message = "{BusinessTermsItem.BusinessTerms.NotNull}")
    @ManyToOne
    @JoinColumn(name = "terms_id", referencedColumnName = "id")
    private BusinessPartnerRelationshipTerms businessPartnerRelationshipTerms;
    @Id
    @NotNull(message = "{BusinessTermsItem.Ordinal.NotNull}")
    @DecimalMin(value = "1", message = "{InvoiceItem.Ordinal.Min}")
    @Column(name = "ordinal")
    private Integer ordinal;
    @ManyToOne
    @JoinColumn(name = "article_code", referencedColumnName = "code")
    @NotNull(message = "{BusinessTermsItem.Article.NotNull}")
    private Article article;
    /**Ukupna vrednost prodatih artikala na jednom dokumentu. ukoliko iznos prelazi zadati totalAmount dobija se dodatni popust*/
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    /**Ukupna količina prodatih artikala na jednom dokumentu. ukoliko količina prelazi zadati totalQuantity dobija se dodatni popust*/
    @Column(name = "total_quantity")
    private BigDecimal totalQuantity;
    @Column(name = "rebate")
    private BigDecimal rebate;


    public BusinessPartnerRelationshipTerms getBusinessPartnerRelationshipTerms() {
        return businessPartnerRelationshipTerms;
    }

    public void setBusinessPartnerRelationshipTerms(BusinessPartnerRelationshipTerms businessPartnerRelationshipTerms) {
        this.businessPartnerRelationshipTerms = businessPartnerRelationshipTerms;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
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


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BusinessPartnerRelationshipTermsItems other = (BusinessPartnerRelationshipTermsItems) obj;
        if (this.businessPartnerRelationshipTerms != other.businessPartnerRelationshipTerms && (this.businessPartnerRelationshipTerms == null
                || !this.businessPartnerRelationshipTerms.equals(other.businessPartnerRelationshipTerms))) {
            return false;
        }
        return !(this.ordinal != other.ordinal && (this.ordinal == null
                || !this.ordinal.equals(other.ordinal)));
    }

    @Override
    public int compareTo(BusinessPartnerRelationshipTermsItems o) {
        if(o.businessPartnerRelationshipTerms.equals(businessPartnerRelationshipTerms)){
            return ordinal.compareTo(o.ordinal);
        } else {
            return businessPartnerRelationshipTerms.getId().compareTo(o.businessPartnerRelationshipTerms.getId());
        }
    }
}
