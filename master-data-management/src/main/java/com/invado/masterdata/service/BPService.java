package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.BusinessPartnerDTO;
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
public class BPService {

    private static final Logger LOG = Logger.getLogger(
            BusinessPartner.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartner create(BusinessPartnerDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateBusinessPartnerPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx"));
        }
        if (a.getCompanyIdNumber() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.CompanyIdNumber"));
        }
        if (a.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.Name"));
        }
        try {

            BusinessPartner businessPartner = new BusinessPartner();

            businessPartner.setCompanyIdNumber(a.getCompanyIdNumber());
            businessPartner.setName(a.getName());
            businessPartner.setName1(a.getName1());
            businessPartner.setAddress(new Address(a.getCountry(), a.getPlace(), a.getStreet(), a.getPostCode()));
            businessPartner.setPhone(a.getPhone());
            businessPartner.setFax(a.getStreet());
            businessPartner.setEMail(a.getEMail());
            businessPartner.setTIN(a.getTIN());
            businessPartner.setCurrencyDesignation(a.getCurrencyDesignation());
            businessPartner.setCurrentAccount(a.getCurrentAccount());
            businessPartner.setActivityCode(a.getActivityCode());
            businessPartner.setRebate(a.getRebate());
            businessPartner.setInterestFreeDays(a.getInterestFreeDays());
            businessPartner.setVAT(a.getVAT());
            if (a.getParentBusinessPartnerId() != null)
                businessPartner.setParentBusinessPartner(dao.find(BusinessPartner.class, a.getParentBusinessPartnerId()));
            businessPartner.setContactPerson(new ContactPerson(a.getContactPersoneName(), a.getContactPersonePhone(), a.getContactPersoneFunction(), a.getEMail()));

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }

            dao.persist(businessPartner);
            return businessPartner;
        } catch (IllegalArgumentException | EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.Create" + ex.getMessage(), ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartner update(BusinessPartnerDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBusinessPartnerPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Code"));
        }
        if (dto.getCompanyIdNumber() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.CompanyIdNumber"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.Name"));
        }
        try {
            BusinessPartner item = dao.find(BusinessPartner.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartner.EntityNotFoundEx",
                                dto.getCompanyIdNumber())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setName(dto.getName());
            item.setName1(dto.getName1());
            if (dto.getCountry() != null)
                item.setAddress(new Address(dto.getCountry(), dto.getPlace(), dto.getStreet(), dto.getPostCode()));
            if (dto.getContactPersoneName() != null)
                item.setContactPerson(new ContactPerson(dto.getContactPersoneName(), dto.getContactPersonePhone(), dto.getContactPersoneFunction(), dto.getEMail()));
            item.setCurrentAccount(dto.getCurrentAccount());
            item.setEMail(dto.getEMail());
            item.setFax(dto.getFax());
            item.setPhone(dto.getPhone());
            item.setRebate(dto.getRebate());
            item.setVersion(dto.getVersion());
            item.setActivityCode(dto.getActivityCode());
            item.setInterestFreeDays(dto.getInterestFreeDays());
            item.setTIN(dto.getTIN());
            item.setCompanyIdNumber(dto.getCompanyIdNumber());
            item.setCurrencyDesignation(dto.getCurrencyDesignation());
            if (dto.getParentBusinessPartnerId() != null)
                item.setParentBusinessPartner(dao.find(BusinessPartner.class, dto.getParentBusinessPartnerId()));

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

    @Transactional
    public List<BusinessPartner> readParentPartners() {
        Query parent = dao.createNamedQuery(BusinessPartner.READ_PARENT);
        List<BusinessPartner> parents = parent.getResultList();
        return parents;
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteBusinessPartnerPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartner service = dao.find(BusinessPartner.class, id);
            if (service != null) {
                if (dao.createNamedQuery("Invoice.GetByPartner")
                        .setParameter("companyIdNumber", id)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "BusinessPartner.ReferentialIntegrityEx.InvoiceItem",
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
                    Utils.getMessage("BusinessPartner.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerDTO read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartner businessPartner = dao.find(BusinessPartner.class, id);
            if (businessPartner == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartner.EntityNotFoundEx", id)
                );
            }
            return businessPartner.getDTO();
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
    public ReadRangeDTO<BusinessPartnerDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBusinessPartnerPermission
        Integer id = null;
        String companyIdNumber = null;
        String name = null;
        String TIN = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id = (Integer) s.getValue();
            }
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
            Integer pageSize = dao.find(ApplicationSetup.class, 2).getPageSize();

            Long countEntities = this.count(dao,
                    id,
                    companyIdNumber,
                    name,
                    TIN);
            System.out.println("broj entiteta je "+countEntities);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("BusinessPartner.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BusinessPartnerDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartner = last page number * BusinessPartners per page
                int start = numberOfPages.intValue() * pageSize;
                List<BusinessPartnerDTO> businessPartnerDTOList = convertToDTO(this.search(dao, id, companyIdNumber, name, TIN, start, pageSize));
                result.setData(businessPartnerDTOList);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<BusinessPartnerDTO> businessPartnerDTOList = convertToDTO(this.search(dao, id, companyIdNumber, name, TIN, p.getPage() * pageSize, pageSize));
                result.setData(businessPartnerDTOList);
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

    private List<BusinessPartnerDTO> convertToDTO(List<BusinessPartner> lista) {
        List<BusinessPartnerDTO> listaDTO = new ArrayList<>();
        for (BusinessPartner pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            String companyIdNumber,
            String name,
            String TIN) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartner> root = c.from(BusinessPartner.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartner_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.companyIdNumber)),
                    cb.parameter(String.class, "companyIdNumber")));
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
        if (id != null) {
            q.setParameter("id", companyIdNumber.toUpperCase() + "%");
        }
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            q.setParameter("companyIdNumber", companyIdNumber.toUpperCase() + "%");
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
                                         Integer id,
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
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartner_.companyIdNumber),
                    cb.parameter(Integer.class, "id")));
        }
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.companyIdNumber)),
                    cb.parameter(String.class, "companyIdNumber")));
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
        query.orderBy(cb.asc(root.get(BusinessPartner_.name)));
        TypedQuery<BusinessPartner> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            typedQuery.setParameter("code", companyIdNumber.toUpperCase() + "%");
        }
        if (name != null && name.isEmpty() == false) {
            typedQuery.setParameter("desc", name.toUpperCase() + "%");
        }
        if (TIN != null && TIN.isEmpty() == false) {
            typedQuery.setParameter("TIN", TIN);
        }
        System.out.println("first " + first + " a ps je " + pageSize);
        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        List<BusinessPartner> tempList = typedQuery.getResultList();
        return tempList;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BusinessPartner> readAll(Integer id,
                                         String companyIdNumber,
                                         String name,
                                         String TIN) {
        try {
            return this.search(dao, id, companyIdNumber, name, TIN, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.ReadAll"), ex);
        }
    }

    @Transactional(readOnly = true)
    public List<BusinessPartner> readPartnerByName(String name) {
        try {
            return dao.createNamedQuery(
                    BusinessPartner.READ_BY_NAME_ORDERBY_NAME,
                    BusinessPartner.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "BusinessPartner.Exception.ReadItemByDescription"),
                    ex);
        }
    }
}
