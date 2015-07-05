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
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.GeneralLedger;
import com.invado.finance.domain.journal_entry.JournalEntry;
import com.invado.finance.domain.journal_entry.JournalEntryType;
import com.invado.finance.domain.journal_entry.JournalEntryTypePK;
import com.invado.finance.service.dto.JournalEntryTypeDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.exception.JournalEntryTypeConstraintViolationException;
import com.invado.finance.service.exception.JournalEntryTypeExistsException;
import com.invado.finance.service.exception.JournalEntryTypeNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import static com.invado.finance.Utils.getMessage;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Bobic Dragan
 */
@Transactional(rollbackFor = Exception.class)
public class TypeServicesImpl {
    
    private static final Logger LOG = Logger.getLogger(TypeServicesImpl.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager EM;
    @Autowired
    private Validator validator;

    public Long update(JournalEntryTypeDTO dto)
            throws JournalEntryTypeConstraintViolationException,
            JournalEntryTypeNotFoundException {
        if (dto.getClientId() == null) {
            throw new JournalEntryTypeNotFoundException(
                    getMessage("JournalEntryType.IllegalArgument.ClientId"));
        }
        if (dto.getTypeId() == null) {
            throw new JournalEntryTypeNotFoundException(
                    getMessage("JournalEntryType.IllegalArgument.Id"));
        }
        try {
            JournalEntryType tmp = EM.find(JournalEntryType.class,
                    new JournalEntryTypePK(dto.getTypeId(), dto.getClientId()));
            if (tmp == null) {
                throw new JournalEntryTypeNotFoundException(
                        getMessage("JournalEntryType.JournalEntryTypeNotExists",
                                dto.getClientId(), dto.getTypeId()));
            }
            tmp.setName(dto.getName());
            tmp.setNumber(dto.getJournalEntryNumber());
            tmp.setVersion(dto.getVersion());
            List<String> messages = validator.validate(tmp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new JournalEntryTypeConstraintViolationException(
                        getMessage("JournalEntryType.ConstraintViolationEx"),
                        messages);
            }
            return tmp.getVersion();
        } catch (JournalEntryTypeConstraintViolationException 
                | JournalEntryTypeNotFoundException e) {
            throw e;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        getMessage("JournalEntryType.OptimisticLock",
                                dto.getClientId(),
                                dto.getTypeId())
                );
            } else {
                LOG.log(Level.WARNING,
                        "",
                        ex);
                throw new SystemException(
                        getMessage("JournalEntryType.Persistence.Update"));
            }
        } 
    }

    public void create(JournalEntryTypeDTO DTO)
            throws JournalEntryTypeExistsException,
            JournalEntryTypeConstraintViolationException {
        if (DTO.getClientId() == null) {
            throw new JournalEntryTypeExistsException(
                    getMessage("JournalEntryType.IllegalArgument.ClientId"));
        }
        if (DTO.getTypeId() == null) {
            throw new JournalEntryTypeExistsException(
                    getMessage("JournalEntryType.IllegalArgument.Id"));
        }
        try {
            if (EM.find(JournalEntryType.class,
                    new JournalEntryTypePK(DTO.getTypeId(), DTO.getClientId())) != null) {
                throw new JournalEntryTypeExistsException(
                        getMessage("JournalEntryType.JournalEntryTypeExists",
                                DTO.getClientId(),
                                DTO.getTypeId()));
            }
            JournalEntryType tmp = new JournalEntryType();
            tmp.set(DTO, EM.find(Client.class, DTO.getClientId()));
            List<String> messages = validator.validate(tmp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new JournalEntryTypeConstraintViolationException(
                        getMessage("JournalEntryType.ConstraintViolationEx"),
                        messages);
            }
            EM.persist(tmp);
        } catch (JournalEntryTypeConstraintViolationException | JournalEntryTypeExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(
                    Level.WARNING,
                    "JournalEntryType.Persistence.Create",
                    ex);
            throw new SystemException(
                    getMessage("JournalEntryType.Persistence.Create"));
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public JournalEntryTypeDTO readJournalEntryType(Integer clientID,
            Integer typeID)
            throws JournalEntryTypeNotFoundException {
        try {
            JournalEntryType tmp = EM.find(JournalEntryType.class,
                    new JournalEntryTypePK(typeID, clientID));
            if (tmp == null) {
                throw new JournalEntryTypeNotFoundException(
                        getMessage("JournalEntryType.JournalEntryTypeNotExists",
                                clientID, typeID));
            }
            return tmp.getDTO();
        } catch (JournalEntryTypeNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    getMessage("JournalEntryType.Persistence.Read"));
        } 
    }

    public void delete(Integer clientID, Integer typeID)
            throws JournalEntryTypeNotFoundException,
            ReferentialIntegrityException {
        try {
            JournalEntryType temp = EM.find(JournalEntryType.class,
                    new JournalEntryTypePK(typeID, clientID));
            if (temp == null) {
                throw new JournalEntryTypeNotFoundException(
                        getMessage("JournalEntryType.JournalEntryTypeNotExists",
                                clientID, typeID));
            }
            if (!EM.createNamedQuery(GeneralLedger.READ_BY_JOURNAL_ENTRY_TYPE)
                    .setParameter(1, typeID)
                    .setParameter(2, clientID)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("JournalEntryType.ReferentialIntegrity.RecordedJournalEntry",
                                clientID, typeID));
            }
            if (!EM.createNamedQuery(Analytical.READ_BY_JOURNAL_ENTRY_TYPE)
                    .setParameter(1, typeID)
                    .setParameter(2, clientID)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("JournalEntryType.ReferentialIntegrity.RecordedJournalEntry",
                                clientID, typeID));
            }
            if (!EM.createNamedQuery(JournalEntry.READ_BY_TYPE)
                    .setParameter(1, typeID)
                    .setParameter(2, clientID)
                    .getResultList().isEmpty()) {
                throw new ReferentialIntegrityException(
                        getMessage("JournalEntryType.ReferentialIntegrity.JournalEntry",
                                clientID, typeID));
            }
            EM.remove(temp);
        } catch (JournalEntryTypeNotFoundException | ReferentialIntegrityException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    getMessage("JournalEntryType.Persistence.Delete"));
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadRangeDTO<JournalEntryTypeDTO> readJournalEntryTypePage(PageRequestDTO p)
            throws PageNotExistsException {
        try {
            Integer pageSize = EM.find(ApplicationSetup.class, 1).getPageSize();
            Long countEntities = EM.createNamedQuery(JournalEntryType.COUNT_ALL,
                    Long.class).getSingleResult();
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                throw new PageNotExistsException(
                        getMessage("JournalEntryType.PageNotExists",
                                pageNumber));
            }
            ReadRangeDTO<JournalEntryTypeDTO> result = new ReadRangeDTO<>();
            //if page number is -1 read last page
            if (pageNumber.equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                List<JournalEntryType> data = EM.createNamedQuery(
                        JournalEntryType.READ_ALL_ORDERBY_CLIENT_AND_TYPEID,
                        JournalEntryType.class)
                        .setFirstResult(start)
                        .setMaxResults(pageSize)
                        .getResultList();
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<JournalEntryType> data = EM.createNamedQuery(
                        JournalEntryType.READ_ALL_ORDERBY_CLIENT_AND_TYPEID,
                        JournalEntryType.class)
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
                    getMessage("JournalEntryType.Persistence.ReadAll"));
        } 
    }

    private List<JournalEntryTypeDTO> convertToDTO(List<JournalEntryType> list) {
        List<JournalEntryTypeDTO> result = new ArrayList<>();
        for (JournalEntryType pr : list) {
            result.add(pr.getDTO());
        }
        return result;
    }

}
