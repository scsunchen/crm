package com.invado.masterdata.contoller;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.BusinessPartner_;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
import com.invado.masterdata.service.exception.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.*;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Created by NikolaB on 6/2/2015.
 */
@Service
public class BusinessPartnerController {

    private static final Logger LOG = Logger.getLogger(
            BusinessPartner.class.getName());

    @PersistenceContext(unitName = "unit")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartner create(BusinessPartner a) throws IllegalArgumentException,
            EntityExistsException {
        //check CreateBusinessPartnerPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx"));
        }
        try {
            if (dao.find(BusinessPartner.class, a.getCompanyIdNumber()) != null) {
                throw new EntityExistsException(
                        Utils.getMessage("BusinessPartner.EntityExistsEx", a.getCompanyIdNumber())
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
                    Utils.getMessage("BusinessPartner.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartner update(BusinessPartner dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBusinessPartnerPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx"));
        }
        if (dto.getCompanyIdNumber() == null || dto.getCompanyIdNumber().length() == 0) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Code"));
        }
        try {
            BusinessPartner item = dao.find(BusinessPartner.class,
                    dto.getCompanyIdNumber(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartner.EntityNotFoundEx",
                                dto.getCompanyIdNumber())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setAddress(dto.getAddress());
            item.setContactPerson(dto.getContactPerson());
            item.setCurrentAccount(dto.getCurrentAccount());
            item.setEMail(dto.getEMail());
            item.setFax(dto.getFax());
            item.setPhone(dto.getPhone());
            item.setRebate(dto.getRebate());
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
                        Utils.getMessage("BusinessPartner.OptimisticLockEx",
                                dto.getCompanyIdNumber()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("BusinessPartner.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String companyIdNumber) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteBusinessPartnerPermission
        if (companyIdNumber == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartner service = dao.find(BusinessPartner.class, companyIdNumber);
            if (service != null) {
                if (dao.createNamedQuery("Invoice.GetByPartner")
                        .setParameter("companyIdNumber", companyIdNumber)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "BusinessPartner.ReferentialIntegrityEx.InvoiceItem",
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
                    Utils.getMessage("BusinessPartner.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartner read(String companyIdNumber) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerPermission
        if (companyIdNumber == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartner BusinessPartner = dao.find(BusinessPartner.class, companyIdNumber);
            if (BusinessPartner == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartner.EntityNotFoundEx", companyIdNumber)
                );
            }
            return BusinessPartner;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BusinessPartner> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBusinessPartnerPermission
        String companyIdNumber = null;
        String name = null;
        String TIN = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("companyIdNumber") && s.getValue() instanceof String) {
                companyIdNumber = (String) s.getValue();
            }
            if (s.getKey().equals("name") && s.getValue() instanceof String) {
                name = (String) s.getValue();
            }
            if (s.getKey().equals("TIN") && s.getValue() instanceof String) {
                TIN = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao,
                    companyIdNumber,
                    name,
                    TIN);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("BusinessPartner.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BusinessPartner> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartner = last page number * BusinessPartners per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.search(dao,
                        companyIdNumber,
                        name,
                        TIN,
                        start,
                        pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(this.search(dao,
                        companyIdNumber,
                        name,
                        TIN,
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
                    Utils.getMessage("BusinessPartner.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private Long count(
            EntityManager EM,
            String companyIdNumber,
            String name,
            String TIN) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartner> root = c.from(BusinessPartner.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.companyIdNumber)),
                    cb.parameter(String.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.name)),
                    cb.parameter(String.class, "desc")));
        }
        if (TIN != null && TIN.isEmpty() == false) {
            criteria.add(cb.like(
                            root.get(BusinessPartner_.TIN),
                            cb.parameter(String.class, "TIN"))
            );
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            q.setParameter("code", companyIdNumber.toUpperCase() + "%");
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", name.toUpperCase() + "%");
        }
        if (TIN != null && TIN.isEmpty() == false) {
            q.setParameter("TIN", TIN);
        }

        return q.getSingleResult();
    }

    private List<BusinessPartner> search(EntityManager em,
                                         String companyIdNumber,
                                         String name,
                                         String TIN,
                                         int first,
                                         int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusinessPartner> query = cb.createQuery(BusinessPartner.class);
        Root<BusinessPartner> root = query.from(BusinessPartner.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.companyIdNumber)),
                    cb.parameter(String.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.name)),
                    cb.parameter(String.class, "desc")));
        }
        if (TIN != null && TIN.isEmpty() == false) {
            criteria.add(cb.like(root.get(BusinessPartner_.TIN),
                    cb.parameter(String.class, "TIN")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BusinessPartner_.companyIdNumber)));
        TypedQuery<BusinessPartner> typedQuery = em.createQuery(query);
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            typedQuery.setParameter("code", companyIdNumber.toUpperCase() + "%");
        }
        if (name != null && name.isEmpty() == false) {
            typedQuery.setParameter("desc", name.toUpperCase() + "%");
        }
        if (TIN != null && TIN.isEmpty() == false) {
            typedQuery.setParameter("TIN", TIN);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BusinessPartner> readAll(String companyIdNumber,
                                         String name,
                                         String TIN) {
        try {
            return this.search(dao, companyIdNumber, name, TIN, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.ReadAll"), ex);
        }
    }
}
