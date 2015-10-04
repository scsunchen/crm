package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Currency;
import com.invado.core.domain.Currency_;
import com.invado.core.dto.CurrencyDTO;
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
import sun.util.resources.cldr.aa.CurrencyNames_aa;

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
 * Created by NikolaB on 6/28/2015.
 */
@Service
public class CurrencyService {

    private static final Logger LOG = Logger.getLogger(
            Currency.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public Currency create(CurrencyDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateCurrencyPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Currency.IllegalArgumentEx"));
        }
        if (a.getISOCode() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Currency.IllegalArgumentEx.ISOCode"));
        }
        if (a.getCurrency() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Currency.IllegalArgumentEx.Currency"));
        }
        try {

            Currency currency = new Currency();

            currency.setDescription(a.getDescription());
            currency.setState(a.getState());
            currency.setISONumber(a.getISONumber());
            currency.setISOCode(a.getISOCode());
            currency.setCurrency(a.getCurrency());
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(currency);
            return currency;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Currency.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Currency update(CurrencyDTO dto) throws IllegalArgumentException, ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateCurrencyPermission
        if (dto == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Currency.IllegalArgumentEx"));
        }
        if (dto.getISOCode() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Currency.IllegalArgumentEx.ISOCode"));
        }
        if (dto.getCurrency() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Currency.IllegalArgumentEx.Currency"));
        }
        try {
            Currency item = dao.find(Currency.class,
                    dto.getISOCode(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Currency.EntityNotFoundEx",
                                dto.getISOCode())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setCurrency(dto.getCurrency());
            item.setDescription(dto.getDescription());
            item.setISOCode(dto.getISOCode());
            item.setISONumber(dto.getISONumber());
            item.setState(dto.getState());
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
                        Utils.getMessage("Currency.OptimisticLockEx",
                                dto.getISOCode()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Currency.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String code) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteCurrencyPermission
        if (code == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Currency.IllegalArgumentEx.Code")
            );
        }
        try {
            Currency service = dao.find(Currency.class, code);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Currency.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public CurrencyDTO read(String ISOCode) throws EntityNotFoundException {
        //TODO : check ReadCurrencyPermission
        if (ISOCode == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("Currency.IllegalArgumentEx.Code")
            );
        }
        try {
            Currency Currency = dao.find(Currency.class, ISOCode);
            if (Currency == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Currency.EntityNotFoundEx", ISOCode)
                );
            }
            return Currency.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Currency.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<CurrencyDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadCurrencyPermission
        String ISOCode = null;
        Integer ISONumber = null;
        String state = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("ISOCode") && s.getValue() instanceof String) {
                ISOCode = (String) s.getValue();
            }
            if (s.getKey().equals("ISONumber") && s.getValue() instanceof String) {
                ISONumber = (Integer) s.getValue();
            }
            if (s.getKey().equals("state") && s.getValue() instanceof String) {
                state = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, ISOCode, ISONumber, state);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Currency.PageNotExists", pageNumber));
            }
            ReadRangeDTO<CurrencyDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Currency = last page number * Currencys per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(convertToDTO(this.search(dao, ISOCode, ISONumber, state, start, pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao, ISOCode, ISONumber, state,
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
                    Utils.getMessage("Currency.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<CurrencyDTO> convertToDTO(List<Currency> lista) {
        List<CurrencyDTO> listaDTO = new ArrayList<>();
        for (Currency pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }
    private Long count(
            EntityManager EM,
            String ISOCode, Integer ISONumber, String state) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Currency> root = c.from(Currency.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (ISOCode != null && ISOCode.isEmpty() == false) {
            criteria.add(cb.equal(cb.upper(root.get(Currency_.ISOCode)),
                    cb.parameter(Integer.class, "ISOCode")));
        }
        if (ISONumber != null) {
            criteria.add(cb.equal(root.get(Currency_.ISONumber),
                    cb.parameter(String.class, "ISONumber")));
        }
        if (state != null && state.isEmpty() == false) {
            criteria.add(cb.equal(cb.upper(root.get(Currency_.state)),
                    cb.parameter(Integer.class, "state")));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (ISOCode != null && ISOCode.isEmpty() == false) {
            q.setParameter("ISOCode", ISOCode);
        }
        if (ISONumber != null) {
            q.setParameter("ISONumber", ISONumber);
        }
        if (state != null && state.isEmpty() == false) {
            q.setParameter("state", state);
        }

        return q.getSingleResult();
    }

    private List<Currency> search(EntityManager em,
                                  String ISOCode,
                                  Integer ISONumber,
                                  String state,
                                  int first,
                                  int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Currency> query = cb.createQuery(Currency.class);
        Root<Currency> root = query.from(Currency.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (ISONumber != null) {
            criteria.add(cb.equal(root.get(Currency_.ISONumber),
                    cb.parameter(Integer.class, "ISONumber")));
        }
        if (ISOCode != null && ISOCode.isEmpty() == false) {
            criteria.add(cb.like(root.get(Currency_.ISOCode),
                    cb.parameter(String.class, "ISOCode")));
        }
        if (state != null && state.isEmpty() == false) {
            criteria.add(cb.like(root.get(Currency_.state),
                    cb.parameter(String.class, "state")));
        }
        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Currency_.ISOCode)));
        TypedQuery<Currency> typedQuery = em.createQuery(query);
        if (ISONumber != null) {
            typedQuery.setParameter("ISONumber", ISONumber);
        }
        if (ISOCode != null && ISOCode.isEmpty() == false) {
            typedQuery.setParameter("ISOCode", ISOCode.toUpperCase() + "%");
        }
        if (state != null && state.isEmpty() == false) {
            typedQuery.setParameter("state", ISOCode.toUpperCase() + "%");
        }
        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<CurrencyDTO> readAll(
            String ISOCode,
            Integer ISONumber,
            String state) {
        try {
            return convertToDTO(this.search(dao, ISOCode, ISONumber, state, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Currency.PersistenceEx.ReadAll"), ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Currency> readByName(String name) {
        try {
            return dao.createNamedQuery(
                    Currency.READ_BY_NAME_ORDERBY_NAME,
                    Currency.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "Currency.Exception.ReadItemByDescription"),
                    ex);
        }
    }
}

