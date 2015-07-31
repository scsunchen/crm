/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;


import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.OrgUnit;
import com.invado.core.domain.OrgUnitPK;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.JournalEntryItem;
import com.invado.finance.service.dto.JournalEntryItemDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.domain.journal_entry.Description;
import com.invado.finance.domain.journal_entry.AccountDetermination;
import com.invado.finance.domain.journal_entry.JournalEntry;
import com.invado.finance.domain.journal_entry.JournalEntryItemPK;
import com.invado.finance.domain.journal_entry.JournalEntryPK;
import com.invado.finance.service.exception.IllegalAccountException;
import com.invado.finance.service.exception.IllegalBusinessPartnerException;
import com.invado.finance.service.exception.JournalEntryConstraintViolationException;
import com.invado.finance.service.exception.JournalEntryItemNotFoundException;
import com.invado.finance.service.exception.PostedJournalEntryUpdateException;
import java.rmi.RemoteException;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import static com.invado.finance.Utils.*;
import com.invado.finance.service.dto.UpdateJournalEntryResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bobic Dragan
 */
@Service
public class JournalEntryItemService  {
 
    private static final Logger LOG = Logger.getLogger(JournalEntryItemService.class.getName());
    @PersistenceContext(name = "unit")
    private EntityManager EM;
    @Autowired
    private Validator validator;
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<JournalEntryItemDTO> readAllJournalItems(Integer clientId,
                                                         Integer typeId,
                                                         Integer number) {
        try {
            List<JournalEntryItem> data = EM.createNamedQuery(
                    JournalEntryItem.READ_BY_JOURNAL_ENTRY_ORDER_BY_ORDINAL)
                    .setParameter(1, clientId)
                    .setParameter(2, typeId)
                    .setParameter(3, number)
                    .getResultList();
            return this.convertToDTO(data);
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    getMessage("JournalEntry.Persistence.ReadAllItems"), ex
            );
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadRangeDTO<JournalEntryItemDTO> readJournalEntryItemPage(
            PageRequestDTO p)
            throws PageNotExistsException {       
        try {
            Integer pageNumber = p.getPage();
            Integer clientId = null;
            Integer journalEntryType = null;
            Integer journalEntryNumber = null;

            for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
                if (s.getKey().equals("clientId") && s.getValue() instanceof Integer) {
                    clientId = (Integer) s.getValue();
                }
                if (s.getKey().equals("journalEntryType") && s.getValue() instanceof Integer) {
                    journalEntryType = (Integer) s.getValue();
                }
                if (s.getKey().equals("journalEntryNumber") && s.getValue() instanceof Integer) {
                    journalEntryNumber = (Integer) s.getValue();
                }
            }
            Integer pageSize = EM.find(ApplicationSetup.class, 1).getPageSize();
            Long countEntities = EM.createNamedQuery(
                    JournalEntryItem.COUNT_BY_JOURNAL_ENTRY, Long.class)
                    .setParameter(1, clientId)
                    .setParameter(2, journalEntryType)
                    .setParameter(3, journalEntryNumber)
                    .getSingleResult();
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                throw new PageNotExistsException(
                        getMessage("JournalEntryItem.PageNotExists", pageNumber)
                );
            }
            ReadRangeDTO<JournalEntryItemDTO> result = new ReadRangeDTO<>();
            //if page number is -1 read last page
            if (pageNumber.equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                List<JournalEntryItem> data = EM.createNamedQuery(
                        JournalEntryItem.READ_BY_JOURNAL_ENTRY_ORDER_BY_ORDINAL)
                        .setParameter(1, clientId)
                        .setParameter(2, journalEntryType)
                        .setParameter(3, journalEntryNumber)
                        .setFirstResult(start)
                        .setMaxResults(pageSize)
                        .getResultList();
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<JournalEntryItem> data = EM.createNamedQuery(
                        JournalEntryItem.READ_BY_JOURNAL_ENTRY_ORDER_BY_ORDINAL)
                        .setParameter(1, clientId)
                        .setParameter(2, journalEntryType)
                        .setParameter(3, journalEntryNumber)
                        .setFirstResult(pageNumber * pageSize)
                        .setMaxResults(pageSize)
                        .getResultList();
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    getMessage("JournalEntry.Persistence.ReadAllItems"), ex
            );
        } 
    }

    private List<JournalEntryItemDTO> convertToDTO(List<JournalEntryItem> lista) {
        List<JournalEntryItemDTO> listaDTO = new ArrayList<>();
        for (JournalEntryItem pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public JournalEntryItemDTO readJournalEntryItem(Integer clientID,
            Integer typeID,
            Integer number,
            Integer ordinal)
            throws JournalEntryItemNotFoundException {
        try {
            JournalEntryItem temp = EM.find(JournalEntryItem.class,
                    new JournalEntryItemPK(clientID, typeID, number, ordinal));
            if (temp == null) {
                throw new JournalEntryItemNotFoundException(
                        getMessage("JournalEntry.JournalEntryItemNotExists",
                        clientID,
                        typeID,
                        number,
                        ordinal));
            }
            return temp.getDTO();
        } catch (JournalEntryItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "", e);
            throw new SystemException(getMessage("JournalEntry.Persistence.ReadItem"),e);
        } 
    }
    
//    @Transactional(rollbackFor = Exception.class)
//    public UpdateJournalEntryResultDTO updateJournalEntryItem(
//            JournalEntryItemDTO dto,
//            Long journalEntryVersion)
//            throws JournalEntryItemNotFoundException,
//            ReferentialIntegrityException,
//            IllegalAccountException,
//            IllegalBusinessPartnerException,
//            JournalEntryConstraintViolationException,
//            PostedJournalEntryUpdateException {
//        try {
//            JournalEntry journalEntry = EM.find(JournalEntry.class,
//                    new JournalEntryPK(dto.getClientId(),
//                                       dto.getTypeId(),
//                                       dto.getJournalEntryNumber())
//            );
//            if (journalEntry == null) {
//                throw new ReferentialIntegrityException(
//                        getMessage("JournalEntry.JournalEntryNotExists",
//                        dto.getClientId(),
//                        dto.getTypeId(),
//                        dto.getJournalEntryNumber()));
//            }
//            if (journalEntry.getPosted() == true) {
//                throw new PostedJournalEntryUpdateException(
//                        getMessage("JournalEntry.PostedJournalEntryUpdate",
//                        dto.getClientId(),
//                        dto.getTypeId(),
//                        dto.getJournalEntryNumber()));
//            }
//            journalEntry.setVersion(journalEntryVersion);
//            EM.lock(journalEntry, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//            JournalEntryItem item = EM.find(JournalEntryItem.class,
//                    new JournalEntryItemPK(dto.getClientId(),
//                                           dto.getTypeId(),
//                                           dto.getJournalEntryNumber(),
//                                           dto.getOrdinalNumber())
//            );
//            if (item == null) {
//                throw new JournalEntryItemNotFoundException(
//                        getMessage("JournalEntry.JournalEntryItemNotExists",
//                        dto.getClientId(),
//                        dto.getTypeId(),
//                        dto.getJournalEntryNumber(),
//                        dto.getOrdinalNumber()));
//            }
//            BigDecimal amountBeforeUpdate = (item.getAmount());
//            ChangeType changeTypeBeforeUpdate = item.getChangeType();
//            OrgUnit orgUnit = null;
//            if (dto.getClientId() != null && dto.getUnitId() != null) {
//                orgUnit = EM.find(OrgUnit.class,
//                        new OrgUnitPK(dto.getUnitId(), dto.getClientId()));
//                if (orgUnit == null) {
//                    throw new ReferentialIntegrityException(
//                            getMessage("JournalEntry.OrgUnitNotExists",
//                            dto.getUnitId()));
//                }
//            }
//            Description description = null;
//            if (dto.getDescId() != null) {
//                description = EM.find(Description.class, dto.getDescId());
//                if (description == null) {
//                    throw new ReferentialIntegrityException(
//                            getMessage("JournalEntry.DescNotExists", dto.getDescId()));
//                }
//            }
//
//            Account account = null;
//            if (dto.getAccountCode() != null && !dto.getAccountCode().isEmpty()) {
//                account = EM.find(Account.class, dto.getAccountCode());
//                if (account == null) {
//                    throw new ReferentialIntegrityException(
//                            getMessage("JournalEntry.AccountNotExists",
//                            dto.getAccountCode()));
//                }
//            }
//
//            BusinessPartner businessPartner = null;
//            if (dto.getPartnerCompanyId() != null && !dto.getPartnerCompanyId().isEmpty()) {
//                businessPartner = EM.find(BusinessPartner.class, dto.getPartnerCompanyId());
//                if (businessPartner == null) {
//                    //if user enter business partner that not exists
//                    throw new ReferentialIntegrityException(
//                            getMessage("JournalEntry.BusinessPartnerNotExists",
//                            dto.getPartnerCompanyId()));
//                }
//            }            
//            List<ApplicationUser> userList = EM.createNamedQuery(
//                    ApplicationUser.READ_BY_USERNAME_AND_PASSWORD, 
//                    ApplicationUser.class)
//                    .setParameter(1, dto.getUsername())
//                    .setParameter(2, dto.getPassword())
//                    .getResultList();
//            if (userList.isEmpty() == true) {
//                throw new ReferentialIntegrityException(
//                        getMessage("JournalEntry.UserNotExists", dto.getUsername()));
//            }
//            
//            item.set(dto, journalEntry, description, orgUnit, businessPartner, account, userList.get(0));
//            this.validate(item);
//            this.updateJournalEntryBalance(journalEntry,
//                    item,
//                    amountBeforeUpdate,
//                    changeTypeBeforeUpdate);
//            UpdateJournalEntryResultDTO result = new UpdateJournalEntryResultDTO();
//            result.version = journalEntry.getVersion();
//            result.debitBalance = journalEntry.getBalanceDebit();
//            result.creditBalance = journalEntry.getBalanceCredit();
//            result.balance = journalEntry.getBalanceDebit().subtract(journalEntry.getBalanceCredit());
//            return result;
//        } catch (JournalEntryItemNotFoundException 
//                | ReferentialIntegrityException | IllegalAccountException 
//                | JournalEntryConstraintViolationException 
//                | IllegalBusinessPartnerException 
//                | PostedJournalEntryUpdateException e) {
//            throw e;
//        } catch (Exception e) {
//             if (e instanceof OptimisticLockException
//                    || e.getCause() instanceof OptimisticLockException) {
//                throw new SystemException(getMessage("JournalEntry.OptimisticLock",
//                    dto.getClientId(),
//                    dto.getTypeId(),
//                    dto.getJournalEntryNumber()),e);
//            } else {
//                LOG.log(Level.WARNING, "JournalEntry.Persistence.UpdateItem", e);
//                throw new SystemException(getMessage("JournalEntry.Persistence.UpdateItem"),e);
//            }
//        }  
//    }

//    //Ok
//    private void validate(JournalEntryItem item)
//            throws JournalEntryConstraintViolationException,
//            IllegalBusinessPartnerException,
//            IllegalAccountException {
//        List<String> s = validator.validate(item).stream()
//                .map(ConstraintViolation::getMessage)
//                .collect(Collectors.toList());
//        if (!s.isEmpty()) {
//            throw new JournalEntryConstraintViolationException(
//                    getMessage("JournalEntry.ConstraintViolation"), s);
//        }        
//        //provera tacnosti saradnika
//        Account account = item.getAccount();
//        if (item.getPartner() == null) {
//            if (!account.getDetermination().equals(AccountDetermination.GENERAL_LEDGER)) {
//                throw new IllegalBusinessPartnerException(
//                        getMessage("JournalEntry.NullBusinessPartner"));
//            }
//            //FIXME : moze da proknjizi na sinteticki konto!!!!
//            return;
//        }
//        if (account.getDetermination().equals(AccountDetermination.GENERAL_LEDGER)) {
//            throw new IllegalBusinessPartnerException(
//                    getMessage("JournalEntry.NotNullBusinessPartner"));
//        }
//        //provera konta
//        if (account.jeSinteticki()) {
//            throw new IllegalAccountException(getMessage("JournalEntry.SyntheticAccount"));
//        }
//    }
//
//    private void updateJournalEntryBalance(
//            JournalEntry journalEntry,
//            JournalEntryItem item,
//            BigDecimal itemAmountBeforeUpdate,
//            ChangeType changeTypeBeforeUpdate) {
//        BigDecimal balanceCredit = journalEntry.getBalanceCredit();
//        BigDecimal balanceDebit = journalEntry.getBalanceDebit();
//        //oduzmi onu vrednost koju stavke ima pre promene podataka
//        //(stanje zateceno u bazi)
//        if (changeTypeBeforeUpdate == ChangeType.DEBIT) {
//            balanceDebit = balanceDebit.subtract(itemAmountBeforeUpdate);
//        } else {
//            balanceCredit = balanceCredit.subtract(itemAmountBeforeUpdate);
//        }
//        //dodaj novu vrednost
//        if (item.getChangeType() == ChangeType.DEBIT) {
//            balanceDebit = balanceDebit.add(item.getAmount());
//            journalEntry.setBalanceDebit(balanceDebit);
//            journalEntry.setBalanceCredit(balanceCredit);
//            return;
//        }
//        balanceCredit = balanceCredit.add(item.getAmount());
//        journalEntry.setBalanceDebit(balanceDebit);
//        journalEntry.setBalanceCredit(balanceCredit);
//    }
    
    @Transactional(rollbackFor = Exception.class)
    public UpdateJournalEntryResultDTO deleteJournalEntryItem(
            Integer clientID,
            Integer typeID,
            Integer number,
            Integer ordinal,
            Long journalEntryVersion)
            throws JournalEntryItemNotFoundException,
            PostedJournalEntryUpdateException {
        try {
            //remove item from journal entry
            JournalEntryItem item = EM.find(JournalEntryItem.class,
                    new JournalEntryItemPK(clientID,
                    typeID,
                    number,
                    ordinal));
            if (item == null) {
                throw new JournalEntryItemNotFoundException(
                        getMessage("JournalEntry.JournalEntryItemNotExists",
                        clientID,
                        typeID,
                        number,
                        ordinal));
            }
            JournalEntry journalEntry = EM.find(JournalEntry.class,
                    new JournalEntryPK(clientID, typeID, number));
            if (journalEntry.getPosted() == true) {
                throw new PostedJournalEntryUpdateException(
                        getMessage("JournalEntry.PostedJournalEntryUpdate",
                        clientID,
                        typeID,
                        number));
            }
            journalEntry.setVersion(journalEntryVersion);
            EM.lock(journalEntry, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            //remove item from database
            EM.remove(item);

            //update journal entry balance
            if (item.getChangeType() == ChangeType.DEBIT) {
                BigDecimal debitBalance = journalEntry.getBalanceDebit();
                debitBalance = debitBalance.subtract(item.getAmount());
                journalEntry.setBalanceDebit(debitBalance);
            } else {
                BigDecimal creditBalance = journalEntry.getBalanceCredit();
                creditBalance = creditBalance.subtract(item.getAmount());
                journalEntry.setBalanceCredit(creditBalance);
            }
            UpdateJournalEntryResultDTO result = new UpdateJournalEntryResultDTO();
            result.version = journalEntry.getVersion();
            result.debitBalance = journalEntry.getBalanceDebit();
            result.creditBalance = journalEntry.getBalanceCredit();
            result.balance = journalEntry.getBalanceDebit().subtract(
                    journalEntry.getBalanceCredit());
            return result;
        } catch (JournalEntryItemNotFoundException 
                | PostedJournalEntryUpdateException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(getMessage("JournalEntry.OptimisticLock",
                        clientID,
                        typeID,
                        number),ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        getMessage("JournalEntry.Persistence.DeleteItem"),ex);
            }
        } 
    }
    
    @Transactional(rollbackFor = Exception.class)
    public UpdateJournalEntryResultDTO createJournalEntryItem(
            JournalEntryItemDTO dto,
            Long journalEntryVersion)
            throws RemoteException,
            ReferentialIntegrityException,
            IllegalAccountException,
            IllegalBusinessPartnerException,
            JournalEntryConstraintViolationException,
            PostedJournalEntryUpdateException {
        try {
            JournalEntry journalEntry = EM.find(JournalEntry.class,
                    new JournalEntryPK(dto.getClientId(),
                                       dto.getTypeId(),
                                       dto.getJournalEntryNumber()));
            if (journalEntry == null) {
                throw new ReferentialIntegrityException(
                        getMessage("JournalEntry.JournalEntryNotExists",
                        dto.getClientId(),
                        dto.getTypeId(),
                        dto.getJournalEntryNumber()));
            }
            if (journalEntry.getPosted() == true) {
                throw new PostedJournalEntryUpdateException(
                        getMessage("JournalEntry.PostedJournalEntryUpdate",
                        dto.getClientId(),
                        dto.getTypeId(),
                        dto.getJournalEntryNumber()));
            }            
            EM.lock(journalEntry, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            journalEntry.setVersion(journalEntryVersion);
            //check referential integrity***************************************
            OrgUnit orgUnit = null;
            if (dto.getClientId() != null && dto.getUnitId() != null) {
                orgUnit = EM.find(OrgUnit.class, new OrgUnitPK(
                        dto.getUnitId(),
                        dto.getClientId()));
                if (orgUnit == null) {
                    throw new ReferentialIntegrityException(
                            getMessage("JournalEntry.OrgUnitNotExists",
                            dto.getUnitId()));
                }
            }
            Description description = null;
            if (dto.getDescId() != null) {
                description = EM.find(Description.class, dto.getDescId());
                if (description == null) {
                    throw new ReferentialIntegrityException(
                            getMessage("JournalEntry.DescNotExists", dto.getDescId())
                    );
                }
            }
            Account account = null;
            if (dto.getAccountCode() != null && !dto.getAccountCode().isEmpty()) {
                account = EM.find(Account.class, dto.getAccountCode());
                if (account == null) {
                    throw new ReferentialIntegrityException(
                            getMessage("JournalEntry.AccountNotExists", 
                            dto.getAccountCode())
                   );
                }
            }
            BusinessPartner businessPartner = null;
            if (dto.getPartnerCompanyId() != null && !dto.getPartnerCompanyId().isEmpty()) {
                businessPartner = EM.find(BusinessPartner.class, dto.getPartnerCompanyId());
                if (businessPartner == null) {
                    //if user enter business partner that not exists
                    throw new ReferentialIntegrityException(
                            getMessage("JournalEntry.BusinessPartnerNotExists",
                            dto.getPartnerCompanyId()));
                }
            }
            List<ApplicationUser> userList = EM.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME, 
                    ApplicationUser.class)
                    .setParameter(1, dto.getUsername())
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new ReferentialIntegrityException(
                        getMessage("JournalEntry.UserNotExists", 
                                   dto.getUsername())
                );
            }
            //******************************************************************
            //set item**********************************************************
            Integer ordinal =  0;
            for (JournalEntryItem item  : journalEntry.getItems()) {
                if(item.getOrdinalNumber() > ordinal) {
                    ordinal = item.getOrdinalNumber();
                }                
            }
            JournalEntryItem temp = new JournalEntryItem(journalEntry, ordinal + 1 );
            temp.setCreditDebitRelationDate(dto.getCreditDebitRelationDate());
            temp.setDocument(dto.getDocument());
            temp.setDesc(description);
            temp.setOrgUnit(orgUnit);
            temp.setInternalDocument(dto.getInternalDocument());
            temp.setPartner(businessPartner);
            temp.setAccount(account);
            if(account != null) {
                temp.setDetermination(account.getDetermination());                
            }
            temp.setValueDate(dto.getValueDate());
            temp.setUser(userList.get(0));
            if((dto.getDebit() != null ) & (dto.getCredit() != null)) {
                throw new JournalEntryConstraintViolationException("JournalEntryItem.DebitAndCreditItem");
            }
            if(dto.getDebit() != null) {
                temp.setChangeType(ChangeType.DEBIT);
                temp.setAmount(dto.getDebit());
            }
            if(dto.getCredit() != null) {
                temp.setChangeType(ChangeType.CREDIT);
                temp.setAmount(dto.getCredit());
            }
            //******************************************************************            
            this.validate1(temp);
            journalEntry.addItem(temp);
            EM.persist(temp);
            this.updateJournalEntryBalance(journalEntry, temp);
            UpdateJournalEntryResultDTO result = new UpdateJournalEntryResultDTO();
            result.version = journalEntry.getVersion();
            result.debitBalance = journalEntry.getBalanceDebit();
            result.creditBalance = journalEntry.getBalanceCredit();
            result.balance = journalEntry.getBalanceDebit().subtract(journalEntry.getBalanceCredit());
            return result;
        } catch (IllegalBusinessPartnerException 
                | ReferentialIntegrityException 
                | IllegalAccountException 
                | PostedJournalEntryUpdateException 
                | JournalEntryConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(getMessage("JournalEntry.OptimisticLock",
                    dto.getClientId(),
                    dto.getTypeId(),
                    dto.getJournalEntryNumber()),ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(getMessage("JournalEntry.Persistence.CreateItem"),ex);
            }
        } 
    }

    private void validate1(JournalEntryItem item) 
            throws JournalEntryConstraintViolationException,
            IllegalAccountException,
            IllegalBusinessPartnerException {
        List<String> messages = validator.validate(item).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        if (!messages.isEmpty()) {
            throw new JournalEntryConstraintViolationException(getMessage(""), 
                                                               messages);
        }
        Account account = item.getAccount();
        if (item.getPartner() == null) {
            if (!account.getDetermination().equals(AccountDetermination.GENERAL_LEDGER)) {
                throw new IllegalBusinessPartnerException(
                        getMessage("JournalEntry.NullBusinessPartner"));
            }
            //FIXME : moze da proknjizi na sinteticki konto!!!!
            return;
        }
        if (account.getDetermination().equals(AccountDetermination.GENERAL_LEDGER)) {
            throw new IllegalBusinessPartnerException(
                    getMessage("JournalEntry.NotNullBusinessPartner"));
        }

        if (item.getAccount().jeSinteticki()) {
            throw new IllegalAccountException(getMessage("JournalEntry.SyntheticAccount"));
        }
    }

    private void updateJournalEntryBalance(JournalEntry journalEntry,
            JournalEntryItem item) {
        if (item.getChangeType() == ChangeType.DEBIT) {
            BigDecimal saldoDuguje = journalEntry.getBalanceDebit();
            saldoDuguje = saldoDuguje.add(item.getAmount());
            journalEntry.setBalanceDebit(saldoDuguje);
            return;
        }
        BigDecimal saldoPotrazuje = journalEntry.getBalanceCredit();
        saldoPotrazuje = saldoPotrazuje.add(item.getAmount());
        journalEntry.setBalanceCredit(saldoPotrazuje);
    }    

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Integer getAppSetupBusinessYear() {
        try {
            return EM.find(ApplicationSetup.class, 1)
                    .getYear();
        } catch (Exception e) {
            LOG.log(Level.WARNING, 
                    "", 
                    e);
            throw new SystemException(
                    getMessage("JournalEntry.Persistence.CreditRelationDate"),e);
        } 
    }
}