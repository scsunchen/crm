/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 *
 * @author bdragan
 */
@StaticMetamodel(BusinessPartnerTerms.class)
public class BusinessPartnerTerms_ {

    public static volatile SingularAttribute<BusinessPartnerTerms, Integer> id;
    public static volatile SingularAttribute<BusinessPartnerTerms, BusinessPartner> businessPartner;
    public static volatile SingularAttribute<BusinessPartnerTerms, LocalDate> dateFrom;
    public static volatile SingularAttribute<BusinessPartnerTerms, LocalDate> endDate;
    public static volatile SingularAttribute<BusinessPartnerTerms, Integer> daysToPay;
    public static volatile SingularAttribute<BusinessPartnerTerms, BigDecimal> rebate;
    public static volatile SingularAttribute<BusinessPartnerTerms, BusinessPartnerTerms.Status> status;
    public static volatile SingularAttribute<BusinessPartnerTerms, Long> version;
    public static volatile ListAttribute<BusinessPartnerTerms, BusinessPartnerTermsItem> items;
}
