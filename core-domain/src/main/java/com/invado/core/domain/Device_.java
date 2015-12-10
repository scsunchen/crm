package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Created by NikolaB on 6/21/2015.
 */
@StaticMetamodel(Device.class)
public class Device_ {

    public static volatile SingularAttribute<Device, Integer> id;
    public static volatile SingularAttribute<Device, String> customCode;
    public static volatile SingularAttribute<Device, Article> article;
    public static volatile SingularAttribute<Device, String> serialNumber;
    public static volatile SingularAttribute<Device, DeviceStatus> status;
    public static volatile SingularAttribute<Device, Date> creationDate;
    public static volatile SingularAttribute<Device, Date> workingStartTime;
    public static volatile SingularAttribute<Device, Date> workingEndTime;
    public static volatile SingularAttribute<Device, String> installedSoftwareVersion;
    public static volatile SingularAttribute<Device, String> articleDesc;
    public static volatile SingularAttribute<Device, String> articleCode;
    public static volatile SingularAttribute<Device, Long> version;
}
