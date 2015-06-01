package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Currency.class)
public class Currency_ { 

    public static volatile SingularAttribute<Currency, String> description;
    public static volatile SingularAttribute<Currency, String> ISOCode;
    public static volatile SingularAttribute<Currency, Long> version;

}