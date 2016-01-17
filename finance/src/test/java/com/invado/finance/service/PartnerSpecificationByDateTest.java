/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.BusinessPartner_;
import com.invado.core.domain.Client;
import com.invado.core.domain.Client_;
import com.invado.core.domain.OrgUnit;
import com.invado.core.domain.OrgUnit_;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.AccountDetermination;
import com.invado.finance.domain.journal_entry.AccountType;
import com.invado.finance.domain.journal_entry.Account_;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.Analytical_;
import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.domain.journal_entry.JournalEntryType;
import com.invado.finance.service.dto.PartnerSpecificationDTO;
import com.invado.finance.service.dto.ReadSpecificationsDTO;
import com.invado.finance.service.dto.RequestPartnerSpecificationDTO;
import com.invado.finance.service.dto.StavkaSpecifikacijeDTO;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
public class PartnerSpecificationByDateTest {

    @Inject
    private IMocksControl mocks;
    @Inject
    @Named("typedQueryMocksControl")
    private TypedQuery<Analytical> query;
    @Inject
    @Named("entityManagerMocksControl")
    private EntityManager EM;
    @Inject
    private PartnerSpecificationByDate service;

    @Before
    public void before() {
        service.dao = EM;
    }

    @Test
    public void testReadPartnerSpecification() throws Exception {
        Integer clientId = 1;        

        Client client = new Client(clientId);
        client.setName("Test");
        Account account = new Account("202000",
                "Customer",
                AccountDetermination.CUSTOMERS,
                AccountType.ANALITICKI);
        OrgUnit orgUnit = new OrgUnit(
                client,
                1,
                "",
                "Test org. unit",
                "place",
                "street",
                null,
                1L);
        JournalEntryType journalEntryType = new JournalEntryType(client.getId(), 98);
        BusinessPartner businessPartner = new BusinessPartner("1242351");
        Analytical analytics1 = new Analytical();
        analytics1.setAccount(account);
        analytics1.setOrgUnit(orgUnit);
        analytics1.setPartner(businessPartner);
        analytics1.setJournalEntryType(journalEntryType);
        analytics1.setAmount(ChangeType.CREDIT, BigDecimal.valueOf(150));

        Analytical analytics2 = new Analytical();
        analytics2.setAccount(account);
        analytics2.setOrgUnit(orgUnit);
        analytics2.setPartner(businessPartner);
        analytics2.setJournalEntryType(journalEntryType);
        analytics2.setAmount(ChangeType.DEBIT, BigDecimal.valueOf(200));

        ApplicationSetup appSetup = new ApplicationSetup(1);
        appSetup.setApplicationVersion(ApplicationSetup.ApplicationVersion.COMPANY_ACCOUNTING);
        appSetup.setPageSize(35);
        appSetup.setYear(2012);

        CriteriaBuilder cb = mocks.createMock(CriteriaBuilder.class);
        CriteriaQuery<Analytical> cQuery = mocks.createMock(CriteriaQuery.class);
        Root<Analytical> root = mocks.createMock(Root.class);
        Join<Analytical, OrgUnit> orgUnitJoin = mocks.createMock(Join.class);
        ParameterExpression param = mocks.createMock(ParameterExpression.class);
        Order order = mocks.createMock(Order.class);
        Predicate predicate = mocks.createMock(Predicate.class);
        Path path = mocks.createMock(Path.class);

        expect(EM.find(Client.class, clientId)).andStubReturn(client);
        expect(EM.find(ApplicationSetup.class, 1))
                .andStubReturn(appSetup);
        expect(EM.getCriteriaBuilder()).andReturn(cb);
        expect(cb.createQuery(Analytical.class))
                .andReturn(cQuery);
        expect(cQuery.from(Analytical.class)).andReturn(root);
        expect(cQuery.select(root)).andReturn(cQuery);
        expect(root.join(Analytical_.orgUnit, JoinType.LEFT))
                .andReturn(orgUnitJoin);
        expect(cb.conjunction()).andReturn(predicate);
        expect(orgUnitJoin.get(OrgUnit_.client)).andReturn(path);
        expect(path.get(Client_.id)).andReturn(path);
        expect(cb.parameter(Integer.class, "client")).andReturn(param);
        expect(cb.equal(path, param)).andReturn(predicate);
        expect(cb.and(predicate, predicate)).andReturn(predicate);
        expect(cQuery.where(predicate)).andReturn(cQuery);

        expect(root.get(Analytical_.account)).andReturn(path);
        expect(path.get(Account_.number)).andReturn(path);
        expect(cb.asc(path)).andReturn(order);
        expect(root.get(Analytical_.orgUnit)).andReturn(path);
        expect(path.get(OrgUnit_.id)).andReturn(path);
        expect(cb.asc(path)).andReturn(order);
        expect(root.get(Analytical_.partner)).andReturn(path);
        expect(path.get(BusinessPartner_.companyIdNumber)).andReturn(path);
        expect(cb.asc(path)).andReturn(order);
        expect(cQuery.orderBy(order, order, order)).andReturn(cQuery);
        expect(EM.createQuery(cQuery)).andReturn(query);
        expect(query.setParameter("client", clientId)).andReturn(query);
        expect(query.getResultList()).andReturn(
                Arrays.asList(analytics1, analytics2)
        );

        mocks.replay();
        RequestPartnerSpecificationDTO request = new RequestPartnerSpecificationDTO();
        request.setClientID(clientId);
        ReadSpecificationsDTO dto = service.readPartnerSpecificationByDate(request);
        mocks.verify();

        Assert.assertEquals(Integer.valueOf(35), dto.pageSize);
        Assert.assertEquals(Integer.valueOf(0), dto.numberOfPages);
        Assert.assertEquals((int) 1, dto.specifications.size());
        PartnerSpecificationDTO item = dto.specifications.get(0);
        Assert.assertEquals(0, BigDecimal.valueOf(200).compareTo(item.debitTotal));
        Assert.assertEquals(0, BigDecimal.valueOf(150).compareTo(item.creditTotal));
        Assert.assertEquals(0, BigDecimal.valueOf(50).compareTo(item.balanceTotal));
        Assert.assertEquals((int) 1, item.items.size());

        StavkaSpecifikacijeDTO item1 = item.items.get(0);
        Assert.assertEquals(0, BigDecimal.valueOf(50).compareTo(item1.balance));
        Assert.assertEquals(0, BigDecimal.valueOf(150).compareTo(item1.credit));
        Assert.assertEquals(0, BigDecimal.valueOf(200).compareTo(item1.debit));
        Assert.assertEquals("202000", item1.sifraKonta);
        Assert.assertEquals("1242351", item1.businessPartnerRegNo);
        Assert.assertEquals(Integer.valueOf(1), item1.idOrgJedinice);
    }

}
