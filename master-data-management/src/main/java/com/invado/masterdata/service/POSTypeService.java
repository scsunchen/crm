package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.POSType;
import com.invado.core.domain.POSType_;
import com.invado.core.exception.*;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.IllegalArgumentException;
import com.invado.masterdata.Utils;
import com.invado.core.dto.POSTypeDTO;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Validator;

import javax.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.stream.Collectors;

/**
 * Created by Nikola on 10/11/2015.
 */
@Service
public class POSTypeService {
    private static final Logger LOG = Logger.getLogger(
            POSType.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public POSType create(POSTypeDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreatePOSTypePermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("POSType.IllegalArgumentEx"));
        }
        if (a.getId() == null ) {
            throw new ConstraintViolationException(
                    Utils.getMessage("POSType.IllegalArgumentEx.Code"));
        }
        if (a.getDescription() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("POSType.IllegalArgumentException.Description"));
        }
        try {
            POSType POSType = new POSType();

            POSType.setId(a.getId());
            POSType.setDescription(a.getDescription());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(POSType);
            return POSType;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("POSType.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public POSType update(POSTypeDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdatePOSTypePermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("POSType.IllegalArgumentEx"));
        }
        if (dto.getId() == null ) {
            throw new ConstraintViolationException(
                    Utils.getMessage("POSType.IllegalArgumentEx.Id"));
        }
        if (dto.getDescription() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("POSType.IllegalArgumentException.Description"));
        }
       try {
            POSType item = dao.find(POSType.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("POSType.EntityNotFoundEx",
                                dto.getId())
                );
            }

            item.setId(dto.getId());
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
                        Utils.getMessage("POSType.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("POSType.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeletePOSTypePermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("POSType.IllegalArgumentEx.Id")
            );
        }
        try {
            POSType service = dao.find(POSType.class, id);
            if (service != null) {
                if (dao.createNamedQuery("POSType.ReadByPOSType")
                        .setParameter("id", id)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "POSType.ReferentialIntegrityEx.Client",
                            id)
                    );
                }
                dao.remove(service);
                dao.flush();
            }
        } catch (ReferentialIntegrityException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("POSType.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public POSType read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadPOSTypePermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("POSType.IllegalArgumentEx.Id")
            );
        }
        try {
            POSType POSType = dao.find(POSType.class, id);
            if (POSType == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("POSType.EntityNotFoundEx", id)
                );
            }
            return POSType;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("POSType.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<POSTypeDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadPOSTypePermission
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
                        Utils.getMessage("POSType.PageNotExists", pageNumber));
            }
            ReadRangeDTO<POSTypeDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first POSType = last page number * POSTypes per page
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
                    Utils.getMessage("POSType.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<POSTypeDTO> convertToDTO(List<POSType> lista) {
        List<POSTypeDTO> listaDTO = new ArrayList<>();
        for (POSType pr : lista) {
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
        Root<POSType> root = c.from(POSType.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(POSType_.id),
                    cb.parameter(Integer.class, "code")));
        }
        if (description != null && description.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(POSType_.description)),
                    cb.parameter(String.class, "name")));
        }


        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null ) {
            q.setParameter("id", id);
        }
        if (description != null && description.isEmpty() == false) {
            q.setParameter("description", description.toUpperCase() + "%");
        }


        return q.getSingleResult();
    }

    private List<POSType> search(EntityManager em,
                                  Integer id,
                                  String description,
                                  int first,
                                  int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<POSType> query = cb.createQuery(POSType.class);
        Root<POSType> root = query.from(POSType.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null ) {
            criteria.add(cb.equal(root.get(POSType_.id),
                    cb.parameter(Integer.class, "code")));
        }
        if (description != null && description.isEmpty() == false) {
            criteria.add(cb.like(root.get(POSType_.description),
                    cb.parameter(String.class, "description")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(POSType_.id)));
        TypedQuery<POSType> typedQuery = em.createQuery(query);
        if (id != null ) {
            typedQuery.setParameter("code", id);
        }
        if (description != null && description.isEmpty() == false) {
            typedQuery.setParameter("description", description.toUpperCase() + "%");
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<POSTypeDTO> readAll(
            Integer id,
            String description) {
        try {
            return convertToDTO(this.search(dao, id, description, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("POSType.PersistenceEx.ReadAll"), ex);
        }
    }
}
