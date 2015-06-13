package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Client;
import com.invado.core.domain.Client_;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
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
 * Created by NikolaB on 6/9/2015.
 */
@Service
public class ClientService {

    private static final Logger LOG = Logger.getLogger(
            Client.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";

    @Autowired
    private TownshipService townshipService;
    @Autowired
    private BankCreditorService bankCreditorService;

    @Transactional(rollbackFor = Exception.class)
    public Client create(Client a) throws com.invado.masterdata.service.exception.IllegalArgumentException,
            javax.persistence.EntityExistsException {
        //check CreateClientPermission

        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Client.IllegalArgumentEx"));
        }
        try {
            if (dao.find(Client.class, a.getId()) != null) {
                throw new javax.persistence.EntityExistsException(
                        Utils.getMessage("Client.EntityExistsEx", a.getId())
                );
            }
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            a.setTownship(townshipService.read(a.getTownship().getCode()));
            System.out.println("id baneke je "+a.getBankCreditor());
            a.setBank(bankCreditorService.read(Integer.parseInt(a.getBankCreditor())));
            dao.persist(a);
            return a;
        } catch (IllegalArgumentException | javax.persistence.EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Client update(Client dto) throws ConstraintViolationException,
            javax.persistence.EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateClientPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentEx"));
        }
        if (dto.getCompanyIDNumber() == null || dto.getCompanyIDNumber().length() == 0) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentEx.Code"));
        }
        try {
            Client item = dao.find(Client.class,
                    dto.getCompanyIDNumber(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("Client.EntityNotFoundEx",
                                dto.getCompanyIDNumber())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setCountry(dto.getCountry());
            item.setPlace(dto.getPlace());
            item.setTownship(dto.getTownship());
            item.setStreet(dto.getStreet());
            item.setPostCode(dto.getPostCode());
            item.setEMail(dto.getEMail());
            item.setFax(dto.getFax());
            item.setPhone(dto.getPhone());
            item.setBank(dto.getBank());
            item.setStatus(dto.getStatus());
            item.setVersion(dto.getVersion());
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
                        Utils.getMessage("Client.OptimisticLockEx",
                                dto.getCompanyIDNumber()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Client.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String companyIDNumber) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteClientPermission
        if (companyIDNumber == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Client.IllegalArgumentEx.Code")
            );
        }
        try {
            Client service = dao.find(Client.class, companyIDNumber);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Client read(String companyIDNumber) throws javax.persistence.EntityNotFoundException {
        //TODO : check ReadClientPermission
        if (companyIDNumber == null) {
            throw new javax.persistence.EntityNotFoundException(
                    Utils.getMessage("Client.IllegalArgumentEx.Code")
            );
        }
        try {
            Client Client = dao.find(Client.class, companyIDNumber);
            if (Client == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("Client.EntityNotFoundEx", companyIDNumber)
                );
            }
            return Client;
        } catch (javax.persistence.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<Client> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadClientPermission
        String companyIDNumber = null;
        String name = null;
        String TIN = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("companyIDNumber") && s.getValue() instanceof String) {
                companyIDNumber = (String) s.getValue();
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
                    companyIDNumber,
                    name,
                    TIN);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Client.PageNotExists", pageNumber));
            }
            ReadRangeDTO<Client> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Client = last page number * Clients per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.search(dao,
                        companyIDNumber,
                        name,
                        TIN,
                        start,
                        pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(this.search(dao,
                        companyIDNumber,
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
                    Utils.getMessage("Client.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private Long count(
            EntityManager EM,
            String companyIDNumber,
            String name,
            String TIN) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Client> root = c.from(Client.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (companyIDNumber != null && companyIDNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Client_.companyIDNumber)),
                    cb.parameter(String.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Client_.name)),
                    cb.parameter(String.class, "desc")));
        }
        if (TIN != null && TIN.isEmpty() == false) {
            criteria.add(cb.like(
                            root.get(Client_.TIN),
                            cb.parameter(String.class, "TIN"))
            );
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (companyIDNumber != null && companyIDNumber.isEmpty() == false) {
            q.setParameter("code", companyIDNumber.toUpperCase() + "%");
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", name.toUpperCase() + "%");
        }
        if (TIN != null && TIN.isEmpty() == false) {
            q.setParameter("TIN", TIN);
        }

        return q.getSingleResult();
    }

    private List<Client> search(EntityManager em,
                                String companyIDNumber,
                                String name,
                                String TIN,
                                int first,
                                int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (companyIDNumber != null && companyIDNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Client_.companyIDNumber)),
                    cb.parameter(String.class, "code")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Client_.name)),
                    cb.parameter(String.class, "desc")));
        }
        if (TIN != null && TIN.isEmpty() == false) {
            criteria.add(cb.like(root.get(Client_.TIN),
                    cb.parameter(String.class, "TIN")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Client_.companyIDNumber)));
        TypedQuery<Client> typedQuery = em.createQuery(query);
        if (companyIDNumber != null && companyIDNumber.isEmpty() == false) {
            typedQuery.setParameter("code", companyIDNumber.toUpperCase() + "%");
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
    public List<Client> readAll(String companyIDNumber,
                                String name,
                                String TIN) {
        try {
            return this.search(dao, companyIDNumber, name, TIN, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.ReadAll"), ex);
        }
    }
}


