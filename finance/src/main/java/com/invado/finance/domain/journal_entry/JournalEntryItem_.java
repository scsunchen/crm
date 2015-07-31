package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.OrgUnit;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(JournalEntryItem.class)
public class JournalEntryItem_ { 

    public static volatile SingularAttribute<JournalEntryItem, LocalDate> valueDate;
    public static volatile SingularAttribute<JournalEntryItem, Description> desc;
    public static volatile SingularAttribute<JournalEntryItem, AccountDetermination> determination;
    public static volatile SingularAttribute<JournalEntryItem, JournalEntry> journalEntry;
    public static volatile SingularAttribute<JournalEntryItem, OrgUnit> orgUnit;
    public static volatile SingularAttribute<JournalEntryItem, String> internalDocument;
    public static volatile SingularAttribute<JournalEntryItem, BigDecimal> amount;
    public static volatile SingularAttribute<JournalEntryItem, ChangeType> changeType;
    public static volatile SingularAttribute<JournalEntryItem, String> document;
    public static volatile SingularAttribute<JournalEntryItem, Integer> ordinalNumber;
    public static volatile SingularAttribute<JournalEntryItem, Account> account;
    public static volatile SingularAttribute<JournalEntryItem, LocalDate> creditDebitRelationDate;
    public static volatile SingularAttribute<JournalEntryItem, BusinessPartner> partner;
    public static volatile SingularAttribute<JournalEntryItem, ApplicationUser> user;

}