package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

/**
 * Created by Nikola on 13/12/2015.
 */
@StaticMetamodel(TransactionType.class)
public class InvoicingTransaction_ {

    public static volatile SingularAttribute<InvoicingTransaction, Integer> id;
    public static volatile SingularAttribute<InvoicingTransaction, Client> ditributor;
    public static volatile SingularAttribute<InvoicingTransaction, LocalDate> invoicingDate;
    public static volatile SingularAttribute<InvoicingTransaction, LocalDate> invoicedFrom;
    public static volatile SingularAttribute<InvoicingTransaction, LocalDate> invoicedTo;
    public static volatile SingularAttribute<InvoicingTransaction, Long> version;

}
