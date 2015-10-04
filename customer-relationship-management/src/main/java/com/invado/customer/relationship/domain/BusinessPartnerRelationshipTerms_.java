package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;
import java.util.List;

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
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, String> remark;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, Long> version;
    public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, BusinessPartnerRelationshipTerms.Status> status;
    //public static volatile SingularAttribute<BusinessPartnerRelationshipTerms, List<BusinessPartnerRelationshipTermsItems>> items;
    public static volatile PluralAttribute<BusinessPartnerRelationshipTerms, List, BusinessPartnerRelationshipTermsItems> items;
}
