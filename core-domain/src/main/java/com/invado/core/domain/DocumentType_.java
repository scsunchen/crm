package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by Nikola on 23/12/2015.
 */
@StaticMetamodel(DocumentType.class)
public class DocumentType_ {

    public static volatile SingularAttribute<DocumentType, Integer> id;
    public static volatile SingularAttribute<DocumentType, String> description;
}

