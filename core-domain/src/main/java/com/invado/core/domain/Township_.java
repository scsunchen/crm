package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by NikolaB on 6/10/2015.
 */
@StaticMetamodel(Township.class)
public class Township_ {
    public static volatile SingularAttribute<Township, Integer> code;
    public static volatile SingularAttribute<Township, String> name;
    public static volatile SingularAttribute<Township, Long> version;
}
