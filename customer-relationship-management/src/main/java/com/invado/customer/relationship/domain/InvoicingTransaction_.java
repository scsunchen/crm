package com.invado.customer.relationship.domain;

import com.invado.core.domain.Client;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

/**
 * Created by Nikola on 13/12/2015.
 */
@StaticMetamodel(TransactionType.class)
public class InvoicingTransaction_ {

    public static volatile SingularAttribute<TransactionType, Integer> id;
    public static volatile SingularAttribute<TransactionType, Client> ditributor;
    public static volatile SingularAttribute<TransactionType, LocalDate> invoicingDate;
    public static volatile SingularAttribute<TransactionType, LocalDate> invoicedFrom;
    public static volatile SingularAttribute<TransactionType, LocalDate> invoicedTo;
    public static volatile SingularAttribute<TransactionType, Long> version;

}
