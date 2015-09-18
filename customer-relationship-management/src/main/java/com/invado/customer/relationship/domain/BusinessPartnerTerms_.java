package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;
import java.time.LocalDate;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.List;

/**
 * Created by NikolaB on 6/14/2015.
 */
@StaticMetamodel(BusinessPartnerTerms.class)
public class BusinessPartnerTerms_ {


    public static volatile SingularAttribute<BusinessPartnerTerms, Integer> id;
    public static volatile SingularAttribute<BusinessPartnerTerms, BusinessPartner> businessPartner;
    public static volatile SingularAttribute<BusinessPartnerTerms, LocalDate> dateFrom;
    public static volatile SingularAttribute<BusinessPartnerTerms, LocalDate> endDate;
    public static volatile SingularAttribute<BusinessPartnerTerms, Integer> daysToPay;
    public static volatile SingularAttribute<BusinessPartnerTerms, BusinessPartnerTerms.Status> status;
    public static volatile SingularAttribute<BusinessPartnerTerms, Integer> rebate;
    public static volatile SingularAttribute<BusinessPartnerTerms, List> items;
    public static volatile SingularAttribute<BusinessPartnerTerms, Long> version;

}
