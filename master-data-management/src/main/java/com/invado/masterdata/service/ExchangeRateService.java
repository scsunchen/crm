package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.domain.ExchangeRate;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.masterdata.service.dto.ExchangeRateDTO;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.EntityExistsException;
import com.invado.masterdata.service.exception.EntityNotFoundException;
import com.invado.core.exception.IllegalArgumentException;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
public class ExchangeRateService {
    private static final Logger LOG = Logger.getLogger(
            ExchangeRate.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";

    @Autowired
    private LocalDateConverter localDateConverter;


    @Transactional(rollbackFor = Exception.class)
    public ExchangeRate create(ExchangeRateDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateExchangeRatePermission
        if (a == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentEx"));
        }
        if (a.getApplicationDate() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.date"));
        }
        if (a.getCurrencyISOCode() == null || a.getCurrencyISOCode().isEmpty()) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.toCurrencyISOCode"));
        }
        if (a.getCurrency() == null || a.getCurrency().isEmpty()) {
            throw new ConstraintViolationException(
                    Utils.getMessage("ExchangeRate.IllegalArgumentException.toCurrency"));
        }
        if (a.getBuying() == null ) {
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

            ExchangeRatePK exchangeRatePK = new ExchangeRatePK(localDateConverter.convertToDatabaseColumn(a.getApplicationDate()), a.getCurrencyISOCode());

            if (dao.find(ExchangeRate.class, exchangeRatePK) != null) {
                throw new EntityExistsException(
                        Utils.getMessage("ExchangeRate.EntityExistsEx", a.getApplicationDate() + " " + a.getCurrencyISOCode())
                );
            }

            ExchangeRate exchangeRate = new ExchangeRate(localDateConverter.convertToDatabaseColumn(a.getApplicationDate()),dao.find(Currency.class, a.getCurrencyISOCode()));

            exchangeRate.setToCurrency(dao.find(Currency.class, a.getCurrencyISOCode()));
            exchangeRate.setBuying(a.getBuying());
            exchangeRate.setMiddle(a.getMiddle());
            exchangeRate.setSelling(a.getSelling());
            exchangeRate.setListNumber(a.getListNumber());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            dao.persist(exchangeRate);
            return exchangeRate;
        } catch (ConstraintViolationException | EntityExistsException ex) {
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

            ExchangeRatePK exchangeRatePK = new ExchangeRatePK(localDateConverter.convertToDatabaseColumn(dto.getApplicationDate()), dto.getCurrencyISOCode());
            ExchangeRate item = dao.find(ExchangeRate.class, exchangeRatePK,
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("ExchangeRate.EntityNotFoundEx",
                                dto.getApplicationDate() + ", " + dto.getCurrencyISOCode())
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
                                dto.getApplicationDate() + ", " + dto.getCurrencyISOCode()),
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
            ExchangeRatePK exchangeRatePK = new ExchangeRatePK(localDateConverter.convertToDatabaseColumn(applicationDate), toCurrency);
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
            ExchangeRatePK exchangeRatePK = new ExchangeRatePK(localDateConverter.convertToDatabaseColumn(applicationDate), toCurrency);
            ExchangeRate exchangeRate = dao.find(ExchangeRate.class, exchangeRatePK);
            if (exchangeRate == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("ExchangeRate.EntityNotFoundEx", applicationDate, toCurrency)
                );
            }
            return getDTO(exchangeRate);
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
            throws PageNotExistsException, ParseException {
        //TODO : check ReadExchangeRatePermission
        Date applicationDate = null;
        String toCurrency = null;
        Integer listNumber = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("applicationDate") && s.getValue() instanceof String && !((String) s.getValue()).isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                applicationDate = formatter.parse((String) s.getValue());
            }
            if (s.getKey().equals("toCurrency") && s.getValue() instanceof String && !((String) s.getValue()).isEmpty()) {
                toCurrency = (String) s.getValue();
            }
            if (s.getKey().equals("listNumber") && s.getValue() instanceof Integer && s.getValue() != null) {
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
            listaDTO.add(getDTO(pr));
        }
        return listaDTO;
    }

    public ExchangeRateDTO getDTO(ExchangeRate exchangeRate){

        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();

        exchangeRateDTO.setBuying(exchangeRate.getBuying());
        exchangeRateDTO.setSelling(exchangeRate.getSelling());
        exchangeRateDTO.setMiddle(exchangeRate.getMiddle());
        exchangeRateDTO.setListNumber(exchangeRate.getListNumber());


        Date date = exchangeRate.getApplicationDate();
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
System.out.println("res je "+res);
        System.out.println("appDate je " + exchangeRate.getApplicationDate());
        //Date date = Date.from(exchangeRate.getApplicationDate(). atStartOfDay(ZoneId.systemDefault()).toInstant());
        //LocalDate date = exchangeRate.getApplicationDate().toInstant()  .atZone(ZoneId.systemDefault()).toLocalDate();

        exchangeRateDTO.setApplicationDate(res);
        System.out.println("DTODate je " + exchangeRateDTO.getApplicationDate());
        exchangeRateDTO.setCurrencyISOCode(exchangeRate.getToCurrencyISOCode());
        exchangeRateDTO.setCurrency(exchangeRate.getToCurrencyDescription());

        return exchangeRateDTO;

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
