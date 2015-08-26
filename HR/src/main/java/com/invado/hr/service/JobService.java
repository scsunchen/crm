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
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public Job create(JobDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateJobPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Job.IllegalArgumentEx"));
        }
        if (a.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BankCreditor.IllegalArgumentException.name"));
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
            throw new SystemException(
                    Utils.getMessage("Job.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Job update(JobDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateJobPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Job.IllegalArgumentEx"));
        }
        if (dto.getId() == null ) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Job.IllegalArgumentEx.Code"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BankCreditor.IllegalArgumentException.name"));
        }
        try {
            Job item = dao.find(Job.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Job.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setName(dto.getName());
            item.setDescription(dto.getDescription());
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
        } catch (ConstraintViolationException | EntityNotFoundException ex) {
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
                    Utils.getMessage("Job.IllegalArgumentEx.Code")
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
    public JobDTO read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadJobPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("Job.IllegalArgumentEx.Code")
            );
        }
        try {
            JobDTO job = dao.find(Job.class, id).getDTO();
            if (job == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Job.EntityNotFoundEx", id)
                );
            }
            return job;
        } catch (EntityNotFoundException ex) {
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
        Integer code = null;
        String name = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("code") && s.getValue() instanceof String) {
                code = (Integer) s.getValue();
            }
            if (s.getKey().equals("name") && s.getValue() instanceof String) {
                name = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, code, name);
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
                result.setData(convertToDTO(this.search(dao, code, name,  start, pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao, code, name,
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
            Integer code,
            String name) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Job> root = c.from(Job.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (code != null) {
            criteria.add(cb.equal(root.get(Job_.id),
                    cb.parameter(Integer.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Job_.name)),
                    cb.parameter(String.class, "name")));
        }


        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (code != null ) {
            q.setParameter("code", code);
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", name.toUpperCase() + "%");
        }


        return q.getSingleResult();
    }

    private List<Job> search(EntityManager em,
                                  Integer code,
                                  String name,
                                  int first,
                                  int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Job> query = cb.createQuery(Job.class);
        Root<Job> root = query.from(Job.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (code != null ) {
            criteria.add(cb.equal(root.get(Job_.id),
                    cb.parameter(Integer.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(root.get(Job_.name),
                    cb.parameter(String.class, "name")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Job_.id)));
        TypedQuery<Job> typedQuery = em.createQuery(query);
        if (code != null ) {
            typedQuery.setParameter("code", code);
        }
        if (name != null && name.isEmpty() == false) {
            typedQuery.setParameter("name", name.toUpperCase() + "%");
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<JobDTO> readAll(
            Integer code,
            String name) {
        try {
            return convertToDTO(this.search(dao, code, name, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Job.PersistenceEx.ReadAll"), ex);
        }
    }
}
