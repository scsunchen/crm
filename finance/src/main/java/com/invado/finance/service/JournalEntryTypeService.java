package com.invado.finance.service;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import com.invado.finance.service.exception.JournalEntryTypeNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.service.exception.ClientNotFoundException;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bobic Dragan
 */

@Service
public class JournalEntryTypeService {

    private static final Logger LOG = Logger.getLogger(JournalEntryTypeService.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager EM;
    @Inject
    private Validator validator;

    @Transactional(readOnly = true)
    public List<JournalEntryTypeDTO> readJournalEntryTypeByNameAndClient(Integer clientId,
            String name) {
        try {
            return EM.createNamedQuery(JournalEntryType.READ_BY_CLIENT_AND_NAME,
                            JournalEntryType.class)
                    .setParameter("clientId", clientId)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList()
                    .stream()
                    .map(JournalEntryType::getDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("JournalEntryType.Persistence.Read"),ex);
        }
    }
    
    @Transactional(readOnly = true)
    public List<JournalEntryTypeDTO> readJournalEntryTypeByName(String name) {
        try {
            return EM.createNamedQuery(JournalEntryType.READ_BY_NAME,
                            JournalEntryType.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList()
                    .stream()
                    .map(JournalEntryType::getDTO)
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("JournalEntryType.Persistence.Read"),ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
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
                        getMessage("JournalEntryType.JournalEntryTypeNotExists"));
            }
            tmp.setName(dto.getName());
            tmp.setNumber(dto.getJournalEntryNumber());
            if (tmp.getVersion().compareTo(dto.getVersion()) != 0) {
                throw new OptimisticLockException();
            }
            List<String> messages = validator.validate(tmp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new JournalEntryTypeConstraintViolationException(
                        getMessage("JournalEntryType.ConstraintViolationEx"),
                        messages);
            }
            return tmp.getVersion();
        } catch (JournalEntryTypeConstraintViolationException | JournalEntryTypeNotFoundException e) {
            throw e;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        getMessage("JournalEntryType.OptimisticLock"),
                        ex
                );
            } else {
                LOG.log(Level.WARNING,
                        "",
                        ex);
                throw new SystemException(
                        getMessage("JournalEntryType.Persistence.Update"), ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(JournalEntryTypeDTO DTO)
            throws JournalEntryTypeConstraintViolationException,
            ClientNotFoundException {
        if (DTO == null) {
            throw new SystemException(getMessage("JournalEntryType.Persistence.Create"));
        }
        if (DTO.getClientId() == null) {
            throw new ClientNotFoundException(
                    getMessage("JournalEntryType.IllegalArgument.ClientId"));
        }
        try {
            Integer id = (Integer) EM.createNamedQuery(JournalEntryType.READ_MAXID_BY_CLIENT)
                    .setParameter(1, DTO.getClientId())
                    .getSingleResult();
            Client client = EM.find(Client.class, DTO.getClientId());
            if (client == null) {
                throw new ClientNotFoundException(getMessage("JournalEntryType.ClientNotFound"));
            }
            JournalEntryType tmp = new JournalEntryType();
            tmp.setClient(client);
            tmp.setId(id== null ? 1 : id + 1);
            tmp.setName(DTO.getName());
            tmp.setNumber(DTO.getJournalEntryNumber());
            List<String> messages = validator.validate(tmp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new JournalEntryTypeConstraintViolationException(
                        getMessage("JournalEntryType.ConstraintViolationEx"),
                        messages);
            }
            EM.persist(tmp);
        } catch (JournalEntryTypeConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    getMessage("JournalEntryType.Persistence.Create"),
                    ex
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public JournalEntryTypeDTO read(Integer clientID,
            Integer typeID)
            throws JournalEntryTypeNotFoundException {
        try {
            JournalEntryType tmp = EM.find(JournalEntryType.class,
                    new JournalEntryTypePK(typeID, clientID));
            if (tmp == null) {
                throw new JournalEntryTypeNotFoundException(
                        getMessage("JournalEntryType.JournalEntryTypeNotExists"));
            }
            return tmp.getDTO();
        } catch (JournalEntryTypeNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    getMessage("JournalEntryType.Persistence.Read"),
                    ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer clientID, Integer typeID)
            throws JournalEntryTypeNotFoundException,
            ReferentialIntegrityException {
        try {
            JournalEntryType temp = EM.find(JournalEntryType.class,
                    new JournalEntryTypePK(typeID, clientID));
            if (temp != null) {
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
            }
        } catch (ReferentialIntegrityException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    getMessage("JournalEntryType.Persistence.Delete"), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadRangeDTO<JournalEntryTypeDTO> readPage(PageRequestDTO p)
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
            throw new SystemException(getMessage("JournalEntryType.Persistence.ReadAll"), ex);
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
