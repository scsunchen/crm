package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.DocumentType;
import com.invado.core.domain.DocumentType_;
import com.invado.core.dto.DocumentTypeDTO;
import com.invado.core.exception.*;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.IllegalArgumentException;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Nikola on 23/12/2015.
 */
@Service
public class DocumentTypeService {

    private static final Logger LOG = Logger.getLogger(
            DocumentType.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Inject
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public DocumentType create(DocumentTypeDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateDocumentTypePermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("DocumentType.IllegalArgumentEx"));
        }
        if (a.getDescription() == null ) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DocumentType.IllegalArgumentEx.Description"));
        }
        try {
            DocumentType documentType = new DocumentType();

            documentType.setDescription(a.getDescription());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(documentType);
            return documentType;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DocumentType.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public DocumentType update(DocumentTypeDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateDocumentTypePermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DocumentType.IllegalArgumentEx"));
        }
        if (dto.getDescription() == null ) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DocumentType.IllegalArgumentEx.Description"));
        }
       try {
            DocumentType item = dao.find(DocumentType.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("DocumentType.EntityNotFoundEx",
                                dto.getId())
                );
            }

            item.setDescription(dto.getDescription());
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setVersion(dto.getVersion());
            List<String> msgs = validator.validate(item).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            dao.flush();
            return item;
        } catch (ConstraintViolationException | EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("DocumentType.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("DocumentType.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteDocumentTypePermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("DocumentType.IllegalArgumentEx.Id")
            );
        }
        try {
            DocumentType documentType = dao.find(DocumentType.class, id);
            if (documentType != null) {
                if (dao.createNamedQuery("BusinessPartnerDocument.GetByType")
                        .setParameter("type", documentType)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "DocumentType.ReferentialIntegrityEx.Document",
                            id)
                    );
                }
                dao.remove(documentType);
                dao.flush();
            }
        } catch (ReferentialIntegrityException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DocumentType.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public DocumentType read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadDocumentTypePermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("DocumentType.IllegalArgumentEx.Id")
            );
        }
        try {
            DocumentType documentType = dao.find(DocumentType.class, id);
            if (documentType == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("DocumentType.EntityNotFoundEx", id)
                );
            }
            return documentType;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DocumentType.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<DocumentTypeDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadDocumentTypePermission
        Integer id = null;
        String description = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id =(Integer)s.getValue();
            }
            if (s.getKey().equals("description") && s.getValue() instanceof String) {
                description = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, id, description);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("DocumentType.PageNotExists", pageNumber));
            }
            ReadRangeDTO<DocumentTypeDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first DocumentType = last page number * DocumentTypes per page
                int start = numberOfPages.intValue() * pageSize;

                result.setData(convertToDTO(this.search(dao, id, description,  start, pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao, id, description,
                        p.getPage() * pageSize,
                        pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DocumentType.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<DocumentTypeDTO> convertToDTO(List<DocumentType> lista) {
        List<DocumentTypeDTO> listaDTO = new ArrayList<>();
        for (DocumentType pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }
    private Long count(
            EntityManager EM,
            Integer id,
            String description) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<DocumentType> root = c.from(DocumentType.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(DocumentType_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (description != null && description.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(DocumentType_.description)),
                    cb.parameter(String.class, "description")));
        }


        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null ) {
            q.setParameter("id", id);
        }
        if (description != null && description.isEmpty() == false) {
            q.setParameter("name", description.toUpperCase() + "%");
        }


        return q.getSingleResult();
    }

    private List<DocumentType> search(EntityManager em,
                                  Integer id,
                                  String description,
                                  int first,
                                  int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DocumentType> query = cb.createQuery(DocumentType.class);
        Root<DocumentType> root = query.from(DocumentType.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null ) {
            criteria.add(cb.equal(root.get(DocumentType_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (description != null && description.isEmpty() == false) {
            criteria.add(cb.like(root.get(DocumentType_.description),
                    cb.parameter(String.class, "name")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(DocumentType_.id)));
        TypedQuery<DocumentType> typedQuery = em.createQuery(query);
        if (id != null ) {
            typedQuery.setParameter("id", id);
        }
        if (description != null && description.isEmpty() == false) {
            typedQuery.setParameter("description", description.toUpperCase() + "%");
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<DocumentTypeDTO> readAll(
            Integer id,
            String description) {
        try {
            return convertToDTO(this.search(dao, id, description, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("DocumentType.PersistenceEx.ReadAll"), ex);
        }
    }



}