package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

/**
 * Created by Nikola on 01/01/2016.
 */
@StaticMetamodel(DeviceHolderPartner.class)
public class DeviceServiceProviderRegistration_ {

    public static volatile SingularAttribute<DeviceServiceProviderRegistration, Integer> id;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, BusinessPartner> serviceProvider;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, Device> device;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, String> registrationId;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, PrepaidRefillType> refillType;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, ConnectionType> connectionType;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, String> workingStartTime;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, String> workingEndTime;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, LocalDate> activationDate;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, String> MSISDN;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, Integer> transactionLimit;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, Long> limitPerDay;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, Integer> limitPerMonth;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, String> ICCID;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, String> registration;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, DeviceStatus> deviceStatus;
    public static volatile SingularAttribute<DeviceServiceProviderRegistration, Long> version;
}
