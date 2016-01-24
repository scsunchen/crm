/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.OrgUnit;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.AccountDetermination;
import com.invado.finance.domain.journal_entry.AccountType;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.domain.journal_entry.Description;
import com.invado.finance.domain.journal_entry.GeneralLedger;
import com.invado.finance.domain.journal_entry.JournalEntry;
import com.invado.finance.domain.journal_entry.JournalEntryItem;
import com.invado.finance.domain.journal_entry.JournalEntryPK;
import com.invado.finance.domain.journal_entry.JournalEntryType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.validation.Validator;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.easymock.EasyMock.*;
import org.junit.Assert;

/**
 *
 * @author dbobic
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/unit-test.xml"})
public class RecordJournalEntryServiceTest {
    
    @Inject
    private IMocksControl mocks; 
    @Inject
    @Named("entityManagerMocksControl")
    private EntityManager EM;
    @Inject
    @Named("validatorMocksControl")
    private Validator validator;
    @Inject
    private RecordJournalEntryService service;
    
    @Before
    public void before() {
        service.EM = EM;
        mocks.reset();
    }
    
    @Test
    public void testRecordJournalEntry() throws Exception {
        Integer clientId = 1;
        Integer typeId = 1;
        Integer journalEntryNumber = 1;
        
        JournalEntryType type = new JournalEntryType(clientId, typeId);
        type.setName("Invoice");
        JournalEntry journalEntry = new JournalEntry(type, 1);
        journalEntry.setPosted(Boolean.FALSE);
        journalEntry.setNumber(journalEntryNumber);
        LocalDate someDate = LocalDate.of(2016, Month.JANUARY, 19);
        journalEntry.setRecordDate(someDate);
        //************************businessPartnerDebit**************************
        JournalEntryItem businessPartnerDebit = new JournalEntryItem(journalEntry);
        businessPartnerDebit.setCreditDebitRelationDate(someDate);
        businessPartnerDebit.setValueDate(someDate);
        Account domesticCustomers = new Account("204000");
        domesticCustomers.setDescription("");
        domesticCustomers.setDetermination(AccountDetermination.CUSTOMERS);
        domesticCustomers.setType(AccountType.ANALITICKI);
        domesticCustomers.setVersion(1L);
        businessPartnerDebit.setAccount(domesticCustomers);
        businessPartnerDebit.setAmount(ChangeType.DEBIT, BigDecimal.valueOf(120));
        businessPartnerDebit.setDocument("1/2016");
        businessPartnerDebit.setInternalDocument("");
        businessPartnerDebit.setDesc(new Description(1));
        businessPartnerDebit.setDetermination(AccountDetermination.CUSTOMERS);
        businessPartnerDebit.setOrgUnit(new OrgUnit(1));
        BusinessPartner partner = new BusinessPartner();
        partner.setId(1);
        businessPartnerDebit.setPartner(partner);
        businessPartnerDebit.setUser(new ApplicationUser(1));
        journalEntry.addItem(businessPartnerDebit);        
        //**********************************************************************
        JournalEntryItem vatCredit = new JournalEntryItem(journalEntry);
        vatCredit.setCreditDebitRelationDate(someDate);
        vatCredit.setValueDate(someDate);
        Account VAT = new Account("470000");
        VAT.setDescription("");
        VAT.setDetermination(AccountDetermination.GENERAL_LEDGER);
        VAT.setType(AccountType.ANALITICKI);
        VAT.setVersion(1L);
        vatCredit.setAccount(VAT);
        vatCredit.setAmount(ChangeType.CREDIT, BigDecimal.valueOf(20));
        vatCredit.setDocument("1/2016");
        vatCredit.setInternalDocument("");
        vatCredit.setDesc(new Description(1));
        vatCredit.setDetermination(AccountDetermination.GENERAL_LEDGER);
        vatCredit.setOrgUnit(new OrgUnit(1));
        vatCredit.setPartner(null);
        vatCredit.setUser(new ApplicationUser(1));
        journalEntry.addItem(vatCredit);
        //**********************************************************************
        JournalEntryItem incomeCredit = new JournalEntryItem(journalEntry);
        incomeCredit.setCreditDebitRelationDate(someDate);
        incomeCredit.setValueDate(someDate);
        Account income = new Account("614000");
        income.setDescription("");
        income.setDetermination(AccountDetermination.GENERAL_LEDGER);
        income.setType(AccountType.ANALITICKI);
        income.setVersion(1L);
        incomeCredit.setAccount(income);
        incomeCredit.setAmount(ChangeType.CREDIT, BigDecimal.valueOf(100));
        incomeCredit.setDocument("1/2016");
        incomeCredit.setInternalDocument("");
        incomeCredit.setDesc(new Description(1));
        incomeCredit.setDetermination(AccountDetermination.GENERAL_LEDGER);
        incomeCredit.setOrgUnit(new OrgUnit(1));
        incomeCredit.setPartner(null);
        incomeCredit.setUser(new ApplicationUser(1));
        journalEntry.addItem(incomeCredit);
        //**********************************************************************
        
        expect(EM.find(
                JournalEntry.class, 
                new JournalEntryPK(clientId, typeId, journalEntryNumber), 
                LockModeType.OPTIMISTIC_FORCE_INCREMENT))
                .andReturn(journalEntry);
        expect(validator.validate(EasyMock.anyObject()))
                .andStubReturn(Collections.emptySet());
        Capture<Analytical> analyticalCapture = EasyMock.newCapture();
        EM.persist(capture(analyticalCapture));
        Capture<GeneralLedger> vatCapture = EasyMock.newCapture();
        EM.persist(capture(vatCapture));
        Capture<GeneralLedger> incomeCapture = EasyMock.newCapture();
        EM.persist(capture(incomeCapture));
        Capture<GeneralLedger> zbirnaStavkaCapture = EasyMock.newCapture();
        EM.persist(capture(zbirnaStavkaCapture));
        
        mocks.replay();
        service.recordJournalEntry(clientId, typeId, journalEntryNumber, 1L);
        mocks.verify();
        //create analytical 
        Analytical partnerAnalytical = analyticalCapture.getValue();
        Assert.assertEquals("204000", partnerAnalytical.getAccountNumber());
        Assert.assertEquals(Integer.valueOf(1), partnerAnalytical.getPartner().getId());
        Assert.assertEquals(0, BigDecimal.valueOf(120).compareTo(partnerAnalytical.getDebit()));
        
        GeneralLedger VATLedger = vatCapture.getValue();
        Assert.assertEquals("470000", VATLedger.getAccountNumber());
        Assert.assertEquals(0, BigDecimal.valueOf(20).compareTo(VATLedger.getAmount()));
        Assert.assertEquals(ChangeType.CREDIT, VATLedger.getChangeType());
        
        GeneralLedger incomeLedger = incomeCapture.getValue();
        Assert.assertEquals("614000", incomeLedger.getAccountNumber());
        Assert.assertEquals(0, BigDecimal.valueOf(100).compareTo(incomeLedger.getAmount()));
        Assert.assertEquals(ChangeType.CREDIT, incomeLedger.getChangeType());
        
        GeneralLedger zbirnaStavka = zbirnaStavkaCapture.getValue();
        Assert.assertEquals("204000", zbirnaStavka.getAccountNumber());
        Assert.assertEquals(0, BigDecimal.valueOf(120).compareTo(zbirnaStavka.getAmount()));
        Assert.assertEquals(ChangeType.DEBIT, zbirnaStavka.getChangeType());
        
        
    }
    
}
