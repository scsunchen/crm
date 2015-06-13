package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by NikolaB on 6/10/2015.
 */
@StaticMetamodel(BankCreditor.class)
public class BankCreditor_ {
    public static volatile SingularAttribute<BankCreditor, String> phone;
    public static volatile SingularAttribute<BankCreditor, Long> version;
    public static volatile SingularAttribute<BankCreditor, String> id;
    public static volatile SingularAttribute<BankCreditor, Address> place;
    public static volatile SingularAttribute<BankCreditor, Address> street;
    public static volatile SingularAttribute<BankCreditor, Address> postCode;
    public static volatile SingularAttribute<BankCreditor, String> account;
    public static volatile SingularAttribute<BankCreditor, String> name;
    public static volatile SingularAttribute<BankCreditor, String> contactPerson;
    public static volatile SingularAttribute<BankCreditor, String> contactFunction;
    public static volatile SingularAttribute<BankCreditor, String> contactPhone;

}
