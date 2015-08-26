package com.invado.customer.relationship.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Created by NikolaB on 6/21/2015.
 */
@StaticMetamodel(DeviceStatus.class)
public class DeviceStatus_ {
    public static volatile SingularAttribute<DeviceStatus, Integer> id;
    public static volatile SingularAttribute<DeviceStatus, String> name;
    public static volatile SingularAttribute<DeviceStatus, String> description;
    public static volatile SingularAttribute<DeviceStatus, Long> version;
}
