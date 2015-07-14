package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrgUnit.class)
public class OrgUnit_ { 

    public static volatile SingularAttribute<OrgUnit, Integer> id;
    public static volatile SingularAttribute<OrgUnit, String> customId;
    public static volatile SingularAttribute<OrgUnit, Client> client;
    public static volatile SingularAttribute<OrgUnit, String> name;
    public static volatile SingularAttribute<OrgUnit, String> street;
    public static volatile SingularAttribute<OrgUnit, String> place;
    public static volatile SingularAttribute<OrgUnit, OrgUnit> parentOrgUnit;
    public static volatile SingularAttribute<OrgUnit, Long> version;


}