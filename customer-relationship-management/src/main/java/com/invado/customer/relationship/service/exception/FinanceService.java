package com.invado.customer.relationship.service.exception;

import com.invado.core.domain.*;
import com.invado.core.dto.InvoiceDTO;
import com.invado.core.exception.*;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.customer.relationship.Utils;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Nikola on 04/12/2015.
 */
public class FinanceService {

    private static final Logger LOG = Logger.getLogger(FinanceService.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager dao;
    @Inject
    private Validator validator;

    @Transactional(rollbackFor = Exception.class)
    public void createInvoice(InvoiceDTO dto) throws com.invado.core.exception.ConstraintViolationException,
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
}
