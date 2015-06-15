package com.invado.customer.relationship.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.BusinessPartner;
import com.invado.customer.relationship.Utils;
import com.invado.customer.relationship.domain.BusinessPartnerRelationshipTerms;
import com.invado.customer.relationship.domain.BusinessPartnerRelationshipTerms_;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.exception.*;
import com.invado.customer.relationship.service.exception.ConstraintViolationException;
import com.invado.customer.relationship.service.exception.EntityNotFoundException;
import com.invado.customer.relationship.service.exception.IllegalArgumentException;
import com.invado.customer.relationship.service.exception.PageNotExistsException;
import com.invado.customer.relationship.service.exception.ReferentialIntegrityException;
import com.invado.customer.relationship.service.exception.SystemException;
import com.invado.customer.relationship.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.EntityExistsException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by NikolaB on 6/13/2015.
 */
@Service
public class BusinessPartnerRelationshipTermsService {

    private static final Logger LOG = Logger.getLogger(
            BusinessPartnerRelationshipTerms.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartnerRelationshipTerms create(BusinessPartnerRelationshipTerms a) throws IllegalArgumentException,
            javax.persistence.EntityExistsException {
        //check CreateBusinessPartnerRelationshipTermsPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx"));
        }
        try {
            if (dao.find(BusinessPartnerRelationshipTerms.class, a.getId()) != null) {
                throw new javax.persistence.EntityExistsException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.EntityExistsEx", a.getId())
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
        } catch (IllegalArgumentException | EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartnerRelationshipTerms update(BusinessPartnerRelationshipTerms dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBusinessPartnerRelationshipTermsPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx"));
        }
        if (dto.getId() == null ) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx.Code"));
        }
        try {
            BusinessPartnerRelationshipTerms item = dao.find(BusinessPartnerRelationshipTerms.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setBusinessPartner(dto.getBusinessPartner());
            item.setDateFrom(dto.getDateFrom());
            item.setDaysToPay(dto.getDaysToPay());
            item.setRebate(dto.getRebate());
            item.setRemark(dto.getRemark());
            item.setEndDate(dto.getEndDate());
            item.setStatus(dto.getStatus());
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
                        Utils.getMessage("BusinessPartnerRelationshipTerms.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteBusinessPartnerRelationshipTermsPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartnerRelationshipTerms service = dao.find(BusinessPartnerRelationshipTerms.class, id);
            if (service != null) {

                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerRelationshipTerms read(String companyIdNumber) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerRelationshipTermsPermission
        if (companyIdNumber == null) {
            throw new javax.persistence.EntityNotFoundException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartnerRelationshipTerms BusinessPartnerRelationshipTerms = dao.find(BusinessPartnerRelationshipTerms.class, companyIdNumber);
            if (BusinessPartnerRelationshipTerms == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.EntityNotFoundEx", companyIdNumber)
                );
            }
            return BusinessPartnerRelationshipTerms;
        } catch (javax.persistence.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BusinessPartnerRelationshipTerms> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBusinessPartnerRelationshipTermsPermission
        Integer id = null;
        Date dateFrom = null;
        Date endDate = null;
        BusinessPartner businessPartner = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id")) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("dateFrom") && s.getValue() instanceof Date) {
                dateFrom = (Date) s.getValue();
            }
            if (s.getKey().equals("endDate") && s.getValue() instanceof Date) {
                endDate = (Date) s.getValue();
            }
            if (s.getKey().equals("businessPartner") && s.getValue() instanceof BusinessPartner) {
                businessPartner = (BusinessPartner) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao,
                    id,
                    dateFrom,
                    endDate,
                    businessPartner);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BusinessPartnerRelationshipTerms> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartnerRelationshipTerms = last page number * BusinessPartnerRelationshipTermss per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.search(dao,
                        id,
                        dateFrom,
                        endDate,
                        businessPartner,
                        start,
                        pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(this.search(dao,
                        id,
                        dateFrom,
                        endDate,
                        businessPartner,
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
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private Long count(
            EntityManager EM,
            Integer id,
            Date dateFrom,
            Date endDate,
            BusinessPartner businessPartner) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartnerRelationshipTerms> root = c.from(BusinessPartnerRelationshipTerms.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null ) {
            criteria.add(cb.equal(root.get(BusinessPartnerRelationshipTerms_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (businessPartner != null ) {
            criteria.add(cb.equal(root.get(BusinessPartnerRelationshipTerms_.businessPartner),
                    cb.parameter(BusinessPartner.class, "businessPartner")));
        }
        if (dateFrom != null ) {
            criteria.add(cb.equal(root.get(BusinessPartnerRelationshipTerms_.dateFrom),
                    cb.parameter(Date.class, "dateFrom")));
        }
        if (endDate != null ) {
            criteria.add(cb.equal(root.get(BusinessPartnerRelationshipTerms_.endDate),
                    cb.parameter(Date.class, "endDate")));
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null ) {
            q.setParameter("id", id);
        }
        if (businessPartner != null ) {
            q.setParameter("businessPartner", businessPartner);
        }
        if (dateFrom != null ) {
            q.setParameter("dateFrom", dateFrom);
        }
        if (endDate != null ) {
            q.setParameter("endDate", endDate);
        }

        return q.getSingleResult();
    }

    private List<BusinessPartnerRelationshipTerms> search(EntityManager em,
                                                          Integer id,
                                                          Date dateFrom,
                                                          Date endDate,
                                                          BusinessPartner businessPartner,
                                                          int first,
                                                          int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusinessPartnerRelationshipTerms> query = cb.createQuery(BusinessPartnerRelationshipTerms.class);
        Root<BusinessPartnerRelationshipTerms> root = query.from(BusinessPartnerRelationshipTerms.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null ) {
            criteria.add(cb.equal(root.get(BusinessPartnerRelationshipTerms_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (businessPartner != null ) {
            criteria.add(cb.equal(root.get(BusinessPartnerRelationshipTerms_.businessPartner),
                    cb.parameter(BusinessPartner.class, "businessPartner")));
        }
        if (dateFrom != null ) {
            criteria.add(cb.equal(root.get(BusinessPartnerRelationshipTerms_.dateFrom),
                    cb.parameter(Date.class, "dateFrom")));
        }
        if (endDate != null ) {
            criteria.add(cb.equal(root.get(BusinessPartnerRelationshipTerms_.endDate),
                    cb.parameter(Date.class, "endDate")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BusinessPartnerRelationshipTerms_.id)));
        TypedQuery<BusinessPartnerRelationshipTerms> typedQuery = em.createQuery(query);
        if (id != null ) {
            typedQuery.setParameter("id", id);
        }
        if (businessPartner != null ) {
            typedQuery.setParameter("businessPartner", businessPartner);
        }
        if (dateFrom != null ) {
            typedQuery.setParameter("dateFrom", dateFrom);
        }
        if (endDate != null ) {
            typedQuery.setParameter("endDate", endDate);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BusinessPartnerRelationshipTerms> readAll(Integer id,
                                                          Date dateFrom,
                                                          Date endDate,
                                                          BusinessPartner businessPartner) {
        try {
            return this.search(dao, id, dateFrom, endDate, businessPartner, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new com.invado.masterdata.service.exception.SystemException(
                    com.invado.masterdata.Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.ReadAll"), ex);
        }
    }
}
