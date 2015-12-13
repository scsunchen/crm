package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

/**
 * Created by Nikola on 06/12/2015.
 */
@StaticMetamodel(DeviceHolderPartner.class)
public class DeviceHolderPartner_ {

    public static volatile SingularAttribute<DeviceHolderPartner, Integer> id;
    public static volatile SingularAttribute<DeviceHolderPartner, Device> device;
    public static volatile SingularAttribute<DeviceHolderPartner, BusinessPartner> businessPartner;
    public static volatile SingularAttribute<DeviceHolderPartner, LocalDate> startDate;
    public static volatile SingularAttribute<DeviceHolderPartner, LocalDate> endDate;
    public static volatile SingularAttribute<DeviceHolderPartner, ConnectionType> connectionType;
    public static volatile SingularAttribute<DeviceHolderPartner, PrepaidRefillType> refillType;
    public static volatile SingularAttribute<DeviceHolderPartner, Long> version;

}
