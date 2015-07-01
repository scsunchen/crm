package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Client;
import com.invado.core.domain.OrgUnit;
import com.invado.core.domain.OrgUnit_;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
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
 * Created by NikolaB on 6/7/2015.
 */
@Service
public class OrgUnitService {
    private static final Logger LOG = Logger.getLogger(
            OrgUnit.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public OrgUnit create(OrgUnit a) throws com.invado.masterdata.service.exception.IllegalArgumentException,
            javax.persistence.EntityExistsException {
        //check CreateOrgUnitPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("OrgUnit.IllegalArgumentEx"));
        }
        try {
            if (dao.find(OrgUnit.class, a.getId()) != null) {
                throw new javax.persistence.EntityExistsException(
                        Utils.getMessage("OrgUnit.EntityExistsEx", a.getId())
                );
            }
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(a);
            return a;
        } catch (IllegalArgumentException | javax.persistence.EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("OrgUnit.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public OrgUnit update(OrgUnit dto) throws ConstraintViolationException,
            javax.persistence.EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateOrgUnitPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("OrgUnit.IllegalArgumentEx"));
        }
        if (dto.getId() == null ) {
            throw new ConstraintViolationException(
                    Utils.getMessage("OrgUnit.IllegalArgumentEx.Code"));
        }
        try {
            OrgUnit item = dao.find(OrgUnit.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("OrgUnit.EntityNotFoundEx",
                                dto.getId())
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
        } catch (ConstraintViolationException | javax.persistence.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("OrgUnit.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("OrgUnit.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String companyIdNumber) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteOrgUnitPermission
        if (companyIdNumber == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("OrgUnit.IllegalArgumentEx.Code")
            );
        }
        try {
            OrgUnit service = dao.find(OrgUnit.class, companyIdNumber);
            if (service != null) {
                if (dao.createNamedQuery("Invoice.GetByPartner")
                        .setParameter("companyIdNumber", companyIdNumber)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "OrgUnit.ReferentialIntegrityEx.InvoiceItem",
                            companyIdNumber)
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
                    Utils.getMessage("OrgUnit.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public OrgUnit read(Integer id) throws javax.persistence.EntityNotFoundException {
        //TODO : check ReadOrgUnitPermission
        if (id == null) {
            throw new javax.persistence.EntityNotFoundException(
                    Utils.getMessage("OrgUnit.IllegalArgumentEx.Code")
            );
        }
        try {
            OrgUnit OrgUnit = dao.find(OrgUnit.class, id);
            if (OrgUnit == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("OrgUnit.EntityNotFoundEx", id)
                );
            }
            return OrgUnit;
        } catch (javax.persistence.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("OrgUnit.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<OrgUnit> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadOrgUnitPermission
        Integer id = null;
        String name = null;
        Client client = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof String) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("name") && s.getValue() instanceof String) {
                name = (String) s.getValue();
            }
            if (s.getKey().equals("client") && s.getValue() instanceof Client) {
                client = (Client) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, client, id, name);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("OrgUnit.PageNotExists", pageNumber));
            }
            ReadRangeDTO<OrgUnit> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first OrgUnit = last page number * OrgUnits per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.search(dao, client, id, name,  start, pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(this.search(dao, client, id, name,
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
                    Utils.getMessage("OrgUnit.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private Long count(
            EntityManager EM,
            Client client,
            Integer id,
            String name) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<OrgUnit> root = c.from(OrgUnit.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(OrgUnit_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(OrgUnit_.name)),
                    cb.parameter(String.class, "name")));
        }
        if (client != null) {
            criteria.add(cb.equal(root.get(OrgUnit_.client), "client"));
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null ) {
            q.setParameter("id", id);
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", name.toUpperCase() + "%");
        }
        if (client != null ) {
            q.setParameter("client", client);
        }

        return q.getSingleResult();
    }

    private List<OrgUnit> search(EntityManager em,
                                 Client client,
                                 Integer id,
                                 String name,
                                         int first,
                                         int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrgUnit> query = cb.createQuery(OrgUnit.class);
        Root<OrgUnit> root = query.from(OrgUnit.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null ) {
            criteria.add(cb.equal(root.get(OrgUnit_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (client != null) {
            criteria.add(cb.equal(root.get(OrgUnit_.client),"client"));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(root.get(OrgUnit_.name),
                    cb.parameter(String.class, "name")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(OrgUnit_.id)));
        TypedQuery<OrgUnit> typedQuery = em.createQuery(query);
        if (id != null ) {
            typedQuery.setParameter("id", id);
        }
        if (name != null && name.isEmpty() == false) {
            typedQuery.setParameter("name", name.toUpperCase() + "%");
        }
        if (client != null ) {
            typedQuery.setParameter("client", client);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<OrgUnit> readAll(Client client,
                                Integer id,
                                String name) {
        try {
            return this.search(dao, client, id, name, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("OrgUnit.PersistenceEx.ReadAll"), ex);
        }
    }

}