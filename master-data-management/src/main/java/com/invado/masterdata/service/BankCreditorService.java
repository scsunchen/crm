package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.BankCreditor_;
import com.invado.core.dto.BankCreditorDTO;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
import com.invado.masterdata.service.exception.EntityExistsException;
import com.invado.masterdata.service.exception.EntityNotFoundException;
import com.invado.masterdata.service.exception.IllegalArgumentException;
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
 * Created by NikolaB on 6/10/2015.
 */
@Service
public class BankCreditorService {
    private static final Logger LOG = Logger.getLogger(
            BankCreditor.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public BankCreditor create(BankCreditorDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateBankCreditorPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BankCreditor.IllegalArgumentEx"));
        }
        if (a.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BankCreditor.IllegalArgumentException.name"));
        }
        try {

            BankCreditor bankCreditor = new BankCreditor();

            bankCreditor.setPostCode(a.getPostCode());
            bankCreditor.setName(a.getName());
            bankCreditor.setAccount(a.getAccount());
            bankCreditor.setContactFunction(a.getContactFunction());
            bankCreditor.setContactPerson(a.getContactPerson());
            bankCreditor.setContactPhone(a.getContactPhone());
            bankCreditor.setId(a.getId());
            bankCreditor.setPlace(a.getPlace());
            bankCreditor.setStreet(a.getName());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(bankCreditor);
            return bankCreditor;
        } catch (IllegalArgumentException | javax.persistence.EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BankCreditor.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BankCreditor update(BankCreditorDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBankCreditorPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BankCreditor.IllegalArgumentEx"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BankCreditor.IllegalArgumentException.name"));
        }
        try {

           BankCreditor item = dao.find(BankCreditor.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("BankCreditor.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setPlace(dto.getPlace());
            item.setStreet(dto.getStreet());
            item.setPostCode(dto.getPostCode());
            item.setContactPerson(dto.getContactPerson());
            item.setAccount(dto.getAccount());
            item.setContactPhone(dto.getContactPhone());
            item.setContactFunction(dto.getContactFunction());
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
                        Utils.getMessage("BankCreditor.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("BankCreditor.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteBankCreditorPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BankCreditor.IllegalArgumentEx.Code")
            );
        }
        try {
            BankCreditor service = dao.find(BankCreditor.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        }  catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BankCreditor.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BankCreditorDTO read(Integer id) throws javax.persistence.EntityNotFoundException {
        //TODO : check ReadBankCreditorPermission
        if (id == null) {
            throw new javax.persistence.EntityNotFoundException(
                    Utils.getMessage("BankCreditor.IllegalArgumentEx.Code")
            );
        }
        try {
            BankCreditor BankCreditor = dao.find(BankCreditor.class, id);
            if (BankCreditor == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("BankCreditor.EntityNotFoundEx", id)
                );
            }
            return BankCreditor.getDTO();
        } catch (javax.persistence.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BankCreditor.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BankCreditorDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBankCreditorPermission
        Integer id = null;
        String name = null;
        String TIN = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
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
                        Utils.getMessage("BankCreditor.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BankCreditorDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BankCreditor = last page number * BankCreditors per page
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
                    Utils.getMessage("BankCreditor.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<BankCreditorDTO> convertToDTO(List<BankCreditor> lista) {
        List<BankCreditorDTO> listaDTO = new ArrayList<>();
        for (BankCreditor pr : lista) {
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
        Root<BankCreditor> root = c.from(BankCreditor.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null ) {
            criteria.add(cb.equal(root.get(BankCreditor_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BankCreditor_.name)),
                    cb.parameter(String.class, "name")));
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null ) {
            q.setParameter("id", id);
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", name.toUpperCase() + "%");
        }


        return q.getSingleResult();
    }

    private List<BankCreditor> search(EntityManager em,
                                         Integer id,
                                         String name,
                                         int first,
                                         int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BankCreditor> query = cb.createQuery(BankCreditor.class);
        Root<BankCreditor> root = query.from(BankCreditor.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null ) {
            criteria.add(cb.equal(root.get(BankCreditor_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BankCreditor_.name)),
                    cb.parameter(String.class, "name")));
        }


        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BankCreditor_.id)));
        TypedQuery<BankCreditor> typedQuery = em.createQuery(query);
        if (id != null ) {
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
    public List<BankCreditorDTO> readAll(Integer id,
                                         String name) {
        try {
            return convertToDTO(this.search(dao, id, name, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("BankCreditor.PersistenceEx.ReadAll"), ex);
        }
    }
}
