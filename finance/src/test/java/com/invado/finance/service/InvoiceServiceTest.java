/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.Article;
import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.Currency;
import com.invado.core.domain.Invoice;
import com.invado.core.domain.InvoiceBusinessPartner;
import com.invado.core.domain.InvoiceItem;
import com.invado.core.domain.InvoiceItemPK;
import com.invado.core.domain.InvoicePK;
import com.invado.core.domain.InvoiceType;
import com.invado.core.domain.OrgUnit;
import com.invado.core.domain.Properties;
import com.invado.core.domain.VatPercent;
import com.invado.core.dto.InvoiceItemDTO;
import com.invado.finance.service.dto.InvoiceReportDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.validation.Validator;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author dbobic
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/unit-test.xml"})
public class InvoiceServiceTest {
    
    @Inject
    private IMocksControl mocks; 
    @Inject
    @Named("typedQueryMocksControl")
    private TypedQuery<ApplicationUser> appUserQuery;
    @Inject
    @Named("typedQueryMocksControl")
    private TypedQuery<Integer> maxOrdinalNumberQuery;
    @Inject
    @Named("entityManagerMocksControl")
    private EntityManager EM;
    @Inject
    @Named("validatorMocksControl")
    private Validator validator;    
    @Inject
    private InvoiceService service;
    
    @Before
    public void before() {
        service.dao = EM;
        mocks.reset();
    }
    
