package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

/**
 * Created by Nikola on 18/10/2015.
 */
@StaticMetamodel(BusinessPartnerContactDetails.class)
public class BusinessPartnerContactDetails_ {
    public static volatile SingularAttribute<BusinessPartnerContactDetails, Integer> id;
    public static volatile SingularAttribute<BusinessPartnerContactDetails, ContactPerson> contactPerson;
    public static volatile SingularAttribute<BusinessPartnerContactDetails, Address> Address;
    public static volatile SingularAttribute<BusinessPartnerContactDetails, LocalDate> dateFrom;
    public static volatile SingularAttribute<BusinessPartnerContactDetails, LocalDate> dateTo;
    public static volatile SingularAttribute<BusinessPartnerContactDetails, BusinessPartner> merchant;
    public static volatile SingularAttribute<BusinessPartnerContactDetails, BusinessPartner> pointOfSale;
    public static volatile SingularAttribute<BusinessPartnerContactDetails, Long> version;

}

