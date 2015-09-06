package com.invado.customer.relationship.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by Nikola on 05/09/2015.
 */
@StaticMetamodel(TransactionType.class)
public class TransactionType_ {
    public static volatile SingularAttribute<TransactionType, Integer> id;
    public static volatile SingularAttribute<TransactionType,String> type;
    public static volatile SingularAttribute<TransactionType,String> description;
    public static volatile SingularAttribute<TransactionType,Boolean> invoiceable;
    public static volatile SingularAttribute<TransactionType,String> invoicingStatuses;
}
