package com.invado.hr.service;

import com.invado.core.domain.*;
import com.invado.hr.Utils;
import com.invado.hr.service.dto.PageRequestDTO;
import com.invado.hr.service.dto.ReadRangeDTO;
import com.invado.hr.service.exception.*;
import com.invado.hr.service.exception.EntityExistsException;
import com.invado.hr.service.exception.EntityNotFoundException;
import com.invado.hr.service.exception.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by NikolaB on 6/14/2015.
 */
@Service
public class EmployeeService {
    private static final Logger LOG = Logger.getLogger(
            Employee.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public Employee create(Employee a) throws IllegalArgumentException,
            EntityExistsException {
        //check CreateEmployeePermission


        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Employee.IllegalArgumentEx"));
        }
        try {
            a.setOrgUnit(dao.find(OrgUnit.class, a.getTransientOrgUnitId()));
            a.setJob(dao.find(Job.class, a.getTransientJobId()));
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
                    Utils.getMessage("Employee.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Employee update(Employee dto) throws ConstraintViolationException,
            com.invado.hr.service.exception.EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateEmployeePermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Employee.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Employee.IllegalArgumentEx.Code"));
        }
        try {
            Employee item = dao.find(Employee.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("Employee.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setName(dto.getName());
            item.setAddress(dto.getAddress());
            item.setDateOfBirth(dto.getDateOfBirth());
            item.setEmail(dto.getEmail());
            item.setEndDate(dto.getEndDate());
            item.setHireDate(dto.getHireDate());
            if (item.getTransientJobId() != null) {
                item.setJob(dao.find(Job.class, item.getTransientJobId()));
            }else{
                item.setJob(dao.find(Job.class, item.getJob().getId()));
            }
            item.setPhone(dto.getPhone());
            item.setLastName(dto.getLastName());
            item.setMiddleName(dto.getMiddleName());
            if (item.getTransientOrgUnitId() != null){
                item.setOrgUnit(dao.find(OrgUnit.class, item.getTransientOrgUnitId()));
            }else{
                item.setOrgUnit(dao.find(OrgUnit.class, item.getOrgUnit().getId()));
            }
            item.setPhone(dto.getPhone());
            item.setPicture(dto.getPicture());
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
                        Utils.getMessage("Employee.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Employee.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteEmployeePermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Employee.IllegalArgumentEx.Code")
            );
        }
        try {
            Employee service = dao.find(Employee.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Employee.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Employee read(Integer id) throws com.invado.hr.service.exception.EntityNotFoundException {
        //TODO : check ReadEmployeePermission
        if (id == null) {
            throw new com.invado.hr.service.exception.EntityNotFoundException(
                    Utils.getMessage("Employee.IllegalArgumentEx.Code")
            );
        }
        try {
            Employee Employee = dao.find(Employee.class, id);
            if (Employee == null) {
                throw new com.invado.hr.service.exception.EntityNotFoundException(
                        Utils.getMessage("Employee.EntityNotFoundEx", id)
                );
            }
            return Employee;
        } catch (com.invado.hr.service.exception.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Employee.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<Employee> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadEmployeePermission
        Integer id = null;
        String name = null;
        String TIN = null;
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

            Long countEntities = this.count(dao,
                    id,
                    name);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Employee.PageNotExists", pageNumber));
            }
            ReadRangeDTO<Employee> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Employee = last page number * Employees per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.search(dao,
                        id,
                        name,
                        start,
                        pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(this.search(dao,
                        id,
                        name,
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
                    Utils.getMessage("Employee.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private Long count(
            EntityManager EM,
            Integer id,
            String name) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Employee> root = c.from(Employee.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Employee_.id),
                    cb.parameter(String.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Employee_.name)),
                    cb.parameter(String.class, "desc")));
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

    private List<Employee> search(EntityManager em,
                                  Integer id,
                                  String name,
                                  int first,
                                  int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Employee_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Employee_.name)),
                    cb.parameter(String.class, "name")));
        }


        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Employee_.id)));
        TypedQuery<Employee> typedQuery = em.createQuery(query);
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
    public List<Employee> readAll(Integer id,
                                  String name) {
        try {
            return this.search(dao, id, name, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Employee.PersistenceEx.ReadAll"), ex);
        }
    }
}
