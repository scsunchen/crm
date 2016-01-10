/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.ApplicationUser;
import com.invado.finance.Utils;
import com.invado.core.domain.Article;
import com.invado.core.domain.Article_;
import com.invado.core.domain.InvoiceItem;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author root
 */
@Service
public class ArticleService {

    private static final Logger LOG = Logger.getLogger(
            ArticleService.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager dao;

    @Inject
    private Validator validator;


    @Transactional(rollbackFor = Exception.class)
    public Article create(Article a) throws ConstraintViolationException,
            EntityExistsException {
        //check CreateArticlePermission
        if (a == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Article.IllegalArgumentEx"));
        }
        if (a.getCode() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Article.IllegalArgumentEx.Code"));
        }
        try {
            if (dao.find(Article.class, a.getCode()) != null) {
                throw new EntityExistsException(
                        Utils.getMessage("Article.EntityExistsEx", a.getCode())
                );
            }
            a.setUnitsInStock(BigDecimal.ZERO);
            a.setUpdated(LocalDate.now());
            ApplicationUser user = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME,
                    ApplicationUser.class)
                    .setParameter(1, a.getLastUpdateBy().getUsername())
                    .getSingleResult();
            a.setLastUpdateBy(user);
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            dao.persist(a);
            return a;
        } catch (ConstraintViolationException | EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Article.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Article update(Article dto) throws 
            ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateArticlePermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    "", Arrays.asList(Utils.getMessage("Article.IllegalArgumentEx")));
        }
        if (dto.getCode() == null || dto.getCode().length() == 0) {
            throw new ConstraintViolationException(
                    "", Arrays.asList(Utils.getMessage("Article.IllegalArgumentEx.Code")));
        }
        try {
            Article item = dao.find(Article.class,
                    dto.getCode(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Article.EntityNotFoundEx",
                                dto.getCode())
                );
            }
            dto.setUpdated(LocalDate.now());
            dto.setUnitsInStock(item.getUnitsInStock());
            ApplicationUser user = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME,
                    ApplicationUser.class)
                    .setParameter(1, dto.getLastUpdateBy().getUsername())
                    .getSingleResult();
            dto.setLastUpdateBy(user);
            List<String> msgs = validator.validate(dto).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new com.invado.core.exception.ConstraintViolationException("", msgs);
            }
            dao.merge(dto);
            return item;
        } catch (com.invado.core.exception.ConstraintViolationException
                | EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("Article.OptimisticLockEx",
                                dto.getCode()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Article.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String code) throws ConstraintViolationException,
            ReferentialIntegrityException {
        //TODO : check DeleteArticlePermission
        if (code == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Article.IllegalArgumentEx.Code")
            );
        }
        try {
            Article service = dao.find(Article.class, code);
            if (service != null) {
                if (dao.createNamedQuery(InvoiceItem.READ_BY_ARTICLE)
                        .setParameter("code", code)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "Article.ReferentialIntegrityEx.InvoiceItem",
                            code)
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
                    Utils.getMessage("Article.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Article read(String code) throws EntityNotFoundException {
        //TODO : check ReadArticlePermission
        if (code == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("Article.IllegalArgumentEx.Code")
            );
        }
        try {
            Article article = dao.find(Article.class, code);
            if (article == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Article.EntityNotFoundEx", code)
                );
            }
            return article;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Article.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<Article> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadArticlePermission
        String code = null;
        String desc = null;
        LocalDate updateFrom = null;
        LocalDate updateTo = null;
        BigDecimal stockFrom = null;
        BigDecimal stockTo = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("code") && s.getValue() instanceof String) {
                code = (String) s.getValue();
            }
            if (s.getKey().equals("description") && s.getValue() instanceof String) {
                desc = (String) s.getValue();
            }
            if (s.getKey().equals("updatedFrom") && s.getValue() instanceof LocalDate) {
                updateFrom = (LocalDate) s.getValue();
            }
            if (s.getKey().equals("updatedTo") && s.getValue() instanceof LocalDate) {
                updateTo = (LocalDate) s.getValue();
            }
            if (s.getKey().equals("stockFrom") && s.getValue() instanceof BigDecimal) {
                stockFrom = (BigDecimal) s.getValue();
            }
            if (s.getKey().equals("stockTo") && s.getValue() instanceof BigDecimal) {
                stockTo = (BigDecimal) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao,
                    code,
                    desc,
                    updateFrom,
                    updateTo,
                    stockFrom,
                    stockTo);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Article.PageNotExists", pageNumber));
            }
            ReadRangeDTO<Article> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first article = last page number * articles per page 
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.search(dao,
                        code,
                        desc,
                        updateFrom,
                        updateTo,
                        stockFrom,
                        stockTo,
                        start,
                        pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(this.search(dao,
                        code,
                        desc,
                        updateFrom,
                        updateTo,
                        stockFrom,
                        stockTo,
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
                    Utils.getMessage("Article.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private Long count(
            EntityManager EM,
            String code,
            String desc,
            LocalDate dateFrom,
            LocalDate dateTo,
            BigDecimal stockFrom,
            BigDecimal stockTo) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Article> root = c.from(Article.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (code != null && code.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Article_.code)),
                    cb.parameter(String.class, "code")));
        }
        if (desc != null && desc.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Article_.description)),
                    cb.parameter(String.class, "desc")));
        }
        if (dateFrom != null) {
            criteria.add(cb.greaterThanOrEqualTo(
                            root.get(Article_.updated),
                            cb.parameter(Date.class, "updatedFrom"))
            );
        }
        if (dateTo != null) {
            criteria.add(cb.lessThanOrEqualTo(root.get(Article_.updated),
                            cb.parameter(Date.class, "updatedTo"))
            );
        }
        if (stockFrom != null) {
            criteria.add(cb.greaterThanOrEqualTo(root.get(Article_.unitsInStock),
                            cb.parameter(BigDecimal.class, "stockFrom"))
            );
        }
        if (stockTo != null) {
            criteria.add(cb.lessThanOrEqualTo(root.get(Article_.unitsInStock),
                            cb.parameter(BigDecimal.class, "stockTo"))
            );
        }
        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (code != null && code.isEmpty() == false) {
            q.setParameter("code", "%"+ code.toUpperCase() + "%");
        }
        if (desc != null && desc.isEmpty() == false) {
            q.setParameter("desc", "%"+desc.toUpperCase() + "%");
        }
        if (dateFrom != null) {
            q.setParameter("updatedFrom", dateFrom);
        }
        if (dateTo != null) {
            q.setParameter("updatedTo", dateTo);
        }
        if (stockFrom != null) {
            q.setParameter("stockFrom", stockFrom);
        }
        if (stockTo != null) {
            q.setParameter("stockTo", stockTo);
        }
        return q.getSingleResult();
    }

    private List<Article> search(EntityManager em,
                                 String code,
                                 String desc,
                                 LocalDate updatedFrom,
                                 LocalDate updatedTo,
                                 BigDecimal stockFrom,
                                 BigDecimal stockTo,
                                 int first,
                                 int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Article> query = cb.createQuery(Article.class);
        Root<Article> root = query.from(Article.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (code != null && code.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Article_.code)),
                    cb.parameter(String.class, "code")));
        }
        if (desc != null && desc.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Article_.description)),
                    cb.parameter(String.class, "desc")));
        }
        if (updatedFrom != null) {
            criteria.add(cb.greaterThanOrEqualTo(root.get(Article_.updated),
                    cb.parameter(Date.class, "updatedFrom")));
        }
        if (updatedTo != null) {
            criteria.add(cb.lessThanOrEqualTo(root.get(Article_.updated),
                            cb.parameter(Date.class, "updatedTo"))
            );
        }
        if (stockFrom != null) {
            criteria.add(cb.greaterThanOrEqualTo(root.get(Article_.unitsInStock),
                            cb.parameter(BigDecimal.class, "stockFrom"))
            );
        }
        if (stockTo != null) {
            criteria.add(cb.lessThanOrEqualTo(root.get(Article_.unitsInStock),
                            cb.parameter(BigDecimal.class, "stockTo"))
            );
        }
        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Article_.code)));
        TypedQuery<Article> typedQuery = em.createQuery(query);
        if (code != null && code.isEmpty() == false) {
            typedQuery.setParameter("code", "%" + code.toUpperCase() + "%");
        }
        if (desc != null && desc.isEmpty() == false) {
            typedQuery.setParameter("desc", "%" + desc.toUpperCase() + "%");
        }
        if (updatedFrom != null) {
            typedQuery.setParameter("updatedFrom", updatedFrom);
        }
        if (updatedTo != null) {
            typedQuery.setParameter("updatedTo", updatedTo);
        }
        if (stockFrom != null) {
            typedQuery.setParameter("stockFrom", stockFrom);
        }
        if (stockTo != null) {
            typedQuery.setParameter("stockTo", stockTo);
        }
        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Article> readAll(String code,
                                 String desc,
                                 LocalDate updatedFrom,
                                 LocalDate updatedTo,
                                 BigDecimal stockFrom,
                                 BigDecimal stockTo) {
        try {
            return this.search(dao, code, desc, updatedFrom, updatedTo, stockFrom, stockTo, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Article.PersistenceEx.ReadAll"), ex);
        }
    }
}
