/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.invado.finance.domain.journal_entry;

import com.invado.finance.domain.journal_entry.Account.Type;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 *
 * @author Bobic Dragan
 */
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, String> description;
    public static volatile SingularAttribute<Account, Determination> determination;
    public static volatile SingularAttribute<Account, String> number;
    public static volatile SingularAttribute<Account, Type> type;
    public static volatile SingularAttribute<Account, Long> version;

}
