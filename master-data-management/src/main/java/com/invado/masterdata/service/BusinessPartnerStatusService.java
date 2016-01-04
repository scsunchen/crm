package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.BusinessPartnerStatus;
import com.invado.core.domain.BusinessPartnerStatus_;
import com.invado.core.dto.BusinessPartnerStatusDTO;
import com.invado.core.exception.*;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.IllegalArgumentException;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import javax.inject.Inject;
import javax.persistence.*;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
 * Created by Nikola on 04/01/2016.
 */
@Service
public class BusinessPartnerStatusService {



    private static final Logger LOG = Logger.getLogger(
            BusinessPartnerStatus.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Inject
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartnerStatus create(BusinessPartnerStatusDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateBusinessPartnerStatusPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentEx"));
        }
        if (a.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentException.Name"));
        }
        if (a.getDescription() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentException.Description"));
        }
        try {
            BusinessPartnerStatus businessPartnerStatus = new BusinessPartnerStatus();
            businessPartnerStatus.setName(a.getName());
            businessPartnerStatus.setDescription(a.getDescription());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(businessPartnerStatus);
            return businessPartnerStatus;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerStatus.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartnerStatus update(BusinessPartnerStatusDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBusinessPartnerStatusPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentEx.Id"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentException.Name"));
        }
        if (dto.getDescription() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentException.Description"));
        }
        try {
            BusinessPartnerStatus item = dao.find(BusinessPartnerStatus.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerStatus.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setName(dto.getName());
            item.setDescription(dto.getDescription());
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
                        Utils.getMessage("BusinessPartnerStatus.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("BusinessPartnerStatus.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteBusinessPartnerStatusPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentEx.Id")
            );
        }
        try {
            BusinessPartnerStatus service = dao.find(BusinessPartnerStatus.class, id);


            if (service != null) {
                if (dao.createNamedQuery(BusinessPartner.READ_BY_STATUS)
                        .setParameter("status", service)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "BusinessPartnerStatus.ReferentialIntegrityEx.BusinessPartner",
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
                    Utils.getMessage("BusinessPartnerStatus.PersistenceEx.Delete"),
                    ex);
        }
    }


    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerStatusDTO read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerStatusPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("BusinessPartnerStatus.IllegalArgumentEx.Id")
            );
        }
        try {
            BusinessPartnerStatus BusinessPartnerStatus = dao.find(BusinessPartnerStatus.class, id);
            if (BusinessPartnerStatus == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerStatus.EntityNotFoundEx", id)
                );
            }
            return BusinessPartnerStatus.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerStatus.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BusinessPartnerStatusDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBusinessPartnerStatusPermission
        Integer id = null;
        String name = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof String) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("name") && s.getValue() instanceof String) {
                name = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, id, name);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("BusinessPartnerStatus.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BusinessPartnerStatusDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartnerStatus = last page number * BusinessPartnerStatuss per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(convertToDTO(this.search(dao, id, name, start, pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao, id, name,
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
                    Utils.getMessage("BusinessPartnerStatus.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<BusinessPartnerStatusDTO> convertToDTO(List<BusinessPartnerStatus> lista) {
        List<BusinessPartnerStatusDTO> listaDTO = new ArrayList<>();
        for (BusinessPartnerStatus pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            String name) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartnerStatus> root = c.from(BusinessPartnerStatus.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerStatus_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartnerStatus_.name)),
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

    private List<BusinessPartnerStatus> search(EntityManager em,
                                      Integer id,
                                      String name,
                                      int first,
                                      int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusinessPartnerStatus> query = cb.createQuery(BusinessPartnerStatus.class);
        Root<BusinessPartnerStatus> root = query.from(BusinessPartnerStatus.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerStatus_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(root.get(BusinessPartnerStatus_.name),
                    cb.parameter(String.class, "name")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BusinessPartnerStatus_.id)));
        TypedQuery<BusinessPartnerStatus> typedQuery = em.createQuery(query);
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
    public List<BusinessPartnerStatusDTO> readAll(
            Integer id,
            String name) {
        try {
            return convertToDTO(this.search(dao, id, name, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerStatus.PersistenceEx.ReadAll"), ex);
        }
    }
}
