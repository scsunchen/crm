package com.invado.masterdata.service.interfaces;

import com.invado.core.domain.BusinessPartnerDocument;

/**
 * Created by Nikola on 25/12/2015.
 */
public interface FileUploadDAO {
    void save(BusinessPartnerDocument businessPartnerDocument);
}
