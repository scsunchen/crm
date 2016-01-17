/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Client;
import com.invado.core.domain.Client_;
import com.invado.core.domain.Currency;
import com.invado.core.domain.ExchangeRate;
import com.invado.core.domain.ExchangeRatePK;
import com.invado.core.domain.OrgUnit;
import com.invado.core.domain.OrgUnit_;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.AccountDetermination;
import com.invado.finance.domain.journal_entry.AccountType;
import com.invado.finance.domain.journal_entry.Account_;
import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.domain.journal_entry.GeneralLedger;
import com.invado.finance.domain.journal_entry.GeneralLedger_;
import com.invado.finance.domain.journal_entry.JournalEntryType;
import com.invado.finance.service.dto.CurrencyTypeDTO;
import com.invado.finance.service.dto.LedgerCardDTO;
import com.invado.finance.service.dto.LedgerCardItemDTO;
import com.invado.finance.service.dto.LedgerCardTypeDTO;
import com.invado.finance.service.dto.ReadLedgerCardsDTO;
import com.invado.finance.service.dto.RequestGLCardDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.easymock.EasyMock;
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
public class GLCardTest {
    
    @Inject
    private IMocksControl mocks; 
    @Inject
    @Named("typedQueryMocksControl")
    private TypedQuery<GeneralLedger> query;
    @Inject
    @Named("entityManagerMocksControl")
    private EntityManager EM;
    @Inject
    private GLCard service;
    
    @Before
    public void before() {
        service.dao = EM;
    }
    
