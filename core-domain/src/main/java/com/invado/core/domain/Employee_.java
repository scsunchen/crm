package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Created by NikolaB on 6/14/2015.
 */
@StaticMetamodel(Employee.class)
public class Employee_ {


    public static volatile SingularAttribute<Employee, Integer> id;
    public static volatile SingularAttribute<Employee, String> name;
    public static volatile SingularAttribute<Employee, String> middleName;
    public static volatile SingularAttribute<Employee, String> lastName;
    public static volatile SingularAttribute<Employee, Date> dateOfBirth;
    public static volatile SingularAttribute<Employee, String> phone;
    public static volatile SingularAttribute<Employee, String> email;
    public static volatile SingularAttribute<Employee, byte[]> picture;
    public static volatile SingularAttribute<Employee, OrgUnit> orgUnite;
    public static volatile SingularAttribute<Employee, Date> hireDate;
    public static volatile SingularAttribute<Employee, Date> endDate;
    public static volatile SingularAttribute<Employee, Job> job;
    public static volatile SingularAttribute<Employee, Address> address;

}
