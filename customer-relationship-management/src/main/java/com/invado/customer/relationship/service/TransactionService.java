package com.invado.customer.relationship.service;

import com.invado.core.domain.*;
import com.invado.core.domain.Currency;
import com.invado.core.domain.Properties;
import com.invado.core.dto.InvoiceDTO;
import com.invado.core.dto.InvoiceItemDTO;
import com.invado.core.utils.NativeQueryResultsMapper;
import com.invado.customer.relationship.Utils;
import com.invado.customer.relationship.domain.*;
import com.invado.customer.relationship.service.dto.InvoicingTransactionSetDTO;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.customer.relationship.service.exception.IllegalArgumentException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.stream.Collectors;

/**
 * Created by Nikola on 23/08/2015.
 */
@Service
public class TransactionService {


    private static final Logger LOG = Logger.getLogger(
            Transaction.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager dao;

    @Inject
    private Validator validator;


    @Transactional(rollbackFor = Exception.class)
    public Transaction create(TransactionDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateTransactionPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Transaction.IllegalArgumentEx"));
        }
        try {

            Transaction transaction = new Transaction();

            transaction.setId(a.getId());
            transaction.setAmount(a.getAmount());
            transaction.setDistributor(dao.find(Client.class, a.getDistributorId()));
            transaction.setPointOfSale(dao.find(BusinessPartner.class, a.getPointOfSaleId()));
            transaction.setServiceProvider(dao.find(BusinessPartner.class, a.getServiceProviderId()));
            transaction.setStatusId(a.getStatusId());
            transaction.setTerminal(dao.find(Device.class, a.getTerminalId()));
            transaction.setType(dao.find(TransactionType.class, a.getTypeId()));
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }

            dao.persist(transaction);
            return transaction;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Transaction.Exception.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Transaction update(TransactionDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateTransactionPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Transaction.IllegalArgumentEx"));
        }

        try {
            Transaction transaction = dao.find(Transaction.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (transaction == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Transaction.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(transaction, LockModeType.OPTIMISTIC);

            transaction.setId(dto.getId());
            transaction.setAmount(dto.getAmount());
            if (dto.getDistributorId() != null)
                transaction.setDistributor(dao.find(Client.class, dto.getDistributorId()));
            if (dto.getPointOfSaleId() != null)
                transaction.setPointOfSale(dao.find(BusinessPartner.class, dto.getPointOfSaleId()));
            if (dto.getServiceProviderId() != null)
                transaction.setServiceProvider(dao.find(BusinessPartner.class, dto.getServiceProviderId()));
            if (dto.getTerminalId() != null)
                transaction.setTerminal(dao.find(Device.class, dto.getTerminalId()));
            if (dto.getTypeId() != null)
                transaction.setType(dao.find(TransactionType.class, dto.getTypeId()));

            List<String> msgs = validator.validate(transaction).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            dao.flush();
            return transaction;
        } catch (ConstraintViolationException | EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("Transaction.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Transaction.Exception.Update"),
                        ex);
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteTransactionPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Transaction.IllegalArgumentEx.Code")
            );
        }
        try {
            Transaction service = dao.find(Transaction.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Transaction.Exception.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public TransactionDTO read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadTransactionPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("Transaction.IllegalArgumentEx.Code")
            );
        }
        try {
            Transaction transaction = dao.find(Transaction.class, id);
            if (transaction == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Transaction.EntityNotFoundEx", id)
                );
            }
            return transaction.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Transaction.Exception.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<TransactionDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadTransactionPermission
        Integer id = null;
        Integer distributorId = null;
        Integer pointOfSaleId = null;
        Integer serviceProviderId = null;
        Integer terminalId = null;
        Integer typeId = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && !(s.getValue() == null) && !s.getValue().toString().isEmpty()) {
                id = Integer.valueOf(s.getValue().toString());
            }
            if (s.getKey().equals("distributorId") && !(s.getValue() == null) && !s.getValue().toString().isEmpty()) {
                distributorId = Integer.valueOf(s.getValue().toString());
            }
            if (s.getKey().equals("pointOfSaleId") && !(s.getValue() == null) && !s.getValue().toString().isEmpty()) {
                pointOfSaleId = Integer.valueOf(s.getValue().toString());
            }
            if (s.getKey().equals("serviceProviderId") && !(s.getValue() == null) && !s.getValue().toString().isEmpty()) {
                serviceProviderId = Integer.valueOf(s.getValue().toString());
            }
            if (s.getKey().equals("terminalId") && !(s.getValue() == null) && !s.getValue().toString().isEmpty()) {
                terminalId = Integer.valueOf(s.getValue().toString());
            }
            if (s.getKey().equals("typeId") && !(s.getValue() == null) && !s.getValue().toString().isEmpty()) {
                typeId = Integer.valueOf(s.getValue().toString());
            }

        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, id, distributorId, pointOfSaleId, serviceProviderId, terminalId, typeId);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Transaction.PageNotExists", pageNumber));
            }
            ReadRangeDTO<TransactionDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Transaction = last page number * Transaction per page
                int start = numberOfPages.intValue() * pageSize;
                List<TransactionDTO> TransactionDTOList = convertToDTO(this.search(dao, id, distributorId, pointOfSaleId, serviceProviderId, terminalId, typeId, start, pageSize));
                result.setData(TransactionDTOList);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<TransactionDTO> TransactionDTOList = convertToDTO(this.search(dao, id, distributorId, pointOfSaleId, serviceProviderId, terminalId, typeId, p.getPage() * pageSize, pageSize));
                result.setData(TransactionDTOList);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Transaction.Exception.ReadAll", ex)
            );
        }
    }

    private List<TransactionDTO> convertToDTO(List<Transaction> lista) {
        List<TransactionDTO> listaDTO = new ArrayList<>();
        for (Transaction pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            Integer distributorId,
            Integer pointOfSaleId,
            Integer serviceProviderId,
            Integer terminalId,
            Integer typeId) {

        Client distributor = null;
        BusinessPartner pointOfSale = null;
        BusinessPartner serviceProvider = null;
        Device terminal = null;
        TransactionType type = null;

        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Transaction> root = c.from(Transaction.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Transaction_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (distributorId != null) {
            distributor = dao.find(Client.class, distributorId);
            criteria.add(cb.equal(root.get(Transaction_.distributor),
                    cb.parameter(Client.class, "distributor")));
        }
        if (pointOfSaleId != null) {
            pointOfSale = dao.find(BusinessPartner.class, pointOfSaleId);
            criteria.add(cb.equal(root.get(Transaction_.pointOfSale),
                    cb.parameter(BusinessPartner.class, "pointOfSale")));
        }
        if (serviceProviderId != null) {
            serviceProvider = dao.find(BusinessPartner.class, serviceProviderId);
            criteria.add(cb.equal(root.get(Transaction_.serviceProvider),
                    cb.parameter(BusinessPartner.class, "serviceProvider")));
        }
        if (terminalId != null) {
            terminal = dao.find(Device.class, terminalId);
            criteria.add(cb.equal(root.get(Transaction_.terminal),
                    cb.parameter(Device.class, "terminal")));
        }
        if (typeId != null) {
            type = dao.find(TransactionType.class, typeId);
            criteria.add(cb.equal(root.get(Transaction_.type),
                    cb.parameter(TransactionType.class, "type")));
        }


        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (distributor != null) {
            q.setParameter("distributor", distributor);
        }
        if (pointOfSale != null) {
            q.setParameter("pointOfSale", pointOfSale);
        }
        if (serviceProvider != null) {
            q.setParameter("serviceProvider", serviceProvider);
        }
        if (terminal != null) {
            q.setParameter("terminal", terminal);
        }
        if (type != null) {
            q.setParameter("type", type);
        }

        return q.getSingleResult();
    }


    private List<Transaction> search(EntityManager em,
                                     Integer id,
                                     Integer distributorId,
                                     Integer pointOfSaleId,
                                     Integer serviceProviderId,
                                     Integer terminalId,
                                     Integer typeId,
                                     int first,
                                     int pageSize) {

        Client distributor = null;
        BusinessPartner pointOfSale = null;
        BusinessPartner serviceProvider = null;
        Device terminal = null;
        TransactionType type = null;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Transaction_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (distributorId != null) {
            distributor = dao.find(Client.class, distributorId);
            criteria.add(cb.equal(root.get(Transaction_.distributor),
                    cb.parameter(Client.class, "distributor")));
        }
        if (pointOfSaleId != null) {
            pointOfSale = dao.find(BusinessPartner.class, pointOfSaleId);
            criteria.add(cb.equal(root.get(Transaction_.pointOfSale),
                    cb.parameter(BusinessPartner.class, "pointOfSale")));
        }
        if (serviceProviderId != null) {
            serviceProvider = dao.find(BusinessPartner.class, serviceProviderId);
            criteria.add(cb.equal(root.get(Transaction_.serviceProvider),
                    cb.parameter(BusinessPartner.class, "serviceProvider")));
        }
        if (terminalId != null) {
            terminal = dao.find(Device.class, terminalId);
            criteria.add(cb.equal(root.get(Transaction_.terminal),
                    cb.parameter(Device.class, "terminal")));
        }
        if (typeId != null) {
            type = dao.find(TransactionType.class, typeId);
            criteria.add(cb.equal(root.get(Transaction_.type),
                    cb.parameter(TransactionType.class, "type")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Transaction_.pointOfSale)), cb.asc(root.get(Transaction_.serviceProvider)), cb.asc(root.get(Transaction_.id)));
        TypedQuery<Transaction> q = em.createQuery(query);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (distributor != null) {
            q.setParameter("distributor", distributor);
        }
        if (pointOfSale != null) {
            q.setParameter("pointOfSale", pointOfSale);
        }
        if (serviceProvider != null) {
            q.setParameter("serviceProvider", serviceProvider);
        }
        if (terminal != null) {
            q.setParameter("terminal", terminal);
        }
        if (type != null) {
            q.setParameter("type", type);
        }
        q.setFirstResult(first);
        q.setMaxResults(pageSize);
        List<Transaction> tempList = q.getResultList();
        return tempList;
    }


    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<InvoicingTransactionSetDTO> readInvoicingSetPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadTransactionPermission


        Date invoicingDate = null;
        Integer distributorId = null;

        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("invoicingDateTo") && !(s.getValue() == null) && !s.getValue().toString().isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    String in = s.getValue().toString();
                    invoicingDate = formatter.parse(in);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //out = (Date)s.getValue();
            }
            if (s.getKey().equals("distributorId") && !(s.getValue() == null) && !s.getValue().toString().isEmpty()) {
                distributorId = Integer.valueOf(s.getValue().toString());
            }
        }

        InvoicingTransaction invoicingTransaction = new InvoicingTransaction();
        TypedQuery<InvoicingTransaction> queryLastInvoicingTransaction = dao.createNamedQuery(InvoicingTransaction.LAST_TRANSACTION, InvoicingTransaction.class);
        invoicingTransaction = queryLastInvoicingTransaction.getSingleResult();


        Query query = dao.createNamedQuery(Transaction.INVOICING_SUM_PER_MERCHANT);
        query.setParameter("distributorId", distributorId == null ? new Integer(0) : distributorId);
        query.setParameter("invoicingDateTo", invoicingDate == null ? Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()) : invoicingDate);
        query.setParameter("invoicingDateFrom", Date.from(invoicingTransaction.getInvoicedTo().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Query countEntitiesQuery = dao.createNamedQuery(Transaction.COUNT_INVOICING_SUM_PER_MERCHANT);
            countEntitiesQuery.setParameter("distributorId", distributorId == null ? new Integer(0) : distributorId);
            countEntitiesQuery.setParameter("invoicingDateTo", invoicingDate == null ? Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()) : invoicingDate);
            countEntitiesQuery.setParameter("invoicingDateFrom", Date.from(invoicingTransaction.getInvoicedTo().atStartOfDay(ZoneId.systemDefault()).toInstant()));

            Long countEntities = new Long(countEntitiesQuery.getSingleResult().toString());

            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Transaction.PageNotExists", pageNumber));
            }
            ReadRangeDTO<InvoicingTransactionSetDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                query.setFirstResult(start);
                query.setMaxResults(pageSize);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                int start = p.getPage() * pageSize;
                query.setFirstResult(start);
                query.setMaxResults(pageSize);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }

            List<InvoicingTransactionSetDTO> invoicingTransactionSetDTOs = NativeQueryResultsMapper.map(query.getResultList(), InvoicingTransactionSetDTO.class);
            result.setData(invoicingTransactionSetDTOs);

            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Transaction.Exception.ReadAll", ex)
            );
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void createInvoice(InvoiceDTO dto) throws ConstraintViolationException,
            com.invado.core.exception.ReferentialIntegrityException,
            com.invado.core.exception.EntityExistsException {
        if (dto == null) {
            throw new com.invado.core.exception.ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException"));
        }
        if (dto.getClientId() == null) {
            throw new com.invado.core.exception.ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (dto.getOrgUnitId() == null) {
            throw new com.invado.core.exception.ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.getDocument() == null) {
            throw new com.invado.core.exception.ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Document"));
        }
        if (dto.getIsDomesticCurrency() == null) {
            throw new com.invado.core.exception.ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.IsDomesticCurrency"));
        }
        if (dto.getIsDomesticCurrency() == false && dto.getCurrencyISOCode() == null) {
            throw new com.invado.core.exception.ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.CurrencyISOCode"));
        }
        try {
            Invoice temp = dao.find(Invoice.class, new InvoicePK(dto.getClientId(),
                    dto.getOrgUnitId(),
                    dto.getDocument()));
            if (temp != null) {
                throw new com.invado.core.exception.EntityExistsException(
                        Utils.getMessage("Invoice.EntityExistsException",
                                dto.getClientId(), dto.getOrgUnitId(), dto.getDocument()));
            }
            OrgUnit unit = dao.find(OrgUnit.class, dto.getOrgUnitId());
            if (unit == null) {
                throw new com.invado.core.exception.ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.OrgUnit",
                                dto.getClientId(), dto.getOrgUnitId()));
            }
            BusinessPartner partner = null;
            if (dto.getPartnerID() != null) {
                partner = dao.find(BusinessPartner.class, dto.getPartnerID());
                if (partner == null) {
                    throw new com.invado.core.exception.ReferentialIntegrityException(
                            Utils.getMessage("Invoice.ReferentialIntegrityException.Partner",
                                    dto.getPartnerID()));
                }
            }
            List<ApplicationUser> userList = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME,
                    ApplicationUser.class)
                    .setParameter(1, dto.getUsername())
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new com.invado.core.exception.ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.User",
                                dto.getUsername()));
            }
            BankCreditor bank = (dto.getBankID() == null) ? null : dao.find(BankCreditor.class, dto.getBankID());
            if (dto.getBankID() != null & bank == null) {
                throw new com.invado.core.exception.ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.Bank",
                                dto.getBankID()));
            }
            Invoice invoice = new Invoice(unit, dto.getDocument());
            invoice.setPartner(partner);
            invoice.setCreditRelationDate(dto.getCreditRelationDate());
            invoice.setInvoiceDate(dto.getInvoiceDate());
            invoice.setValueDate(dto.getValueDate());
            invoice.setPaid(dto.getPaid());
            invoice.setRecorded(Boolean.FALSE);
            invoice.setPrinted(Boolean.FALSE);
            invoice.setInvoiceType(dto.getProForma());
            invoice.setPartnerType(dto.getPartnerType());
            invoice.setUser(userList.get(0));
            Currency currency = null;
            String ISOCode = dao.find(Properties.class, "domestic_currency")
                    .getValue();
            if (dto.getCurrencyISOCode() == null
                    || (dto.getCurrencyISOCode().equals(ISOCode) | dto.getCurrencyISOCode().isEmpty())) {
                invoice.setIsDomesticCurrency(Boolean.TRUE);
                //read domestic currency ISO code from application properties
                currency = dao.find(Currency.class, ISOCode);
                //if domestic currency does not exists in database create it
                if (currency == null) {
                    Currency domesticCurrency = new Currency(ISOCode);
                    domesticCurrency.setDescription("");
                    currency = dao.merge(domesticCurrency);
                    dao.flush();
                }
            } else {
                invoice.setIsDomesticCurrency(Boolean.FALSE);
                currency = dao.find(Currency.class, dto.getCurrencyISOCode());
                if (currency == null) {
                    throw new com.invado.core.exception.ReferentialIntegrityException(
                            Utils.getMessage(
                                    "Invoice.ReferentialIntegrityException.Currency",
                                    dto.getCurrencyISOCode())
                    );
                }
            }
            invoice.setCurrency(currency);
            invoice.setIsDomesticCurrency(dto.getIsDomesticCurrency());
            invoice.setContractNumber(dto.getContractNumber());
            invoice.setContractDate(dto.getContractDate());
            invoice.setBank(bank);
            invoice.setInvoicingTransaction(dto.getInvoicingTransaction());
            List<String> msgs = validator.validate(invoice)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new com.invado.core.exception.ConstraintViolationException("", msgs);
            }
            dao.persist(invoice);
        } catch (com.invado.core.exception.EntityExistsException | ReferentialIntegrityException | ConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Create"), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addItem(InvoiceItemDTO dto) throws ReferentialIntegrityException,
            ConstraintViolationException,
            EntityExistsException {
        // TODO : check update invoice permission
        if (dto == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException"));
        }
        if (dto.getClientId() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (dto.getUnitId() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.getInvoiceDocument() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        if (dto.getArticleCode() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.ArticleCode"));
        }

        try {
            Invoice invoice = dao.find(Invoice.class,
                    new InvoicePK(dto.getClientId(), dto.getUnitId(), dto.getInvoiceDocument()));
            if (invoice == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.EntityNotFoundException",
                                dto.getClientId(), dto.getUnitId(), dto.getInvoiceDocument()));
            }
            if (invoice.isRecorded().equals(Boolean.TRUE)) {
                throw new ConstraintViolationException(
                        Utils.getMessage("Invoice.IllegalArgumentException.AddItemRecorded"));
            }
            List<ApplicationUser> userList = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME,
                    ApplicationUser.class)
                    .setParameter(1, dto.getUsername())
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.User",
                                dto.getUsername()));
            }
            OrgUnit unit = dao.find(OrgUnit.class, dto.getUnitId());
            if (unit == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.OrgUnit",
                                dto.getClientId(), dto.getUnitId()));
            }
            invoice.setUser(userList.get(0));
            dao.lock(invoice, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Article article = dao.find(Article.class, dto.getArticleCode());
            if (article == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.Article",
                                dto.getArticleCode()));
            }
            //can't throw nouniqueresultexception
            Integer max = dao.createNamedQuery("Invoice.GetMaxOrdinalNumber",
                    Integer.class)
                    .setParameter("document", dto.getInvoiceDocument())
                    .setParameter("orgUnit", unit)
                    .getSingleResult();
            Integer ordinalNumber = (max == null) ? 1 : (max + 1);
            InvoiceItem item = new InvoiceItem(invoice, ordinalNumber);
            //ne moze da se desi da imam dve stavke sa istim kljucem
            item.setNetPrice(dto.getNetPrice());
            BigDecimal vatPercent = null;
            switch (article.getVATRate()) {
                case GENERAL_RATE:
                    vatPercent = new BigDecimal(dao.find(Properties.class,
                            "vat_general_rate").getValue());
                    break;
                case LOWER_RATE:
                    vatPercent = new BigDecimal(dao.find(Properties.class,
                            "vat_low_rate").getValue());
                    break;
            }
            switch (invoice.getPartnerType()) {
                case DOMESTIC:
                    item.setVatPercent(vatPercent);
                    break;
                case ABROAD:
                    item.setVatPercent(BigDecimal.ZERO);
                    break;
            }
            item.setRabatPercent(dto.getRabatPercent());
            item.setReturnValue(dto.getReturnValue());
            item.setQuantity(dto.getQuantity());
            item.setArticleVAT(article.getVATRate());
            item.setArticle(article);
            item.setItemDescription(article.getDescription());
            item.setUnitOfMeasure(article.getUnitOfMeasureCode());
            String[] netPriceValidation = validator.validateProperty(item, "netPrice")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (netPriceValidation.length > 0) {
                throw new ConstraintViolationException("", netPriceValidation);
            }
            String[] rabatValidation = validator.validateProperty(item, "rabatPercent")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (rabatValidation.length > 0) {
                throw new ConstraintViolationException("", rabatValidation);
            }
            String[] quantityValidation = validator.validateProperty(item, "quantity")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (rabatValidation.length > 0) {
                throw new ConstraintViolationException("", quantityValidation);
            }
            BigDecimal net = dto.getNetPrice().subtract(dto.getNetPrice().multiply(dto.getRabatPercent()));
            BigDecimal total = (net.multiply(BigDecimal.ONE.add(item.getVatPercent())))
                    .multiply(dto.getQuantity());
            item.setTotalCost(total.setScale(2, RoundingMode.HALF_UP));
            invoice.addItem(item);
            List<String> msgs = validator.validate(invoice).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            if (invoice.getVersion().compareTo(dto.getInvoiceVersion()) != 0) {
                throw new OptimisticLockException();
            }
            return invoice.getVersion();
        } catch (ConstraintViolationException | ReferentialIntegrityException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Utils.getMessage("Invoice.OptimisticLockEx",
                        dto.getClientId(), dto.getUnitId(), dto.getInvoiceDocument()), ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.AddItem"), ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateItem(InvoiceItemDTO dto) throws ReferentialIntegrityException,
            ConstraintViolationException,
            EntityNotFoundException {
        // TODO : check update invoice permission
        if (dto == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException"));
        }
        if (dto.getClientId() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (dto.getUnitId() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.getInvoiceDocument() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        if (dto.getOrdinal() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Ordinal"));
        }
        if (dto.getArticleCode() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.ArticleCode")
            );
        }
        try {
            Invoice invoice = dao.find(Invoice.class,
                    new InvoicePK(dto.getClientId(), dto.getUnitId(), dto.getInvoiceDocument())
            );
            if (invoice == null) {
                throw new EntityNotFoundException(Utils.getMessage(
                        "Invoice.EntityNotFoundException",
                        dto.getClientId(),
                        dto.getUnitId(),
                        dto.getInvoiceDocument()));
            }
            if (invoice.isRecorded().equals(Boolean.TRUE)) {
                throw new ConstraintViolationException(Utils.getMessage(
                        "Invoice.IllegalArgumentException.UpdateItemRecorded"));
            }
            List<ApplicationUser> userList = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME,
                    ApplicationUser.class)
                    .setParameter(1, dto.getUsername())
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.User",
                                dto.getUsername()));
            }
            invoice.setUser(userList.get(0));
            dao.lock(invoice, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            InvoiceItem temp = dao.find(InvoiceItem.class,
                    new InvoiceItemPK(dto.getClientId(), dto.getUnitId(), dto.getInvoiceDocument(), dto.getOrdinal()));
            if (temp == null) {
                throw new EntityNotFoundException(Utils.getMessage(
                        "Invoice.EntityNotFoundException.InvoiceItem",
                        dto.getClientId(), dto.getUnitId(), dto.getInvoiceDocument(), dto.getOrdinal()));
            }
            temp.setQuantity(dto.getQuantity());
            temp.setNetPrice(dto.getNetPrice());
            BigDecimal vatPercent = null;
            switch (temp.getArticleVAT()) {
                case GENERAL_RATE:
                    vatPercent = new BigDecimal(dao.find(Properties.class, "vat_general_rate").getValue());
                    break;
                case LOWER_RATE:
                    vatPercent = new BigDecimal(dao.find(Properties.class, "vat_low_rate").getValue());
                    break;
            }
            switch (invoice.getPartnerType()) {
                case DOMESTIC:
                    temp.setVatPercent(vatPercent);
                    break;
                case ABROAD:
                    temp.setVatPercent(BigDecimal.ZERO);
                    break;
            }
            temp.setRabatPercent(dto.getRabatPercent());
            temp.setReturnValue(dto.getReturnValue());
            String[] netPriceValidation = validator.validateProperty(temp, "netPrice")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (netPriceValidation.length > 0) {
                throw new ConstraintViolationException("", netPriceValidation);
            }
            String[] rabatValidation = validator.validateProperty(temp, "rabatPercent")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (rabatValidation.length > 0) {
                throw new ConstraintViolationException("", rabatValidation);
            }
            String[] quantityValidation = validator.validateProperty(temp, "quantity")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (rabatValidation.length > 0) {
                throw new ConstraintViolationException("", quantityValidation);
            }
            BigDecimal net = dto.getNetPrice().subtract(dto.getNetPrice().multiply(dto.getRabatPercent()));
            BigDecimal total = (net.multiply(BigDecimal.ONE.add(temp.getVatPercent())))
                    .multiply(dto.getQuantity());
            temp.setTotalCost(total.setScale(2, RoundingMode.HALF_UP));
            List<String> msgs = validator.validate(invoice).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            if (invoice.getVersion().compareTo(dto.getInvoiceVersion()) != 0) {
                throw new OptimisticLockException();
            }
            return invoice.getVersion();
        } catch (ConstraintViolationException | ReferentialIntegrityException | EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Utils.getMessage("Invoice.OptimisticLockEx",
                        dto.getClientId(), dto.getUnitId(), dto.getInvoiceDocument()), ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.UpdateItem"), ex);
            }
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Transaction> readAll(Integer id,
                                     Integer distributorId,
                                     Integer pointOfSaleId,
                                     Integer serviceProviderId,
                                     Integer terminalId,
                                     Integer typeId) {
        try {
            return this.search(dao, id, distributorId, pointOfSaleId, serviceProviderId, terminalId, typeId, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Transaction.Exception.ReadAll"), ex);
        }
    }


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Map<Integer, InvoiceDTO> genInvoicesI(TransactionDTO paramTransactionDTO) throws ReferentialIntegrityException,
            ConstraintViolationException, EntityExistsException, EntityNotFoundException {

        Integer maxDocument = new Integer(1);
        LocalDate invoicingDate = LocalDate.now();
        Date out = null;

        InvoicingTransaction currentInvoicingTransaction = new InvoicingTransaction();
        InvoicingTransaction lastInvoicingTransaction = new InvoicingTransaction();
        TypedQuery<InvoicingTransaction> queryLastInvoicingTransaction = dao.createNamedQuery(InvoicingTransaction.LAST_TRANSACTION, InvoicingTransaction.class);
        lastInvoicingTransaction = queryLastInvoicingTransaction.getSingleResult();

        Query queryInvoicingCandidates = dao.createNamedQuery(Transaction.INVOICING_CANDIDATES_PER_MERCHANT);


        queryInvoicingCandidates.setParameter("distributorId", paramTransactionDTO.getInvoicingDistributorId());
        queryInvoicingCandidates.setParameter("invoicingDateTo", Date.from(paramTransactionDTO.getInvoicingGenDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        System.out.println("Do " + Date.from(paramTransactionDTO.getInvoicingGenDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryInvoicingCandidates.setParameter("invoicingDateFrom", Date.from(lastInvoicingTransaction.getInvoicedTo().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant()));
        System.out.println("od " + Date.from(lastInvoicingTransaction.getInvoicedTo().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant()));
        currentInvoicingTransaction.setInvoicedFrom(lastInvoicingTransaction.getInvoicedTo().plusDays(1));
        currentInvoicingTransaction.setInvoicedTo(paramTransactionDTO.getInvoicingGenDate());
        currentInvoicingTransaction.setInvoicingDate(LocalDate.now());
        currentInvoicingTransaction.setDitributor(dao.find(Client.class, paramTransactionDTO.getInvoicingDistributorId()));
        dao.persist(currentInvoicingTransaction);
        dao.flush();
        dao.refresh(currentInvoicingTransaction);

        System.out.println("id je ovaj " + currentInvoicingTransaction.getId());

        List<InvoicingTransactionSetDTO> invoicingTransactionSetDTOs = NativeQueryResultsMapper.map(queryInvoicingCandidates.getResultList(), InvoicingTransactionSetDTO.class);
        System.out.println("kandidata " + invoicingTransactionSetDTOs.size());
        Map<Integer, Invoice> invoicesPerPOSMap = new HashMap<Integer, Invoice>();
        Map<Integer, InvoiceDTO> invoicePerMerchantMap = new HashMap<Integer, InvoiceDTO>();
        InvoiceDTO invoice = new InvoiceDTO();

        for (InvoicingTransactionSetDTO transactionSetDTO : invoicingTransactionSetDTOs) {

            if (invoicePerMerchantMap.get(transactionSetDTO.getMerchantId()) == null) {
                Client client = dao.find(Client.class, transactionSetDTO.getDistributorId());
                OrgUnit orgUnit = dao.createNamedQuery(OrgUnit.READ_ROOT_BY_CLIENT, OrgUnit.class)
                        .setParameter("clientId", client.getId())
                        .getSingleResult();

                invoice = new InvoiceDTO();
                invoice.setDocument(String.valueOf(dao.createNamedQuery(Invoice.READ_MAX_DOCUMENT, Integer.class)
                        .setParameter("client", client)
                        .setParameter("orgUnit", orgUnit).getSingleResult()));
                invoice.setOrgUnitId(orgUnit.getId());
                invoice.setOrgUnitDesc(orgUnit.getName());
                maxDocument = dao.createNamedQuery(Invoice.READ_MAX_DOCUMENT, Integer.class)
                        .setParameter("client", client)
                        .setParameter("orgUnit", orgUnit).getSingleResult();
                invoice.setDocument(maxDocument == null ? new Integer("1") + "" : maxDocument + "");
                invoice.setClientId(client.getId());
                invoice.setClientDesc(client.getName());
                invoice.setCreditRelationDate(invoicingDate);
                invoice.setInvoiceDate(invoicingDate);
                invoice.setIsDomesticCurrency(true);
                invoice.setPaid(false);
                invoice.setProForma(InvoiceType.INVOICE);
                invoice.setPartnerID(transactionSetDTO.getMerchantId());
                invoice.setPartnerName(transactionSetDTO.getMerchantName());
                invoice.setPartnerType(InvoiceBusinessPartner.DOMESTIC);
                invoice.setBankID(client.getBank().getId());
                invoice.setValueDate(invoicingDate);
                invoice.setUsername("nikola");
                invoice.setInvoicingTransaction(currentInvoicingTransaction);
                invoicePerMerchantMap.put(transactionSetDTO.getMerchantId(), invoice);
                createInvoice(invoice);
            } else {
                invoice = invoicePerMerchantMap.get(transactionSetDTO.getMerchantId());
            }
            InvoiceItemDTO invoiceItemDTO = null;
            try {
                invoiceItemDTO = dao.createNamedQuery(InvoiceItem.READ_ITEMS_BY_INVOICE_AND_ARTICLE, InvoiceItem.class)
                        .setParameter("document", invoice.getDocument())
                        .setParameter("orgUnit", invoice.getOrgUnitId())
                        .setParameter("clientId", invoice.getClientId())
                        .setParameter("code", transactionSetDTO.getArticleCode())
                        .getSingleResult().getInvoiceItemDTO();
            } catch (NoResultException ex) {
                invoiceItemDTO = null;
            }
            if (invoiceItemDTO == null) {
                invoiceItemDTO = new InvoiceItemDTO();

                invoiceItemDTO.setClientId(invoice.getClientId());
                invoiceItemDTO.setInvoiceDocument(invoice.getDocument());
                invoiceItemDTO.setUnitId(invoice.getOrgUnitId());
                invoiceItemDTO.setArticleCode(transactionSetDTO.getArticleCode());
                Article article = dao.find(Article.class, transactionSetDTO.getArticleCode());
                BigDecimal vatPercent = null;
                switch (article.getVATRate()) {
                    case GENERAL_RATE:
                        vatPercent = new BigDecimal(dao.find(Properties.class,
                                "vat_general_rate").getValue());
                        break;
                    case LOWER_RATE:
                        vatPercent = new BigDecimal(dao.find(Properties.class,
                                "vat_low_rate").getValue());
                        break;
                }
                invoiceItemDTO.setVATPercent(vatPercent);
                invoiceItemDTO.setNetPrice(transactionSetDTO.getAmount().divide(invoiceItemDTO.getVATPercent().add(new BigDecimal(1)), 2, RoundingMode.HALF_UP));
                BusinessPartnerTermsItem it = dao.createNamedQuery(BusinessPartnerTermsItem.READ_TERMS_PER_PARTNER_AND_ARTICLE, BusinessPartnerTermsItem.class)
                        .setParameter("partner", transactionSetDTO.getMerchantId())
                        .setParameter("serviceId", transactionSetDTO.getArticleCode())
                        .getSingleResult();
                invoiceItemDTO.setRabatPercent(it.getRebate());
                invoiceItemDTO.setQuantity(new BigDecimal(1));
                invoiceItemDTO.setTotalCost(invoiceItemDTO.getNetPrice().multiply(invoiceItemDTO.getRabatPercent().add(new BigDecimal(1)))
                        .multiply(invoiceItemDTO.getVATPercent()).setScale(2, RoundingMode.HALF_UP));
                invoiceItemDTO.setArticleDesc(article.getDescription());
                invoiceItemDTO.setOrdinal(invoice.getMaxItemOrdinal() == null ? 1 : invoice.getMaxItemOrdinal() + 1);
                invoiceItemDTO.setUsername("nikola");
                invoiceItemDTO.setInvoiceVersion(invoice.getVersion() == null ? 0 : invoice.getVersion());
                invoice.setMaxItemOrdinal(invoiceItemDTO.getOrdinal());
                invoice.setTotalAmount(invoiceItemDTO.getTotalCost());
                addItem(invoiceItemDTO);
            } else {
                invoiceItemDTO.setNetPrice(invoiceItemDTO.getNetPrice().add(transactionSetDTO.getAmount().divide(invoiceItemDTO.getVATPercent().add(new BigDecimal(1)), 2, RoundingMode.HALF_UP)));
                invoiceItemDTO.setTotalCost(invoiceItemDTO.getTotalCost().add(invoiceItemDTO.getNetPrice().multiply(invoiceItemDTO.getRabatPercent().add(new BigDecimal(1)))
                        .multiply(invoiceItemDTO.getVATPercent())).setScale(2, RoundingMode.HALF_UP));
                invoice.setTotalAmount(invoice.getTotalAmount() == null ? invoiceItemDTO.getTotalCost() : invoice.getTotalAmount().add(invoiceItemDTO.getTotalCost()));
                invoiceItemDTO.setUsername(invoice.getUsername());
                invoiceItemDTO.setInvoiceVersion(invoice.getVersion() == null ? 0 : invoice.getVersion());
                updateItem(invoiceItemDTO);
            }

            Transaction transaction = dao.find(Transaction.class, transactionSetDTO.getTransactionId());
            transaction.setInvoicingStatus(true);
            dao.persist(transaction);

        }


        return invoicePerMerchantMap;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Map<Integer, InvoiceDTO> genInvoicesUI(TransactionDTO paramTransactionDTO) throws ReferentialIntegrityException,
            ConstraintViolationException, EntityExistsException, EntityNotFoundException {

        Integer maxDocument = new Integer(1);
        LocalDate invoicingDate = LocalDate.now();
        Date out = null;
        BigDecimal rabat = null;

        InvoicingTransaction currentInvoicingTransaction = new InvoicingTransaction();
        InvoicingTransaction lastInvoicingTransaction = new InvoicingTransaction();
        TypedQuery<InvoicingTransaction> queryLastInvoicingTransaction = dao.createNamedQuery(InvoicingTransaction.LAST_TRANSACTION, InvoicingTransaction.class);
        lastInvoicingTransaction = queryLastInvoicingTransaction.getSingleResult();

        Query queryInvoicingCandidates = dao.createNamedQuery(Transaction.INVOICING_CANDIDATES_PER_MERCHANT);


        queryInvoicingCandidates.setParameter("distributorId", paramTransactionDTO.getInvoicingDistributorId());
        queryInvoicingCandidates.setParameter("invoicingDateTo", Date.from(paramTransactionDTO.getInvoicingGenDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        queryInvoicingCandidates.setParameter("invoicingDateFrom", Date.from(lastInvoicingTransaction.getInvoicedTo().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant()));
        currentInvoicingTransaction.setInvoicedFrom(lastInvoicingTransaction.getInvoicedTo().plusDays(1));
        currentInvoicingTransaction.setInvoicedTo(paramTransactionDTO.getInvoicingGenDate());
        currentInvoicingTransaction.setDitributor(dao.find(Client.class, paramTransactionDTO.getInvoicingDistributorId()));
        currentInvoicingTransaction.setInvoicingDate(LocalDate.now());

        List<InvoicingTransactionSetDTO> invoicingTransactionSetDTOs = NativeQueryResultsMapper.map(queryInvoicingCandidates.getResultList(), InvoicingTransactionSetDTO.class);
        Map<Integer, Invoice> invoicesPerPOSMap = new HashMap<Integer, Invoice>();
        Map<Integer, InvoiceDTO> invoicePerMerchantMap = new HashMap<Integer, InvoiceDTO>();
        InvoiceDTO invoice = new InvoiceDTO();

        for (InvoicingTransactionSetDTO transactionSetDTO : invoicingTransactionSetDTOs) {

            if (invoicePerMerchantMap.get(transactionSetDTO.getMerchantId()) == null) {
                Client client = dao.find(Client.class, transactionSetDTO.getDistributorId());
                OrgUnit orgUnit = dao.createNamedQuery(OrgUnit.READ_ROOT_BY_CLIENT, OrgUnit.class)
                        .setParameter("clientId", client.getId())
                        .getSingleResult();

                invoice = new InvoiceDTO();
                invoice.setDocument(String.valueOf(dao.createNamedQuery(Invoice.READ_MAX_DOCUMENT, Integer.class)
                        .setParameter("client", client)
                        .setParameter("orgUnit", orgUnit).getSingleResult()));
                invoice.setOrgUnitId(orgUnit.getId());
                invoice.setOrgUnitDesc(orgUnit.getName());
                maxDocument = dao.createNamedQuery(Invoice.READ_MAX_DOCUMENT, Integer.class)
                        .setParameter("client", client)
                        .setParameter("orgUnit", orgUnit).getSingleResult();
                invoice.setDocument(maxDocument == null ? new Integer("1") + "" : maxDocument + "");
                invoice.setClientId(client.getId());
                invoice.setClientDesc(client.getName());
                invoice.setCreditRelationDate(invoicingDate);
                invoice.setInvoiceDate(invoicingDate);
                invoice.setIsDomesticCurrency(true);
                invoice.setPaid(false);
                invoice.setProForma(InvoiceType.INVOICE);
                invoice.setPartnerID(transactionSetDTO.getMerchantId());
                invoice.setPartnerName(transactionSetDTO.getMerchantName());
                invoice.setPartnerType(InvoiceBusinessPartner.DOMESTIC);
                invoice.setBankID(client.getBank().getId());
                invoice.setValueDate(invoicingDate);
                invoice.setUsername("nikola");
                invoice.setInvoicingTransactionId(currentInvoicingTransaction.getId());
                invoicePerMerchantMap.put(transactionSetDTO.getMerchantId(), invoice);
                createInvoice(invoice);
            } else {
                invoice = invoicePerMerchantMap.get(transactionSetDTO.getMerchantId());
            }
            InvoiceItemDTO invoiceItemDTO = null;
            try {
                System.out.println("podaci " + invoice.getDocument() + " " + invoice.getOrgUnitId() + " " + invoice.getClientId());
                invoiceItemDTO = dao.createNamedQuery(InvoiceItem.READ_ITEMS_BY_INVOICE_AND_ARTICLE, InvoiceItem.class)
                        .setParameter("document", invoice.getDocument())
                        .setParameter("orgUnit", invoice.getOrgUnitId())
                        .setParameter("clientId", invoice.getClientId())
                        .setParameter("code", transactionSetDTO.getArticleCode())
                        .getSingleResult().getInvoiceItemDTO();
            } catch (NoResultException ex) {
                invoiceItemDTO = null;
            }
            if (invoiceItemDTO == null) {
                invoiceItemDTO = new InvoiceItemDTO();

                invoiceItemDTO.setClientId(invoice.getClientId());
                invoiceItemDTO.setInvoiceDocument(invoice.getDocument());
                invoiceItemDTO.setUnitId(invoice.getOrgUnitId());
                invoiceItemDTO.setArticleCode(transactionSetDTO.getArticleCode());
                Article article = dao.find(Article.class, transactionSetDTO.getArticleCode());
                BigDecimal vatPercent = null;
                switch (article.getVATRate()) {
                    case GENERAL_RATE:
                        vatPercent = new BigDecimal(dao.find(Properties.class,
                                "vat_general_rate").getValue());
                        break;
                    case LOWER_RATE:
                        vatPercent = new BigDecimal(dao.find(Properties.class,
                                "vat_low_rate").getValue());
                        break;
                }
                invoiceItemDTO.setVATPercent(vatPercent);
                invoiceItemDTO.setNetPrice(transactionSetDTO.getAmount().divide(invoiceItemDTO.getVATPercent().add(new BigDecimal(1)), 2, RoundingMode.HALF_UP));
                System.out.println("evo podaci " + transactionSetDTO.getMerchantId() + "  " + transactionSetDTO.getArticleCode());
                BusinessPartnerTermsItem it = dao.createNamedQuery(BusinessPartnerTermsItem.READ_TERMS_PER_PARTNER_AND_ARTICLE, BusinessPartnerTermsItem.class)
                        .setParameter("partner", transactionSetDTO.getMerchantId())
                        .setParameter("serviceId", transactionSetDTO.getArticleCode())
                        .getSingleResult();
                rabat = it.getRebate();
                invoiceItemDTO.setRabatPercent(new BigDecimal(0));
                invoiceItemDTO.setReturnValue(invoiceItemDTO.getNetPrice().multiply(rabat).setScale(2, RoundingMode.HALF_UP));
                invoiceItemDTO.setQuantity(new BigDecimal(1));
                invoiceItemDTO.setTotalCost(invoiceItemDTO.getNetPrice().multiply(invoiceItemDTO.getVATPercent()));
                invoiceItemDTO.setArticleDesc(article.getDescription());
                invoiceItemDTO.setOrdinal(invoice.getMaxItemOrdinal() == null ? 1 : invoice.getMaxItemOrdinal() + 1);
                invoiceItemDTO.setUsername("nikola");
                invoiceItemDTO.setInvoiceVersion(invoice.getVersion() == null ? 0 : invoice.getVersion());
                invoice.setMaxItemOrdinal(invoiceItemDTO.getOrdinal());
                invoice.setTotalAmount(invoiceItemDTO.getTotalCost());
                invoice.setReturnValue(invoiceItemDTO.getReturnValue());
                addItem(invoiceItemDTO);
            } else {
                invoiceItemDTO.setNetPrice(invoiceItemDTO.getNetPrice().add(transactionSetDTO.getAmount().divide(invoiceItemDTO.getVATPercent().add(new BigDecimal(1)), 2, RoundingMode.HALF_UP)));
                invoiceItemDTO.setReturnValue(invoiceItemDTO.getReturnValue().add(transactionSetDTO.getAmount().multiply(rabat)).setScale(2, RoundingMode.HALF_UP));
                invoiceItemDTO.setTotalCost(invoiceItemDTO.getTotalCost().add(invoiceItemDTO.getNetPrice().multiply(invoiceItemDTO.getVATPercent())).setScale(2, RoundingMode.HALF_UP));
                invoice.setTotalAmount(invoice.getTotalAmount() == null ? invoiceItemDTO.getTotalCost() : invoice.getTotalAmount().add(invoiceItemDTO.getTotalCost()));
                invoice.setReturnValue(invoice.getReturnValue() == null ? invoiceItemDTO.getReturnValue() : invoice.getReturnValue().add(invoiceItemDTO.getReturnValue()));
                invoiceItemDTO.setUsername(invoice.getUsername());
                invoiceItemDTO.setInvoiceVersion(invoice.getVersion() == null ? 0 : invoice.getVersion());
                updateItem(invoiceItemDTO);
            }

            Transaction transaction = dao.find(Transaction.class, transactionSetDTO.getTransactionId());
            transaction.setInvoicingStatus(true);
            dao.persist(transaction);

        }

        dao.persist(currentInvoicingTransaction);
        return invoicePerMerchantMap;

    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public List<InvoiceDTO> readInvoiciePerPeriod(Integer invoicingTransactionid) {

        List<InvoiceDTO> listDTO = new ArrayList<InvoiceDTO>();
        if (invoicingTransactionid != null) {
            List<Invoice> list = dao.createNamedQuery(Invoice.READ_BY_INVOICING_TRANSACTION, Invoice.class)
                    .setParameter("invoicingTransaction", dao.find(InvoicingTransaction.class, invoicingTransactionid))
                    .getResultList();
            for (Invoice invoice : list)
                listDTO.add(invoice.getDTO());
        }
        return listDTO;
    }

    @Transactional(readOnly = true)
    public List<TransactionType> readTransactionTypeByType(String name) {
        try {
            return dao.createNamedQuery(TransactionType.READ_BY_DESCRIPTION, TransactionType.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "TransactionType.Exception.ReadAll"),
                    ex);
        }
    }

    @Transactional(readOnly = true)
    public TransactionType readTransactionTypeById(Integer id) {
        try {
            return dao.createNamedQuery(TransactionType.READ_BY_ID, TransactionType.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "TransactionType.Exception.ReadAll"),
                    ex);
        }
    }
}


