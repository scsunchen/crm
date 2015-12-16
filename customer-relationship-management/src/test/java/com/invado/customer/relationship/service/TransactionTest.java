package com.invado.customer.relationship.service;

import com.invado.core.dto.InvoicingTransactionDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Nikola on 05/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/application-context-test.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class TransactionTest {

    @Inject
    private InvoicingTransactionService service;

    @Test
    public void testOne(){

       List<InvoicingTransactionDTO> list = service.getAllPeriods();
        for (InvoicingTransactionDTO item : list)
            System.out.println(item.getId()+" - "+item.getDisplayPeriod()+" - "+item.getInvoicingDate());
    }

    @Test
    public void dummy(){
        int month = 3;
        System.out.println(LocalDateTime.of( 2015, --month, 25, 0, 0));
        System.out.println(LocalDateTime.of( 2015, --month, 25, 0, 0));
    }
}
