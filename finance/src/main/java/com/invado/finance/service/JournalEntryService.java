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
import com.invado.finance.service.dto.JournalEntryDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.JournalEntry;
import com.invado.finance.domain.journal_entry.JournalEntryItem;
import com.invado.finance.domain.journal_entry.JournalEntryItemPK;
import com.invado.finance.domain.journal_entry.JournalEntryPK;
import com.invado.finance.domain.journal_entry.JournalEntryType;
import com.invado.finance.domain.journal_entry.JournalEntryTypePK;
import com.invado.finance.domain.journal_entry.JournalEntry_;
import com.invado.finance.service.exception.IllegalAccountException;
import com.invado.finance.service.exception.JournalEntryConstraintViolationException;
import com.invado.finance.service.exception.JournalEntryExistsException;
import com.invado.finance.service.exception.JournalEntryNotFoundException;
import com.invado.finance.service.exception.PostedJournalEntryDeletionException;
import com.invado.finance.service.exception.PostedJournalEntryUpdateException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import com.invado.finance.service.dto.AccountDTO;
import com.invado.finance.service.dto.JournalEntryItemDTO;
import com.invado.finance.service.dto.JournalEntryReportDTO;
import com.invado.finance.service.exception.AccountNotFoundException;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.domain.journal_entry.JournalEntryPK_;
import java.util.Arrays;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bobic Dragan
 */
@Service
public class JournalEntryService {

