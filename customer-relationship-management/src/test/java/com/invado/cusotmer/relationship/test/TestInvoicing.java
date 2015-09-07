/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.cusotmer.relationship.test;


import com.invado.customer.relationship.service.TransactionService;
import com.invado.customer.relationship.service.dto.InvoicingTransactionSetDTO;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author bdragan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:res/application-context-test.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class TestInvoicing {

    /**
     * @param args the command line arguments
     */

    @Autowired
    private TransactionService transactionService;

    @Test
    public void testInv() throws Exception{
        // TODO code application logic here
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory("unit");
//        EntityManager em = factory.createEntityManager();
//        System.out.println(em.createQuery("Select x from InvoiceItem x").getResultList());
/*
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
*/
        /*testiram da li je ok sa branch*/

//        System.out.println(service.read("222"));

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(1);
        ReadRangeDTO<InvoicingTransactionSetDTO> invoicingTransactionSetDTOReadRangeDTO = transactionService.readInvoicingSetPage(request);
    }

}
