/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.BusinessPartner_;
import com.invado.core.domain.Client;
import com.invado.core.domain.Client_;
import com.invado.core.domain.Currency;
import com.invado.core.domain.OrgUnit;
import com.invado.core.domain.OrgUnitPK;
import com.invado.core.utils.Utils;
import com.invado.core.domain.Article;
import com.invado.core.domain.Article_;
import com.invado.finance.domain.Invoice;
import com.invado.finance.domain.InvoiceBusinessPartner;
import com.invado.finance.domain.InvoiceItem;
import com.invado.finance.domain.InvoiceItemPK;
import com.invado.finance.domain.InvoiceItem_;
import com.invado.finance.domain.InvoicePK;
import com.invado.finance.domain.InvoiceType;
import com.invado.finance.domain.Invoice_;
import com.invado.finance.domain.Properties;
import com.invado.finance.service.dto.InvoiceDTO;
import com.invado.finance.service.dto.InvoiceItemDTO;
import com.invado.finance.service.dto.InvoiceReportDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.exception.IllegalArgumentException;
import com.invado.finance.service.exception.EntityExistsException;
import com.invado.finance.service.exception.EntityNotFoundException;
import com.invado.finance.service.exception.PageNotExistsException;
import com.invado.finance.service.exception.ReferentialIntegrityException;
import com.invado.finance.service.exception.SystemException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

/**
 *
 * @author root
 */
@Service
public class InvoiceService {

