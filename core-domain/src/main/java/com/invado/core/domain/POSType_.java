package com.invado.core.domain;

/**
 * Created by Nikola on 10/11/2015.
 */

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(POSType.class)
public class POSType_ {
    public static volatile SingularAttribute<POSType, Integer> id;
    public static volatile SingularAttribute<POSType, String> description;
    public static volatile SingularAttribute<POSType, Long> version;
}
