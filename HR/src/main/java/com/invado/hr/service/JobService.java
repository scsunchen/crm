package com.invado.hr.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.Job;
import com.invado.core.domain.Job_;
import com.invado.core.dto.BankCreditorDTO;
import com.invado.core.dto.JobDTO;
import com.invado.hr.Utils;
import com.invado.hr.service.dto.PageRequestDTO;
import com.invado.hr.service.dto.ReadRangeDTO;
import com.invado.hr.service.exception.*;
import com.invado.hr.service.exception.EntityExistsException;
import com.invado.hr.service.exception.EntityNotFoundException;
import com.invado.hr.service.exception.IllegalArgumentException;
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
 * Created by NikolaB on 6/14/2015.
 */
@Service
public class JobService {
    private static final Logger LOG = Logger.getLogger(
            Job.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;


    @Transactional(rollbackFor = Exception.class)
    public Job create(JobDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateJobPermission


        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Job.IllegalArgumentEx"));
        }
        if (a.getName() == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Job.IllegalArgumentException.name"));
        }

        try {
            Job job = new Job();
            job.setName(a.getName());
            job.setDescription(a.getDescription());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(job);
            return job;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            ex.printStackTrace();
            throw new SystemException(
                    Utils.getMessage("Job.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Job update(JobDTO dto) throws ConstraintViolationException,
            com.invado.hr.service.exception.EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateJobPermission
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Job.IllegalArgumentException.name"));
        }

        try {
            Job item = dao.find(Job.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("Job.EntityNotFoundEx",
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
        } catch (ConstraintViolationException | javax.persistence.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("Job.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Job.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteJobPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Job.IllegalArgumentEx.Id")
            );
        }
        try {
            Job service = dao.find(Job.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Job.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public JobDTO read(Integer id) throws com.invado.hr.service.exception.EntityNotFoundException {
        //TODO : check ReadJobPermission
        if (id == null) {
            throw new com.invado.hr.service.exception.EntityNotFoundException(
                    Utils.getMessage("Job.IllegalArgumentEx.Id")
            );
        }
        try {
            Job job = dao.find(Job.class, id);
            if (job == null) {
                throw new com.invado.hr.service.exception.EntityNotFoundException(
                        Utils.getMessage("Job.EntityNotFoundEx", id)
                );
            }
            return job.getDTO();
        } catch (com.invado.hr.service.exception.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Job.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<JobDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadJobPermission
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
                        Utils.getMessage("Job.PageNotExists", pageNumber));
            }
            ReadRangeDTO<JobDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Job = last page number * Jobs per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(convertToDTO(this.search(dao,
                        id,
                        name,
                        start,
                        pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao,
                        id,
                        name,
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
                    Utils.getMessage("Job.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<JobDTO> convertToDTO(List<Job> lista) {
        List<JobDTO> listaDTO = new ArrayList<>();
        for (Job pr : lista) {
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
        Root<Job> root = c.from(Job.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Job_.id),
                    cb.parameter(String.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Job_.name)),
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

    private List<Job> search(EntityManager em,
                                  Integer id,
                                  String name,
                                  int first,
                                  int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Job> query = cb.createQuery(Job.class);
        Root<Job> root = query.from(Job.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Job_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Job_.name)),
                    cb.parameter(String.class, "name")));
        }


        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Job_.id)));
        TypedQuery<Job> typedQuery = em.createQuery(query);
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
    public List<JobDTO> readAll(Integer id,
                                     String name) {
        try {
            return convertToDTO(this.search(dao, id, name, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Job.PersistenceEx.ReadAll"), ex);
        }
    }
}
