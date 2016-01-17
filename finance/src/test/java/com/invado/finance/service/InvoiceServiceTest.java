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
import com.invado.core.dto.InvoiceDTO;
import com.invado.core.dto.InvoiceItemDTO;
import java.math.BigDecimal;
import java.security.Permissions;
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
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import org.easymock.IMocksControl;
import org.easymock.internal.EasyMockProperties;
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
}
