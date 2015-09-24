package com.invado.customer.relationship.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Article;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.BusinessPartner_;
import com.invado.core.exception.*;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.customer.relationship.Utils;
import com.invado.customer.relationship.domain.BusinessPartnerTerms;
import com.invado.customer.relationship.domain.BusinessPartnerTermsItem;
import com.invado.customer.relationship.domain.BusinessPartnerTermsItemPK;
import com.invado.customer.relationship.domain.BusinessPartnerTerms_;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 * Created by NikolaB on 6/13/2015.
 */
@Service
public class BusinessPartnerTermsService {

    private static final Logger LOG = Logger.getLogger(BusinessPartnerTerms.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager dao;
    @Inject
    private Validator validator;

    @Transactional(rollbackFor = Exception.class)
    public void create(BusinessPartnerTerms a) throws ConstraintViolationException {
        try {
            if (a.getTransientPartnerId() != null) {
                a.setBusinessPartner(dao.find(BusinessPartner.class, a.getTransientPartnerId()));
            }else {
                a.setBusinessPartner(null);
            }
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }

            dao.persist(a);
        } catch (ConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BusinessPartnerTerms update(BusinessPartnerTerms dto)
            throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBusinessPartnerRelationshipTermsPermission
        try {
            BusinessPartnerTerms item = dao.find(BusinessPartnerTerms.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.EntityNotFoundEx",
                                dto.getId())
                );
            }
            if (item.getTransientPartnerId() != null) {
                item.setBusinessPartner(
                        dao.find(BusinessPartner.class, dto.getTransientPartnerId())
                );
            }
            item.setDateFrom(dto.getDateFrom());
            item.setEndDate(dto.getEndDate());
            item.setDaysToPay(dto.getDaysToPay());
            item.setRebate(dto.getRebate());
            item.setStatus(dto.getStatus());
            List<String> msgs = validator.validate(item).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            if (item.getVersion().compareTo(dto.getVersion()) != 0) {
                throw new OptimisticLockException();
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
    public void delete(Integer id) {
        //TODO : check DeleteBusinessPartnerRelationshipTermsPermission        
        try {
            BusinessPartnerTerms service = dao.find(BusinessPartnerTerms.class, id);
            if (service != null) {

                dao.remove(service);
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerTerms read(Integer id) throws EntityNotFoundException {
        try {
            BusinessPartnerTerms result = dao.find(BusinessPartnerTerms.class, id);
            if (result == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.EntityNotFoundEx", id)
                );
            }
            result.setTransientPartnerId(result.getBusinessPartner().getId());
            //initialize lazy collections of items
            result.getItemsSize();
            return result;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BusinessPartnerTerms> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBusinessPartnerRelationshipTermsPermission
        Integer id = null;
        LocalDate dateFrom = null;
        LocalDate endDate = null;
        BusinessPartner businessPartner = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id")) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("dateFrom") && s.getValue() instanceof LocalDate) {
                dateFrom = (LocalDate) s.getValue();
            }
            if (s.getKey().equals("endDate") && s.getValue() instanceof LocalDate) {
                endDate = (LocalDate) s.getValue();
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
            ReadRangeDTO<BusinessPartnerTerms> result = new ReadRangeDTO<>();
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
            LocalDate dateFrom,
            LocalDate endDate,
            BusinessPartner businessPartner) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartnerTerms> root = c.from(BusinessPartnerTerms.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerTerms_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (businessPartner != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerTerms_.businessPartner),
                    cb.parameter(BusinessPartner.class, "businessPartner")));
        }
        if (dateFrom != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerTerms_.dateFrom),
                    cb.parameter(LocalDate.class, "dateFrom")));
        }
        if (endDate != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerTerms_.endDate),
                    cb.parameter(LocalDate.class, "endDate")));
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (businessPartner != null) {
            q.setParameter("businessPartner", businessPartner);
        }
        if (dateFrom != null) {
            q.setParameter("dateFrom", dateFrom);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }

        return q.getSingleResult();
    }

    private List<BusinessPartnerTerms> search(EntityManager em,
            Integer id,
            LocalDate dateFrom,
            LocalDate endDate,
            BusinessPartner businessPartner,
            int first,
            int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusinessPartnerTerms> query = cb.createQuery(BusinessPartnerTerms.class);
        Root<BusinessPartnerTerms> root = query.from(BusinessPartnerTerms.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerTerms_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (businessPartner != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerTerms_.businessPartner),
                    cb.parameter(BusinessPartner.class, "businessPartner")));
        }
        if (dateFrom != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerTerms_.dateFrom),
                    cb.parameter(Date.class, "dateFrom")));
        }
        if (endDate != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerTerms_.endDate),
                    cb.parameter(Date.class, "endDate")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BusinessPartnerTerms_.businessPartner)
                                .get(BusinessPartner_.name)),
                        cb.desc(root.get(BusinessPartnerTerms_.dateFrom)));
        TypedQuery<BusinessPartnerTerms> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (businessPartner != null) {
            typedQuery.setParameter("businessPartner", businessPartner);
        }
        if (dateFrom != null) {
            typedQuery.setParameter("dateFrom", dateFrom);
        }
        if (endDate != null) {
            typedQuery.setParameter("endDate", endDate);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BusinessPartnerTerms> readAll(Integer id,
            LocalDate dateFrom,
            LocalDate endDate,
            BusinessPartner businessPartner) {
        try {
            return this.search(dao, id, dateFrom, endDate, businessPartner, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.ReadAll"), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addItem(BusinessPartnerTermsItem i) 
            throws ConstraintViolationException, 
            ReferentialIntegrityException {
        try {
            Long version = i.getTerms().getVersion();
            BusinessPartnerTerms terms = dao.find(
                    BusinessPartnerTerms.class,
                    i.getTerms().getId(),
                    LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (terms == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.TermsReferentialIntegrityException")
                );
            }
            i.setTerms(terms);
            if(i.getArticle() != null && i.getArticle().getCode() != null && 
                    i.getArticle().getCode().isEmpty() == false) {
                Article service = dao.find(Article.class, i.getArticle().getCode());
                if(service == null) {
                    throw new ReferentialIntegrityException(
                            Utils.getMessage("BusinessPartnerRelationshipTerms.ArticleReferentialIntegrityException")
                    );
                }
                i.setArticle(service);                    
            } else {
                i.setArticle(null);
                
            }
            Integer maxOrdinal = 1;
            for (BusinessPartnerTermsItem item : terms.getItems()) {
                if(item.getOrdinal() > maxOrdinal) {
                    maxOrdinal = item.getOrdinal();
                }
            }
            i.setOrdinal(maxOrdinal + 1);
            List<String> msgs = validator.validate(i).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            if (terms.getVersion().compareTo(version) != 0) {
                    throw new OptimisticLockException();
            }            
            terms.addItem(i);
            dao.persist(i);
        } catch (ConstraintViolationException | ReferentialIntegrityException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Additem"),
                    ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeItem(Integer termsId,
            Integer ordinal,
            Long version) {
        try {
            BusinessPartnerTermsItem item = dao.find(BusinessPartnerTermsItem.class,
                    new BusinessPartnerTermsItemPK(termsId, ordinal));
            if (item != null) {
                BusinessPartnerTerms terms = dao.find(BusinessPartnerTerms.class,
                        termsId,
                        LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                terms.removeItem(item);
                dao.remove(item);
                if (terms.getVersion().compareTo(version) != 0) {
                    throw new OptimisticLockException();
                }
            }
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.OptimisticLockEx"),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.RemoveItem"),
                        ex);
            }
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerTermsItem readItem(Integer termsId,
                                             Integer ordinal) 
                                             throws EntityNotFoundException {
        try {
            BusinessPartnerTermsItem item = dao.find(
                    BusinessPartnerTermsItem.class,
                    new BusinessPartnerTermsItemPK(termsId, ordinal));
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.ItemNotFound")
                );
            }
            return item;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.ReadItem"),
                    ex);
        }
    }
}
