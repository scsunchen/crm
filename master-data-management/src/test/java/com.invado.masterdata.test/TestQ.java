package com.invado.masterdata.test;

import com.invado.core.exception.ConstraintViolationException;
import telekomWS.client.ServiceClient;
import com.invado.core.domain.Currency;
import com.invado.core.dto.BusinessPartnerContactDetailsDTO;
import com.invado.masterdata.service.BusinessPartnerContactDetailsService;
import com.invado.masterdata.service.dto.ExchangeRateDTO;
import com.invado.masterdata.service.ExchangeRateService;

import com.invado.masterdata.service.exception.EntityExistsException;
import com.invado.core.exception.IllegalArgumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Nikola on 07/10/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/application-context-test.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class TestQ{

    /**
     * @param args the command line arguments
     */

    @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private BusinessPartnerContactDetailsService contactDetailsService;

    @PersistenceContext(name = "baza")
    private EntityManager dao;


    @Test
    public void testQuery(){

        Currency currency = dao.createNamedQuery(Currency.READ_BY_NAME_ORDERBY_NAME, Currency.class)
                .setParameter("name", "Euro")
                .getSingleResult();
        System.out.println(currency.toString());
        System.out.println(currency.getISOCode());
    }


@Test
    public void testCreateExcangeRate() throws EntityExistsException, ConstraintViolationException, IllegalArgumentException {

    ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();

    //exchangeRateDTO.setApplicationDate(LocalDate.now());
    exchangeRateDTO.setCurrencyISOCode("BAM");
    exchangeRateDTO.setListNumber(1);
    exchangeRateDTO.setBuying(BigDecimal.valueOf(10));
    exchangeRateDTO.setMiddle(BigDecimal.valueOf(10));
    exchangeRateDTO.setSelling(BigDecimal.valueOf(10));
    try {
        exchangeRateService.create(exchangeRateDTO);
    } catch (com.invado.core.exception.ConstraintViolationException e) {
        e.printStackTrace();
    }

}

    @Test
    public void testCreateContacts() throws EntityExistsException, ConstraintViolationException, IllegalArgumentException {

        BusinessPartnerContactDetailsDTO dto = new BusinessPartnerContactDetailsDTO();
        dto.setDateFrom(LocalDate.now());
        dto.setCountry("Serbia");
        dto.setPostCode("11273");
        dto.setStreet("Sv. Rafaila Šišatovačkog 7");
        dto.setPlace("Batajnica");
        dto.setMerchantId(436372);
        dto.setPointOfSaleId(446500);
        dto.setEmail("nikola@nesto.negde.com");
        dto.setFunction("mega car");
        dto.setName("Nikoa bresliev");
        dto.setPhone("+38163407279");

        contactDetailsService.create(dto);

    }

    public void testWS(){
        ServiceClient serviceClient = new ServiceClient();
        //serviceClient.poslovniPartnerUnos()

    }
}

