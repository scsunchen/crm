/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.OrgUnit;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.service.dto.OpenItemStatementsDTO;
import com.invado.finance.service.dto.RequestOpenItemStatementsDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import junit.framework.Assert;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import org.easymock.IMocksControl;
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
public class OpenItemStatementsTest {
    
    @Inject
    private IMocksControl mocks; 
    @Inject
    @Named("typedQueryMocksControl")
    private TypedQuery<Analytical> query;
    @Inject
    @Named("entityManagerMocksControl")
    private EntityManager EM;
    @Inject
    private OpenItemStatements service;
    
    @Before
    public void before() {
        service.dao = EM;
        mocks.reset();
    }
    
    @Test
    public void testReadOpenItemStatements() throws Exception {
        Integer orgUnitID = 1;
        Integer clientID = 2;
        RequestOpenItemStatementsDTO DTO = new RequestOpenItemStatementsDTO();
        DTO.setClientID(clientID);
        DTO.setOrgUnitID(orgUnitID);
        DTO.setAccountNumber("202000");
        DTO.setValueDate(LocalDate.of(2011, Month.MARCH, 1));
        DTO.setPartnerID(1);
        DTO.setP(RequestOpenItemStatementsDTO.Prikaz.ANALYTIC);
        
        Analytical a = new Analytical(1L);
        a.setAmount(ChangeType.DEBIT, BigDecimal.TEN);
        Analytical a1 = new Analytical(2L);
        a1.setAmount(ChangeType.DEBIT, BigDecimal.TEN);
        Analytical a2 = new Analytical(3L);
        a2.setAmount(ChangeType.CREDIT, BigDecimal.valueOf(20));
        expect(EM.find(OrgUnit.class, DTO.getOrgUnitID()))
                .andReturn(new OrgUnit());
        expect(EM.find(Account.class, DTO.getAccountNumber())).andReturn(new Account());
        expect(EM.find(BusinessPartner.class, DTO.getPartnerID())).andReturn(new BusinessPartner());
        expect(EM.createQuery(EasyMock.anyString())).andReturn(query);
        expect(query.setParameter(1, Analytical.Status.OPEN)).andReturn(query);
        expect(query.setParameter(2, LocalDate.of(2011, Month.MARCH, 1))).andReturn(query);
        expect(query.getResultList()).andReturn(Arrays.asList(a,a1,a2));
        
        EM.lock(a, LockModeType.OPTIMISTIC);
        EM.lock(a1, LockModeType.OPTIMISTIC);
        EM.lock(a2, LockModeType.OPTIMISTIC);
        expect(EM.find(Client.class, DTO.getClientID())).andReturn(new Client());
        expect(EM.find(Account.class, DTO.getAccountNumber()))
                .andReturn(new Account());
        expect(EM.find(BusinessPartner.class, DTO.getPartnerID())).andReturn(
                new BusinessPartner());

        mocks.replay();
        List<OpenItemStatementsDTO> lista = service.readOpenItemStatements(DTO);
        mocks.verify();
        
        OpenItemStatementsDTO iosdto = lista.get(0);
        Assert.assertEquals(iosdto.totalDebit, BigDecimal.valueOf(20));
        Assert.assertEquals(iosdto.totalCredit, BigDecimal.valueOf(20));
        Assert.assertEquals(iosdto.balanceDebit, BigDecimal.valueOf(0));
        Assert.assertEquals(iosdto.balanceCredit, BigDecimal.valueOf(0));
    }
    
}
