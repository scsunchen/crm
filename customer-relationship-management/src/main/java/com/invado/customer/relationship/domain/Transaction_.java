package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Nikola on 23/08/2015.
 */
@StaticMetamodel(Device.class)
public class Transaction_ {

    public static volatile SingularAttribute<Transaction, Integer> id;
    public static volatile SingularAttribute<Transaction, String> statusId;
    public static volatile SingularAttribute<Transaction, TransactionType> type;
    public static volatile SingularAttribute<Transaction, Integer> amount;
    public static volatile SingularAttribute<Transaction, Device> terminal;
    public static volatile SingularAttribute<Transaction, BusinessPartner> pointOfSale;
    public static volatile SingularAttribute<Transaction, Client> distributor;
    public static volatile SingularAttribute<Transaction, BusinessPartner> serviceProvider;
    public static volatile SingularAttribute<Transaction, LocalDateTime> requestTime;
    public static volatile SingularAttribute<Transaction, LocalDateTime> responseTime;
}