    @Test
    public void testAddItem() throws Exception {
        InvoiceItemDTO dto = new InvoiceItemDTO();
        dto.setClientId(1);
        dto.setUnitId(3);
        dto.setInvoiceDocument( "document" );
        dto.setOrdinal(1);
        dto.setVATPercent(new BigDecimal("0.18"));
        dto.setNetPrice( new BigDecimal("100") );
        dto.setQuantity( new BigDecimal("2") );
        dto.setRabatPercent( BigDecimal.ZERO );
        dto.setArticleCode("1");
        dto.setTotalCost( new BigDecimal("118") );
        dto.setUsername("bdragan");
        dto.setPassword( new char[]{'d', 'r', 'a', 'g'} );
        dto.setInvoiceVersion( 1L );
        Article articleCode = new Article(dto.getArticleCode());
        articleCode.setDescription("desc");
        articleCode.setUnitOfMeasureCode("kom");
        articleCode.setVATRate(VatPercent.GENERAL_RATE);
        articleCode.setVersion(1L);

        OrgUnit orgUnit = new OrgUnit(dto.getUnitId());
        Invoice invoice = new Invoice(orgUnit, dto.getInvoiceDocument());
        invoice.setCreditRelationDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setInvoiceDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setValueDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setPaid(Boolean.FALSE);
        invoice.setPartner(new BusinessPartner("23131"));
        invoice.setInvoiceType(InvoiceType.INVOICE);
        invoice.setPartnerType(InvoiceBusinessPartner.DOMESTIC);
        invoice.setPrinted(Boolean.FALSE);
        invoice.setRecorded(Boolean.FALSE);
        invoice.setIsDomesticCurrency(Boolean.TRUE);
        invoice.setCurrency(new Currency("RSD"));
        invoice.setUser(new ApplicationUser("bdragan", new char[]{'p', 'a', 's', 's'}));
        invoice.setBank(new BankCreditor(1));
        invoice.setVersion(1L);
        Properties prop = new Properties();
        prop.setValue("0.18");

        expect(EM.find(Invoice.class, new InvoicePK(dto.getClientId(), 
                                                    dto.getUnitId(), 
                                                    dto.getInvoiceDocument()))
        ).andReturn(invoice);
        expect(EM.createNamedQuery(
                ApplicationUser.READ_BY_USERNAME,
                ApplicationUser.class)).andReturn(appUserQuery);
        expect(appUserQuery.setParameter(1, dto.getUsername())).andReturn(appUserQuery);
        expect(appUserQuery.getResultList()).andReturn(
                Arrays.asList(new ApplicationUser()));
        expect(EM.find(OrgUnit.class, dto.getUnitId())).andReturn(orgUnit);
        EM.lock(invoice, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        expect(EM.find(Article.class, dto.getArticleCode())).andReturn(articleCode);
        expect(EM.createNamedQuery("Invoice.GetMaxOrdinalNumber", Integer.class))
                .andReturn(maxOrdinalNumberQuery);
        expect(maxOrdinalNumberQuery.setParameter("document", dto.getInvoiceDocument()))
                .andReturn(maxOrdinalNumberQuery);
        expect(maxOrdinalNumberQuery.setParameter("orgUnit", orgUnit))
                .andReturn(maxOrdinalNumberQuery);
        expect(maxOrdinalNumberQuery.getSingleResult()).andReturn(null);                
        expect(EM.find(Properties.class, "vat_general_rate")).andReturn(prop);
       expect(validator.validateProperty(EasyMock.anyObject(), EasyMock.anyString()))
                .andStubReturn(Collections.emptySet());
        expect(validator.validate(EasyMock.anyObject()))
                .andStubReturn(Collections.emptySet());
        
        mocks.replay();
        service.addItem(dto);
        mocks.verify();

        Assert.assertEquals((int) 1, invoice.getItemsSize());
        InvoiceItem item1 = invoice.getUnmodifiableItemsSet().get(0);
        Assert.assertEquals(item1.getOrdinal(), dto.getOrdinal());
        Assert.assertEquals(0, item1.getVatPercent().compareTo(dto.getVATPercent()));
        Assert.assertEquals(0, item1.getNetPrice().compareTo(dto.getNetPrice()));
        Assert.assertEquals(0, item1.getQuantity().compareTo(dto.getQuantity()));
        Assert.assertEquals(0, item1.getRebatePercent().compareTo(dto.getRabatPercent()));
        BigDecimal net = dto.getNetPrice().subtract(dto.getNetPrice().multiply(dto.getRabatPercent()));
        BigDecimal total = (net.multiply(BigDecimal.ONE.add(new BigDecimal(prop.getValue()))))
                .multiply(dto.getQuantity());
        Assert.assertEquals(0, item1.getTotalCost().compareTo(total));
        Assert.assertEquals(item1.getItemCode(), dto.getArticleCode());
    }
    
    @Test
    public void testUpdateItem() throws Exception {
        InvoiceItemDTO dto = new InvoiceItemDTO();
        dto.setClientId(1);
        dto.setUnitId(3);
        dto.setInvoiceDocument( "document" );
        dto.setOrdinal(1);
        dto.setVATPercent(new BigDecimal("0.18"));
        dto.setNetPrice( new BigDecimal("100") );
        dto.setQuantity( new BigDecimal("2") );
        dto.setRabatPercent( BigDecimal.ZERO );
        dto.setArticleCode("1");
        dto.setTotalCost( new BigDecimal("118") );
        dto.setUsername("bdragan");
        dto.setPassword( new char[]{'d', 'r', 'a', 'g'} );
        dto.setInvoiceVersion( 1L );
        
        OrgUnit orgUnit = new OrgUnit(dto.getUnitId());
        Invoice invoice = new Invoice(orgUnit, dto.getInvoiceDocument());
        invoice.setCreditRelationDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setInvoiceDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setValueDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setPaid(Boolean.FALSE);
        invoice.setPartner(new BusinessPartner("23131"));
        invoice.setInvoiceType(InvoiceType.INVOICE);
        invoice.setPartnerType(InvoiceBusinessPartner.DOMESTIC);
        invoice.setPrinted(Boolean.FALSE);
        invoice.setRecorded(Boolean.FALSE);
        invoice.setIsDomesticCurrency(Boolean.TRUE);
        invoice.setCurrency(new Currency("RSD"));
        invoice.setUser(new ApplicationUser("bdragan", new char[]{'p', 'a', 's', 's'}));
        invoice.setBank(new BankCreditor(1));
        invoice.setVersion(1L);

        InvoiceItem item = new InvoiceItem(invoice, 1);
        item.setNetPrice(BigDecimal.valueOf(100));
        item.setQuantity(BigDecimal.ONE);
        item.setArticle(new Article("1"));
        item.setItemDescription("description");
        item.setUnitOfMeasure("KG");
        item.setRabatPercent(BigDecimal.ZERO);
        item.setArticleVAT(VatPercent.GENERAL_RATE);
        item.setVatPercent(BigDecimal.valueOf(0.18));
        item.setTotalCost(BigDecimal.valueOf(118));
        invoice.addItem(item);
        Properties prop = new Properties();
        prop.setValue("0.18");

        expect(EM.find(Invoice.class, 
                new InvoicePK(dto.getClientId(), dto.getUnitId(), dto.getInvoiceDocument())))
                .andReturn(invoice);
        expect(EM.createNamedQuery(
                ApplicationUser.READ_BY_USERNAME,
                ApplicationUser.class))
                .andReturn(appUserQuery);
        expect(appUserQuery.setParameter(1, dto.getUsername())).andReturn(appUserQuery);
        expect(appUserQuery.getResultList()).andReturn(Arrays.asList(new ApplicationUser()));
        EM.lock(invoice, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        expect(EM.find(InvoiceItem.class, new InvoiceItemPK(
                dto.getClientId(),
                dto.getUnitId(),
                dto.getInvoiceDocument(), 
                dto.getOrdinal())))
                .andReturn(item);
        expect(EM.find(Properties.class, "vat_general_rate")).andReturn(prop);
        expect(validator.validateProperty(EasyMock.anyObject(), EasyMock.anyString()))
                .andStubReturn(Collections.emptySet());
        expect(validator.validate(EasyMock.anyObject()))
                .andStubReturn(Collections.emptySet());
        
        mocks.replay();
        service.updateItem(dto);
        mocks.verify();

        Assert.assertEquals((int) 1, invoice.getItemsSize());
        InvoiceItem item1 = invoice.getUnmodifiableItemsSet().get(0);
        Assert.assertEquals(item1.getOrdinal(), dto.getOrdinal());
        Assert.assertEquals(0, item1.getVatPercent().compareTo(dto.getVATPercent()));
        Assert.assertEquals(0, item1.getNetPrice().compareTo(dto.getNetPrice()));
        Assert.assertEquals(0, item1.getQuantity().compareTo(dto.getQuantity()));
        Assert.assertEquals(0, item1.getRebatePercent().compareTo(dto.getRabatPercent()));
        BigDecimal net = dto.getNetPrice().subtract(dto.getNetPrice().multiply(dto.getRabatPercent()));
        BigDecimal total = (net.multiply(BigDecimal.ONE.add(new BigDecimal(prop.getValue()))))
                .multiply(dto.getQuantity());
        Assert.assertEquals(0, item1.getTotalCost().compareTo(total));
    }
    
    @Test
    public void testReadReport() throws Exception {
        Integer clientId = 1;
        Integer orgUnitId = 3;
        String document = "12348/4";
        Properties generalRate= new Properties("vat_general_rate");
        generalRate.setValue("0.18");
        Properties lowRate= new Properties("vat_general_rate");
        lowRate.setValue("0.08");
        OrgUnit orgUnit = new OrgUnit(orgUnitId);
        orgUnit.setClient(new Client(clientId));
        Invoice invoice = new Invoice(orgUnit, document);
        invoice.setCreditRelationDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setInvoiceDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setValueDate(LocalDate.of(2011, Month.JANUARY, 1));
        invoice.setPaid(Boolean.FALSE);
        invoice.setPartner(new BusinessPartner("1009202"));
        invoice.setInvoiceType(InvoiceType.INVOICE);
        invoice.setPartnerType(InvoiceBusinessPartner.DOMESTIC);
        invoice.setPrinted(Boolean.FALSE);
        invoice.setRecorded(Boolean.FALSE);
        invoice.setUser(new ApplicationUser("bdragan", new char[]{'p','a','s','s'}));
        invoice.setVersion(1L);
        invoice.setContractNumber("032-05/12");
        invoice.setContractDate(LocalDate.of(2010, Month.JANUARY, 1));
        Currency currency = new Currency("RSD");
        invoice.setCurrency(currency);
        BankCreditor bank = new BankCreditor(1);
        bank.setName("EFG");
        bank.setAccount("1234567");
        invoice.setBank(bank);
        Article article = new Article("1");
        article.setDescription("Article");
        article.setSalePriceWithVAT(BigDecimal.ONE);
        article.setUnitOfMeasureCode("KOM");
        article.setUpdated(LocalDate.of(2014, Month.JANUARY, 1));
        article.setLastUpdateBy(new ApplicationUser(1));
        article.setVATRate(VatPercent.GENERAL_RATE);
        article.setVersion(1L);

        InvoiceItem item1 = new InvoiceItem(invoice, 1);
        item1.setNetPrice(BigDecimal.valueOf(100));
        item1.setQuantity(BigDecimal.ONE);
        item1.setArticle(article);
        item1.setItemDescription(article.getDescription());
        item1.setUnitOfMeasure(article.getUnitOfMeasureCode());
        item1.setRabatPercent(BigDecimal.ZERO);
        item1.setArticleVAT(VatPercent.GENERAL_RATE);
        item1.setVatPercent(BigDecimal.valueOf(0.18));
        item1.setTotalCost(BigDecimal.valueOf(118));
        invoice.addItem(item1);

        InvoiceItem item2 = new InvoiceItem(invoice, 2);
        item2.setNetPrice(BigDecimal.valueOf(40));
        item2.setQuantity(BigDecimal.TEN);
         item2.setArticle(article);
        item2.setItemDescription(article.getDescription());
        item2.setUnitOfMeasure(article.getUnitOfMeasureCode());
        item2.setRabatPercent(BigDecimal.valueOf(0.05));
        item2.setArticleVAT(VatPercent.GENERAL_RATE);
        item2.setVatPercent(BigDecimal.valueOf(0.18));
        item2.setTotalCost(BigDecimal.valueOf(448.4));
        invoice.addItem(item2);

        expect(EM.find(Invoice.class, new InvoicePK(clientId, orgUnitId, document)))
                .andReturn(invoice);
        expect(EM.find(Client.class, invoice.getClientId())).andReturn(new Client(1));
        expect(EM.find(Properties.class, "vat_general_rate")).andReturn(generalRate);
        expect(EM.find(Properties.class, "vat_low_rate")).andReturn(lowRate);
        
        mocks.replay();
        InvoiceReportDTO actual = service.readInvoiceReport(
                clientId, 
                orgUnitId, 
                document);
        mocks.verify();

        Assert.assertEquals(invoice.getDocument(),actual.document);
        Assert.assertEquals(invoice.getInvoiceDate(), actual.invoiceDate);
        Assert.assertEquals(invoice.getCreditRelationDate(),actual.creditRelationDate);
        Assert.assertEquals(invoice.getValueDate(), actual.valueDate);
        Assert.assertEquals(Boolean.FALSE, actual.printed);
        Assert.assertEquals(Boolean.TRUE, invoice.isPrinted());
        Assert.assertEquals( invoice.getVersion(), actual.version);
        Assert.assertEquals( invoice.getCurrencyISOCode(), actual.currencyISOCode);
        Assert.assertEquals( invoice.getCurrencyDescription(), actual.currencyDesc);
        Assert.assertEquals( invoice.getContractNumber(), actual.contractNumber);
        Assert.assertEquals( invoice.getContractDate(), actual.contractDate);
        Assert.assertEquals( invoice.getBankName(), actual.bankName);
        Assert.assertEquals( invoice.getBankAccountNumber(), actual.bankAccount);

        Assert.assertEquals(2, actual.items.size());
        InvoiceReportDTO.Item actualItem1 = actual.items.get(0);
        Assert.assertEquals(item1.getVatPercent(), actualItem1.VATPercent);
        Assert.assertEquals(item1.getOrdinal(), actualItem1.ordinal);
        Assert.assertEquals(item1.getQuantity(), actualItem1.quantity);
        Assert.assertEquals(item1.getNetPrice(), actualItem1.netPrice);
        Assert.assertEquals(item1.getItemDescription(), actualItem1.serviceDesc);
        Assert.assertEquals(item1.getTotalCost(), actualItem1.itemTotal);
        Assert.assertEquals(item1.getRebatePercent(), actualItem1.rebatePercent);
        InvoiceReportDTO.Item actualItem2 = actual.items.get(1);
        Assert.assertEquals(item2.getVatPercent(), actualItem2.VATPercent);
        Assert.assertEquals(item2.getOrdinal(), actualItem2.ordinal);
        Assert.assertEquals(item2.getQuantity(), actualItem2.quantity);
        Assert.assertEquals(item2.getNetPrice(), actualItem2.netPrice);
        Assert.assertEquals(item2.getItemDescription(), actualItem2.serviceDesc);
        Assert.assertEquals(item2.getTotalCost(), actualItem2.itemTotal);
        Assert.assertEquals(item2.getRebatePercent(), actualItem2.rebatePercent);
        Assert.assertEquals(0, BigDecimal.valueOf(20).compareTo(actual.rebateTotal));
        Assert.assertEquals(0, BigDecimal.valueOf(500).compareTo(actual.netPriceTotal));
        Assert.assertEquals(0, BigDecimal.valueOf(566.4).compareTo(actual.invoiceTotalAmount));
        Assert.assertEquals(0, BigDecimal.valueOf(480).compareTo(actual.generalRateBasis));
        Assert.assertEquals(0, BigDecimal.valueOf(86.4).compareTo(actual.generalRateTax));
    }
}
