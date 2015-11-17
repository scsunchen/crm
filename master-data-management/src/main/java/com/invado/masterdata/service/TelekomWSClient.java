package com.invado.masterdata.service;


import com.invado.core.domain.BusinessPartner;
import com.invado.core.dto.BusinessPartnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telekomWS.client.ServiceClient;
import telekomWS.client.exceptions.WSException;


import javax.inject.Inject;

/**
 * Created by Nikola on 11/11/2015.
 */
@Service
public class TelekomWSClient {

    //@Inject
    //private ServiceClient telekomWSClient;
    private ServiceClient telekomWSClient = new ServiceClient();

    public Integer merchatnRegistration(BusinessPartnerDTO businessPartnerDTO) throws WSException {
        return Integer.valueOf(telekomWSClient.poslovniPartnerUnos(businessPartnerDTO.getName(), Integer.valueOf(businessPartnerDTO.getTIN()).intValue(), businessPartnerDTO.getCompanyIdNumber(),
                businessPartnerDTO.getPlace(), businessPartnerDTO.getStreet(), businessPartnerDTO.getContactPersoneName(), businessPartnerDTO.getPhone(),
                businessPartnerDTO.getEMail()));
    }

    public Integer merchantUpdate(BusinessPartnerDTO businessPartnerDTO) throws WSException {
        return Integer.valueOf(telekomWSClient.poslovniPartnerIzmena(businessPartnerDTO.getName(), Integer.valueOf(businessPartnerDTO.getTIN()).intValue(), businessPartnerDTO.getCompanyIdNumber(),
                businessPartnerDTO.getPlace(), businessPartnerDTO.getStreet(), businessPartnerDTO.getContactPersoneName(), businessPartnerDTO.getPhone(),
                businessPartnerDTO.getEMail()));
    }

    public Integer merchantDeactivation(Integer telekomId) throws Exception {
        return Integer.valueOf(telekomWSClient.poslovniPartnerDeaktivacija(telekomId.intValue()));
    }

}