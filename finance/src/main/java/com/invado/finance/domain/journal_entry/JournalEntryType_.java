package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.Client;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(JournalEntryType.class)
public class JournalEntryType_ { 

    public static volatile SingularAttribute<JournalEntryType, Integer> id;
    public static volatile SingularAttribute<JournalEntryType, Client> client;
    public static volatile SingularAttribute<JournalEntryType, String> name;
    public static volatile SingularAttribute<JournalEntryType, Integer> number;
    public static volatile SingularAttribute<JournalEntryType, Long> version;

}