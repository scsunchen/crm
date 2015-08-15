/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.finance.service.dto.*;
import com.invado.core.domain.*;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.service.exception.ZeroExchangeRateException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @see rs.sproduct.finance.services.card.ReceivablePayableCard
 * @author Bobic Dragan
 */
@Service
public class ReceivablePayableCard  {
    
    private static final Logger LOG = Logger.getLogger(ReceivablePayableCard.class.getName());
    
    @PersistenceContext(name = "unit")
    private EntityManager dao;
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadLedgerCardsDTO readReceivablePayableCard(RequestLedgerCardDTO dto) 
            throws EntityNotFoundException, 
            ZeroExchangeRateException {
        
         if (dto.getClientID() == null) {
            throw new EntityNotFoundException(getMessage(
                    "LedgerCard.IllegalArgument.ClientId"));
        }
        if (dto.getCurrency() == null) {
            throw new EntityNotFoundException(getMessage(
                    "LedgerCard.IllegalArgument.Currency"));
        }
        if (dto.getCurrency() == CurrencyTypeDTO.FOREIGN
                && dto.getForeignCurrencyISOCode() == null) {
            throw new EntityNotFoundException(getMessage(
                    "LedgerCard.IllegalArgument.CurrencyISOCode"));
        }
        try {
            if (dao.find(Client.class, dto.getClientID()) == null) {
                throw new EntityNotFoundException(
                        getMessage("LedgerCard.ClientNotExists",
                        dto.getClientID()));
            }
            if (dto.getOrgUnitID() != null && dao.find(OrgUnit.class, dto.getOrgUnitID()) == null) {
                throw new EntityNotFoundException(getMessage(
                        "LedgerCard.OrgUnitNotExists", dto.getClientID(), dto.getOrgUnitID()));
            }
            if (dto.getAccountNumber() != null && dto.getAccountNumber().equals("") == false
                    && dao.find(Account.class, dto.getAccountNumber()) == null) {
                throw new EntityNotFoundException(
                        getMessage("LedgerCard.AccountNotExists",
                        dto.getAccountNumber()));
            }
            if (dto.getPartnerRegNo() != null
                    && dto.getPartnerRegNo().equals("") == false
                    && dao.find(BusinessPartner.class, dto.getPartnerRegNo()) == null) {
                throw new EntityNotFoundException(
                        getMessage("LedgerCard.BusinessPartnerNotExists",
                        dto.getPartnerRegNo()));
            }


            ReadLedgerCardsDTO result = null;
            switch (dto.getType()) {
                case CUSTOMER:
                    List<Analytical> customerList =
                            this.readSupplierCustomerLedgerCard(dao, dto, false);
                    result = this.convertToDTO(dao, dto, customerList);
                    break;
                case SUPPLIER:
                    List<Analytical> supplierList =
                            this.readSupplierCustomerLedgerCard(dao, dto, true);
                    result = this.convertToDTO(dao, dto, supplierList);
                    break;
            }
            return result;
        } catch (EntityNotFoundException | ZeroExchangeRateException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        getMessage("LedgerCard.OptimisticLock"));
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        getMessage("LedgerCard.Persistence"));
            }
        } 
    }

    private ReadLedgerCardsDTO convertToDTO(
            EntityManager EM,
            RequestLedgerCardDTO requestDTO,
            List<Analytical> stavke) 
            throws EntityNotFoundException,
            ZeroExchangeRateException {
        List<LedgerCardDTO> resultList = new ArrayList<>();
        List<String> partners = new ArrayList<>();
        Integer pageSize = EM.find(ApplicationSetup.class, 1)
                .getPageSize();
        Integer totalNumberOfPages = 0;

        //group items by business partner registration number
        for (Analytical item : stavke) {
            if (partners.contains(item.getPartnerCompanyID()) == true) {
                continue;
            }
            BigDecimal balanceTotal = BigDecimal.ZERO;
            BigDecimal debitTotal = BigDecimal.ZERO;
            BigDecimal creditTotal = BigDecimal.ZERO;
            List<LedgerCardDTO.AccountTotal> accountTotal =
                    new ArrayList<>();
            LedgerCardDTO dto = this.getLedgerCardDTO(EM, requestDTO,
                    requestDTO.getAccountNumber(),
                    item.getPartner().getId());
            //add items with same business partner registration number
            for (Analytical item1 : stavke) {
                if (item.getPartnerCompanyID().equals(item1.getPartnerCompanyID())) {
                    LedgerCardItemDTO itemDTO = new LedgerCardItemDTO();
                    itemDTO.typeID = item1.getJournalEntryTypeID();
                    itemDTO.journalEntryNumber = item1.getJournalEntryNumber();
                    itemDTO.journalEntryDate = item1.getRecordDate();
                    itemDTO.document = item1.getDocument();
                    itemDTO.creditDebitRelationDate = item1.getCreditDebitRelationDate();
                    itemDTO.orgUnitID = item1.getOrgUnitID();
                    itemDTO.internalDocument = item1.getInternalDocument();
                    itemDTO.accountNumber = item1.getAccountNumber();
                    itemDTO.description = item1.getDescriptionName();
                    itemDTO.ordinal = item1.getJournalEntryItemOrdinalNumber();
                    itemDTO.valueDate = item1.getValueDate();
                    BigDecimal exchangeRate = BigDecimal.ONE;
                    if (requestDTO.getCurrency() == CurrencyTypeDTO.FOREIGN) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(Calendar.YEAR, item1.getCreditDebitRelationDate().getYear());
                        calendar.set(Calendar.DAY_OF_YEAR, item1.getCreditDebitRelationDate().getDayOfYear());
                        ExchangeRate exRate = EM.find(ExchangeRate.class,
                                new ExchangeRatePK(new Date(calendar.getTimeInMillis()),
                                requestDTO.getForeignCurrencyISOCode()));
                        if (exRate == null) {
                            throw new EntityNotFoundException(
                                    getMessage("LedgerCard.ExchangeRateNotExists",
                                    requestDTO.getForeignCurrencyISOCode(),
                                    item1.getCreditDebitRelationDate()));
                        } else {
                            //pitaj za srednji kurs
                            exchangeRate = exRate.getMiddle();
                        }
                    }
                    if(exchangeRate.compareTo(BigDecimal.ZERO) == 0) {
                        throw new ZeroExchangeRateException(
                                getMessage(
                                "LedgerCard.ZeroExchangeRateException",
                                requestDTO.getForeignCurrencyISOCode(),
                                item1.getCreditDebitRelationDate()));
                    }
                    BigDecimal amount = item1.getAmount().divide(exchangeRate,
                            2,
                            RoundingMode.HALF_UP);
                    switch (item1.getChangeType()) {
                        case DEBIT:
                            itemDTO.debit = amount;//item1.getAmount();
                            itemDTO.credit = BigDecimal.ZERO;
                            balanceTotal = balanceTotal.add(amount);//item1.getAmount());
                            debitTotal = debitTotal.add(amount);//item1.getAmount());
                            break;
                        case CREDIT:
                            itemDTO.debit = BigDecimal.ZERO;
                            itemDTO.credit = amount;//item1.getAmount();
                            balanceTotal = balanceTotal.subtract(amount);//item1.getAmount());
                            creditTotal = creditTotal.add(amount);//item1.getAmount());
                            break;
                    }
                    itemDTO.balance = new BigDecimal(balanceTotal.toString());
                    // sum all account entries 
                    this.addAccountTotal(accountTotal,
                            item1.getAccountNumber(),
                            amount,//item1.getAmount(),
                            item1.getChangeType());
                    dto.items.add(itemDTO);
                }
            }
            //add total(summary) number of items to ledger card number of items 
            Integer items = accountTotal.size() + dto.items.size() + 1;
            Integer numberOfPages = ((items != 0 && items % pageSize == 0)
                    ? items / pageSize
                    : items / pageSize + 1);
            dto.numberOfPages = numberOfPages;
            totalNumberOfPages = totalNumberOfPages + numberOfPages;
            dto.accountTotalItems = accountTotal;
            dto.debitTotal = debitTotal;
            dto.creditTotal = creditTotal;
            dto.balanceTotal = debitTotal.subtract(creditTotal);
            partners.add(item.getPartnerCompanyID());
            resultList.add(dto);
        }
        ReadLedgerCardsDTO result = new ReadLedgerCardsDTO();
        result.pageSize = pageSize;
        result.ledgerCards = resultList;
        //number of pages for all reports must be zero based so subtract 1
        result.numberOfPages = totalNumberOfPages - 1;
        return result;
    }

    private void addAccountTotal(List<LedgerCardDTO.AccountTotal> accountTotal,
            String account,
            BigDecimal amount,
            ChangeType type) {
        //ako je total za konto vec unet azuriraj iznose
        for (LedgerCardDTO.AccountTotal total1 : accountTotal) {
            if (total1.accountCode.equals(account) == true) {
                switch (type) {
                    case DEBIT:
                        total1.debit = total1.debit.add(amount);
                        total1.balance = total1.balance.add(amount);
                        break;
                    case CREDIT:
                        total1.credit = total1.credit.add(amount);
                        total1.balance = total1.balance.subtract(amount);
                        break;
                }
                return;
            }
        }

        LedgerCardDTO.AccountTotal total = new LedgerCardDTO.AccountTotal();
        total.accountCode = account;
        switch (type) {
            case DEBIT:
                total.debit = amount;
                total.credit = BigDecimal.ZERO;
                total.balance = BigDecimal.ZERO.add(amount);
                break;
            case CREDIT:
                total.debit = BigDecimal.ZERO;
                total.credit = amount;
                total.balance = BigDecimal.ZERO.subtract(amount);
                break;
        }
        accountTotal.add(total);
    }

    private LedgerCardDTO getLedgerCardDTO(
            EntityManager dao,
            RequestLedgerCardDTO requestDTO,
            String accountNumber,
            Integer partnerID) {
        LedgerCardDTO dto = new LedgerCardDTO();
        dto.clientName = dao.find(Client.class,requestDTO.getClientID()).getName();
        if (requestDTO.getOrgUnitID() != null) {
            OrgUnit orgUnit = dao.find(OrgUnit.class, requestDTO.getOrgUnitID());
            dto.orgUnitID = orgUnit.getId();
            dto.orgUnitName = orgUnit.getName();
        }
        if (accountNumber != null && accountNumber.isEmpty() == false) {
            Account account = dao.find(Account.class, accountNumber);
            dto.accountNumber = account.getNumber();
            dto.accountName = account.getDescription();
        }
        if (partnerID != null ) {
            BusinessPartner partner = dao.find(BusinessPartner.class, partnerID);
            dto.partnerRegistrationNumber = partner.getCompanyIdNumber();
            dto.partnerName = partner.getName();
            dto.partnerPlace = partner.getPlace();
            dto.partnerStreet = partner.getStreet();
            dto.partnerAccount = partner.getCurrentAccount();
        }
        if (requestDTO.getStatus() != null) {
            switch (requestDTO.getStatus()) {
                case OPEN_ITEMS:
                    dto.status = getMessage(
                            "LedgerCard.OpenStatements");
                    break;
                case CLOSED_ITEMS:
                    dto.status = getMessage(
                            "LedgerCard.ClosedStatements");
                    break;
                default:
                    dto.status = getMessage(
                            "LedgerCard.AllStatements");
                    break;
            }
        }
        dto.valueDateFrom = requestDTO.getValueDateFrom();
        dto.valueDateTo = requestDTO.getValueDateTo();
        dto.creditDebitRelationDateFrom = requestDTO.getCreditDebitRelationDateFrom();
        dto.creditDebitRelationDateTo = requestDTO.getCreditDebitRelationDateTo();
        switch (requestDTO.getType()) {
            case CUSTOMER:
                dto.type = LedgerCardTypeDTO.CUSTOMER;
                break;
            case SUPPLIER:
                dto.type = LedgerCardTypeDTO.SUPPLIER;
                break;
        }
        dto.currency = requestDTO.getCurrency();
        switch(requestDTO.getCurrency()) {
            case DOMESTIC :                                
                dto.currencyISOCode = java.util.Currency.getInstance(Locale.getDefault())
                                                        .getCurrencyCode();                
                break;
            case FOREIGN : 
                dto.currencyISOCode = requestDTO.getForeignCurrencyISOCode();                
                break;
        }
        return dto;
    }
    
    public List<Analytical> readSupplierCustomerLedgerCard(
            EntityManager EM,
            RequestLedgerCardDTO dto,
            boolean isSupplier) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Analytical> c = cb.createQuery(Analytical.class);
        Root<Analytical> root = c.from(Analytical.class);
        c.select(root);
        Join<Analytical, OrgUnit> orgUnitJoin =
                root.join(Analytical_.orgUnit, JoinType.LEFT);
        Join<Analytical, Account> accountJoin =
                root.join(Analytical_.account, JoinType.LEFT);        
        Join<Analytical, BusinessPartner> partnerJoin =
                root.join(Analytical_.partner, JoinType.LEFT);        
        List<Predicate> criteria = new ArrayList<>();
        if(isSupplier == true) {
                criteria.add(cb.equal(root.get(Analytical_.determination), 
                        AccountDetermination.SUPPLIERS));                
        }
        if(isSupplier == false) {
            criteria.add(cb.equal(root.get(Analytical_.determination), 
                    AccountDetermination.CUSTOMERS));
        }
        criteria.add(cb.equal(orgUnitJoin.get(OrgUnit_.client).get(Client_.id),
                cb.parameter(Integer.class, "clientID")));
        if (dto.getOrgUnitID() != null) {
            ParameterExpression<Integer> p =
                    cb.parameter(Integer.class, "orgUnitID");
            criteria.add(cb.equal(orgUnitJoin.get(OrgUnit_.id), p));
        }
        if (dto.getAccountNumber() != null && dto.getAccountNumber().equals("") ==false) {
            ParameterExpression<String> p =
                    cb.parameter(String.class, "accountNumber");
            criteria.add(cb.equal(accountJoin.get(Account_.number), p));
        }
        if (dto.getPartnerRegNo() != null && dto.getPartnerRegNo().equals("") == false) {
            ParameterExpression<String> p =
                    cb.parameter(String.class, "partner");
            criteria.add(cb.equal(partnerJoin.get(BusinessPartner_.companyIdNumber), p));
        }
        if (dto.getCreditDebitRelationDateFrom() != null) {
            ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class, 
                    "creditDebitRelationFrom");
            criteria.add(cb.greaterThanOrEqualTo(
                    root.get(Analytical_.creditDebitRelationDate), p)
            );
        }
        if (dto.getCreditDebitRelationDateTo() != null) {
            ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class, 
                    "creditDebitRelationTo");
            criteria.add(cb.lessThanOrEqualTo(
                    root.get(Analytical_.creditDebitRelationDate), p));
        }
        if (dto.getValueDateFrom() != null) {
            ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class, "valueFrom");
            criteria.add(cb.greaterThanOrEqualTo(root.get(Analytical_.valueDate), p));
        }
        if (dto.getValueDateTo() != null) {
            ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class, "valueTo");
            criteria.add(cb.lessThanOrEqualTo(root.get(Analytical_.valueDate), p));
        }
        if (dto.getStatus().equals(RequestLedgerCardDTO.Status.ALL) == false) {
            ParameterExpression<Analytical.Status> p = cb.parameter(
                    Analytical.Status.class, "status");
            criteria.add(cb.equal(root.get(Analytical_.status), p));
        }
        Predicate predicate = cb.and(criteria.toArray(new Predicate[0]));
        c.where(predicate).orderBy(
                cb.asc( partnerJoin.get(BusinessPartner_.companyIdNumber) ),
                cb.asc( accountJoin.get(Account_.number) ), 
                cb.asc( root.get(Analytical_.recordDate) )
        );
        TypedQuery<Analytical> q = EM.createQuery(c);
        q.setParameter("clientID", dto.getClientID());        
        if(dto.getOrgUnitID() != null) {
            q.setParameter("orgUnitID", dto.getOrgUnitID());
        }
        if(dto.getAccountNumber() != null && dto.getAccountNumber().equals("") == false) {
            q.setParameter("accountNumber", dto.getAccountNumber());
        }
        if(dto.getPartnerRegNo() != null && dto.getPartnerRegNo().equals("") == false) {
            q.setParameter("partner", dto.getPartnerRegNo());
        }
        if(dto.getCreditDebitRelationDateFrom() != null) {
            q.setParameter("creditDebitRelationFrom", 
                    dto.getCreditDebitRelationDateFrom());
        }
        if(dto.getCreditDebitRelationDateTo() != null) {
            q.setParameter("creditDebitRelationTo", dto.getCreditDebitRelationDateTo());
        }
        if(dto.getValueDateFrom() != null) {
            q.setParameter("valueFrom", dto.getValueDateFrom());
        }
        if(dto.getValueDateTo() != null) {
            q.setParameter("valueTo", dto.getValueDateTo());
        }
        if (dto.getStatus().equals(RequestLedgerCardDTO.Status.ALL) == false) {          
            if(dto.getStatus().equals(RequestLedgerCardDTO.Status.OPEN_ITEMS)) {
                q.setParameter("status", Analytical.Status.OPEN);
            } else {
                q.setParameter("status", Analytical.Status.CLOSED);
            }
        }
        return q.getResultList();
    }
    
}