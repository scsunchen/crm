/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Client;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.Account_;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.Determination;
import com.invado.finance.domain.journal_entry.GeneralLedger;
import com.invado.finance.domain.journal_entry.JournalEntryItem;
import com.invado.finance.domain.journal_entry.PartnerSpecification;
import com.invado.finance.domain.journal_entry.TrialBalanceCompany;
import com.invado.finance.domain.journal_entry.TrialBalanceOrgUnit;
import com.invado.finance.service.dto.AccountDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.exception.AccountConstraintViolationException;
import com.invado.finance.service.exception.AccountExistsException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.service.dto.ReadAllDTO;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Bobic Dragan
 */
@Transactional(rollbackFor = Exception.class)
public class AccountServicesImpl {

    private static final Logger LOG = Logger.getLogger(AccountServicesImpl.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager EM;
    @Autowired
    private Validator validator;

    public void delete(String accountNumber) throws AccountNotFoundException,
                                                    ReferentialIntegrityException {
        try {
            Account temp = EM.find(Account.class, accountNumber);
            if (temp == null) {
                throw new AccountNotFoundException(
                        getMessage("Account.AccountNotExists",
                                accountNumber));
            }
            if (!EM.createNamedQuery(Analytical.READ_BY_ACCOUNT)
                    .setParameter(1, accountNumber)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("Account.ReferentialIntegrity.RecordedJournalEntry",
                                accountNumber));
            }
            if (!EM.createNamedQuery(TrialBalanceCompany.READ_BY_ACCOUNT)
                    .setParameter(1, accountNumber)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("Account.ReferentialIntegrity.RecordedJournalEntry",
                                accountNumber));
            }
            if (!EM.createNamedQuery(TrialBalanceOrgUnit.READ_BY_ACCOUNT)
                    .setParameter(1, accountNumber)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("Account.ReferentialIntegrity.RecordedJournalEntry",
                                accountNumber));
            }
            if (!EM.createNamedQuery(PartnerSpecification.READ_BY_ACCOUNT)
                    .setParameter(1, accountNumber)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("Account.ReferentialIntegrity.RecordedJournalEntry",
                                accountNumber));
            }
            if (!EM.createNamedQuery(GeneralLedger.READ_BY_ACCOUNT)
                    .setParameter(1, accountNumber)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("Account.ReferentialIntegrity.RecordedJournalEntry",
                                accountNumber));
            }
            if (!EM.createNamedQuery(JournalEntryItem.READ_BY_ACCOUNT)
                    .setParameter(1, accountNumber)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("Account.ReferentialIntegrity.JournalEntry",
                                accountNumber));
            }
            EM.remove(temp);
        } catch (AccountNotFoundException | ReferentialIntegrityException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.WARNING,
                    "Account.Persistence.Delete",
                    e);
            throw new SystemException(getMessage("Account.Persistence.Delete"));
        } 
    }

    public void create(AccountDTO dto) throws AccountConstraintViolationException,
                                              AccountExistsException {
        try{
            Account tmp = new Account();
            tmp.set(dto);
            //validacija identifikatora konta
            List<String> messages = validator.validate(tmp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new AccountConstraintViolationException(
                        getMessage("Account.ConstraintViolationEx"),
                        messages);
            }
            if (EM.find(Account.class, tmp.getNumber()) != null) {
                throw new AccountExistsException(getMessage("Account.AccountExists",
                        tmp.getNumber()));
            }
            EM.persist(tmp);
        } catch (AccountConstraintViolationException | AccountExistsException iz) {
            throw iz;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("Account.Persistence.Create"));
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public AccountDTO read(String accountNumber) throws AccountNotFoundException {       
        try {
            Account temp = EM.find(Account.class, accountNumber);
            if (temp == null) {
                throw new AccountNotFoundException(
                        getMessage("Account.AccountNotExists", accountNumber));
            }
            return temp.getDTO();
        } catch (AccountNotFoundException iz) {
            throw iz;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "", e);
            throw new SystemException(getMessage("Account.Persistence.Read"));
        } 
    }

    public Long update(AccountDTO DTO) throws AccountConstraintViolationException,
                                              AccountNotFoundException {
        try {
            if (DTO.getNumber() == null || EM.find(Account.class, DTO.getNumber()) == null) {
                throw new AccountNotFoundException(
                        getMessage("Account.AccountNotExists", DTO.getNumber()));
            }
            Account temp = EM.find(Account.class, DTO.getNumber());
            temp.set(DTO);
            //validacija identifikatora konta    
            List<String> messages = validator.validate(temp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new AccountConstraintViolationException(
                        getMessage("Account.ConstraintViolationEx"),
                        messages);
            }
            return temp.getVersion();
        } catch (AccountConstraintViolationException | AccountNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(getMessage("Account.OptimisticLock",
                        DTO.getNumber()));
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(getMessage("Account.Persistence.Update"));
            }
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadAllDTO<AccountDTO> readAll() throws RemoteException {
        try {
            List<Account> list = EM.createNamedQuery(
                    Account.READ_ALL_ORDERBY_NUMBER)
                    .getResultList();
            ApplicationSetup appSetup = EM.find(ApplicationSetup.class, 1);
            String clientName;
            if (appSetup.getApplicationVersion().equals(
                    ApplicationSetup.ApplicationVersion.ACCOUNTING_AGENCY)) {
                clientName = appSetup.getAccountingAgencyName();
            } else {
                clientName = EM.find(Client.class, 1).getName();
            }
            EM.getTransaction().commit();
            List<AccountDTO> result = new ArrayList<>();
            for (Account account : list) {
                result.add(account.getDTO());
            }
            return new ReadAllDTO(result, clientName);
        } catch (Exception e) {
            LOG.log(Level.WARNING,
                    "Account.Persistence.ReadAll",
                    e);
            throw new SystemException(getMessage("Account.Persistence.ReadAll"));
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadRangeDTO<AccountDTO> readPage(PageRequestDTO p) throws PageNotExistsException {
        String number = null;
        Boolean analytic = null;
        Boolean generalLedger = null;

        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("number") && s.getValue() instanceof String) {
                number = (String) s.getValue();
            }
            if (s.getKey().equals("analytic") && s.getValue() instanceof Boolean) {
                analytic = (Boolean) s.getValue();
            }
            if (s.getKey().equals("generalLedger") && s.getValue() instanceof Boolean) {
                generalLedger = (Boolean) s.getValue();
            }
        }
        try {
            Integer pageSize = EM.find(ApplicationSetup.class, 1).getPageSize();
            Long countEntities = this.count(EM, number, analytic, generalLedger);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                throw new PageNotExistsException(
                        getMessage("Account.PageNotExists",
                                pageNumber));
            }
            ReadRangeDTO<AccountDTO> result = new ReadRangeDTO<>();
            //if page number is -1 read last page
            if (pageNumber.equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                List<Account> data = this.readAccounts(EM, start, pageSize, number,
                        analytic, generalLedger);
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<Account> data = this.readAccounts(EM, pageNumber * pageSize,
                        pageSize, number,
                        analytic, generalLedger);
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            EM.getTransaction().commit();
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    getMessage("Account.Persistence.ReadAll"));
        } 

    }

    private List<AccountDTO> convertToDTO(List<Account> lista) {
        List<AccountDTO> result = new ArrayList<>();
        for (Account account : lista) {
            result.add(account.getDTO());
        }
        return result;
    }

    public List<Account> readAccounts(EntityManager em,
            int start,
            int rangeSize,
            String number,
            Boolean analytic,
            Boolean generalLedger) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        query.select(root).orderBy(cb.asc(root.get(Account_.number)));
        Predicate criteria = cb.conjunction();
        if (number != null && number.replace("%", "").isEmpty() == false) {
            criteria = cb.and(criteria,
                    cb.like(cb.upper(root.get(Account_.number)),
                            cb.parameter(String.class, "number")));
        }
        if (analytic != null && analytic.equals(Boolean.TRUE)) {
            criteria = cb.and(criteria,
                    cb.equal(root.get(Account_.type/*Konto_.vrsta*/), Account.Type.ANALITICKI));
        } else if (analytic != null && analytic.equals(Boolean.FALSE)) {
            criteria = cb.and(criteria,
                    cb.equal(root.get(Account_.type/*Konto_.vrsta*/), Account.Type.SINTETICKI));
        }

        if (generalLedger != null && generalLedger.equals(Boolean.TRUE)) {
            criteria = cb.and(criteria,
                    cb.equal(root.get(Account_.determination/*Konto_.pripadnost*/), Determination.GENERAL_LEDGER));
        } else if (generalLedger != null && generalLedger.equals(Boolean.FALSE)) {
            criteria = cb.and(criteria,
                    cb.notEqual(root.get(Account_.determination/*Konto_.pripadnost*/), Determination.GENERAL_LEDGER));
        }
        query.where(criteria);
        TypedQuery<Account> q = em.createQuery(query);
        if (number != null && number.replace("%", "").isEmpty() == false) {
            q.setParameter("number", number.toUpperCase());
        }
        return q.setFirstResult(start).setMaxResults(rangeSize).getResultList();
    }

    private Long count(EntityManager em,
            String number,
            Boolean analytic,
            Boolean generalLedger) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Account> root = query.from(Account.class);
        query.select(cb.count(root));
        Predicate criteria = cb.conjunction();
        if (number != null && number.replace("%", "").isEmpty() == false) {
            criteria = cb.and(criteria,
                    cb.like(cb.upper(root.get(Account_.number)),
                            cb.parameter(String.class, "number")));
        }
        if (analytic != null && analytic.equals(Boolean.TRUE)) {
            criteria = cb.and(criteria,
                    cb.equal(root.get(Account_.type), Account.Type.ANALITICKI));
        } else if (analytic != null && analytic.equals(Boolean.FALSE)) {
            criteria = cb.and(criteria,
                    cb.equal(root.get(Account_.type), Account.Type.SINTETICKI));
        }
        if (generalLedger != null && generalLedger.equals(Boolean.TRUE)) {
            criteria = cb.and(criteria,
                    cb.equal(root.get(Account_.determination), Determination.GENERAL_LEDGER));
        } else if (generalLedger != null && generalLedger.equals(Boolean.FALSE)) {
            criteria = cb.and(criteria,
                    cb.notEqual(root.get(Account_.determination), Determination.GENERAL_LEDGER));
        }
        query.where(criteria);
        TypedQuery<Long> q = em.createQuery(query);
        if (number != null && number.replace("%", "").isEmpty() == false) {
            q.setParameter("number", number.toUpperCase());
        }
        return q.getSingleResult();
    }

}
