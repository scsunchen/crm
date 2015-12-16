package com.invado.core.domain;

import java.time.LocalDate;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Invoice.class)
public class Invoice_ { 

    public static volatile SingularAttribute<Invoice, Client> client;
    public static volatile SingularAttribute<Invoice, Integer> orgUnit;
    public static volatile SingularAttribute<Invoice, OrgUnit> orgUnitE;
    public static volatile SingularAttribute<Invoice, LocalDate> invoiceDate;
    public static volatile SingularAttribute<Invoice, LocalDate> valueDate;
    public static volatile SingularAttribute<Invoice, Boolean> isDomesticCurrency;
    public static volatile SingularAttribute<Invoice, InvoiceType> type;
    public static volatile SingularAttribute<Invoice, InvoiceBusinessPartner> partnerType;
    public static volatile SingularAttribute<Invoice, Long> version;
    public static volatile SingularAttribute<Invoice, Currency> currency;
    public static volatile SingularAttribute<Invoice, LocalDate> contractDate;
    public static volatile SingularAttribute<Invoice, String> document;
    public static volatile SingularAttribute<Invoice, Boolean> paid;
    public static volatile ListAttribute<Invoice, InvoiceItem> items;
    public static volatile SingularAttribute<Invoice, Boolean> recorded;
    public static volatile SingularAttribute<Invoice, Boolean> printed;
    public static volatile SingularAttribute<Invoice, LocalDate> creditRelationDate;
    public static volatile SingularAttribute<Invoice, BusinessPartner> partner;
    public static volatile SingularAttribute<Invoice, BankCreditor> bank;
    public static volatile SingularAttribute<Invoice, ApplicationUser> user;
    public static volatile SingularAttribute<Invoice, String> contractNumber;
    public static volatile SingularAttribute<Invoice, InvoicingTransaction> invoicingTransaction;

}