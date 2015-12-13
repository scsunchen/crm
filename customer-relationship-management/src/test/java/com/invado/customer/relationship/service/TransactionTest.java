package com.invado.customer.relationship.service;

import com.invado.core.dto.InvoiceDTO;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikola on 05/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/application-context.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class TransactionTest {

    @Inject
    private TransactionService transactionService;

    @Test
    public void testOne(){

        Map<Integer, InvoiceDTO> invoicePerMerchantMap = new HashMap<Integer, InvoiceDTO>();
        TransactionDTO paramTransactionDTO = new TransactionDTO();
        //paramTransactionDTO.setInvoicingDistributorId("2");
        //paramTransactionDTO.setInvoicingGenDate("05.12.2015");
        try {
            invoicePerMerchantMap = transactionService.genInvoicesI(paramTransactionDTO);
            //invoicePerMerchantMap = transactionService.genInvoicesUI(paramTransactionDTO);
        } catch (ReferentialIntegrityException e) {
            e.printStackTrace();
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
        } catch (EntityExistsException e) {
            e.printStackTrace();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("evo ga nesto");
    }

    @Test
    public void dummy(){
        int month = 3;
        System.out.println(LocalDateTime.of( 2015, --month, 25, 0, 0));
        System.out.println(LocalDateTime.of( 2015, --month, 25, 0, 0));
    }
}
