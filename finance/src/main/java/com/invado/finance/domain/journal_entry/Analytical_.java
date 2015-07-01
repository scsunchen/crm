package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.OrgUnit;
import com.invado.finance.domain.journal_entry.Analytical.Status;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Analytical.class)
public class Analytical_ { 

    public static volatile SingularAttribute<Analytical, LocalDate> valueDate;
    public static volatile SingularAttribute<Analytical, Integer> journalEntryItemOrdinalNumber;
    public static volatile SingularAttribute<Analytical, Status> status;
    public static volatile SingularAttribute<Analytical, Determination> determination;
    public static volatile SingularAttribute<Analytical, JournalEntryType> journalEntryType;
    public static volatile SingularAttribute<Analytical, OrgUnit> orgUnit;
    public static volatile SingularAttribute<Analytical, String> internalDocument;
    public static volatile SingularAttribute<Analytical, Long> version;
    public static volatile SingularAttribute<Analytical, Long> id;
    public static volatile SingularAttribute<Analytical, BigDecimal> amount;
    public static volatile SingularAttribute<Analytical, ChangeType> changeType;
    public static volatile SingularAttribute<Analytical, String> document;
    public static volatile SingularAttribute<Analytical, LocalDate> recordDate;
    public static volatile SingularAttribute<Analytical, Integer> journalEntryNumber;
    public static volatile SingularAttribute<Analytical, Description> description;
    public static volatile SingularAttribute<Analytical, Account> account;
    public static volatile SingularAttribute<Analytical, LocalDate> creditDebitRelationDate;
    public static volatile SingularAttribute<Analytical, BusinessPartner> partner;
    public static volatile SingularAttribute<Analytical, ApplicationUser> user;

}