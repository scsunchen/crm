package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.domain.ExchangeRate;
import com.invado.core.dto.ExchangeRateDTO;
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


import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Nikola on 04/10/2015.
 */
@Service
public class ExcangeRateService {
    private static final Logger LOG = Logger.getLogger(
            ExchangeRate.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public ExchangeRate create(ExchangeRateDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateExchangeRatePermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentEx"));
        }
        if (a.getApplicationDate() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.date"));
        }
        if (a.getToCurrency() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.toCurrency"));
        }
        if (a.getBuying() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.buying"));
        }
        if (a.getMiddle() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.middle"));
        }
        if (a.getSelling() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.selling"));
        }
        try {

            ExchangeRatePK exchangeRatePK = new ExchangeRatePK(a.getApplicationDate(), a.getISOCode());

            if (dao.find(ExchangeRate.class, exchangeRatePK) != null) {
                throw new com.invado.core.exception.EntityExistsException(
                        com.invado.finance.Utils.getMessage("ExcangeRate.EntityExistsEx", a.getApplicationDate() + " " + a.getISOCode())
                );
            }
            ExchangeRate exchangeRate = new ExchangeRate(a.getApplicationDate(), dao.find(Currency.class, a.getISOCode()));

            exchangeRate.setBuying(a.getBuying());
            exchangeRate.setMiddle(a.getMiddle());
            exchangeRate.setSelling(a.getSelling());
            exchangeRate.setListNumber(a.getListNumber());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(exchangeRate);
            return exchangeRate;
        } catch (IllegalArgumentException | javax.persistence.EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("ExchangeRate.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public ExchangeRate update(ExchangeRateDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateExchangeRatePermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentEx"));
        }
        if (dto.getApplicationDate() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.date"));
        }
        if (dto.getToCurrency() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.toCurrency"));
        }
        if (dto.getBuying() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.buying"));
        }
        if (dto.getMiddle() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.middle"));
        }
        if (dto.getSelling() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.selling"));
        }
        try {

            ExchangeRatePK exchangeRatePK = new ExchangeRatePK(dto.getApplicationDate(), dto.getISOCode());
            ExchangeRate item = dao.find(ExchangeRate.class, exchangeRatePK,
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("ExchangeRate.EntityNotFoundEx",
                                dto.getApplicationDate() + ", " + dto.getISOCode())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setMiddle(dto.getMiddle());
            item.setSelling(dto.getSelling());
            item.setBuying(dto.getBuying());

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
                        Utils.getMessage("ExchangeRate.OptimisticLockEx",
                                dto.getApplicationDate() + ", " + dto.getISOCode()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("ExchangeRate.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(LocalDate applicationDate, String toCurrency) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteExchangeRatePermission
        if (applicationDate == null || toCurrency == null || toCurrency.isEmpty()) {
            throw new IllegalArgumentException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentEx.Code")
            );
        }
        try {
            ExchangeRatePK exchangeRatePK = new ExchangeRatePK(applicationDate, toCurrency);
            ExchangeRate service = dao.find(ExchangeRate.class, exchangeRatePK);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("ExchangeRate.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ExchangeRateDTO read(LocalDate applicationDate, String toCurrency) throws EntityNotFoundException {
        //TODO : check ReadExchangeRatePermission
        if (applicationDate == null || toCurrency == null || toCurrency.isEmpty()) {
            throw new EntityNotFoundException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentEx.Code")
            );
        }
        try {
            ExchangeRatePK exchangeRatePK = new ExchangeRatePK(applicationDate, toCurrency);
            ExchangeRate ExchangeRate = dao.find(ExchangeRate.class, exchangeRatePK);
            if (ExchangeRate == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("ExchangeRate.EntityNotFoundEx", applicationDate, toCurrency)
                );
            }
            return ExchangeRate.getDTO();
        } catch (javax.persistence.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("ExchangeRate.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<ExchangeRateDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadExchangeRatePermission
        Date applicationDate = null;
        String toCurrency = null;
        Integer listNumber = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("applicationDate") && s.getValue() instanceof Date) {
                applicationDate = (Date) s.getValue();
            }
            if (s.getKey().equals("toCurrency") && s.getValue() instanceof String) {
                toCurrency = (String) s.getValue();
            }
            if (s.getKey().equals("listNumber") && s.getValue() instanceof Integer) {
                listNumber = (Integer) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao,
                    applicationDate,
                    toCurrency,
                    listNumber);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("ExchangeRate.PageNotExists", pageNumber));
            }
            ReadRangeDTO<ExchangeRateDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first ExchangeRate = last page number * ExchangeRates per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(convertToDTO(this.search(dao,
                        applicationDate,
                        toCurrency,
                        listNumber,
                        start,
                        pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao,
                        applicationDate,
                        toCurrency,
                        listNumber,
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
                    Utils.getMessage("ExchangeRate.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<ExchangeRateDTO> convertToDTO(List<ExchangeRate> lista) {
        List<ExchangeRateDTO> listaDTO = new ArrayList<>();
        for (ExchangeRate pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Date applicationDate,
            String toCurrency,
            Integer listNumber) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<ExchangeRate> root = c.from(ExchangeRate.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (applicationDate != null) {
            criteria.add(cb.equal(root.get(ExchangeRate_.applicationDate),
                    cb.parameter(Date.class, "applicationDate")));
        }
        if (toCurrency != null && toCurrency.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(ExchangeRate_.toCurrency).get(Currency_.ISOCode)),
                    cb.parameter(String.class, "toCurrency")));
        }
        if (listNumber != null) {
            criteria.add(cb.equal(root.get(ExchangeRate_.listNumber),
                    cb.parameter(Integer.class, "listNumber")));
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (applicationDate != null) {
            q.setParameter("applicationDate", applicationDate);
        }
        if (toCurrency != null && toCurrency.isEmpty() == false) {
            q.setParameter("toCurrency", toCurrency);
        }
        if (listNumber != null) {
            q.setParameter("listNumber", listNumber);
        }

        return q.getSingleResult();
    }

    private List<ExchangeRate> search(EntityManager em,
                                      Date applicationDate,
                                      String toCurrency,
                                      Integer listNumber,
                                      int first,
                                      int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExchangeRate> query = cb.createQuery(ExchangeRate.class);
        Root<ExchangeRate> root = query.from(ExchangeRate.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (applicationDate != null) {
            criteria.add(cb.equal(root.get(ExchangeRate_.applicationDate),
                    cb.parameter(Date.class, "applicationDate")));
        }
        if (toCurrency != null && toCurrency.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(ExchangeRate_.toCurrency).get(Currency_.ISOCode)),
                    cb.parameter(String.class, "toCurrency")));
        }
        if (listNumber != null) {
            criteria.add(cb.equal(root.get(ExchangeRate_.listNumber),
                    cb.parameter(Integer.class, "listNumber")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(ExchangeRate_.applicationDate)))
                .orderBy(cb.asc(root.get(ExchangeRate_.toCurrency).get(Currency_.ISOCode)));
        TypedQuery<ExchangeRate> typedQuery = em.createQuery(query);
        if (applicationDate != null) {
            typedQuery.setParameter("applicationDate", applicationDate);
        }
        if (toCurrency != null && toCurrency.isEmpty() == false) {
            typedQuery.setParameter("toCurrency", toCurrency);
        }
        if (listNumber != null) {
            typedQuery.setParameter("listNumber", listNumber);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

}
