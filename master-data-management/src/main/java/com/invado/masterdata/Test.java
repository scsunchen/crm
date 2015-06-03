/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.masterdata;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory("unit");
//        EntityManager em = factory.createEntityManager();
//        System.out.println(em.createQuery("Select x from InvoiceItem x").getResultList());
        
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "applicationContext.xml");

//        System.out.println(service.read("222"));
    }
    
}
