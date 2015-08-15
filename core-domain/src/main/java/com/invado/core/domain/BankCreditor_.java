package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by NikolaB on 6/10/2015.
 */
@StaticMetamodel(BankCreditor.class)
public class BankCreditor_ {

    public static volatile SingularAttribute<BankCreditor, Integer> id;
    public static volatile SingularAttribute<BankCreditor, String> name;
    public static volatile SingularAttribute<BankCreditor, String> postCode;
    public static volatile SingularAttribute<BankCreditor, String> place;
    public static volatile SingularAttribute<BankCreditor, String> street;
    public static volatile SingularAttribute<BankCreditor, String> contactPerson;
    public static volatile SingularAttribute<BankCreditor, String> contactFunction;
    public static volatile SingularAttribute<BankCreditor, String> contactPhone;
    public static volatile SingularAttribute<BankCreditor, String> account;
    public static volatile SingularAttribute<BankCreditor, Long> version;

}
