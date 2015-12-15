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
import com.invado.finance.Utils;
import com.invado.core.domain.Article;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.domain.Invoice;
import com.invado.core.domain.InvoiceBusinessPartner;
import com.invado.core.domain.InvoiceItem;
import com.invado.core.domain.InvoiceItemPK;
import com.invado.core.domain.InvoicePK;
import com.invado.core.domain.InvoiceType;
import com.invado.core.domain.Invoice_;
import com.invado.core.domain.Properties;
import com.invado.core.dto.InvoiceDTO;
import com.invado.core.dto.InvoiceItemDTO;
import com.invado.finance.service.dto.InvoiceReportDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;

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
    @Inject
    private Validator validator;

    @Transactional(rollbackFor = Exception.class)
    public void createInvoice(InvoiceDTO dto) throws ConstraintViolationException,
            ReferentialIntegrityException,
            EntityExistsException {
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException"));
        }
        if (dto.getClientId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (dto.getOrgUnitId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.getDocument() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Document"));
        }
        if (dto.getIsDomesticCurrency() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.IsDomesticCurrency"));
        }
        if (dto.getIsDomesticCurrency() == false && dto.getCurrencyISOCode() == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.CurrencyISOCode"));
        }
        try {
            Invoice temp = dao.find(Invoice.class, new InvoicePK(dto.getClientId(),
                    dto.getOrgUnitId(),
                    dto.getDocument()));
            if (temp != null) {
                throw new EntityExistsException(
                        Utils.getMessage("Invoice.EntityExistsException",
                                dto.getClientId(), dto.getOrgUnitId(), dto.getDocument()));
            }
            OrgUnit unit = dao.find(OrgUnit.class, dto.getOrgUnitId());
            if (unit == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.OrgUnit",
                                dto.getClientId(), dto.getOrgUnitId()));
            }
            BusinessPartner partner = null;
            if (dto.getPartnerID() != null) {
                partner = dao.find(BusinessPartner.class, dto.getPartnerID());
                if (partner == null) {
                    throw new ReferentialIntegrityException(
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
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.User",
                                dto.getUsername()));
            }
            BankCreditor bank = (dto.getBankID() == null) ? null : dao.find(BankCreditor.class, dto.getBankID());
            if (dto.getBankID() != null & bank == null) {
                throw new ReferentialIntegrityException(
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
                    throw new ReferentialIntegrityException(
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
            List<String> msgs = validator.validate(invoice)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            dao.persist(invoice);
        } catch (EntityExistsException | ReferentialIntegrityException | ConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Create"), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteInvoice(Integer clientId,
            Integer orgUnitId,
            String document)
            throws ConstraintViolationException,
            EntityNotFoundException {
        // TODO:check delete invoice permission
        if (clientId == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (orgUnitId == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new ConstraintViolationException(
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
                throw new ConstraintViolationException(Utils.getMessage(
                        "Invoice.IllegalArgumentException.DeleteRecorded")
                );
            }
            dao.remove(temp);
        } catch (EntityNotFoundException | ConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Delete"), ex);
        }
    }

    @Transactional(readOnly = true)
    public InvoiceDTO readInvoice(Integer clientId,
            Integer orgUnitId,
            String document)
            throws ConstraintViolationException,
            EntityNotFoundException {
        // TODO : check read invoice permission
        if (clientId == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (orgUnitId == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new ConstraintViolationException(
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
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Read"), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateInvoice(InvoiceDTO dto) throws ConstraintViolationException,
            ReferentialIntegrityException,
            EntityNotFoundException {
        // TODO : check update invoice permission
        if (dto == null) {
            throw new ConstraintViolationException(Utils.getMessage("Invoice.IllegalArgumentException"));
        }
        if (dto.getClientId() == null) {
            throw new ConstraintViolationException(Utils.getMessage("Invoice.IllegalArgumentException.Client"));
        }
        if (dto.getOrgUnitId() == null) {
            throw new ConstraintViolationException(Utils.getMessage("Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (dto.getDocument() == null) {
            throw new ConstraintViolationException(Utils.getMessage("Invoice.IllegalArgumentException.Document"));
        }
        if (dto.getIsDomesticCurrency() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.IsDomesticCurrency"));
        }
        if (dto.getIsDomesticCurrency() == false && dto.getCurrencyISOCode() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Invoice.IllegalArgumentException.CurrencyISOCode"));
        }
        try {
            Invoice temp = dao.find(Invoice.class,
                    new InvoicePK(dto.getClientId(), dto.getOrgUnitId(), dto.getDocument()),
                    LockModeType.OPTIMISTIC);
            if (temp == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Invoice.EntityNotFoundException", dto.getClientId(),
                                dto.getOrgUnitId(), dto.getDocument()));
            }
            if (temp.isRecorded().equals(Boolean.TRUE)) {
                throw new ConstraintViolationException(
                        Utils.getMessage("Invoice.IllegalArgumentException.UpdateRecorded")
                );
            }
            BusinessPartner partner = null;
            if (dto.getPartnerID() != null) {
                partner = dao.find(BusinessPartner.class, dto.getPartnerID());
                if (partner == null) {
                    throw new ReferentialIntegrityException(
                            Utils.getMessage("Invoice.ReferentialIntegrityException.Partner",
                                    dto.getPartnerID()));
                }
            }
            ApplicationUser user = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME,
                    ApplicationUser.class)
                    .setParameter(1, dto.getUsername())
                    .getSingleResult();
            Currency currency = null;
            String ISOCode = dao.find(Properties.class, "domestic_currency")
                    .getValue();
            if (dto.getCurrencyISOCode() == null 
                    || (dto.getCurrencyISOCode().equals(ISOCode) | dto.getCurrencyISOCode().isEmpty())) {
                temp.setIsDomesticCurrency(Boolean.TRUE);
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
                temp.setIsDomesticCurrency(Boolean.FALSE);
                currency = dao.find(Currency.class, dto.getCurrencyISOCode());
                if (currency == null) {
                    throw new ReferentialIntegrityException(
                            Utils.getMessage(
                                    "Invoice.ReferentialIntegrityException.Currency",
                                    dto.getCurrencyISOCode())
                    );
                }
            }
            BankCreditor bank = (dto.getBankID() == null) ? null : dao.find(BankCreditor.class, dto.getBankID());
            if (dto.getBankID() != null & bank == null) {
                throw new ReferentialIntegrityException(
                        Utils.getMessage("Invoice.ReferentialIntegrityException.Bank",
                                dto.getBankID()));
            }
            temp.setPartner(partner);
//            //partner type korisnik ne moze da promeni
            temp.setInvoiceType(dto.getProForma());
            temp.setPaid(dto.getPaid());
//            //printed korisnik ne moze da promeni
            temp.setInvoiceDate(dto.getInvoiceDate());
            temp.setCreditRelationDate(dto.getCreditRelationDate());
            temp.setValueDate(dto.getValueDate());
            temp.setCurrency(currency);
//            temp.setIsDomesticCurrency(dto.getIsDomesticCurrency());
            temp.setContractNumber(dto.getContractNumber());
            temp.setContractDate(dto.getContractDate());
            temp.setUser(user);
            temp.setBank(bank);
            List<String> msgs = validator.validate(temp).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            if (temp.getVersion().compareTo(dto.getVersion()) != 0) {
                throw new OptimisticLockException();
            }
            return temp.getVersion();
        } catch (EntityNotFoundException | ReferentialIntegrityException | ConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Utils.getMessage("Invoice.OptimisticLockEx",
                        dto.getClientId(), dto.getOrgUnitId(), dto.getDocument()), ex);
            } else {
                LOG.log(Level.WARNING, "Invoice.PersistenceEx.Update", ex);
                throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Update"), ex);
            }
        }
    }

    @Transactional(readOnly = true)
    public InvoiceItemDTO readItem(Integer clientId,
            Integer unitId,
            String document,
            Integer ordinal)
            throws ConstraintViolationException,
            EntityNotFoundException {
        // TODO : check read invoice permission
        if (clientId == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (unitId == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        if (ordinal == null) {
            throw new ConstraintViolationException(Utils.getMessage(
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
            dto.setClientId(temp.getClientId());
            dto.setUnitId(temp.getOrgUnitId());
            dto.setInvoiceDocument(temp.getInvoiceDocument());
            dto.setOrdinal(temp.getOrdinal());
            dto.setNetPrice(temp.getNetPrice());
            dto.setTotalCost(temp.getTotalCost());
            dto.setQuantity(temp.getQuantity());
            dto.setArticleCode(temp.getItemCode());
            dto.setArticleDesc(temp.getItemDescription());
            dto.setVATPercent(temp.getVatPercent());
            dto.setRabatPercent(temp.getRebatePercent());
            return dto;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Invoice.PersistenceEx.ReadItem"), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long removeItem(Integer clientId,
            Integer unitId,
            String document,
            Integer ordinal,
            String username,
            Long version)
            throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        // TODO : check update invoice permission
        if (clientId == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (unitId == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Document"));
        }
        if (ordinal == null) {
            throw new ConstraintViolationException(Utils.getMessage(
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
                throw new ConstraintViolationException(Utils.getMessage(
                        "Invoice.IllegalArgumentException.DeleteItemRecorded"));
            }
            List<ApplicationUser> userList = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME,
                    ApplicationUser.class)
                    .setParameter(1, username)
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
            if (invoice.getVersion().compareTo(version) != 0) {
                throw new OptimisticLockException();
            }
            return invoice.getVersion();
        } catch (EntityNotFoundException | ReferentialIntegrityException | ConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Utils.getMessage("Invoice.OptimisticLockEx",
                        clientId, unitId, document), ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.DeleteItem"), ex);
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

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadRangeDTO<InvoiceItemDTO> readIncomeItemsPage(PageRequestDTO p)
            throws PageNotExistsException {
        try {
            Integer clientId = null;
            Integer unitId = null;
            String document = null;
            for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
                if (s.getKey().equals("document") && s.getValue() instanceof String) {
                    document = (String) s.getValue();
                }
                if (s.getKey().equals("clientId") && s.getValue() instanceof Integer) {
                    clientId = (Integer) s.getValue();
                }
                if (s.getKey().equals("unitId") && s.getValue() instanceof Integer) {
                    unitId = (Integer) s.getValue();
                }
            }
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();
            Long countEntities = dao.createNamedQuery(
                    InvoiceItem.COUNT_ITEMS_BY_INVOICE, Long.class)
                    .setParameter("clientId", clientId)
                    .setParameter("orgUnit", unitId)
                    .setParameter("document", document)
                    .getSingleResult();
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                throw new PageNotExistsException(Utils.getMessage("Invoice.PageNotExists", p.getPage()));
            }
            ReadRangeDTO<InvoiceItemDTO> result = new ReadRangeDTO<>();
            //if page number is -1 read last page
            if (pageNumber.equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                result.setData(convertToDTO(dao.createNamedQuery(
                    InvoiceItem.READ_ITEMS_BY_INVOICE, InvoiceItem.class)
                    .setParameter("clientId", clientId)
                    .setParameter("orgUnit", unitId)
                    .setParameter("document", document)
                    .setFirstResult(start)
                    .setMaxResults(pageSize)
                    .getResultList()));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(dao.createNamedQuery(
                    InvoiceItem.READ_ITEMS_BY_INVOICE, InvoiceItem.class)
                    .setParameter("clientId", clientId)
                    .setParameter("orgUnit", unitId)
                    .setParameter("document", document)
                    .setFirstResult(p.getPage()* pageSize)
                    .setMaxResults(pageSize)
                    .getResultList()));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "Invoice.PersistenceEx.ReadInvoiceItems"), ex);
        }
    }

    private List<InvoiceItemDTO> convertToDTO(List<InvoiceItem> items) {
        return items.stream().map(item -> {
            InvoiceItemDTO dto = new InvoiceItemDTO();
            dto.setClientId(item.getClientId());
            dto.setClientDesc(item.getClientName());
            dto.setUnitId(item.getOrgUnitId());
            dto.setInvoiceDocument(item.getInvoiceDocument());
            dto.setOrdinal(item.getOrdinal());
            dto.setArticleCode(item.getItemCode());
            dto.setArticleDesc(item.getItemDescription());
            dto.setQuantity(item.getQuantity());
            dto.setNetPrice(item.getNetPrice());
            dto.setVATPercent(item.getVatPercent());
            dto.setRabatPercent(item.getRebatePercent());
            dto.setTotalCost(item.getTotalCost());
            dto.setInvoiceVersion(item.getInvoice().getVersion());
            return dto;
        }).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public InvoiceReportDTO readInvoiceReport(Integer clientId,
            Integer orgUnitId,
            String document)
            throws ConstraintViolationException,
            EntityNotFoundException {
        // TODO : check read invoice permission
        if (clientId == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.Client"));
        }
        if (orgUnitId == null) {
            throw new ConstraintViolationException(Utils.getMessage(
                    "Invoice.IllegalArgumentException.OrgUnit"));
        }
        if (document == null) {
            throw new ConstraintViolationException(Utils.getMessage(
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
            result.partnerID = temp.getPartner().getCompanyIdNumber();
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

            result.proforma = temp.getInvoiceType() != InvoiceType.INVOICE;
            result.isDomesticPartner = temp.getPartnerType() == InvoiceBusinessPartner.DOMESTIC;
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
                        throw new ConstraintViolationException("Illegal item tax rate");
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
            throw new SystemException(Utils.getMessage("Invoice.PersistenceEx.Read"), ex);
        }
    }

    @Transactional(readOnly = true)
    public ReadRangeDTO<InvoiceDTO> readPage(
            PageRequestDTO p)
            throws PageNotExistsException {
        // TODO : check read invoice permission
        String document = null;
        LocalDate from = null;
        LocalDate to = null;
        String businessPartner = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("document") && s.getValue() instanceof String) {
                document = (String) s.getValue();
            }
            if (s.getKey().equals("from") && s.getValue() instanceof LocalDate) {
                from = (LocalDate) s.getValue();
            }
            if (s.getKey().equals("to") && s.getValue() instanceof LocalDate) {
                to = (LocalDate) s.getValue();
            }
            if (s.getKey().equals("partner") && s.getValue() instanceof String) {
                businessPartner = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();
            Long countEntities = this.count(dao, document, from, to, businessPartner);
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
                        start,
                        pageSize);
                result.setData(data.stream().map(Invoice::getDTO).collect(Collectors.toList()));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<Invoice> data = this.search(dao,
                        document,
                        from,
                        to,
                        businessPartner,
                        p.getPage() * pageSize,
                        pageSize);
                result.setData(data.stream().map(Invoice::getDTO).collect(Collectors.toList()));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(p.getPage());
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Invoice.PersistenceEx.ReadPage"), ex);
        }
    }

    public List<Invoice> search(
            EntityManager EM,
            String document,
            LocalDate from,
            LocalDate to,
            String partnerName,
            int start,
            int range) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Invoice> c = cb.createQuery(Invoice.class);
        Root<Invoice> root = c.from(Invoice.class);
        c.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (document != null && document.isEmpty() == false) {
            criteria.add(
                    cb.like(root.get(Invoice_.document),
                            cb.parameter(String.class, "document")));
        }
        if (from != null) {
            criteria.add(
                    cb.greaterThanOrEqualTo(root.get(Invoice_.invoiceDate),
                            cb.parameter(LocalDate.class, "from")));
        }
        if (to != null) {
            criteria.add(cb.lessThanOrEqualTo(
                    root.get(Invoice_.invoiceDate),
                    cb.parameter(LocalDate.class, "to")));
        }
        if (partnerName != null && partnerName.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Invoice_.partner)
                    .get(BusinessPartner_.name)),
                    cb.parameter(String.class, "partner")));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0])))
                .orderBy(
                        cb.asc(root.get(Invoice_.client).get(Client_.id)),
                        cb.asc(root.get(Invoice_.orgUnit)),
                        cb.asc(root.get(Invoice_.document)));
        TypedQuery<Invoice> q = EM.createQuery(c);
        if (document != null && document.isEmpty() == false) {
            q.setParameter("document", "%" + document + "%");
        }
        if (from != null) {
            q.setParameter("from", from);
        }
        if (to != null) {
            q.setParameter("to", to);
        }
        if (partnerName != null && partnerName.isEmpty() == false) {
            q.setParameter("partner", "%" + partnerName.toUpperCase()+ "%");
        }
        q.setFirstResult(start);
        q.setMaxResults(range);
        return q.getResultList();
    }

    public Long count(EntityManager EM,
            String document,
            LocalDate from,
            LocalDate to,
            String partnerName) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Invoice> root = c.from(Invoice.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (document != null && document.isEmpty() == false) {
            criteria.add(cb.like(root.get(Invoice_.document),
                    cb.parameter(String.class, "document")));
        }
        if (from != null) {
            criteria.add(cb.greaterThanOrEqualTo(root.get(Invoice_.invoiceDate),
                    cb.parameter(LocalDate.class, "from")));
        }
        if (to != null) {
            criteria.add(cb.lessThanOrEqualTo(root.get(Invoice_.invoiceDate),
                    cb.parameter(LocalDate.class, "to")));
        }
        if (partnerName != null && partnerName.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Invoice_.partner)
                    .get(BusinessPartner_.name)),
                    cb.parameter(String.class, "partner")));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (document != null && document.isEmpty() == false) {
            q.setParameter("document", document);
        }
        if (from != null) {
            q.setParameter("from", from);
        }
        if (to != null) {
            q.setParameter("to", to);
        }
        if (partnerName != null && partnerName.isEmpty() == false) {
            q.setParameter("partner", "%" + partnerName.toUpperCase()+ "%");
        }
        return q.getSingleResult();
    }

}
