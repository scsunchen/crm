package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by Nikola on 23/12/2015.
 */
@StaticMetamodel(BusinessPartnerAccount.class)
public class BusinessPartnerAccount_ {

    public static volatile SingularAttribute<BusinessPartnerAccount, Integer> id;
    public static volatile SingularAttribute<BusinessPartnerAccount, String> account;
    public static volatile SingularAttribute<BusinessPartnerAccount, BusinessPartner> accountOwner;
    public static volatile SingularAttribute<BusinessPartnerAccount, BusinessPartner> bank;
    public static volatile SingularAttribute<BusinessPartnerAccount, Currency> currency;
    public static volatile SingularAttribute<BusinessPartnerAccount, Long> version;


}