    @Test
    public void testReadForeignCurrencyGLCard() throws Exception {
        RequestGLCardDTO  dto = new RequestGLCardDTO();
        dto.setClientID(1);
        dto.setOrgUnitID(null);
        dto.setAccountNumber("");
        dto.setValueDateFrom(null);
        dto.setValueDateTo(null);
        dto.setCreditDebitRelationDateFrom(null);
        dto.setCreditDebitRelationDateTo(null);
        dto.setCurrency(CurrencyTypeDTO.FOREIGN);        
        dto.setForeignCurrencyISOCode("EUR");        
        Account account = new Account("202000", 
                                  "", 
                                  AccountDetermination.CUSTOMERS,
                                  AccountType.ANALITICKI);
        OrgUnit unit = new OrgUnit(1);
        Client client = new Client(1);
        JournalEntryType type = new JournalEntryType(1, 2);
        GeneralLedger transaction1 = new GeneralLedger();
        transaction1.setAmount(ChangeType.DEBIT, BigDecimal.valueOf(50000));
        transaction1.setDocument("1/1");
        transaction1.setInternalDocument("1/1");
        transaction1.setJournalEntryItemOrdinal(1);
        transaction1.setAccount(account);
        transaction1.setValueDate(LocalDate.of(2014, Month.JANUARY, 1));
        transaction1.setRecordDate(LocalDate.of(2014, Month.JANUARY, 1));
        transaction1.setJournalEntryType(type);
        transaction1.setJournalEntryNumber(1);
        transaction1.setCreditDebitRelationDate(LocalDate.of(2012, Month.JANUARY, 1));
        transaction1.setOrgUnit(unit);
        
        GeneralLedger transaction2 = new GeneralLedger();
        transaction2.setAmount(ChangeType.DEBIT, BigDecimal.valueOf(51600));
        transaction2.setDocument("1/2");
        transaction2.setInternalDocument("1/2");
        transaction2.setJournalEntryItemOrdinal(1);
        transaction2.setAccount(account);
        transaction2.setValueDate(LocalDate.of(2014, Month.JANUARY, 1));
        transaction2.setRecordDate(LocalDate.of(2014, Month.JANUARY, 1));
        transaction2.setJournalEntryType(type);
        transaction2.setJournalEntryNumber(2);
        transaction2.setCreditDebitRelationDate(LocalDate.of(2012, Month.JANUARY, 2));
        transaction2.setOrgUnit(unit);
        
        ApplicationSetup appSetup = new ApplicationSetup(1);
        appSetup.setApplicationVersion(
                ApplicationSetup.ApplicationVersion.COMPANY_ACCOUNTING
        );
        appSetup.setPageSize(35);
        appSetup.setYear(2012);
        Calendar firstJanuary = Calendar.getInstance();
        firstJanuary.set(2012, Calendar.JANUARY, 1);
        Calendar secondJanuary = Calendar.getInstance();
        secondJanuary.set(2012, Calendar.JANUARY, 2);
        ExchangeRate exchangeRate = new ExchangeRate(secondJanuary.getTime(), 
                new Currency(dto.getForeignCurrencyISOCode()));
        exchangeRate.setMiddle(BigDecimal.valueOf(112.0000));
        
        CriteriaBuilder cb = mocks.createMock(CriteriaBuilder.class);
        CriteriaQuery<GeneralLedger> cQuery = mocks.createMock(CriteriaQuery.class);
        Root<GeneralLedger> root = mocks.createMock(Root.class);
        Join<GeneralLedger, OrgUnit> orgUnitJoin = mocks.createMock(Join.class);
        Join<GeneralLedger, Account> accountJoin = mocks.createMock(Join.class);
        ParameterExpression param = mocks.createMock(ParameterExpression.class);
        Order order = mocks.createMock(Order.class);
        Predicate predicate = mocks.createMock(Predicate.class);
        Path path = mocks.createMock(Path.class);
        
        expect(EM.find(Client.class, dto.getClientID())).andReturn(client);
        expect(EM.getCriteriaBuilder()).andReturn(cb);
        expect(cb.createQuery(GeneralLedger.class)).andReturn(cQuery);
        expect(cQuery.from(GeneralLedger.class)).andReturn(root);
        expect(cQuery.select(root)).andReturn(cQuery);        
        expect(root.join(GeneralLedger_.orgUnit, JoinType.LEFT))
                .andReturn(orgUnitJoin);
        expect(root.join(GeneralLedger_.account, JoinType.LEFT))
                .andReturn(accountJoin);
        expect(orgUnitJoin.get(OrgUnit_.client)).andReturn(path);
        expect(path.get(Client_.id)).andReturn(path);
        expect(cb.parameter(Integer.class, "clientID")).andReturn(param);
        expect(cb.equal(path, param)).andReturn(predicate);        
        expect(cb.and(predicate)).andReturn(predicate);
        expect(cQuery.where(predicate)).andReturn(cQuery);
        expect(accountJoin.get(Account_.number)).andReturn(path);
        expect(cb.asc(path)).andReturn(order);
        expect(root.get(GeneralLedger_.recordDate)).andReturn(path);
        expect(cb.asc(path)).andReturn(order);
        expect(cQuery.orderBy(order, order)).andReturn(cQuery);
        expect(EM.createQuery(cQuery)).andReturn(query);
        expect(query.setParameter("clientID", dto.getClientID())).andReturn(query);
        expect(query.getResultList()).andReturn(Arrays.asList(
                transaction1, transaction2));
        expect(EM.find(Client.class, dto.getClientID())).andStubReturn(client);
        expect(EM.find(Account.class, account.getNumber())).andStubReturn(account);
        expect(EM.find(ApplicationSetup.class, 1)).andStubReturn(appSetup);
        expect(EM.find(ExchangeRate.class, new ExchangeRatePK(
                EasyMock.anyObject(), EasyMock.anyObject())))
                .andStubReturn(exchangeRate);   
        
        mocks.replay();
        ReadLedgerCardsDTO result = service.readGLCard(dto);
        mocks.verify();
        
        Assert.assertEquals(Integer.valueOf(0), result.numberOfPages);  
        Assert.assertEquals(appSetup.getPageSize(), result.pageSize);  
        Assert.assertEquals(1, result.ledgerCards.size());  
        //Header test***********************************************************
        LedgerCardDTO actual = result.ledgerCards.get(0);
        Assert.assertEquals(Integer.valueOf(1), actual.numberOfPages);
        Assert.assertEquals(account.getDescription(), actual.accountName);
        Assert.assertEquals(account.getNumber(), actual.accountNumber);
        Assert.assertEquals(null, actual.orgUnitID);
        Assert.assertEquals(null, actual.partnerRegistrationNumber);
        Assert.assertEquals(null, actual.valueDateFrom);
        Assert.assertEquals(null, actual.valueDateTo);
        Assert.assertEquals(LedgerCardTypeDTO.GENERAL_LEDGER, actual.type);
        Assert.assertEquals(0, BigDecimal.valueOf(907.14).compareTo(actual.debitTotal));
        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(actual.creditTotal));
        Assert.assertEquals(0, BigDecimal.valueOf(907.14).compareTo(actual.balanceTotal));
        //Items test************************************************************
        Assert.assertEquals(2, actual.items.size());
        LedgerCardItemDTO item1Actual = actual.items.get(0);
        Assert.assertEquals(null, item1Actual.accountNumber);
        Assert.assertEquals(0, BigDecimal.valueOf(446.43).compareTo(
                item1Actual.debit));
        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(item1Actual.credit));
        Assert.assertEquals(0, BigDecimal.valueOf(446.43).compareTo(
                item1Actual.balance));
        Assert.assertEquals(transaction1.getCreditDebitRelationDate(),
                            item1Actual.creditDebitRelationDate);
        Assert.assertEquals(transaction1.getDocument(), item1Actual.document);
        Assert.assertEquals(transaction1.getInternalDocument(), 
                            item1Actual.internalDocument);
        Assert.assertEquals(transaction1.getRecordDate(), 
                            item1Actual.journalEntryDate);
        Assert.assertEquals(transaction1.getJournalEntryItemOrdinal(), 
                            item1Actual.ordinal);
        Assert.assertEquals(transaction1.getJournalEntryNumber(),
                            item1Actual.journalEntryNumber);
        Assert.assertEquals(transaction1.getValueDate(),
                            item1Actual.valueDate);
        Assert.assertEquals(transaction1.getJournalEntryTypeID(),
                            item1Actual.typeID);
        Assert.assertEquals(transaction1.getOrgUnitID(), item1Actual.orgUnitID);
        
        LedgerCardItemDTO item2Actual = actual.items.get(1);
        Assert.assertEquals(null, item2Actual.accountNumber);
        Assert.assertEquals(0, BigDecimal.valueOf(460.71).compareTo(
                item2Actual.debit));
        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(item2Actual.credit));
        Assert.assertEquals(0, BigDecimal.valueOf(907.14).compareTo(
                item2Actual.balance));
        Assert.assertEquals(transaction2.getCreditDebitRelationDate(),
                            item2Actual.creditDebitRelationDate);
        Assert.assertEquals(transaction2.getDocument(), item2Actual.document);
        Assert.assertEquals(transaction2.getInternalDocument(), 
                            item2Actual.internalDocument);
        Assert.assertEquals(transaction2.getRecordDate(), 
                            item2Actual.journalEntryDate);
        Assert.assertEquals(transaction2.getJournalEntryItemOrdinal(), 
                            item2Actual.ordinal);
        Assert.assertEquals(transaction2.getJournalEntryNumber(),
                            item2Actual.journalEntryNumber);
        Assert.assertEquals(transaction2.getValueDate(),
                            item2Actual.valueDate);
        Assert.assertEquals(transaction2.getJournalEntryTypeID(),
                            item2Actual.typeID);
        Assert.assertEquals(transaction2.getOrgUnitID(), item2Actual.orgUnitID);
    }    
    
}
