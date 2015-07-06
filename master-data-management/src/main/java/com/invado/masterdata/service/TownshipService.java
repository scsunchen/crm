package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Township;
import com.invado.core.domain.Township_;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
import com.invado.masterdata.service.exception.EntityExistsException;
import com.invado.masterdata.service.exception.EntityNotFoundException;
import com.invado.masterdata.service.exception.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
 * Created by NikolaB on 6/10/2015.
 */
@Service
public class TownshipService {
    private static final Logger LOG = Logger.getLogger(
            Township.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public Township create(Township a) throws IllegalArgumentException,
            EntityExistsException {
        //check CreateTownshipPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Township.IllegalArgumentEx"));
        }
        try {

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(a);
            return a;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Township.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Township update(Township dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateTownshipPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Township.IllegalArgumentEx"));
        }
        if (dto.getCode() == null ) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Township.IllegalArgumentEx.Code"));
        }
        try {
            Township item = dao.find(Township.class,
                    dto.getCode(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Township.EntityNotFoundEx",
                                dto.getCode())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setName(dto.getName());
            /*Dodaj ovde ostalo*/
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
                        Utils.getMessage("Township.OptimisticLockEx",
                                dto.getCode()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Township.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String code) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteTownshipPermission
        if (code == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Township.IllegalArgumentEx.Code")
            );
        }
        try {
            Township service = dao.find(Township.class, code);
            if (service != null) {
                if (dao.createNamedQuery("Client.ReadByTownship")
                        .setParameter("code", code)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "Township.ReferentialIntegrityEx.Client",
                            code)
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
                    Utils.getMessage("Township.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Township read(String code) throws EntityNotFoundException {
        //TODO : check ReadTownshipPermission
        if (code == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("Township.IllegalArgumentEx.Code")
            );
        }
        try {
            Township Township = dao.find(Township.class, code);
            if (Township == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Township.EntityNotFoundEx", code)
                );
            }
            return Township;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Township.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<Township> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadTownshipPermission
        String code = null;
        String name = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("code") && s.getValue() instanceof String) {
                code =(String)s.getValue();
            }
            if (s.getKey().equals("name") && s.getValue() instanceof String) {
                name = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, code, name);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Township.PageNotExists", pageNumber));
            }
            ReadRangeDTO<Township> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Township = last page number * Townships per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.search(dao, code, name,  start, pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(this.search(dao, code, name,
                        p.getPage() * pageSize,
                        pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Township.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private Long count(
            EntityManager EM,
            String code,
            String name) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Township> root = c.from(Township.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (code != null) {
            criteria.add(cb.equal(cb.upper(root.get(Township_.code)),
                    cb.parameter(Integer.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Township_.name)),
                    cb.parameter(String.class, "name")));
        }


        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (code != null ) {
            q.setParameter("code", code);
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", name.toUpperCase() + "%");
        }


        return q.getSingleResult();
    }

    private List<Township> search(EntityManager em,
                                 String code,
                                 String name,
                                 int first,
                                 int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Township> query = cb.createQuery(Township.class);
        Root<Township> root = query.from(Township.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (code != null ) {
            criteria.add(cb.equal(root.get(Township_.code),
                    cb.parameter(Integer.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(root.get(Township_.name),
                    cb.parameter(String.class, "name")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Township_.code)));
        TypedQuery<Township> typedQuery = em.createQuery(query);
        if (code != null ) {
            typedQuery.setParameter("code", code);
        }
        if (name != null && name.isEmpty() == false) {
            typedQuery.setParameter("name", name.toUpperCase() + "%");
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Township> readAll(
                                 String code,
                                 String name) {
        try {
            return this.search(dao, code, name, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Township.PersistenceEx.ReadAll"), ex);
        }
    }
}