    private static final Logger LOG = Logger.getLogger(JournalEntryService.class.getName());
    @PersistenceContext(name = "unit")
    private EntityManager EM;
    @Inject
    private Validator validator;
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadRangeDTO<JournalEntryDTO> readJournalEntryPage(PageRequestDTO p) 
            throws PageNotExistsException {
        try {
            Boolean isPosted = null;
            for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
                if (s.getKey().equals("isPosted")
                        && s.getValue() instanceof Boolean) {
                    isPosted = (Boolean) s.getValue();
                }
            }
            Integer pageSize = EM.find(ApplicationSetup.class, 1).getPageSize();
            Long countEntities = this.count(EM, isPosted);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                throw new PageNotExistsException(
                        getMessage("JournalEntry.PageNotExists",
                                pageNumber));
            }
            ReadRangeDTO<JournalEntryDTO> result = new ReadRangeDTO<>();
            //if page number is -1 read last page
            if (pageNumber.equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                List<JournalEntry> data = this.search(EM, isPosted, start, pageSize);
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<JournalEntry> data = this.search(EM,
                        isPosted,
                        pageNumber * pageSize,
                        pageSize);
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("JournalEntry.Persistence.ReadAll"),
                                      ex);
        }
    }

    public Long count(EntityManager EM, Boolean isPosted) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<JournalEntry> root = c.from(JournalEntry.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (isPosted != null) {
            criteria.add(
                    cb.equal(root.get(JournalEntry_.posted),
                            cb.parameter(Boolean.class, "isPosted")));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (isPosted != null) {
            q.setParameter("isPosted", isPosted);
        }
        return q.getSingleResult();
    }

    public List<JournalEntry> search(EntityManager EM,
            Boolean isPosted,
            int start,
            int range) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<JournalEntry> c = cb.createQuery(JournalEntry.class);
        Root<JournalEntry> root = c.from(JournalEntry.class);
        c.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (isPosted != null) {
            criteria.add(cb.equal(root.get(JournalEntry_.posted),
                    cb.parameter(Boolean.class, "isPosted")));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0]))).orderBy(
                cb.asc(root.get(JournalEntry_.pk).get(JournalEntryPK_.client)),
                cb.asc(root.get(JournalEntry_.pk).get(JournalEntryPK_.type)),
                cb.asc(root.get(JournalEntry_.pk).get(JournalEntryPK_.number)));
        TypedQuery<JournalEntry> q = EM.createQuery(c);
        if (isPosted != null) {
            q.setParameter("isPosted", isPosted);
        }
        q.setFirstResult(start);
        q.setMaxResults(range);
        return q.getResultList();
    }

    private List<JournalEntryDTO> convertToDTO(List<JournalEntry> lista) {
        List<JournalEntryDTO> listaDTO = new ArrayList<>();
        for (JournalEntry pr : lista) {
            listaDTO.add(pr.getReadAllDTO());
        }
        return listaDTO;
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public JournalEntryDTO readJournalEntry(Integer clientID,
            Integer typeID,
            Integer number)
            throws JournalEntryNotFoundException {
        try {
            JournalEntry temp = EM.find(JournalEntry.class,
                    new JournalEntryPK(clientID, typeID, number)
            );
            if (temp == null) {
                throw new JournalEntryNotFoundException(
                        getMessage("JournalEntry.JournalEntryNotExists",
                                clientID,
                                typeID,
                                number));
            }
            return temp.getReadAllDTO();
        } catch (JournalEntryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "", e);
            //poruka je greska prilikom prikaza knjiznog naloga
            throw new SystemException(getMessage("JournalEntry.Persistence.ReadAll"),e);
        } 
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void deleteJournalEntry(
            Integer clientID,
            Integer typeID,
            Integer number)
            throws JournalEntryNotFoundException,
            PostedJournalEntryDeletionException {
        try {
            JournalEntry journalEntry = EM.find(JournalEntry.class,
                    new JournalEntryPK(clientID, typeID, number));
            if (journalEntry == null) {
                throw new JournalEntryNotFoundException(
                        getMessage("JournalEntry.JournalEntryNotExists",
                                clientID,
                                typeID,
                                number)
                );
            }
            if (journalEntry.getPosted() == true) {
                throw new PostedJournalEntryDeletionException(
                        getMessage("JournalEntry.PostedJournalEntryDeletion",
                                clientID,
                                typeID,
                                number)
                );
            }
            EM.remove(journalEntry);
        } catch (JournalEntryNotFoundException | PostedJournalEntryDeletionException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("JournalEntry.Persistence.Delete"), ex);
        } 
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void createJournalEntry(JournalEntryDTO dto) 
            throws JournalEntryExistsException,
            ReferentialIntegrityException,
            JournalEntryConstraintViolationException {
        if (dto.getClientId() == null) {
            throw new ReferentialIntegrityException(
                    getMessage("JournalEntry.IllegalArgument.ClientId"));
        }
        if (dto.getTypeId() == null) {
            throw new ReferentialIntegrityException(
                    getMessage("JournalEntry.IllegalArgument.TypeId"));
        }
        if (dto.getJournalEntryNumber() == null) {
            throw new ReferentialIntegrityException(
                    getMessage("JournalEntry.IllegalArgument.Number"));
        }
        if (dto.getRecordDate()== null) {
            throw new ReferentialIntegrityException(
                    getMessage("JournalEntry.IllegalArgument.Date"));
        }
        try {
            JournalEntryType type = EM.find(JournalEntryType.class,
                    new JournalEntryTypePK(dto.getTypeId(), dto.getClientId())
            );
            if (type == null) {
                throw new ReferentialIntegrityException(
                        getMessage("JournalEntry.ReferentialIntegrity.Type",
                                dto.getClientId(),
                                dto.getTypeId()));
            }
            if (EM.find(JournalEntry.class, new JournalEntryPK(dto.getClientId(),
                    dto.getTypeId(),
                    dto.getJournalEntryNumber()))
                    != null) {
                throw new JournalEntryExistsException(
                        getMessage("JournalEntry.JournalEntryExists",
                                dto.getClientId(),
                                dto.getTypeId(),
                                dto.getJournalEntryNumber()));
            }
            JournalEntry temp = new JournalEntry(type, dto.getJournalEntryNumber());
            Integer businessYear = EM.find(ApplicationSetup.class, 1).getYear();
            
            if(businessYear.compareTo(dto.getRecordDate().getYear()) != 0){
                throw new JournalEntryConstraintViolationException("",
                        Arrays.asList(getMessage("JournalEntry.ConstraintViolation.Year")));
            }
            temp.setRecordDate(dto.getRecordDate());
            temp.setBalanceDebit(BigDecimal.ZERO);
            temp.setBalanceCredit(BigDecimal.ZERO);
            temp.setPosted(Boolean.FALSE);
            List<String> validationMessages = validator.validate(temp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (validationMessages.isEmpty() == false) {
                throw new JournalEntryConstraintViolationException(
                        getMessage("JournalEntry.ConstraintViolation"), validationMessages);
            }
            EM.persist(temp);
            //provera broja knjiznog naloga
            if (temp.getNumber() > type.getNumber()) {
                type.setNumber(temp.getNumber());
            }
        } catch (JournalEntryExistsException | ReferentialIntegrityException 
                | JournalEntryConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("JournalEntry.Persistence.Create"), ex);
        } 
    }
    
    @Transactional(rollbackFor = Exception.class)
    public Long updateJournalEntry(JournalEntryDTO dto) throws JournalEntryNotFoundException,
            JournalEntryConstraintViolationException,
            PostedJournalEntryUpdateException,
            ReferentialIntegrityException {
        if (dto.getClientId() == null) {
            throw new ReferentialIntegrityException(
                    getMessage("JournalEntry.IllegalArgument.ClientId"));
        }
        if (dto.getTypeId() == null) {
            throw new ReferentialIntegrityException(
                    getMessage("JournalEntry.IllegalArgument.TypeId"));
        }
        if (dto.getJournalEntryNumber() == null) {
            throw new ReferentialIntegrityException(
                    getMessage("JournalEntry.IllegalArgument.Number"));
        }
        if (dto.getRecordDate()== null) {
            throw new ReferentialIntegrityException(
                    getMessage("JournalEntry.IllegalArgument.Date"));
        }
        try {
            JournalEntry temp = EM.find(JournalEntry.class,
                    new JournalEntryPK(dto.getClientId(),
                            dto.getTypeId(),
                            dto.getJournalEntryNumber())
            );
            if (temp == null) {
                throw new JournalEntryNotFoundException(
                        getMessage("JournalEntry.JournalEntryNotExists",
                                dto.getClientId(),
                                dto.getTypeId(),
                                dto.getJournalEntryNumber()));
            }
            if (temp.getPosted() == true) {
                throw new PostedJournalEntryUpdateException(
                        getMessage("JournalEntry.PostedJournalEntryUpdate",
                                dto.getClientId(),
                                dto.getTypeId(),
                                dto.getJournalEntryNumber())
                );
            }

            Integer businessYear = EM.find(ApplicationSetup.class, 1).getYear();
            if(businessYear.compareTo(dto.getRecordDate().getYear()) != 0){
                throw new JournalEntryConstraintViolationException("",
                        Arrays.asList(getMessage("JournalEntry.ConstraintViolation.Year")));
            }
            temp.setRecordDate(dto.getRecordDate());
            temp.setVersion(dto.getVersion());
            List<String> messages = validator.validate(temp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new JournalEntryConstraintViolationException(
                        getMessage("JournalEntry.ConstraintViolation"), messages);
            }
            return temp.getVersion();
        } catch (JournalEntryNotFoundException | JournalEntryConstraintViolationException 
                | PostedJournalEntryUpdateException e) {
            throw e;
        } catch (Exception e) {
            if (e instanceof OptimisticLockException
                    || e.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        getMessage("JournalEntry.OptimisticLock",
                                dto.getClientId(),
                                dto.getTypeId(),
                                dto.getJournalEntryNumber()),e);
            } else {
                LOG.log(Level.WARNING, "",
                        e);
                throw new SystemException(
                        getMessage("JournalEntry.Persistence.Update"),e);
            }
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public JournalEntryItemDTO getPreviousJournalEntryItem(Integer clientID,
            Integer typeID,
            Integer number) {
        try {
            List<Integer> ordinalNumbers = EM.createNamedQuery(
                    JournalEntryItem.READ_JOURNAL_ENTRY_MAX_ITEM_ORDINAL,
                    Integer.class)
                    .setParameter(1, clientID)
                    .setParameter(2, typeID)
                    .setParameter(3, number)
                    .getResultList();
            Integer ordinalNumber = 0;
            if (ordinalNumbers.isEmpty() == false) {
                ordinalNumber = ordinalNumbers.get(0);
            }
            JournalEntryItem temp = null;
            if (ordinalNumber != null && ordinalNumber > 0) {
                temp = EM.find(JournalEntryItem.class,
                        new JournalEntryItemPK(clientID,
                                typeID,
                                number,
                                ordinalNumber)
                );
            }
            return temp == null ? null : temp.getDTO();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "", e);
            throw new SystemException(getMessage("JournalEntry.Persistence.ReadLastItem"),e);
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public JournalEntryReportDTO printJournalEntry(Integer clientID,
            Integer typeID,
            Integer number)
            throws JournalEntryNotFoundException {
        try {
            JournalEntry temp = EM.find(JournalEntry.class,
                    new JournalEntryPK(clientID, typeID, number)
            );
            if (temp == null) {
                throw new JournalEntryNotFoundException(
                        getMessage("JournalEntry.JournalEntryNotExists",
                        clientID,
                        typeID,
                        number));
            }
            String naziv = EM.find(Client.class, clientID).getName();
            return temp.getPrintJournalEntryDTO(naziv);
        } catch (JournalEntryNotFoundException iz) {
            throw iz;
        } catch (Exception iz) {
            LOG.log(Level.WARNING, "", iz);
            throw new SystemException(getMessage("JournalEntry.Persistence.Print"),iz);
        }
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public AccountDTO checkAccount(String accountNumber)
            throws AccountNotFoundException,
            IllegalAccountException {
        try {
            Account account = EM.find(Account.class, accountNumber);
            if (account == null) {
                throw new AccountNotFoundException(
                        getMessage("JournalEntry.AccountNotExists", accountNumber)
                );
            }
            if (account.jeSinteticki()) {
                throw new IllegalAccountException(
                        getMessage("JournalEntry.SyntheticAccount"));
            }
            return account.getDTO();
        } catch (AccountNotFoundException | IllegalAccountException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "JournalEntry.Persistence.CheckAccount",
                    ex);
            throw new SystemException(
                    getMessage("JournalEntry.Persistence.CheckAccount"),ex
            );
        } 
    }

}