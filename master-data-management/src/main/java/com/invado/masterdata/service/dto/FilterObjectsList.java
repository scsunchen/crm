package com.invado.masterdata.service.dto;

import com.invado.core.dto.BusinessPartnerDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 24/11/2015.
 */
public class FilterObjectsList {
    private List<BusinessPartnerDTO> businessPartnerDTOs = new ArrayList<BusinessPartnerDTO>();

    public List<BusinessPartnerDTO> getBusinessPartnerDTOs() {
        return businessPartnerDTOs;
    }

    public void setBusinessPartnerDTOs(List<BusinessPartnerDTO> businessPartnerDTOs) {
        this.businessPartnerDTOs = businessPartnerDTOs;
    }
}
