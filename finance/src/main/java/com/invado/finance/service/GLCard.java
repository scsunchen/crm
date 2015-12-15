/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;


import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Client;
import com.invado.core.domain.Client_;
import com.invado.core.domain.ExchangeRate;
import com.invado.core.domain.ExchangeRatePK;
import com.invado.core.domain.OrgUnit;
import com.invado.core.domain.OrgUnit_;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.Account_;
import static com.invado.finance.domain.journal_entry.ChangeType.CREDIT;
import static com.invado.finance.domain.journal_entry.ChangeType.DEBIT;
import com.invado.finance.domain.journal_entry.GeneralLedger;
import com.invado.finance.domain.journal_entry.GeneralLedger_;
import com.invado.finance.service.dto.CurrencyTypeDTO;
import static com.invado.finance.service.dto.CurrencyTypeDTO.FOREIGN;
import com.invado.finance.service.dto.RequestGLCardDTO;
import com.invado.finance.service.dto.LedgerCardDTO;
import com.invado.finance.service.dto.LedgerCardItemDTO;
import com.invado.finance.service.dto.ReadLedgerCardsDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.service.dto.LedgerCardTypeDTO;
import com.invado.finance.service.exception.ZeroExchangeRateException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class GLCard{

    private static final Logger LOG = Logger.getLogger(GLCard.class.getName());
    
    @PersistenceContext(name = "unit")
    private EntityManager dao;
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadLedgerCardsDTO readGLCard(RequestGLCardDTO dto) 
            throws EntityNotFoundException, 
            ZeroExchangeRateException {
        if (dto.getClientID() == null) {
            throw new EntityNotFoundException(
                    getMessage("LedgerCard.IllegalArgument.ClientId"));
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
            if (dto.getAccountNumber() != null &&
                    dto.getAccountNumber().equals("") == false
                    && dao.find(Account.class, dto.getAccountNumber()) == null) {
                throw new EntityNotFoundException(
                        getMessage("LedgerCard.AccountNotExists",
                        dto.getAccountNumber()));
            }
            List<GeneralLedger> generalLedgerList = this.readGeneralLedgerCard(
                    dao,
                    dto);
            ReadLedgerCardsDTO result = this.getGeneralLedgerCardDTO(
                    dao,
                    dto,
                    generalLedgerList);
            return result;
        } catch (EntityNotFoundException | ZeroExchangeRateException iz) {
            throw iz;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                    throw new SystemException(
                            getMessage("LedgerCard.OptimisticLock")
                    );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        getMessage("LedgerCard.Persistence"));
            }
        } 
    }

    private ReadLedgerCardsDTO getGeneralLedgerCardDTO(
            EntityManager EM,
            RequestGLCardDTO requestDTO,
            List<GeneralLedger> stavke) 
            throws EntityNotFoundException, 
            ZeroExchangeRateException {        
        List<LedgerCardDTO> resultList = new ArrayList<>();
        List<String> accountNumbers = new ArrayList<>();
        Integer pageSize = EM.find(ApplicationSetup.class, 1)
                    .getPageSize();
        Integer numberOfPages = 0;
         //group general ledger transactions with same account number
        for (GeneralLedger item : stavke) {
            if (accountNumbers.contains(item.getAccountNumber()) == true) {
                continue;
            }
            BigDecimal balanceTotal = BigDecimal.ZERO;
            BigDecimal debitTotal = BigDecimal.ZERO;
            BigDecimal creditTotal = BigDecimal.ZERO;
            LedgerCardDTO dto = this.getLedgerCardDTO(
                    EM,
                    requestDTO,
                    item.getAccountNumber());
            //add general ledger transactions with same account number
            for (GeneralLedger item1 : stavke) {
                if (item.getAccountNumber().equals(item1.getAccountNumber())) {
                    LedgerCardItemDTO itemDTO = new LedgerCardItemDTO();
                    itemDTO.typeID = item1.getJournalEntryTypeID();
                    itemDTO.journalEntryNumber = item1.getJournalEntryNumber();
                    itemDTO.journalEntryDate = item1.getRecordDate();
                    itemDTO.document = item1.getDocument();
                    itemDTO.creditDebitRelationDate = item1.getCreditDebitRelationDate();
                    itemDTO.orgUnitID = item1.getOrgUnitID();
                    itemDTO.internalDocument = item1.getInternalDocument();
                    if (item1.isDescriptionNull() == true) {
                        itemDTO.description =
                                getMessage("LedgerCard.SumItem");
                    } else {
                        itemDTO.description = item1.getDescriptionName();
                    }
                    itemDTO.ordinal = item1.getJournalEntryItemOrdinal();
                    itemDTO.valueDate = item1.getValueDate();
                    BigDecimal exchangeRate = BigDecimal.ONE;
                    Calendar creditDebitRelationCalendar = Calendar.getInstance();
                    creditDebitRelationCalendar.clear();
                    creditDebitRelationCalendar.set(Calendar.YEAR, 
                            item1.getCreditDebitRelationDate().getYear());
                    creditDebitRelationCalendar.set(Calendar.DAY_OF_YEAR, 
                            item1.getCreditDebitRelationDate().getDayOfYear());
                    if (requestDTO.getCurrency() == CurrencyTypeDTO.FOREIGN) {
                        ExchangeRate exRate = EM.find(ExchangeRate.class,
                                new ExchangeRatePK(new Date(creditDebitRelationCalendar.getTimeInMillis()),
                                requestDTO.getForeignCurrencyISOCode()));
                        if (exRate == null) {
                            throw new EntityNotFoundException(
                                    getMessage("LedgerCard.ExchangeRateNotExists",
                                    requestDTO.getForeignCurrencyISOCode(),
                                    creditDebitRelationCalendar.getTime()));
                        } else {
                            exchangeRate = exRate.getMiddle();
                        }
                    }
                    if(exchangeRate.compareTo(BigDecimal.ZERO) == 0) {
                        throw new ZeroExchangeRateException(
                                getMessage(
                                "LedgerCard.ZeroExchangeRateException",
                                requestDTO.getForeignCurrencyISOCode(),
                                creditDebitRelationCalendar.getTime()));
                    }
                    
                    BigDecimal amount = item1.getAmount().divide(
                            exchangeRate,
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
                    dto.items.add(itemDTO);
                }
            }
            dto.debitTotal = debitTotal;
            dto.creditTotal = creditTotal;
            dto.balanceTotal = debitTotal.subtract(creditTotal);
            Integer items = dto.items.size() + 1;//add one summary item            
            dto.numberOfPages = ((items != 0 && items % pageSize == 0)
                    ? items / pageSize
                    : items / pageSize + 1);
            numberOfPages = numberOfPages + dto.numberOfPages;
            accountNumbers.add(item.getAccountNumber());
            resultList.add(dto);            
        }
        ReadLedgerCardsDTO result = new ReadLedgerCardsDTO();
        result.pageSize = pageSize;
        //number of pages must be zero based so subtract 1
        result.numberOfPages = numberOfPages - 1 ;
        result.ledgerCards = resultList;
        return result;
    }

    private LedgerCardDTO getLedgerCardDTO(EntityManager EM, 
                                           RequestGLCardDTO requestDTO,
                                           String accountNumber) {
        LedgerCardDTO dto = new LedgerCardDTO();
        dto.clientName = EM.find(Client.class, requestDTO.getClientID()).getName();
        if (requestDTO.getOrgUnitID() != null) {
            OrgUnit oj = EM.find(OrgUnit.class, requestDTO.getOrgUnitID());
            dto.orgUnitID = oj.getId();
            dto.orgUnitName = oj.getName();
        }
        if (accountNumber != null && accountNumber.isEmpty() == false) {
            Account konto = EM.find(Account.class, accountNumber);
            dto.accountNumber = konto.getNumber();
            dto.accountName = konto.getDescription();
        }
        dto.valueDateFrom = requestDTO.getValueDateFrom();
        dto.valueDateTo = requestDTO.getValueDateTo();
        dto.creditDebitRelationDateFrom = requestDTO.getCreditDebitRelationDateFrom();
        dto.creditDebitRelationDateTo = requestDTO.getCreditDebitRelationDateTo();
        dto.type = LedgerCardTypeDTO.GENERAL_LEDGER;
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
    
    private List<GeneralLedger> readGeneralLedgerCard(EntityManager EM, 
                                                      RequestGLCardDTO dto) {        
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<GeneralLedger> c = cb.createQuery(GeneralLedger.class);
        Root<GeneralLedger> root = c.from(GeneralLedger.class);
        c.select(root);
        Join<GeneralLedger, OrgUnit> orgUnitJoin =
                root.join(GeneralLedger_.orgUnit, JoinType.LEFT);
        Join<GeneralLedger, Account> accountJoin =
                root.join(GeneralLedger_.account, JoinType.LEFT);        
        List<Predicate> criteria = new ArrayList<>();
        criteria.add(
                cb.equal(
                        orgUnitJoin.get(OrgUnit_.client).get(Client_.id),
                        cb.parameter(Integer.class, "clientID")
                )
        );
        if (dto.getOrgUnitID() != null) {
            ParameterExpression<Integer> p =
                    cb.parameter(Integer.class, "orgUnitID");
            criteria.add(cb.equal(orgUnitJoin.get(OrgUnit_.id), p));
        }
        if (dto.getAccountNumber() != null && dto.getAccountNumber().equals("") == false) {
            ParameterExpression<String> p =
                    cb.parameter(String.class, "accountNumber");
            criteria.add(cb.equal(accountJoin.get(Account_.number), p));
        }
        if (dto.getCreditDebitRelationDateFrom() != null) {
            ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class, 
                    "creditDebitRelationDateFrom");
            criteria.add(cb.greaterThanOrEqualTo(
                    root.get(GeneralLedger_.creditDebitRelationDate), 
                    p));
        }
        if (dto.getCreditDebitRelationDateTo() != null) {
            ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class, 
                    "creditDebitRelationDateTo");
            criteria.add(cb.lessThanOrEqualTo(root.get(
                    GeneralLedger_.creditDebitRelationDate), 
                    p));
        }
        if (dto.getValueDateFrom() != null) {
            ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class, "valueFrom");
            criteria.add(cb.greaterThanOrEqualTo(
                    root.get(GeneralLedger_.valueDate), 
                    p));
        }
        if (dto.getValueDateTo() != null) {
            ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class, "valueTo");
            criteria.add(cb.lessThanOrEqualTo(
                    root.get(GeneralLedger_.valueDate), 
                    p));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0]))).orderBy(
                cb.asc(accountJoin.get(Account_.number)), 
                cb.asc(root.get(GeneralLedger_.recordDate)));
        TypedQuery<GeneralLedger> q = EM.createQuery(c);
        q.setParameter("clientID", dto.getClientID());
        if(dto.getOrgUnitID() != null) {
            q.setParameter("orgUnitID", dto.getOrgUnitID());
        }
        if(dto.getAccountNumber() != null && dto.getAccountNumber().equals("") == false) {
            q.setParameter("accountNumber", dto.getAccountNumber());
        }
        if(dto.getCreditDebitRelationDateFrom() != null) {
            q.setParameter("creditDebitRelationDateFrom", 
                    dto.getCreditDebitRelationDateFrom());
        }
        if(dto.getCreditDebitRelationDateTo() != null) {
            q.setParameter("creditDebitRelationDateTo", 
                    dto.getCreditDebitRelationDateTo());
        }
        if(dto.getValueDateFrom() != null) {
            q.setParameter("valueFrom", dto.getValueDateFrom());
        }
        if(dto.getValueDateTo() != null) {
            q.setParameter("valueTo", dto.getValueDateTo());
        }
        return q.getResultList();
    }    
}
