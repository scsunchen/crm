package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

/**
 * Created by Nikola on 23/12/2015.
 */
@StaticMetamodel(BusinessPartnerContactDetails.class)
public class BusinessPartnerDocument_ {

    public static volatile SingularAttribute<BusinessPartnerDocument, Integer> id;
    public static volatile SingularAttribute<BusinessPartnerDocument, String> description;
    public static volatile SingularAttribute<BusinessPartnerDocument, DocumentType> type;
    public static volatile SingularAttribute<BusinessPartnerDocument, BusinessPartner> businessPartnerOwner;
    public static volatile SingularAttribute<BusinessPartnerDocument, LocalDate> inputDate;
    public static volatile SingularAttribute<BusinessPartnerDocument, LocalDate> validUntil;
    public static volatile SingularAttribute<BusinessPartnerDocument, BusinessPartnerDocument.DocumentStatus> status;
    public static volatile SingularAttribute<BusinessPartnerDocument, String> fileName;
    public static volatile SingularAttribute<BusinessPartnerDocument, String> fileContentType;
    public static volatile SingularAttribute<BusinessPartnerDocument, byte[]> file;
    public static volatile SingularAttribute<BusinessPartnerDocument, Long> version;


}
