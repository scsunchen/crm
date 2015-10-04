/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.cusotmer.relationship.test;


import com.invado.customer.relationship.domain.BusinessPartnerRelationshipTermsItems;
import com.invado.customer.relationship.service.TransactionService;
import com.invado.customer.relationship.service.dto.InvoicingTransactionSetDTO;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import com.invado.finance.service.dto.InvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author bdragan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:res/application-context-test.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class TestInvoicing {

    /**
     * @param args the command line arguments
     */

    @Autowired
    private TransactionService transactionService;

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Test
    public void testInv() throws Exception{

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setInvoicingDate("01.09.2015");
        transactionDTO.setDistributorId(new Integer(2));

        Map<Integer, InvoiceDTO> invoiceDTOMap = transactionService.genInvoices(transactionDTO);

        Iterator it = invoiceDTOMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    @Test
    public void testQuery(){

        BusinessPartnerRelationshipTermsItems it = dao.createNamedQuery(BusinessPartnerRelationshipTermsItems.READ_TERMS_PER_PARTNER_AND_ARTICLE, BusinessPartnerRelationshipTermsItems.class)
                .setParameter("partner", 5)
                .setParameter("serviceId", 13)
                .getSingleResult();
    }

}
