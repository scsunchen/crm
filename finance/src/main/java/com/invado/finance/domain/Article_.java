/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.invado.finance.domain;

import com.invado.core.domain.ApplicationUser;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author Bobic Dragan
 */
@StaticMetamodel(Article.class)
public class Article_ {
    
    public static volatile SingularAttribute<Article, String> code;
    public static volatile SingularAttribute<Article, String> description;
    public static volatile SingularAttribute<Article, VatPercent> VATRate;
    public static volatile SingularAttribute<Article, Boolean> userDefinedUnitOfMeasure;
    public static volatile SingularAttribute<Article, String> unitOfMeasureCode;
    public static volatile SingularAttribute<Article, BigDecimal> salePriceWithVAT;
    public static volatile SingularAttribute<Article, BigDecimal> salePrice;
    public static volatile SingularAttribute<Article, BigDecimal> purchasePrice;
    public static volatile SingularAttribute<Article, BigDecimal> unitsInStock;
    public static volatile SingularAttribute<Article, ApplicationUser> lastUpdateBy;
    public static volatile SingularAttribute<Article, Date> updated;
    public static volatile SingularAttribute<Article, Long> version;
    
}
