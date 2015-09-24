/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.domain;

import com.invado.core.domain.Article;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "CRM_BUSINESS_TERMS_ITEMS")
@NamedQuery(name="test", query = "SELECT x FROM BusinessPartnerTermsItem x WHERE x.terms.id = :terms AND x.ordinal = :o")
@IdClass(BusinessPartnerTermsItemPK.class)
public class BusinessPartnerTermsItem implements Serializable {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "TERMS_ID")
    @NotNull(message = "{BusinessTermsItem.Terms.NotNull}")
    private BusinessPartnerTerms terms;
    @Id    
    @NotNull(message = "{BusinessTermsItem.Ordinal.NotNull}")
    private Integer ordinal;
    @ManyToOne
    @JoinColumn(name = "ARTICLE_CODE")
    @NotNull(message = "{BusinessTermsItem.Article.NotNull}")
    private Article article;
    @Column(name = "TOTAL_AMOUNT")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal totalAmount;
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Column(name = "TOTAL_QUANTITY")
    private BigDecimal totalQuantity;
    @NumberFormat(style = NumberFormat.Style.PERCENT)
    @NotNull(message = "{BusinessTermsItem.Rebate.NotNull}")
    private BigDecimal rebate;

    public BusinessPartnerTermsItem() {
    }

    public BusinessPartnerTermsItem(BusinessPartnerTerms terms, Integer ordinal) {
        this.terms = terms;
        this.ordinal = ordinal;
    }

    public BusinessPartnerTerms getTerms() {
        return terms;
    }

    public void setTerms(BusinessPartnerTerms terms) {
        this.terms = terms;
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
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.terms);
        hash = 17 * hash + Objects.hashCode(this.ordinal);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BusinessPartnerTermsItem other = (BusinessPartnerTermsItem) obj;
        if (!Objects.equals(this.terms, other.terms)) {
            return false;
        }
        if (!Objects.equals(this.ordinal, other.ordinal)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BusinessPartnerTermsItem{" + "terms=" + terms + ", ordinal=" + ordinal + '}';
    }
    
}