    private static final Logger LOG = Logger.getLogger(InvoiceService.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager dao;   
    @Autowired
    private Validator validator;
    private final String username = "a";

    @Transactional
    public void createInvoice(InvoiceDTO dto) throws IllegalArgumentException,
                                                     ReferentialIntegrityException,
                                                     EntityExistsException {
        if (dto == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException"));
        }
        if (dto.clientId == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (dto.orgUnitId == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.document == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Document"));
        }
        if (dto.isDomesticCurrency == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.IsDomesticCurrency"));
        }
        if (dto.isDomesticCurrency == false && dto.currencyISOCode == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.CurrencyISOCode"));
        }
        try {
            Invoice temp = dao.find(Invoice.class, new InvoicePK(dto.clientId,
                    dto.orgUnitId,
                    dto.document));
            if (temp != null) {
                throw new EntityExistsException(
                        Utils.getMessage("Invoice.EntityExistsException",
                        dto.clientId, dto.orgUnitId, dto.document));
            }
            OrgUnit unit = dao.find(OrgUnit.class,
                    new OrgUnitPK(dto.orgUnitId, dto.clientId));
            if (unit == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.OrgUnit",
                                dto.clientId, dto.orgUnitId));
            }
            BusinessPartner partner = null;
            if (dto.partnerID != null) {
                partner = dao.find(BusinessPartner.class, dto.partnerID);
                if (partner == null) {
                    throw new ReferentialIntegrityException(
                            Utils.getMessage("Invoice.ReferentialIntegrityException.Partner",
                                    dto.partnerID));
                }
            }
            List<ApplicationUser> userList = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME_AND_PASSWORD,
                    ApplicationUser.class)
                    .setParameter(1, dto.username)
                    .setParameter(2, dto.password)
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.User",
                                dto.username));
            }
            Currency currency = null;
            if (dto.isDomesticCurrency) {
                //read domestic currency ISO code from application properties
                String ISOCode = dao.find(Properties.class,
                        "domestic_currency")
                        .getValue();
                currency = dao.find(Currency.class, ISOCode);
                //if domestic currency does not exists in database create it
                if (currency == null) {
                    Currency domesticCurrency = new Currency(ISOCode);
                    domesticCurrency.setDescription("");
                    currency = dao.merge(domesticCurrency);
                    dao.flush();
                }
            } else {
                currency = dao.find(Currency.class, dto.currencyISOCode);
                if (currency == null) {
                    throw new ReferentialIntegrityException(
                            Utils.getMessage("Invoice.ReferentialIntegrityException.Currency",
                                    dto.currencyISOCode));
                }
            }
            BankCreditor bank = (dto.bankID == null) ? null : dao.find(BankCreditor.class, dto.bankID);
            if (dto.bankID != null & bank == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.Bank",
                                dto.bankID));
            }
            Invoice invoice = new Invoice(unit, dto.document);
            invoice.setPartner(partner);
            invoice.setCreditRelationDate(dto.creditRelationDate);
            invoice.setInvoiceDate(dto.invoiceDate);
            invoice.setValueDate(dto.valueDate);
            invoice.setPaid(dto.paid);
            invoice.setRecorded(Boolean.FALSE);
            invoice.setPrinted(Boolean.FALSE);
            invoice.setInvoiceType(dto.proForma);
            invoice.setPartnerType(dto.partnerType);
            invoice.setUser(userList.get(0));
            invoice.setCurrency(currency);
            invoice.setIsDomesticCurrency(dto.isDomesticCurrency);
            invoice.setContractNumber(dto.contractNumber);
            invoice.setContractDate(dto.contractDate);
            invoice.setBank(bank);
            List<String> msgs = validator.validate(invoice)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(invoice);
        } catch (EntityExistsException | ReferentialIntegrityException 
                | IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Create"),ex);
        } 
    }

    @Transactional
    public void deleteInvoice(Integer clientId,
                              Integer orgUnitId,
                              String document) 
                              throws IllegalArgumentException,
                              EntityNotFoundException {  
        // TODO:check delete invoice permission
        if (clientId == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (orgUnitId == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Document"));
        }
        try {
            Invoice temp = dao.find(Invoice.class, new InvoicePK(
                    clientId,
                    orgUnitId,
                    document)
            );
            if (temp == null) {
                throw new EntityNotFoundException(Utils.getMessage(
                        "Invoice.EntityNotFoundException",
                        clientId, orgUnitId, document));
            }
            if (temp.isRecorded().equals(Boolean.TRUE)) {
                throw new IllegalArgumentException(Utils.getMessage(
                        "Invoice.IllegalArgumentException.DeleteRecorded")
                );
            }
            dao.remove(temp);
        } catch (EntityNotFoundException | IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Delete"),ex);
        } 
    }

    @Transactional(readOnly = true)
    public InvoiceDTO readInvoice(Integer clientId,
            Integer orgUnitId,
            String document)
            throws IllegalArgumentException,
            EntityNotFoundException {
        // TODO : check read invoice permission
        if (clientId == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (orgUnitId == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Document"));
        }
        try {
            Invoice temp = dao.find(Invoice.class,
                    new InvoicePK(clientId, orgUnitId, document));
            if (temp == null) {
                throw new EntityNotFoundException(Utils.getMessage(
                        "Invoice.EntityNotFoundException",
                        clientId, orgUnitId, document));
            }
            return temp.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Invoice.PersistenceEx.Read", ex);
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Read"),ex);
        } 
    }

    @Transactional
    public Long updateInvoice(InvoiceDTO dto) throws IllegalArgumentException,
            ReferentialIntegrityException,
            EntityNotFoundException {
        // TODO : check update invoice permission
        if (dto == null) {
            throw new IllegalArgumentException(Utils.getMessage("Invoice.IllegalArgumentException"));
        }
        if (dto.clientId == null) {
            throw new IllegalArgumentException(Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (dto.orgUnitId == null) {
            throw new IllegalArgumentException(Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.document == null) {
            throw new IllegalArgumentException(Utils.getMessage("Invoice.IllegalArgumentException.Document"));
        }
        if (dto.isDomesticCurrency == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.IsDomesticCurrency"));
        }
        if (dto.isDomesticCurrency == false && dto.currencyISOCode == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.CurrencyISOCode"));
        }
        try {
            Invoice temp = dao.find(Invoice.class,
                    new InvoicePK(dto.clientId, dto.orgUnitId, dto.document),
                    LockModeType.OPTIMISTIC);
            if (temp == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Invoice.EntityNotFoundException", dto.clientId,
                                dto.orgUnitId, dto.document));
            }
            if (temp.isRecorded().equals(Boolean.TRUE)) {
                throw new IllegalArgumentException(
                        Utils.getMessage("Invoice.IllegalArgumentException.UpdateRecorded")
                );
            }
            BusinessPartner partner = null;
            if (dto.partnerID != null) {
                partner = dao.find(BusinessPartner.class, dto.partnerID);
                if (partner == null) {
                    throw new ReferentialIntegrityException(
                            Utils.getMessage("Invoice.ReferentialIntegrityException.Partner",
                                    dto.partnerID));
                }
            }
            ApplicationUser user = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME, 
                    ApplicationUser.class)
                    .setParameter(1, username)
                    .getSingleResult();
            Currency currency = null;
            if (dto.isDomesticCurrency) {
                //read domestic currency ISO code from application properties
                String ISOCode = dao.find(Properties.class, "domestic_currency")
                        .getValue();
                currency = dao.find(Currency.class, ISOCode);
                //if domestic currency does not exists in database create it
                if (currency == null) {
                    Currency domesticCurrency = new Currency(ISOCode);
                    domesticCurrency.setDescription("");
                    currency = dao.merge(domesticCurrency);
                    dao.flush();
                }
            } else {
                currency = dao.find(Currency.class, dto.currencyISOCode);
                if (currency == null) {
                    throw new ReferentialIntegrityException(
                            Utils.getMessage(
                                    "Invoice.ReferentialIntegrityException.Currency",
                                    dto.currencyISOCode)
                    );
                }
            }
            BankCreditor bank = (dto.bankID == null) ? null : dao.find(BankCreditor.class, dto.bankID);
            if (dto.bankID != null & bank == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.Bank",
                                dto.bankID));
            }
            temp.setPartner(partner);
//            //partner type korisnik ne moze da promeni
            temp.setInvoiceType(dto.proForma);
            temp.setPaid(dto.paid);
//            //printed korisnik ne moze da promeni
            temp.setInvoiceDate(dto.invoiceDate);
            temp.setCreditRelationDate(dto.creditRelationDate);
            temp.setValueDate(dto.valueDate);
            temp.setCurrency(currency);
            temp.setIsDomesticCurrency(dto.isDomesticCurrency);
            temp.setContractNumber(dto.contractNumber);
            temp.setContractDate(dto.contractDate);
            temp.setUser(user);
            temp.setBank(bank);
            temp.setVersion(dto.version);
            List<String> msgs = validator.validate(temp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            //flush?!?!?!
            return temp.getVersion();
        } catch (EntityNotFoundException | ReferentialIntegrityException 
                | IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Utils.getMessage("Invoice.OptimisticLockEx",
                        dto.clientId, dto.orgUnitId, dto.document),ex);
            } else {
                LOG.log(Level.WARNING, "Invoice.PersistenceEx.Update", ex);
                throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Update"),ex);
            }
        } 
    }

    @Transactional(readOnly = true)
    public InvoiceItemDTO readItem(Integer clientId,
            Integer unitId,
            String document,
            Integer ordinal)
            throws IllegalArgumentException,
            EntityNotFoundException {
        // TODO : check read invoice permission
        if (clientId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (unitId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        if (ordinal == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Ordinal"));
        }
        try {
            InvoiceItem temp = dao.find(InvoiceItem.class,
                    new InvoiceItemPK(clientId, unitId, document, ordinal));
            if (temp == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Invoice.EntityNotFoundException.InvoiceItem",
                                clientId, unitId, document, ordinal));
            }
            InvoiceItemDTO dto = new InvoiceItemDTO();
            dto.clientId = temp.getClientId();
            dto.unitId = temp.getOrgUnitId();
            dto.invoiceDocument = temp.getInvoiceDocument();
            dto.ordinal = temp.getOrdinal();
            dto.netPrice = temp.getNetPrice();
            dto.totalCost = temp.getTotalCost();
            dto.quantity = temp.getQuantity();
            dto.articleCode = temp.getItemCode();
            dto.articleDesc = temp.getItemDescription();
            dto.VATPercent = temp.getVatPercent();
            dto.rabatPercent = temp.getRebatePercent();
            return dto;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Invoice.PersistenceEx.ReadItem"),ex);
        } 
    }

    @Transactional
    public Long removeItem(Integer clientId,
            Integer unitId,
            String document,
            Integer ordinal,
            String username,
            char[] pass,
            Long version) 
            throws IllegalArgumentException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        // TODO : check update invoice permission
        if (clientId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (unitId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        if (ordinal == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Ordinal"));
        }
        try {
            Invoice invoice = dao.find(Invoice.class, new InvoicePK(clientId,
                    unitId,
                    document));
            if (invoice == null) {
                throw new EntityNotFoundException(Utils.getMessage(
                        "Invoice.EntityNotFoundException",
                        clientId, unitId, document));
            }
            if (invoice.isRecorded().equals(Boolean.TRUE)) {
                throw new IllegalArgumentException(Utils.getMessage(
                        "Invoice.IllegalArgumentException.DeleteItemRecorded"));
            }
            invoice.setVersion(version);
            List<ApplicationUser> userList = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME_AND_PASSWORD,
                    ApplicationUser.class)
                    .setParameter(1, username)
                    .setParameter(2, pass)
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.User",
                                username));
            }
            invoice.setUser(userList.get(0));
            dao.lock(invoice, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            InvoiceItem invoiceItem = dao.find(InvoiceItem.class,
                    new InvoiceItemPK(clientId, unitId, document, ordinal));
            if (invoiceItem == null) {
                throw new EntityNotFoundException(Utils.getMessage(
                        "Invoice.EntityNotFoundException.InvoiceItem",
                        clientId, unitId, document, ordinal));
            }
            invoice.removeItem(invoiceItem);
            dao.remove(invoiceItem);
            return invoice.getVersion();
        } catch (EntityNotFoundException | ReferentialIntegrityException 
                | IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Utils.getMessage("Invoice.OptimisticLockEx",
                        clientId, unitId, document),ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.DeleteItem"),ex);
            }
        } 
    }

    @Transactional
    public Long updateItem(InvoiceItemDTO dto) throws ReferentialIntegrityException,
            IllegalArgumentException,
            EntityNotFoundException {
        // TODO : check update invoice permission
        if (dto == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException"));
        }
        if (dto.clientId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (dto.unitId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.invoiceDocument == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        if (dto.ordinal == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Ordinal"));
        }
        if (dto.articleCode == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.ArticleCode")
            );
        }
        try {
            Invoice invoice = dao.find(Invoice.class,
                    new InvoicePK(dto.clientId, dto.unitId, dto.invoiceDocument)
            );
            if (invoice == null) {
                throw new EntityNotFoundException(Utils.getMessage(
                        "Invoice.EntityNotFoundException",
                        dto.clientId,
                        dto.unitId,
                        dto.invoiceDocument));
            }
            if (invoice.isRecorded().equals(Boolean.TRUE)) {
                throw new IllegalArgumentException(Utils.getMessage(
                        "Invoice.IllegalArgumentException.UpdateItemRecorded"));
            }
            List<ApplicationUser> userList = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME_AND_PASSWORD,
                    ApplicationUser.class)
                    .setParameter(1, dto.username)
                    .setParameter(2, dto.password)
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.User",
                                dto.username));
            }
            invoice.setUser(userList.get(0));
            invoice.setVersion(dto.invoiceVersion);
            dao.lock(invoice, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            InvoiceItem temp = dao.find(InvoiceItem.class,
                    new InvoiceItemPK(dto.clientId, dto.unitId, dto.invoiceDocument, dto.ordinal));
            if (temp == null) {
                throw new EntityNotFoundException(Utils.getMessage(
                        "Invoice.EntityNotFoundException.InvoiceItem",
                        dto.clientId, dto.unitId, dto.invoiceDocument, dto.ordinal));
            }
            temp.setQuantity(dto.quantity);
            temp.setNetPrice(dto.netPrice);
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
            temp.setRabatPercent(dto.rabatPercent);
            String[] netPriceValidation = validator.validateProperty(temp, "netPrice")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (netPriceValidation.length > 0) {
                throw new IllegalArgumentException(netPriceValidation);
            }
            String[] rabatValidation = validator.validateProperty(temp, "rabatPercent")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (rabatValidation.length > 0) {
                throw new IllegalArgumentException(rabatValidation);
            }
            String[] quantityValidation = validator.validateProperty(temp, "quantity")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (rabatValidation.length > 0) {
                throw new IllegalArgumentException(quantityValidation);
            }
            BigDecimal net = dto.netPrice.subtract(dto.netPrice.multiply(dto.rabatPercent));
            BigDecimal total = (net.multiply(BigDecimal.ONE.add(temp.getVatPercent())))
                    .multiply(dto.quantity);
            temp.setTotalCost(total.setScale(2, RoundingMode.HALF_UP));
            List<String> msgs = validator.validate(invoice).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            return invoice.getVersion();
        } catch (IllegalArgumentException | ReferentialIntegrityException 
                | EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Utils.getMessage("Invoice.OptimisticLockEx",
                        dto.clientId, dto.unitId, dto.invoiceDocument),ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.UpdateItem"),ex);
            }
        } 
    }

    @Transactional
    public Long addItem(InvoiceItemDTO dto) throws ReferentialIntegrityException,
                                                   IllegalArgumentException,
                                                   EntityExistsException {
        // TODO : check update invoice permission
        if (dto == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException"));
        }
        if (dto.clientId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (dto.unitId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.invoiceDocument == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        if (dto.articleCode == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.ArticleCode"));
        }

        try {
            Invoice invoice = dao.find(Invoice.class,
                    new InvoicePK(dto.clientId, dto.unitId, dto.invoiceDocument));
            if (invoice == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.EntityNotFoundException",
                                dto.clientId, dto.unitId, dto.invoiceDocument));
            }
            if (invoice.isRecorded().equals(Boolean.TRUE)) {
                throw new IllegalArgumentException(
                        Utils.getMessage("Invoice.IllegalArgumentException.AddItemRecorded"));
            }
            List<ApplicationUser> userList = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME_AND_PASSWORD,
                    ApplicationUser.class)
                    .setParameter(1, dto.username)
                    .setParameter(2, dto.password)
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.User",
                                dto.username));
            }
            OrgUnit unit = dao.find(OrgUnit.class,
                    new OrgUnitPK(dto.unitId, dto.clientId));
            if (unit == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.OrgUnit",
                                dto.clientId, dto.unitId));
            }
            invoice.setUser(userList.get(0));
            invoice.setVersion(dto.invoiceVersion);
            dao.lock(invoice, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Article article = dao.find(Article.class, dto.articleCode);
            if (article == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.Article",
                                dto.articleCode));
            }
            //can't throw nouniqueresultexception
            Integer max = dao.createNamedQuery("Invoice.GetMaxOrdinalNumber",
                    Integer.class)
                    .setParameter("document", dto.invoiceDocument)
                    .setParameter("orgUnit", unit)
                    .getSingleResult();
            Integer ordinalNumber = (max == null) ? 1 : (max + 1);
            InvoiceItem item = new InvoiceItem(invoice, ordinalNumber);
            //ne moze da se desi da imam dve stavke sa istim kljucem
            item.setNetPrice(dto.netPrice);
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
            item.setRabatPercent(dto.rabatPercent);
            item.setQuantity(dto.quantity);
            item.setArticleVAT(article.getVATRate());
            item.setArticle(article);
            item.setItemDescription(article.getDescription());
            item.setUnitOfMeasure(article.getUnitOfMeasureCode());
            String[] netPriceValidation = validator.validateProperty(item, "netPrice")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (netPriceValidation.length > 0) {
                throw new IllegalArgumentException(netPriceValidation);
            }
            String[] rabatValidation = validator.validateProperty(item, "rabatPercent")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (rabatValidation.length > 0) {
                throw new IllegalArgumentException(rabatValidation);
            }
            String[] quantityValidation = validator.validateProperty(item, "quantity")
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(size -> new String[size]);
            if (rabatValidation.length > 0) {
                throw new IllegalArgumentException(quantityValidation);
            }
            BigDecimal net = dto.netPrice.subtract(dto.netPrice.multiply(dto.rabatPercent));
            BigDecimal total = (net.multiply(BigDecimal.ONE.add(item.getVatPercent())))
                    .multiply(dto.quantity);
            item.setTotalCost(total.setScale(2, RoundingMode.HALF_UP));
            invoice.addItem(item);
             List<String> msgs = validator.validate(invoice).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            return invoice.getVersion();
        } catch (IllegalArgumentException | ReferentialIntegrityException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Utils.getMessage("Invoice.OptimisticLockEx",
                        dto.clientId, dto.unitId, dto.invoiceDocument),ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.AddItem"),ex);
            }
        } 
    }

    @Transactional(readOnly = true)
    public InvoiceItemDTO[] readInvoiceItems(
            Integer clientId,
            Integer orgUnitId,
            String document)
            throws IllegalArgumentException,
            EntityNotFoundException {
        // TODO : check read invoice permission
        if (clientId == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (orgUnitId == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Document"));
        }
        try {
            Invoice temp = dao.find(Invoice.class,
                    new InvoicePK(clientId, orgUnitId, document));
            if (temp == null) {
                throw new EntityNotFoundException(Utils.getMessage("Invoice.EntityNotFoundException",
                        clientId, orgUnitId, document));
            }
            InvoiceItemDTO[] result = new InvoiceItemDTO[temp.getItemsSize()];
            int i = 0;
            List<InvoiceItem> itemList = temp.getUnmodifiableItemsSet();
            for (InvoiceItem item : itemList) {
                InvoiceItemDTO dto = new InvoiceItemDTO();
                dto.clientId = item.getClientId();
                dto.clientDesc = item.getClientName();
                dto.unitId = item.getOrgUnitId();
//                dto.unitDesc = item.getOrgUnitName();
                dto.invoiceDocument = item.getInvoiceDocument();
                dto.ordinal = item.getOrdinal();
                dto.articleCode = item.getItemCode();
                dto.articleDesc = item.getItemDescription();
                dto.quantity = item.getQuantity();
                dto.netPrice = item.getNetPrice();
                dto.VATPercent = item.getVatPercent();
                dto.rabatPercent = item.getRebatePercent();
                dto.totalCost = item.getTotalCost();
                result[i] = dto;
                i = i + 1;
            }
            return result;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "Invoice.PersistenceEx.ReadInvoiceItems"),ex);
        } 
    }

    @Transactional(readOnly = true)
    public InvoiceReportDTO readInvoiceReport(Integer clientId,
            Integer orgUnitId,
            String document)
            throws IllegalArgumentException,
            EntityNotFoundException {       
        // TODO : check read invoice permission
        if (clientId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (orgUnitId == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new IllegalArgumentException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        try {
            Invoice temp = dao.find(Invoice.class,
                    new InvoicePK(clientId, orgUnitId, document));
            if (temp == null) {
                throw new EntityNotFoundException(Utils.getMessage("Invoice.EntityNotFoundException",
                        clientId, orgUnitId, document));
            }
            Client client = dao.find(Client.class, temp.getClientId());//mora da postoji
            InvoiceReportDTO result = new InvoiceReportDTO();
            result.creditRelationDate = temp.getCreditRelationDate();
            result.partnerID = temp.getPartnerID();
            result.partnerName = temp.getPartnerName();
            result.partnerAddress = temp.getPartnerStreet();
            result.partnerPost = temp.getPartnerPost();
            result.partnerCity = temp.getPartnerCity();
            result.partnerTIN = temp.getPartnerTID();
            result.partnerAccount = temp.getPartnerAccount();
            result.partnerPhone = temp.getPartnerPhone();
            result.partnerFax = temp.getPartnerFax();
            result.partnerEMail = temp.getPartnerEMail();
            result.clientName = client.getName();
            result.clientAddress = client.getStreet();
            result.clientCity = client.getPlace();
            result.clientVatCertificateNumber = client.getVatCertificateNumber();
            result.clientPhone = client.getPhone();
            result.clientPost = client.getPostCode();
            result.clientBAC = client.getBusinessActivityCode();
            result.clientTIN = client.getTIN();
            result.clientMail = client.getEMail();
            result.clientLogo = client.getLogo();
            result.document = temp.getDocument();
            result.invoiceDate = temp.getInvoiceDate();
            result.printed = temp.isPrinted();
            result.creditRelationDate = temp.getCreditRelationDate();
            result.valueDate = temp.getValueDate();
            result.currencyISOCode = temp.getCurrencyISOCode();
            result.currencyDesc = temp.getCurrencyDescription();
            result.isDomesticCurrency = temp.isDomesticCurrency();
            result.contractNumber = temp.getContractNumber();
            result.contractDate = temp.getContractDate();
            result.bankName = temp.getBankName();
            result.bankAccount = temp.getBankAccountNumber();

            if (temp.getInvoiceType() == InvoiceType.INVOICE) {
                result.proforma = false;
            } else {
                result.proforma = true;
            }
            if (temp.getPartnerType() == InvoiceBusinessPartner.DOMESTIC) {
                result.isDomesticPartner = true;
            } else {
                result.isDomesticPartner = false;
            }
            BigDecimal invoiceTotal = BigDecimal.ZERO;
            BigDecimal rebateTotal = BigDecimal.ZERO;//sum rebate
            BigDecimal netPriceTotal = BigDecimal.ZERO;//sum net price
            BigDecimal generalRateBasis = BigDecimal.ZERO;//sum net price
            BigDecimal generalRateTax = BigDecimal.ZERO;//sum net price
            BigDecimal lowerRateBasis = BigDecimal.ZERO;//sum net price
            BigDecimal lowerRateTax = BigDecimal.ZERO;//sum net price
            for (InvoiceItem i : temp.getUnmodifiableItemsSet()) {
                InvoiceReportDTO.Item item = new InvoiceReportDTO.Item();
                item.ordinal = i.getOrdinal();
                item.serviceDesc = i.getItemDescription();
                item.unitOfMeasure = i.getUnitOfMeasure();
                item.quantity = i.getQuantity();
                item.netPrice = i.getNetPrice();
                item.VATPercent = i.getVatPercent();
                item.rebatePercent = i.getRebatePercent();
                item.itemTotal = i.getTotalCost();
                BigDecimal articlePrice = i.getNetPrice().multiply(i.getQuantity());
                netPriceTotal = netPriceTotal.add(articlePrice);
                BigDecimal rebate = articlePrice.multiply(i.getRebatePercent());
                rebateTotal = rebateTotal.add(rebate);
                switch (i.getArticleVAT()) {
                    case GENERAL_RATE:
                        generalRateBasis = generalRateBasis.add(articlePrice.subtract(rebate));
                        generalRateTax = generalRateTax.add(articlePrice.subtract(rebate)
                                .multiply(i.getVatPercent()));
                        break;
                    case LOWER_RATE:
                        lowerRateBasis = lowerRateBasis.add(articlePrice.subtract(rebate));
                        lowerRateTax = lowerRateTax.add(articlePrice.subtract(rebate)
                                .multiply(i.getVatPercent()));
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal item tax rate");
                }
                invoiceTotal = invoiceTotal.add((articlePrice.subtract(rebate))
                        .multiply(BigDecimal.ONE.add(i.getVatPercent())));
                result.items.add(item);
            }
            result.invoiceTotalAmount = invoiceTotal.setScale(2, RoundingMode.HALF_UP);
            result.rebateTotal = rebateTotal.setScale(2, RoundingMode.HALF_UP);
            result.netPriceTotal = netPriceTotal.setScale(2, RoundingMode.HALF_UP);
            result.generalRateBasis = generalRateBasis.setScale(2, RoundingMode.HALF_UP);
            result.generalRateTax = generalRateTax.setScale(2, RoundingMode.HALF_UP);
            String generalRatePercent = dao.find(Properties.class, "vat_general_rate").getValue();
            result.generalRatePercent = new BigDecimal(generalRatePercent);
            String lowerRatePercent = dao.find(Properties.class, "vat_low_rate").getValue();
            result.lowerRateBasis = lowerRateBasis.setScale(2, RoundingMode.HALF_UP);
            result.lowerRateTax = lowerRateTax.setScale(2, RoundingMode.HALF_UP);
            result.lowerRatePercent = new BigDecimal(lowerRatePercent);
            if (temp.getInvoiceType() == (InvoiceType.INVOICE)) {
                temp.setPrinted(Boolean.TRUE);
            }
            result.version = temp.getVersion();
            return result;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Read"),ex);
        } 
    }

    @Transactional(readOnly = true)
    public ReadRangeDTO<InvoiceDTO> readPage(
            PageRequestDTO p)
            throws PageNotExistsException {
        // TODO : check read invoice permission
        String document = null;
        Date from = null;
        Date to = null;
        String articleCode = null;
        String businessPartner = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("document") && s.getValue() instanceof String) {
                document = (String) s.getValue();
            }
            if (s.getKey().equals("from") && s.getValue() instanceof Date) {
                from = (Date) s.getValue();
            }
            if (s.getKey().equals("to") && s.getValue() instanceof Date) {
                to = (Date) s.getValue();
            }
            if (s.getKey().equals("serviceID") && s.getValue() instanceof String) {
                articleCode = (String) s.getValue();
            }
            if (s.getKey().equals("partner") && s.getValue() instanceof String) {
                businessPartner = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();
            Long countEntities = this.count(dao, document, from, to, businessPartner,
                    articleCode);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            if (p.getPage().compareTo(-1) == -1
                    || p.getPage().compareTo(numberOfPages.intValue()) == 1) {
                throw new PageNotExistsException(
                        Utils.getMessage("Invoice.PageNotExists", p.getPage()));
            }
            ReadRangeDTO<InvoiceDTO> result = new ReadRangeDTO<>();
            //if page number is -1 read last page
            if (p.getPage().equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                List<Invoice> data = this.search(dao,
                        document,
                        from,
                        to,
                        businessPartner,
                        articleCode,
                        start,
                        pageSize);
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<Invoice> data = this.search(dao,
                        document,
                        from,
                        to,
                        businessPartner,
                        articleCode,
                        p.getPage() * pageSize,
                        pageSize);
                result.setData(this.convertToDTO(data));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(p.getPage());
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Invoice.PersistenceEx.ReadPage"),ex);
        } 
    }

    private List<InvoiceDTO> convertToDTO(List<Invoice> lista) {
        List<InvoiceDTO> listaDTO = new ArrayList<>();
        for (Invoice pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    public List<Invoice> search(
            EntityManager EM,
            String document,
            Date from,
            Date to,
            String partner,
            String articleCode,
            int start,
            int range) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Invoice> c = cb.createQuery(Invoice.class);
        Root<Invoice> root = c.from(Invoice.class);
        c.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (document != null && document.replace("%", "").isEmpty() == false) {
            criteria.add(
                    cb.like(root.get(Invoice_.document),
                            cb.parameter(String.class, "document")));
        }
        if (from != null) {
            criteria.add(
                    cb.greaterThanOrEqualTo(root.get(Invoice_.invoiceDate),
                            cb.parameter(Date.class, "from")));
        }
        if (to != null) {
            criteria.add(cb.lessThanOrEqualTo(
                    root.get(Invoice_.invoiceDate),
                    cb.parameter(Date.class, "to")));
        }
        if (partner != null) {
            criteria.add(cb.like(root.get(Invoice_.partner)
                    .get(BusinessPartner_.companyIdNumber),
                    cb.parameter(String.class, "partner")));
        }
        if (articleCode != null && articleCode.isEmpty() == false) {
            Subquery<InvoiceItem> sq = c.subquery(InvoiceItem.class);
            Root<InvoiceItem> rootsq = sq.from(InvoiceItem.class);
            sq.select(rootsq).where(
                    cb.equal(rootsq.get(InvoiceItem_.article).get(Article_.code),
                            cb.parameter(Integer.class, "articleCode")),
                    cb.equal(rootsq.get(InvoiceItem_.invoice)
                            .get(Invoice_.document), root.get(Invoice_.document)));
            criteria.add(cb.exists(sq));
        }
//        Join<Invoice, OrgUnit> orgUnit = root.join(Invoice_.orgUnit);
        c.where(cb.and(criteria.toArray(new Predicate[0])))
                .orderBy(
                cb.asc(root.get(Invoice_.client).get(Client_.id)),
                cb.asc(root.get(Invoice_.orgUnit)),
                cb.asc(root.get(Invoice_.document)));
        TypedQuery<Invoice> q = EM.createQuery(c);
        if (document != null) {
            q.setParameter("document", document);
        }
        if (from != null) {
            q.setParameter("from", from);
        }
        if (to != null) {
            q.setParameter("to", to);
        }
        if (partner != null) {
            q.setParameter("partner", partner);
        }
        if (articleCode != null && articleCode.isEmpty() == false) {
            q.setParameter("articleCode", articleCode);
        }
        q.setFirstResult(start);
        q.setMaxResults(range);
        return q.getResultList();
    }

    public Long count(EntityManager EM,
            String document,
            Date from,
            Date to,
            String partner,
            String articleCode) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Invoice> root = c.from(Invoice.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (document != null) {
            criteria.add(cb.like(root.get(Invoice_.document),
                    cb.parameter(String.class, "document")));
        }
        if (from != null) {
            criteria.add(cb.greaterThanOrEqualTo(root.get(Invoice_.invoiceDate),
                    cb.parameter(Date.class, "from")));
        }
        if (to != null) {
            criteria.add(cb.lessThanOrEqualTo(root.get(Invoice_.invoiceDate),
                    cb.parameter(Date.class, "to")));
        }
        if (partner != null) {
            criteria.add(cb.like(root.get(Invoice_.partner)
                    .get(BusinessPartner_.companyIdNumber),
                    cb.parameter(String.class, "partner")));
        }
        if (articleCode != null && articleCode.isEmpty() == false) {
            Subquery<InvoiceItem> sq = c.subquery(InvoiceItem.class);
            Root<InvoiceItem> rootsq = sq.from(InvoiceItem.class);
            sq.select(rootsq).where(cb.equal(rootsq.get(InvoiceItem_.article)
                    .get(Article_.code),
                    cb.parameter(Integer.class, "articleCode")),
                    cb.equal(rootsq.get(InvoiceItem_.invoice).get(Invoice_.document),
                            root.get(Invoice_.document)));
            criteria.add(cb.exists(sq));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (document != null) {
            q.setParameter("document", document);
        }
        if (from != null) {
            q.setParameter("from", from);
        }
        if (to != null) {
            q.setParameter("to", to);
        }
        if (partner != null) {
            q.setParameter("partner", partner);
        }
        if (articleCode != null && articleCode.isEmpty() == false) {
            q.setParameter("articleCode", articleCode);
        }
        return q.getSingleResult();
    }

}
