package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by NikolaB on 6/14/2015.
 */
@StaticMetamodel(Job.class)
public class Job_ {
    public static volatile SingularAttribute<Job, Integer> id;
    public static volatile SingularAttribute<Job, String> name;
    public static volatile SingularAttribute<Job, String> description;
}
