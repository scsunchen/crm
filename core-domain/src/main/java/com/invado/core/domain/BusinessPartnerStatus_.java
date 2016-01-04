package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by Nikola on 04/01/2016.
 */
@StaticMetamodel(DeviceStatus.class)
public class BusinessPartnerStatus_ {
    public static volatile SingularAttribute<BusinessPartnerStatus, Integer> id;
    public static volatile SingularAttribute<BusinessPartnerStatus, String> name;
    public static volatile SingularAttribute<BusinessPartnerStatus, String> description;
    public static volatile SingularAttribute<BusinessPartnerStatus, Long> version;

}
