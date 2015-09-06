package com.invado.core.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ExchangeRate.class)
public class ExchangeRate_ { 

    public static volatile SingularAttribute<ExchangeRate, BigDecimal> selling;
    public static volatile SingularAttribute<ExchangeRate, Date> applicationDate;
    public static volatile SingularAttribute<ExchangeRate, BigDecimal> buying;
    public static volatile SingularAttribute<ExchangeRate, Integer> listNumber;
    public static volatile SingularAttribute<ExchangeRate, Currency> toCurrency;
    public static volatile SingularAttribute<ExchangeRate, BigDecimal> middle;
    public static volatile SingularAttribute<ExchangeRate, Long> version;

}