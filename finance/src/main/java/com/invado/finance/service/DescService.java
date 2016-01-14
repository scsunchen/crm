/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.Description;
import com.invado.finance.domain.journal_entry.GeneralLedger;
import com.invado.finance.domain.journal_entry.JournalEntryItem;
import com.invado.finance.service.dto.DescDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import com.invado.finance.service.exception.DescriptionConstraintViolationException;
import com.invado.finance.service.exception.DescriptionNotFoundException;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import static com.invado.finance.Utils.getMessage;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bobic Dragan
 */
@Service
public class DescService {

    private static final Logger LOG = Logger.getLogger(
            ArticleService.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager EM;
    @Inject
    private Validator validator;
    
    @Transactional(rollbackFor = Exception.class)
    public Long update(DescDTO dto) throws DescriptionConstraintViolationException,
                                           DescriptionNotFoundException {
        try {
            if (dto.getId() == null || EM.find(Description.class, dto.getId()) == null) {
                throw new DescriptionNotFoundException(getMessage("Desc.DescNotExists")
                );
            }
            Description temp = EM.find(Description.class, dto.getId(), LockModeType.OPTIMISTIC);
            temp.setName(dto.getName());
            //validacija identifikatora opisa
            List<String> messages = validator.validate(temp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new DescriptionConstraintViolationException(
                        getMessage("Desc.ConstraintViolationEx"),
                        messages);
            }
            if(temp.getVersion().compareTo(dto.getVersion()) != 0) {
                throw new OptimisticLockException();
            };
            return temp.getVersion();
        } catch (DescriptionConstraintViolationException | DescriptionNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        getMessage("Desc.OptimisticLock", dto.getId()),ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(getMessage("Desc.Persistence.Update"),ex);
            }
        } 
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void create(DescDTO dto) throws DescriptionConstraintViolationException {
        try {
            Description temp = new Description();
            temp.set(dto);
            //validacija identifikatora opisa
            List<String> messages = validator.validate(temp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!messages.isEmpty()) {
                throw new DescriptionConstraintViolationException(
                        getMessage("Desc.ConstraintViolationEx"),
                        messages);
            }
            EM.persist(temp);
        } catch (DescriptionConstraintViolationException  ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("Desc.Persistence.Create"),ex);
        } 
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id)
            throws ReferentialIntegrityException {
        try {
            Description temp = EM.find(Description.class, id);
            if (temp != null) {
                if (!EM.createNamedQuery(Analytical.READ_BY_DESCRIPTION)
                        .setParameter(1, id)
                        .getResultList().isEmpty()) {
                    throw new ReferentialIntegrityException(
                            getMessage("Desc.ReferentialIntegrity.RecordedJournalEntry",
                                    id));
                }
                if (!EM.createNamedQuery(GeneralLedger.READ_BY_DESCRIPTION)
                        .setParameter(1, id)
                        .getResultList().isEmpty()) {
                    throw new ReferentialIntegrityException(
                            getMessage("Desc.ReferentialIntegrity.RecordedJournalEntry",
                                    id));
                }
                if (!EM.createNamedQuery(JournalEntryItem.READ_BY_DESCRIPTION)
                        .setParameter(1, id)
                        .getResultList().isEmpty()) {
                    throw new ReferentialIntegrityException(
                            getMessage("Desc.ReferentialIntegrity.JournalEntry",
                                    id));
                }
                EM.remove(temp);
            }
        } catch (ReferentialIntegrityException iz) {
            throw iz;
        } catch (Exception iz) {
            LOG.log(Level.WARNING, "", iz);
            throw new SystemException(getMessage("Desc.Persistence.Delete"),iz);
        } 
    }
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public DescDTO read(Integer id) throws DescriptionNotFoundException {
        try {
            Description temp = EM.find(Description.class, id);
            if (temp == null) {
                throw new DescriptionNotFoundException(getMessage("Desc.DescNotExists"));
            }
            return temp.getDTO();
        } catch (DescriptionNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("Desc.Persistence.Read"),ex);
        } 
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadRangeDTO<DescDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        try {
            Integer pageSize = EM.find(ApplicationSetup.class, 1).getPageSize();
            Long countEntities = EM.createNamedQuery(
                    Description.COUNT_ALL,
                    Long.class).getSingleResult();
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                throw new PageNotExistsException(
                        getMessage("Description.PageNotExists",
                                pageNumber));
            }
            ReadRangeDTO<DescDTO> result = new ReadRangeDTO<>();
            //if page number is -1 read last page
            if (pageNumber.equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                List<Description> data = EM.createNamedQuery(
                        Description.READ_ALL_ORDERBY_ID, Description.class)
                        .setFirstResult(start)
                        .setMaxResults(pageSize)
                        .getResultList();
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<Description> data = EM.createNamedQuery(
                        Description.READ_ALL_ORDERBY_ID, Description.class)
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
            throw new SystemException(getMessage("Desc.Persistence.ReadAll"),ex);
        } 
    }

    private List<DescDTO> convertToDTO(List<Description> list) {
        List<DescDTO> result = new ArrayList<>();
        for (Description desc : list) {
            result.add(desc.getDTO());
        }
        return result;
    }

}
