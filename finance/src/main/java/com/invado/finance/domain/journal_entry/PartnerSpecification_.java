package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.OrgUnit;
import java.math.BigDecimal;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(PartnerSpecification.class)
public class PartnerSpecification_ { 

    public static volatile SingularAttribute<PartnerSpecification, Long> id;
    public static volatile SingularAttribute<PartnerSpecification, BigDecimal> currentTurnoverCredit;
    public static volatile SingularAttribute<PartnerSpecification, BigDecimal> currentTurnoverDebit;
    public static volatile SingularAttribute<PartnerSpecification, Integer> daysLate;
    public static volatile SingularAttribute<PartnerSpecification, BigDecimal> openingBalanceDebit;
    public static volatile SingularAttribute<PartnerSpecification, Account> account;
    public static volatile SingularAttribute<PartnerSpecification, Determination> determination;
    public static volatile SingularAttribute<PartnerSpecification, BusinessPartner> partner;
    public static volatile SingularAttribute<PartnerSpecification, BigDecimal> openingBalanceCredit;
    public static volatile SingularAttribute<PartnerSpecification, OrgUnit> orgUnit;
    public static volatile SingularAttribute<PartnerSpecification, Long> version;

}