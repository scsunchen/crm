package com.invado.customer.relationship.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.customer.relationship.Utils;
import com.invado.customer.relationship.domain.BusinessPartnerTerms;
import com.invado.customer.relationship.domain.BusinessPartnerTermsItem;
import com.invado.customer.relationship.domain.BusinessPartnerTerms_;
import com.invado.customer.relationship.service.dto.BusinessPartnerTermsDTO;
import com.invado.customer.relationship.service.dto.BusinessPartnerRelationshipTermsItemsDTO;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
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
import java.time.LocalDate;
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
public class BusinessPartnerTermsService {

    private static final Logger LOG = Logger.getLogger(BusinessPartnerTerms.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager dao;

    @Autowired
    private Validator validator;


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartnerTerms create(BusinessPartnerTerms a) 
            throws ConstraintViolationException,
            javax.persistence.EntityExistsException {
        //check CreateBusinessPartnerRelationshipTermsPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx"));
        }
        try {
            if (a.getTransientPartnerId() != null)
                a.setBusinessPartner(dao.find(BusinessPartner.class, a.getTransientPartnerId()));
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }

            dao.persist(a);
            return a;
        } catch (ConstraintViolationException | EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartnerTerms update(BusinessPartnerTerms dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBusinessPartnerRelationshipTermsPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx.Code"));
        }
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
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setBusinessPartner(dao.find(BusinessPartner.class, dto.getTransientPartnerId()));
            item.setDateFrom(dto.getDateFrom());
            item.setDaysToPay(dto.getDaysToPay());
            item.setRebate(dto.getRebate());
            item.setEndDate(dto.getEndDate());
            item.setStatus(dto.getStatus());
            //item.setVersion(dto.getVersion());
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
            System.out.println("poruka je " + ex.getMessage());
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
            BusinessPartnerTerms service = dao.find(BusinessPartnerTerms.class, id);
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
    public BusinessPartnerTerms read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerRelationshipTermsPermission
        if (id == null) {
            throw new javax.persistence.EntityNotFoundException(
                    Utils.getMessage("BusinessPartnerRelationshipTerms.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartnerTerms BusinessPartnerRelationshipTerms = dao.find(BusinessPartnerTerms.class, id);
            if (BusinessPartnerRelationshipTerms == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerRelationshipTerms.EntityNotFoundEx", id)
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
    public ReadRangeDTO<BusinessPartnerTermsDTO> readPage(PageRequestDTO p)
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
            if (s.getKey().equals("dateFrom") && s.getValue() instanceof Date) {
                dateFrom = (LocalDate) s.getValue();
            }
            if (s.getKey().equals("endDate") && s.getValue() instanceof Date) {
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
            ReadRangeDTO<BusinessPartnerTermsDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartnerRelationshipTerms = last page number * BusinessPartnerRelationshipTermss per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.convertToDTO(this.search(dao,
                        id,
                        dateFrom,
                        endDate,
                        businessPartner,
                        start,
                        pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());

            } else {
                result.setData(this.convertToDTO(this.search(dao,
                        id,
                        dateFrom,
                        endDate,
                        businessPartner,
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
                    Utils.getMessage("BusinessPartnerRelationshipTerms.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<BusinessPartnerTermsDTO> convertToDTO(List<BusinessPartnerTerms> lista) {
        List<BusinessPartnerTermsDTO> listaDTO = new ArrayList<>();
        for (BusinessPartnerTerms pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
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
                .orderBy(cb.asc(root.get(BusinessPartnerTerms_.id)));
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

    @Transactional(readOnly = true)
    public BusinessPartnerRelationshipTermsItemsDTO[] readTermsItems(Integer id)
            throws ConstraintViolationException,
            EntityNotFoundException {
        // TODO : check read invoice permission
        if (id == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }

        try {
            BusinessPartnerTerms temp = dao.find(BusinessPartnerTerms.class, id);
            if (temp == null) {
                throw new EntityNotFoundException(Utils.getMessage("BusinessPartnerTerms.EntityNotFoundException", id));
            }
            BusinessPartnerRelationshipTermsItemsDTO[] result = new BusinessPartnerRelationshipTermsItemsDTO[temp.getItemsSize()];
            int i = 0;
            List<BusinessPartnerTermsItem> itemList = temp.getUnmodifiableItemsSet();
            for (BusinessPartnerTermsItem item : itemList) {
                BusinessPartnerRelationshipTermsItemsDTO dto = new BusinessPartnerRelationshipTermsItemsDTO();
                dto.setBusinessPartnerRelationshipTermsId(item.getBusinessPartnerTerms().getId());
                dto.setArticleCode(item.getArticle().getCode());
               dto.setArticleName(item.getArticle().getDescription());
                dto.setOrdinal(item.getOrdinal());
                dto.setRebate(item.getRebate());
                dto.setTotalAmount(item.getTotalAmount());
                dto.setTotalQuantity(item.getTotalQuantity());
                dto.setBusinessPartnerRelationshipTermsVersion(item.getBusinessPartnerTerms().getVersion());

                result[i] = dto;
                i = i + 1;
            }
            return result;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "BusinessPartnerTerms.PersistenceEx.ReadInvoiceItems"), ex);
        }
    }
}
