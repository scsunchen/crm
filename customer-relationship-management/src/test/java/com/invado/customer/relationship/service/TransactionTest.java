package com.invado.customer.relationship.service;

import com.invado.core.domain.Client;
import com.invado.core.dto.InvoicingTransactionDTO;
import com.invado.core.exception.PageNotExistsException;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import com.invado.customer.relationship.service.dto.TransactionSummaryElementsDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Nikola on 05/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/application-context.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class TransactionTest {

    @Inject
    private InvoicingTransactionService service;
    @Inject
    private TransactionService transactionService;


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

    @Test
    public void testStream() throws PageNotExistsException {

        PageRequestDTO request = new PageRequestDTO();

        request.setPage(0);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId", "2"));
        ReadRangeDTO<TransactionDTO> items = transactionService.readPage(request);

        BigDecimal res = items.getData().stream().map(TransactionDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println(res);
    }
}
