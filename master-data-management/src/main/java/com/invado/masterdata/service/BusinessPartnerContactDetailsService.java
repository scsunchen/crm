package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.BusinessPartnerContactDetailsDTO;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
import com.invado.masterdata.service.exception.EntityExistsException;
import com.invado.masterdata.service.exception.EntityNotFoundException;
import com.invado.core.exception.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Validator;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Nikola on 18/10/2015.
 */
@Service
public class BusinessPartnerContactDetailsService {

    private static final Logger LOG = Logger.getLogger(
            BusinessPartnerContactDetails.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartnerContactDetails create(BusinessPartnerContactDetailsDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateBusinessPartnerContactDetailsPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentEx"));
        }
        if (a.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentEx.Name"));
        }
        if (a.getPhone() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentException.Phone"));
        }
        if (a.getEmail() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentException.Email"));
        }
        try {

            BusinessPartnerContactDetails businessPartnerContactDetails = new BusinessPartnerContactDetails();
            ContactPerson contactPerson = new ContactPerson(a.getName(), a.getPhone(), a.getFunction(), a.getEmail());
            businessPartnerContactDetails.setContactPerson(contactPerson);
            Address address = new Address(a.getCountry(), a.getPlace(), a.getStreet(), a.getPostCode());
            businessPartnerContactDetails.setAddress(address);
            businessPartnerContactDetails.setDateFrom(LocalDate.now());
            businessPartnerContactDetails.setMerchant(dao.find(BusinessPartner.class, a.getMerchantId()));
            businessPartnerContactDetails.setPointOfSale(dao.find(BusinessPartner.class, a.getPointOfSaleId()));

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(businessPartnerContactDetails);
            return businessPartnerContactDetails;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerContactDetails.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartnerContactDetails update(BusinessPartnerContactDetailsDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException,
            IllegalArgumentException{
        //check UpdateBusinessPartnerContactDetailsPermission
        if (dto == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentEx"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentEx.Name"));
        }
        if (dto.getPhone() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentException.Phone"));
        }
        if (dto.getEmail() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentException.Email"));
        }
        try {
            BusinessPartnerContactDetails item = dao.find(BusinessPartnerContactDetails.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerContactDetails.EntityNotFoundEx",
                                dto.getId())
                );
            }

            ContactPerson contactPerson = new ContactPerson(dto.getName(), dto.getPhone(), dto.getFunction(), dto.getEmail());
            item.setContactPerson(contactPerson);
            Address address = new Address(dto.getCountry(), dto.getPlace(), dto.getStreet(), dto.getPostCode());
            item.setAddress(address);
            item.setDateFrom(LocalDate.now());
            item.setMerchant(dao.find(BusinessPartner.class, dto.getMerchantId()));
            item.setPointOfSale(dao.find(BusinessPartner.class, dto.getPointOfSaleId()));
            item.setVersion(dto.getVersion());
            dao.lock(item, LockModeType.OPTIMISTIC);

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
                        Utils.getMessage("BusinessPartnerContactDetails.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("BusinessPartnerContactDetails.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteBusinessPartnerContactDetailsPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartnerContactDetails service = dao.find(BusinessPartnerContactDetails.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerContactDetails.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerContactDetails read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerContactDetailsPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("BusinessPartnerContactDetails.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartnerContactDetails BusinessPartnerContactDetails = dao.find(BusinessPartnerContactDetails.class, id);
            if (BusinessPartnerContactDetails == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerContactDetails.EntityNotFoundEx", id)
                );
            }
            return BusinessPartnerContactDetails;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerContactDetails.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BusinessPartnerContactDetailsDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBusinessPartnerContactDetailsPermission
        Integer id = null;
        String name = null;
        Integer merchantId = null;
        Integer pointOfSaleId = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("merchantId") && s.getValue() instanceof Integer) {
                merchantId = (Integer) s.getValue();
            }
            if (s.getKey().equals("pointOfSaleId") && s.getValue() instanceof Integer) {
                pointOfSaleId = (Integer) s.getValue();
            }
            if (s.getKey().equals("name") && s.getValue() instanceof String) {
                name = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 2).getPageSize();

            Long countEntities = this.count(dao, id, name, merchantId, pointOfSaleId);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("BusinessPartnerContactDetails.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BusinessPartnerContactDetailsDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartnerContactDetails = last page number * BusinessPartnerContactDetailss per page
                int start = numberOfPages.intValue() * pageSize;

                result.setData(convertToDTO(this.search(dao, id, name, merchantId, pointOfSaleId, start, pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao, id, name, merchantId, pointOfSaleId,
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
                    Utils.getMessage("BusinessPartnerContactDetails.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<BusinessPartnerContactDetailsDTO> convertToDTO(List<BusinessPartnerContactDetails> lista) {
        List<BusinessPartnerContactDetailsDTO> listaDTO = new ArrayList<>();
        for (BusinessPartnerContactDetails pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            String name,
            Integer merchantId,
            Integer pointOfSaleId) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartnerContactDetails> root = c.from(BusinessPartnerContactDetails.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerContactDetails_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (merchantId != null) {
            BusinessPartner merchant = dao.find(BusinessPartner.class, merchantId);
            criteria.add(cb.equal(root.get(BusinessPartnerContactDetails_.merchant),
                    cb.parameter(BusinessPartner.class, "merchant")));
        }
        if (pointOfSaleId != null) {
            BusinessPartner pointOfSale = dao.find(BusinessPartner.class, pointOfSaleId);
            criteria.add(cb.equal(root.get(BusinessPartnerContactDetails_.pointOfSale),
                    cb.parameter(BusinessPartner.class, "pointOfSale")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartnerContactDetails_.contactPerson.getName())),
                    cb.parameter(String.class, "name")));
        }


        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", name.toUpperCase() + "%");
        }


        return q.getSingleResult();
    }

    private List<BusinessPartnerContactDetails> search(EntityManager em,
                                                       Integer id,
                                                       String name,
                                                       Integer merchantId,
                                                       Integer pointOfSaleId,
                                                       int first,
                                                       int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusinessPartnerContactDetails> query = cb.createQuery(BusinessPartnerContactDetails.class);
        Root<BusinessPartnerContactDetails> root = query.from(BusinessPartnerContactDetails.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (merchantId != null) {
            BusinessPartner merchant = dao.find(BusinessPartner.class, merchantId);
            criteria.add(cb.equal(root.get(BusinessPartnerContactDetails_.merchant),
                    cb.parameter(BusinessPartner.class, "merchant")));
        }
        if (pointOfSaleId != null) {
            BusinessPartner pointOfSale = dao.find(BusinessPartner.class, pointOfSaleId);
            criteria.add(cb.equal(root.get(BusinessPartnerContactDetails_.pointOfSale),
                    cb.parameter(BusinessPartner.class, "pointOfSale")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(root.get(BusinessPartnerContactDetails_.contactPerson.getName()),
                    cb.parameter(String.class, "name")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BusinessPartnerContactDetails_.id)));
        TypedQuery<BusinessPartnerContactDetails> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (name != null && name.isEmpty() == false) {
            typedQuery.setParameter("name", name.toUpperCase() + "%");
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BusinessPartnerContactDetailsDTO> readAll(
            Integer id,
            String name,
            Integer merchantId,
            Integer pointOfSaleId) {
        try {
            return convertToDTO(this.search(dao, id, name, merchantId, pointOfSaleId, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerContactDetails.PersistenceEx.ReadAll"), ex);
        }
    }
}


