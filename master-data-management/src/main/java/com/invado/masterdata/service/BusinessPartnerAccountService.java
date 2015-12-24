package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.BusinessPartnerAccountDTO;
import com.invado.core.exception.*;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.IllegalArgumentException;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
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
 * Created by Nikola on 23/12/2015.
 */
@Service
public class BusinessPartnerAccountService {

    private static final Logger LOG = Logger.getLogger(
            BusinessPartnerAccount.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Inject
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartnerAccount create(BusinessPartnerAccountDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateBusinessPartnerAccountPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx"));
        }
        if (a.getBankId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.Bank"));
        }
        if (a.getAccount() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.Account"));
        }
        if (a.getAccountOwnerId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.BusinessPartner"));
        }
        if (a.getBankId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.Bank"));
        }
        try {
            BusinessPartnerAccount businessPartnerAccount = new BusinessPartnerAccount();

            businessPartnerAccount.setAccount(a.getAccount());
            businessPartnerAccount.setAccountOwner(dao.find(BusinessPartner.class, a.getAccountOwnerId()));
            businessPartnerAccount.setBank(dao.find(BusinessPartner.class, a.getBankId()));
            if (a.getCurrency() != null)
                businessPartnerAccount.setCurrency(dao.find(Currency.class, a.getISOCode()));

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(businessPartnerAccount);
            return businessPartnerAccount;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerAccount.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartnerAccount update(BusinessPartnerAccountDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBusinessPartnerAccountPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx"));
        }
        if (dto.getBankId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.Bank"));
        }
        if (dto.getAccount() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.Account"));
        }
        if (dto.getAccountOwnerId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.BusinessPartner"));
        }
        if (dto.getBankId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.Bank"));
        }
        try {
            BusinessPartnerAccount item = dao.find(BusinessPartnerAccount.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerAccount.EntityNotFoundEx",
                                dto.getId())
                );
            }

            item.setAccount(dto.getAccount());
            item.setAccountOwner(dao.find(BusinessPartner.class, dto.getAccountOwnerId()));
            item.setBank(dao.find(BusinessPartner.class, dto.getBankId()));
            if (dto.getCurrency() != null)
                item.setCurrency(dao.find(Currency.class, dto.getISOCode()));

            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setVersion(dto.getVersion());
            List<String> msgs = validator.validate(item).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            dao.flush();
            return item;
        } catch (ConstraintViolationException | EntityNotFoundException  ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("BusinessPartnerAccount.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("BusinessPartnerAccount.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteBusinessPartnerAccountPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.Id")
            );
        }
        try {
            BusinessPartnerAccount BusinessPartnerAccount = dao.find(BusinessPartnerAccount.class, id);
            if (BusinessPartnerAccount != null) {
                dao.remove(id);
                dao.flush();
            }
        }  catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerAccount.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerAccount read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerAccountPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("BusinessPartnerAccount.IllegalArgumentEx.Id")
            );
        }
        try {
            BusinessPartnerAccount BusinessPartnerAccount = dao.find(BusinessPartnerAccount.class, id);
            if (BusinessPartnerAccount == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerAccount.EntityNotFoundEx", id)
                );
            }
            return BusinessPartnerAccount;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerAccount.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BusinessPartnerAccountDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBusinessPartnerAccountPermission
        Integer id = null;
        String account = null;
        BusinessPartner accountOwner = null;
        BusinessPartner bank = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("account") && s.getValue() instanceof String) {
                account = (String) s.getValue();
            }

            if (s.getKey().equals("accountOwner")) {
                if (s.getValue() instanceof BusinessPartner) {
                    accountOwner = (BusinessPartner) s.getValue();
                } else if (s.getValue() instanceof Integer) {
                    accountOwner = dao.find(BusinessPartner.class, s.getValue());
                }
            }
            if (s.getKey().equals("bank")) {
                if (s.getValue() instanceof BusinessPartner) {
                    bank = (BusinessPartner) s.getValue();
                } else if (s.getValue() instanceof Integer) {
                    bank = dao.find(BusinessPartner.class, s.getValue());
                }
            }
        }

        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, id, account, accountOwner, bank);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("BusinessPartnerAccount.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BusinessPartnerAccountDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartnerAccount = last page number * BusinessPartnerAccounts per page
                int start = numberOfPages.intValue() * pageSize;

                result.setData(convertToDTO(this.search(dao, id, account, accountOwner, bank, start, pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao, id,  account, accountOwner, bank,
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
                    Utils.getMessage("BusinessPartnerAccount.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<BusinessPartnerAccountDTO> convertToDTO(List<BusinessPartnerAccount> lista) {
        List<BusinessPartnerAccountDTO> listaDTO = new ArrayList<>();
        for (BusinessPartnerAccount pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            String account,
            BusinessPartner accountOwner,
            BusinessPartner bank) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartnerAccount> root = c.from(BusinessPartnerAccount.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerAccount_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (account != null && account.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartnerAccount_.account)),
                    cb.parameter(String.class, "account")));
        }
        if (accountOwner != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerAccount_.accountOwner),
                    cb.parameter(BusinessPartnerAccount.class, "accountOwner")));
        }
        if (bank != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerAccount_.bank),
                    cb.parameter(BusinessPartnerAccount.class, "bank")));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (account != null) {
            q.setParameter("account", account);
        }
        if (accountOwner != null) {
            q.setParameter("accountOwner", accountOwner);
        }
        if (bank != null) {
            q.setParameter("bank", bank);
        }


        return q.getSingleResult();
    }

    private List<BusinessPartnerAccount> search(EntityManager em,
                                                Integer id,
                                                String account,
                                                BusinessPartner accountOwner,
                                                BusinessPartner bank,
                                                int first,
                                                int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusinessPartnerAccount> query = cb.createQuery(BusinessPartnerAccount.class);
        Root<BusinessPartnerAccount> root = query.from(BusinessPartnerAccount.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerAccount_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (account != null && account.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartnerAccount_.account)),
                    cb.parameter(String.class, "account")));
        }
        if (accountOwner != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerAccount_.accountOwner),
                    cb.parameter(BusinessPartnerAccount.class, "accountOwner")));
        }
        if (bank != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerAccount_.bank),
                    cb.parameter(BusinessPartnerAccount.class, "bank")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BusinessPartnerAccount_.id)));
        TypedQuery<BusinessPartnerAccount> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (account != null) {
            typedQuery.setParameter("account", account);
        }
        if (accountOwner != null) {
            typedQuery.setParameter("accountOwner", accountOwner);
        }
        if (bank != null) {
            typedQuery.setParameter("bank", bank);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BusinessPartnerAccountDTO> readAll(
            Integer id,
            String account,
            BusinessPartner accountOwner,
            BusinessPartner bank) {
        try {
            return convertToDTO(this.search(dao, id, account, accountOwner, bank, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartnerAccount.PersistenceEx.ReadAll"), ex);
        }
    }

}

