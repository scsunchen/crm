/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.Article;
import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.Currency;
import com.invado.core.domain.ExchangeRate;
import com.invado.core.domain.ExchangeRatePK;
import com.invado.core.domain.Invoice;
import com.invado.core.domain.InvoiceBusinessPartner;
import com.invado.core.domain.InvoiceItem;
import com.invado.core.domain.InvoicePK;
import com.invado.core.domain.InvoiceType;
import com.invado.core.domain.OrgUnit;
import com.invado.core.domain.VatPercent;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.domain.journal_entry.Description;
import com.invado.finance.domain.journal_entry.JournalEntry;
import com.invado.finance.domain.journal_entry.JournalEntryItem;
import com.invado.finance.domain.journal_entry.JournalEntryPK;
import com.invado.finance.domain.journal_entry.JournalEntryType;
import com.invado.finance.domain.journal_entry.JournalEntryTypePK;
import com.invado.finance.domain.journal_entry.RecordInvoiceAccount;
import com.invado.finance.service.dto.RequestInvoiceRecordingDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
public class RecordInvoiceServiceTest {

    @Inject
    private IMocksControl mocks;
    @Inject
    @Named("entityManagerMocksControl")
    private EntityManager EM;
    @Inject
    @Named("typedQueryMocksControl")
    private TypedQuery<ApplicationUser> userQuery;
    @Inject
    @Named("validatorMocksControl")
    private Validator validator;
    @Inject
    private RecordInvoiceService service;

    @Before
    public void before() {
        service.EM = EM;
        mocks.reset();
    }

    @Test
    public void testRecordDomesticPartnerInvoice() throws Exception {
        RequestInvoiceRecordingDTO dto = new RequestInvoiceRecordingDTO();
        dto.setClientId(1);
        dto.setOrgUnitId(1);
        dto.setDocument("1/2016");
        dto.setJournalEntryNumber(1);
        dto.setJournalEntryTypeNumber(1);
        dto.setEntryOrderType(1);
        dto.setRecordDate(LocalDate.of(2011, Month.JANUARY, 1));

        OrgUnit orgUnit = new OrgUnit(dto.getOrgUnitId());
        orgUnit.setClient(new Client(dto.getClientId()));
        Invoice invoice = new Invoice(orgUnit, dto.getDocument());
        invoice.setCreditRelationDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setInvoiceDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setValueDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setPaid(Boolean.FALSE);
        BusinessPartner partner = new BusinessPartner("23131");
        invoice.setPartner(partner);
        invoice.setInvoiceType(InvoiceType.INVOICE);
        invoice.setPartnerType(InvoiceBusinessPartner.DOMESTIC);
        invoice.setPrinted(Boolean.FALSE);
        invoice.setRecorded(Boolean.FALSE);
        invoice.setIsDomesticCurrency(Boolean.TRUE);
        invoice.setCurrency(new Currency("RSD"));
        invoice.setUser(new ApplicationUser("bdragan", new char[]{'p', 'a', 's', 's'}));
        invoice.setBank(new BankCreditor(1));
        invoice.setVersion(1L);
        Article item = new Article("1");
        InvoiceItem invoiceItem = new InvoiceItem(invoice, 1);
        invoiceItem.setArticle(item);
        invoiceItem.setArticleVAT(VatPercent.GENERAL_RATE);
        invoiceItem.setItemDescription("");
        invoiceItem.setNetPrice(BigDecimal.valueOf(150));
        invoiceItem.setQuantity(BigDecimal.ONE);
        invoiceItem.setRabatPercent(BigDecimal.valueOf(0.05));
        invoiceItem.setVatPercent(new BigDecimal("0.2"));
        BigDecimal net = invoiceItem.getNetPrice().subtract(
                invoiceItem.getNetPrice().multiply(invoiceItem.getRabatPercent()));
        BigDecimal total = (net.multiply(BigDecimal.ONE.add(invoiceItem.getVatPercent())))
                .multiply(invoiceItem.getQuantity());
        invoiceItem.setTotalCost(total);
        invoice.addItem(invoiceItem);

        expect(EM.find(Invoice.class, new InvoicePK(dto.getClientId(), dto.getOrgUnitId(), dto.getDocument())))
                .andReturn(invoice);
        ApplicationSetup setup = new ApplicationSetup(1);
        setup.setYear(2011);
        expect(EM.find(ApplicationSetup.class, 1)).andReturn(setup);
        expect(EM.find(JournalEntry.class, new JournalEntryPK(dto.getClientId(),
                dto.getEntryOrderType(),
                dto.getJournalEntryNumber())))
                .andReturn(null);

        expect(EM.find(BusinessPartner.class, invoice.getPartnerID()))
                .andReturn(partner);
        expect(EM.find(OrgUnit.class, dto.getOrgUnitId())).andReturn(orgUnit);
        expect(EM.find(Description.class, dto.getDescription())).andReturn(new Description());
        expect(EM.createNamedQuery(
                ApplicationUser.READ_BY_USERNAME,
                ApplicationUser.class))
                .andReturn(userQuery);
        expect(userQuery.setParameter(1, dto.getUser())).andReturn(userQuery);
        expect(userQuery.getResultList()).andReturn(Arrays.asList(new ApplicationUser()));
        expect(EM.find(JournalEntryType.class,
                new JournalEntryTypePK(dto.getEntryOrderType(), dto.getClientId())))
                .andReturn(new JournalEntryType(dto.getClientId(), dto.getEntryOrderType()));
        expect(EM.find(RecordInvoiceAccount.class, "customer_domestic"))
                .andReturn(new RecordInvoiceAccount("", new Account("204000")));
        expect(EM.find(RecordInvoiceAccount.class, "income_domestic"))
                .andStubReturn(new RecordInvoiceAccount("", new Account("614000")));
        expect(EM.find(RecordInvoiceAccount.class, "vat_general_rate"))
                .andReturn(new RecordInvoiceAccount("", new Account("470000")));
        expect(validator.validate(EasyMock.anyObject()))
                .andStubReturn(Collections.emptySet());
        Capture<JournalEntry> capture = EasyMock.newCapture();
        EM.persist(capture(capture));

        mocks.replay();
        service.perform(dto);
        mocks.verify();

        JournalEntry journalEntry = capture.getValue();
        List<JournalEntryItem> partnerItems = 
                journalEntry.getItems().stream().filter(
                        (JournalEntryItem t) -> t.getAccountNumber().equals("204000")
                ).collect(Collectors.toList());
        Assert.assertEquals(1, partnerItems.size());
        JournalEntryItem partnerItem = partnerItems.get(0);
        Assert.assertEquals(0, BigDecimal.valueOf(171).compareTo(partnerItem.getAmount()));
        Assert.assertEquals(ChangeType.DEBIT, partnerItem.getChangeType());
        List<JournalEntryItem> vatItems = 
                journalEntry.getItems().stream().filter(
                        (JournalEntryItem t) -> t.getAccountNumber().equals("470000")
                ).collect(Collectors.toList());
        Assert.assertEquals(1, vatItems.size());
        JournalEntryItem vatItem = vatItems.get(0);
        Assert.assertEquals(0, BigDecimal.valueOf(28.5).compareTo(vatItem.getAmount()));
        Assert.assertEquals(ChangeType.CREDIT, vatItem.getChangeType());
        List<JournalEntryItem> incomeItems = journalEntry.getItems().stream()
                .filter((JournalEntryItem t) -> 
                        t.getAccountNumber().equals("614000") 
                                & t.getChangeType() == ChangeType.CREDIT
                ).collect(Collectors.toList());
        Assert.assertEquals(1, incomeItems.size());
        Assert.assertEquals(0, BigDecimal.valueOf(150).compareTo(incomeItems.get(0).getAmount()));
        List<JournalEntryItem> rebateItems = journalEntry.getItems().stream()
                .filter((JournalEntryItem t) -> 
                        t.getAccountNumber().equals("614000") 
                                & t.getChangeType() == ChangeType.DEBIT
                ).collect(Collectors.toList());
        Assert.assertEquals(1, rebateItems.size());
        Assert.assertEquals(0, BigDecimal.valueOf(7.5).compareTo(rebateItems.get(0).getAmount()));
    }
    
