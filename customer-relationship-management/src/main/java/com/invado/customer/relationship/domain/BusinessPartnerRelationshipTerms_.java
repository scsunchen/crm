package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Created by NikolaB on 6/14/2015.
 */
@StaticMetamodel(BusinessPartnerRelationshipTerms.class)
public class BusinessPartnerRelationshipTerms_ {


    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Integer> id;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, BusinessPartner> businessPartner;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Date> dateFrom;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Date> endDate;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Integer> daysToPay;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Integer> rebate;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, String> rebremark;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Long> version;

}
