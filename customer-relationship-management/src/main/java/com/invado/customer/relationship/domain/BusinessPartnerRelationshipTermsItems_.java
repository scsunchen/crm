package com.invado.customer.relationship.domain;

import com.invado.core.domain.Article;

import javax.persistence.Column;
import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;

/**
 * Created by Nikola on 19/09/2015.
 */
public class BusinessPartnerRelationshipTermsItems_ {

    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, BusinessPartnerRelationshipTerms> businessPartnerRelationshipTerms;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Integer> ordinal;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, ServiceProviderServices> service;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Article> article;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, BigDecimal> totalAmount;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, BigDecimal> totalQuantity;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, BigDecimal> rebate;
}