    @Test
    public void testRecordAbroadPartnerInvoice() throws Exception {
        RequestInvoiceRecordingDTO dto = new RequestInvoiceRecordingDTO();
        dto.setClientId(1);
        dto.setOrgUnitId(1);
        dto.setDocument("1/2016");
        dto.setJournalEntryNumber(1);
        dto.setJournalEntryTypeNumber(1);
        dto.setEntryOrderType(1);
        dto.setRecordDate(LocalDate.of(2011, Month.JANUARY, 1));

        OrgUnit orgUnit = new OrgUnit(dto.getOrgUnitId());
        orgUnit.setClient(new Client(dto.getClientId()));
        Invoice invoice = new Invoice(orgUnit, dto.getDocument());
        invoice.setCreditRelationDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setInvoiceDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setValueDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setPaid(Boolean.FALSE);
        BusinessPartner partner = new BusinessPartner("23131");
        invoice.setPartner(partner);
        invoice.setInvoiceType(InvoiceType.INVOICE);
        invoice.setPartnerType(InvoiceBusinessPartner.ABROAD);
        invoice.setPrinted(Boolean.FALSE);
        invoice.setRecorded(Boolean.FALSE);
        invoice.setIsDomesticCurrency(Boolean.FALSE);
        invoice.setCurrency(new Currency("EUR"));
        invoice.setUser(new ApplicationUser("bdragan", new char[]{'p', 'a', 's', 's'}));
        invoice.setBank(new BankCreditor(1));
        invoice.setVersion(1L);
        Article item = new Article("1");
        InvoiceItem invoiceItem = new InvoiceItem(invoice, 1);
        invoiceItem.setArticle(item);
        invoiceItem.setArticleVAT(VatPercent.GENERAL_RATE);
        invoiceItem.setItemDescription("");
        invoiceItem.setNetPrice(BigDecimal.valueOf(150));
        invoiceItem.setQuantity(BigDecimal.ONE);
        invoiceItem.setRabatPercent(BigDecimal.valueOf(0.05));
        invoiceItem.setVatPercent(BigDecimal.ZERO);
        BigDecimal net = invoiceItem.getNetPrice().subtract(
                invoiceItem.getNetPrice().multiply(invoiceItem.getRabatPercent()));
        BigDecimal total = (net.multiply(BigDecimal.ONE.add(invoiceItem.getVatPercent())))
                .multiply(invoiceItem.getQuantity());
        invoiceItem.setTotalCost(total);
        invoice.addItem(invoiceItem);

        expect(EM.find(Invoice.class, new InvoicePK(
                dto.getClientId(), 
                dto.getOrgUnitId(), 
                dto.getDocument()))
        ).andReturn(invoice);
        ApplicationSetup setup = new ApplicationSetup(1);
        setup.setYear(2011);
        expect(EM.find(ApplicationSetup.class, 1)).andReturn(setup);
        expect(EM.find(JournalEntry.class, new JournalEntryPK(dto.getClientId(),
                dto.getEntryOrderType(),
                dto.getJournalEntryNumber())))
                .andReturn(null);

        expect(EM.find(BusinessPartner.class, invoice.getPartnerID()))
                .andReturn(partner);
        expect(EM.find(OrgUnit.class, dto.getOrgUnitId())).andReturn(orgUnit);
        expect(EM.find(Description.class, dto.getDescription())).andReturn(new Description());
        expect(EM.createNamedQuery(
                ApplicationUser.READ_BY_USERNAME,
                ApplicationUser.class))
                .andReturn(userQuery);
        expect(userQuery.setParameter(1, dto.getUser())).andReturn(userQuery);
        expect(userQuery.getResultList()).andReturn(Arrays.asList(new ApplicationUser()));
        expect(EM.find(JournalEntryType.class,
                new JournalEntryTypePK(dto.getEntryOrderType(), dto.getClientId())))
                .andReturn(new JournalEntryType(dto.getClientId(), dto.getEntryOrderType()));
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, invoice.getCreditRelationDate().getYear());
        calendar.set(Calendar.DAY_OF_YEAR, invoice.getCreditRelationDate().getDayOfYear());
        ExchangeRate rate = new ExchangeRate(calendar.getTime(), null);
        rate.setMiddle(BigDecimal.valueOf(122.83));
        expect(EM.find(ExchangeRate.class,
                new ExchangeRatePK(new Date(calendar.getTimeInMillis()),
                        invoice.getCurrencyISOCode()))).andReturn(rate);
        expect(EM.find(RecordInvoiceAccount.class, "customer_abroad"))
                .andReturn(new RecordInvoiceAccount("", new Account("204000")));
        expect(EM.find(RecordInvoiceAccount.class, "income_abroad"))
                .andStubReturn(new RecordInvoiceAccount("", new Account("614000")));
        expect(EM.find(RecordInvoiceAccount.class, "journal_entry_inequailty"))
                .andStubReturn(new RecordInvoiceAccount("", new Account("0000")));
        expect(validator.validate(EasyMock.anyObject()))
                .andStubReturn(Collections.emptySet());
        Capture<JournalEntry> capture = EasyMock.newCapture();
        EM.persist(capture(capture));

