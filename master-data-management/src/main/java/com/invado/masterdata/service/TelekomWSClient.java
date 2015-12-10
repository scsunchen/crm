package com.invado.masterdata.service;


import com.invado.core.domain.BusinessPartner;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.core.dto.DeviceDTO;
import com.invado.core.dto.DeviceHolderPartnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telekomWS.client.ServiceClient;
import telekomWS.client.exceptions.WSException;


import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZoneId;
import java.util.GregorianCalendar;

/**
 * Created by Nikola on 11/11/2015.
 */
@Service
public class TelekomWSClient {

    @PersistenceContext(name = "baza")
    private EntityManager dao;
    //@Inject
    //private ServiceClient telekomWSClient;
    private ServiceClient telekomWSClient = new ServiceClient();


    /*MERCHANT*/
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

    /*TERMINAL*/

    public Integer terminalRegistration(DeviceHolderPartnerDTO deviceHolderDTO) throws Exception {

        GregorianCalendar gcal = GregorianCalendar.from(deviceHolderDTO.getActivationDate().atStartOfDay(ZoneId.systemDefault()));
        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);

        return Integer.valueOf(telekomWSClient.terminalUnos(deviceHolderDTO.getBusinessPartnerId(), xcal, deviceHolderDTO.getLimitPerDay(), deviceHolderDTO.getICCID(),
                deviceHolderDTO.getTransactionLimit(), deviceHolderDTO.getLimitPerMonth(), deviceHolderDTO.getMSISDN(), deviceHolderDTO.getWorkingStartTime(), deviceHolderDTO.getWorkingEndTime(),
                deviceHolderDTO.getDeviceSerialNumber(), dao.find(BusinessPartner.class, deviceHolderDTO.getBusinessPartnerId()).getParentBusinessPartner().getId(),
                deviceHolderDTO.getDeviceCustomCode(), deviceHolderDTO.getRefillTypeId(), deviceHolderDTO.getConnectionTypeId()));
    }


}