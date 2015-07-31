/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 *
 * @author bdragan
 */
@StaticMetamodel(JournalEntryPK.class)
public class JournalEntryPK_ {
        public static volatile SingularAttribute<JournalEntryPK, Integer> client;
        public static volatile SingularAttribute<JournalEntryPK, Integer> type;
        public static volatile SingularAttribute<JournalEntryPK, Integer> number;

}