        mocks.replay();
        service.perform(dto);
        mocks.verify();

        JournalEntry journalEntry = capture.getValue();
        List<JournalEntryItem> partnerItems = 
                journalEntry.getItems().stream().filter(
                        (JournalEntryItem t) -> t.getAccountNumber().equals("204000")
                ).collect(Collectors.toList());
        Assert.assertEquals(1, partnerItems.size());
        JournalEntryItem partnerItem = partnerItems.get(0);
        Assert.assertEquals(0, BigDecimal.valueOf(17503.28).compareTo(partnerItem.getAmount()));
        Assert.assertEquals(ChangeType.DEBIT, partnerItem.getChangeType());
        List<JournalEntryItem> incomeItems = journalEntry.getItems().stream()
                .filter((JournalEntryItem t) -> 
                        t.getAccountNumber().equals("614000") 
                                & t.getChangeType() == ChangeType.CREDIT
                ).collect(Collectors.toList());
        Assert.assertEquals(1, incomeItems.size());
        Assert.assertEquals(0, BigDecimal.valueOf(18424.5).compareTo(
                incomeItems.get(0).getAmount())
        );
        List<JournalEntryItem> rebateItems = journalEntry.getItems().stream()
                .filter((JournalEntryItem t) -> 
                        t.getAccountNumber().equals("614000") 
                                & t.getChangeType() == ChangeType.DEBIT
                ).collect(Collectors.toList());
        Assert.assertEquals(1, rebateItems.size());
        System.out.println(rebateItems.get(0).getAmount());
        Assert.assertEquals(0, BigDecimal.valueOf(921.23).compareTo(
                rebateItems.get(0).getAmount()));
    }

}
