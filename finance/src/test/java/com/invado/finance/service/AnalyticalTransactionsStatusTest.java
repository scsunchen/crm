/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.BusinessPartner;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.ChangeType;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import static org.easymock.EasyMock.expect;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author dbobic
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/unit-test.xml"})
public class AnalyticalTransactionsStatusTest {
    
    @Inject
    private IMocksControl mocks; 
    @Inject
    @Named("typedQueryMocksControl")
    private TypedQuery<Analytical> query;
    @Inject
    @Named("entityManagerMocksControl")
    private EntityManager EM;
    @Inject
    //FIXME : inject entity manager
    private AnalyticalTransactionsStatus service;
    
    @Before
    public void before() {
        service.EM = EM;
    }

    @Test
    public void testCloseItems() throws Exception {
        service.EM = EM;
        BusinessPartner partner = new BusinessPartner("01000");
        Account account = new Account("202000");
        Analytical a = new Analytical(1L);
        a.setPartner(partner);
        a.setAccount(account);
        a.setDocument("1");
        a.setAmount(ChangeType.DEBIT, BigDecimal.TEN);
        Analytical a1 = new Analytical(2L);
        a1.setPartner(partner);
        a1.setAccount(account);
        a1.setDocument("1");
        a1.setAmount(ChangeType.DEBIT, BigDecimal.TEN);
        Analytical a2 = new Analytical(3L);
        a2.setPartner(partner);
        a2.setAccount(account);
        a2.setDocument("1");
        a2.setAmount(ChangeType.CREDIT, BigDecimal.valueOf(20));

        expect(EM.createNamedQuery(Analytical.READ_BY_STATUS, Analytical.class))
                .andReturn(query);
        expect(query.setParameter(1, Analytical.Status.OPEN)).andReturn(query);
        expect(query.getResultList()).andReturn(Arrays.asList(a, a1, a2));
        EM.lock(a, LockModeType.OPTIMISTIC);
        EM.lock(a1, LockModeType.OPTIMISTIC);
        EM.lock(a2, LockModeType.OPTIMISTIC);
        expect(EM.find(Analytical.class, 1L)).andReturn(a);
        expect(EM.find(Analytical.class, 2L)).andReturn(a1);
        expect(EM.find(Analytical.class, 3L)).andReturn(a2);

        mocks.replay();
        service.closeItems();
        mocks.verify();

        //Balance is zero for specified business partnerso all items should be closed
        Assert.assertEquals(a.getStatus(), Analytical.Status.CLOSED);
        Assert.assertEquals(a1.getStatus(), Analytical.Status.CLOSED);
        Assert.assertEquals(a2.getStatus(), Analytical.Status.CLOSED);
    }

}
