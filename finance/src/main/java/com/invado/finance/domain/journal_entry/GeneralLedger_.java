package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.OrgUnit;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(GeneralLedger.class)
public class GeneralLedger_ { 

    public static volatile SingularAttribute<GeneralLedger, LocalDate> valueDate;
    public static volatile SingularAttribute<GeneralLedger, Integer> journalEntryItemOrdinalNumber;
    public static volatile SingularAttribute<GeneralLedger, AccountDetermination> determination;
    public static volatile SingularAttribute<GeneralLedger, JournalEntryType> journalEntryType;
    public static volatile SingularAttribute<GeneralLedger, OrgUnit> orgUnit;
    public static volatile SingularAttribute<GeneralLedger, String> internalDocument;
    public static volatile SingularAttribute<GeneralLedger, Long> version;
    public static volatile SingularAttribute<GeneralLedger, BigDecimal> amount;
    public static volatile SingularAttribute<GeneralLedger, Long> id;
    public static volatile SingularAttribute<GeneralLedger, ChangeType> changeType;
    public static volatile SingularAttribute<GeneralLedger, String> document;
    public static volatile SingularAttribute<GeneralLedger, LocalDate> recordDate;
    public static volatile SingularAttribute<GeneralLedger, Description> description;
    public static volatile SingularAttribute<GeneralLedger, Integer> journalEntryNumber;
    public static volatile SingularAttribute<GeneralLedger, LocalDate> creditDebitRelationDate;
    public static volatile SingularAttribute<GeneralLedger, Account> account;
    public static volatile SingularAttribute<GeneralLedger, ApplicationUser> user;

}