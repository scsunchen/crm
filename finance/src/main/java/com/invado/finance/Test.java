/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author bdragan
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("unit");
        EntityManager em = factory.createEntityManager();
        System.out.println(em.createQuery("Select x from InvoiceItem x").getResultList());
        
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
//                "application-context.xml");
//        InvoiceService service = applicationContext.getBean(InvoiceService.class);
//        PageRequestDTO p = new PageRequestDTO();
//        p.setPage(1);
//        service.readPage(p);
//        service.read("222");
//        Article a  =new Article("233");
//        a.setDescription("nerma");
//        a.setVATRate(VatPercent.GENERAL_RATE);
//        a.setUnitOfMeasureCode("KOM");
//        a.setUserDefinedUnitOfMeasure(true);
//        service.create(a);
//        System.out.println(service.read("222"));
    }
    
}
