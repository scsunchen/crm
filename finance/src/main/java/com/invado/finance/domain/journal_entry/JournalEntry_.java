package com.invado.finance.domain.journal_entry;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(JournalEntry.class)
public class JournalEntry_ { 

    public static volatile ListAttribute<JournalEntry, JournalEntryItem> items;
    public static volatile SingularAttribute<JournalEntry, LocalDate> recordDate;
    public static volatile SingularAttribute<JournalEntry, BigDecimal> balanceCredit;
    public static volatile SingularAttribute<JournalEntry, Integer> number;
    public static volatile SingularAttribute<JournalEntry, JournalEntryType> type;
    public static volatile SingularAttribute<JournalEntry, Boolean> posted;
    public static volatile SingularAttribute<JournalEntry, BigDecimal> balanceDebit;
    public static volatile SingularAttribute<JournalEntry, Long> version;

}