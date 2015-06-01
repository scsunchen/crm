package com.invado.core.domain;

import com.invado.core.domain.Client.Employee;
import com.invado.core.domain.Client.Status;
import com.invado.core.domain.Client.Type;
import java.math.BigDecimal;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Client.class)
public class Client_ { 

    public static volatile SingularAttribute<Client, byte[]> logo;
    public static volatile SingularAttribute<Client, String> phone;
    public static volatile SingularAttribute<Client, Township> township;
    public static volatile SingularAttribute<Client, String> TIN;
    public static volatile SingularAttribute<Client, String> fax;
    public static volatile SingularAttribute<Client, String> businessActivityCode;
    public static volatile SingularAttribute<Client, String> EMail;
    public static volatile SingularAttribute<Client, Status> status;
    public static volatile SingularAttribute<Client, String> street;
    public static volatile SingularAttribute<Client, String> postCode;
    public static volatile SingularAttribute<Client, Type> type;
    public static volatile SingularAttribute<Client, String> companyIDNumber;
    public static volatile SingularAttribute<Client, String> country;
    public static volatile SingularAttribute<Client, Long> version;
    public static volatile SingularAttribute<Client, Integer> id;
    public static volatile SingularAttribute<Client, String> registrationNumber;
    public static volatile SingularAttribute<Client, String> name;
    public static volatile SingularAttribute<Client, String> place;
    public static volatile SingularAttribute<Client, BigDecimal> initialCapital;
    public static volatile SingularAttribute<Client, Employee> employee;
    public static volatile SingularAttribute<Client, String> vatCertificateNumber;
    public static volatile SingularAttribute<Client, BankCreditor> bank;
    public static volatile SingularAttribute<Client, String> bankAccount;

}