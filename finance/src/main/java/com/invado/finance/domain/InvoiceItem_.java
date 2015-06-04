package com.invado.finance.domain;

import com.invado.core.domain.Article;
import com.invado.core.domain.VatPercent;

import java.math.BigDecimal;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(InvoiceItem.class)
public class InvoiceItem_ { 

    public static volatile SingularAttribute<InvoiceItem, Invoice> invoice;
    public static volatile SingularAttribute<InvoiceItem, Integer> ordinal;
    public static volatile SingularAttribute<InvoiceItem, Article> article;
    public static volatile SingularAttribute<InvoiceItem, String> itemDescription;
    public static volatile SingularAttribute<InvoiceItem, String> unitOfMeasure;
    public static volatile SingularAttribute<InvoiceItem, BigDecimal> netPrice;
    public static volatile SingularAttribute<InvoiceItem, BigDecimal> vatPercent;
    public static volatile SingularAttribute<InvoiceItem, VatPercent> articleVAT;
    public static volatile SingularAttribute<InvoiceItem, BigDecimal> totalCost;
    public static volatile SingularAttribute<InvoiceItem, BigDecimal> rabatPercent;
    public static volatile SingularAttribute<InvoiceItem, BigDecimal> quantity;

}