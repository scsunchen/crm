/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.ExchangeRate;
import com.invado.core.domain.ExchangeRatePK;
import com.invado.core.domain.OrgUnit;
import static com.invado.core.domain.VatPercent.GENERAL_RATE;
import static com.invado.core.domain.VatPercent.LOWER_RATE;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.SystemException;
import com.invado.finance.Utils;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.domain.Invoice;
import com.invado.finance.domain.InvoiceBusinessPartner;
import com.invado.finance.domain.InvoiceItem;
import com.invado.finance.domain.InvoicePK;
import com.invado.finance.domain.InvoiceType;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.ChangeType;
import static com.invado.finance.domain.journal_entry.ChangeType.DEBIT;
import com.invado.finance.domain.journal_entry.Description;
import com.invado.finance.domain.journal_entry.RecordInvoiceAccount;
import com.invado.finance.domain.journal_entry.JournalEntry;
import com.invado.finance.domain.journal_entry.JournalEntryItem;
import com.invado.finance.domain.journal_entry.JournalEntryPK;
import com.invado.finance.domain.journal_entry.JournalEntryType;
import com.invado.finance.domain.journal_entry.JournalEntryTypePK;
import com.invado.finance.service.dto.RequestInvoiceRecordingDTO;
import com.invado.finance.service.exception.JournalEntryExistsException;
import com.invado.finance.service.exception.PostedInvoiceException;
import com.invado.finance.service.exception.ProformaInvoicePostingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bobic Dragan
 */
@Service
public class RecordInvoiceService {

    private static final Logger LOG = Logger.getLogger(RecordInvoiceService.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager EM;
    @Autowired
    private Validator validator;

    @Transactional(rollbackFor = Exception.class)
    public void perform(RequestInvoiceRecordingDTO dto) throws EntityNotFoundException,
            JournalEntryExistsException,
            PostedInvoiceException,
            ProformaInvoicePostingException,
            ConstraintViolationException {
        List<String> result = validator.validate(dto).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        if(result.isEmpty() == false) {
            throw new ConstraintViolationException("",result);
        }
        try {
            Invoice invoice = EM.find(Invoice.class,
                    new InvoicePK(dto.getClientId(), dto.getOrgUnitId(), dto.getDocument()));
            if (invoice == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("RecordInvoice.EntityNotFoundEx.Invoice",
                                dto.getClientId(), dto.getOrgUnitId(), dto.getDocument())
                );
            }

            //check if invoice id already recorded(posted)
            if(invoice.isRecorded() == true) {
                throw new PostedInvoiceException(
                        Utils.getMessage("RecordInvoice.IllegalArgument.RecordedInvoice",
                                dto.getClientId(),
                                dto.getOrgUnitId(),
                                dto.getDocument())
                );
            }

            //check if user try to record proforma invoice
            if(invoice.getInvoiceType() == InvoiceType.PROFORMA_INVOICE) {
                throw new ProformaInvoicePostingException(
                        Utils.getMessage("RecordInvoice.IllegalArgument.ProformaPosting"));
            }
            if(EM.find(JournalEntry.class, new JournalEntryPK(dto.getClientId(),
                    dto.getEntryOrderType(),
                    dto.getJournalEntryNumber())) != null) {
                throw new JournalEntryExistsException(
                        Utils.getMessage("RecordInvoice.JournalEntryExists"));
            }
            BusinessPartner partner = EM.find(BusinessPartner.class,
                    invoice.getPartnerID());// ne moze biti null
            OrgUnit orgUnit = EM.find(OrgUnit.class,dto.getOrgUnitId());//ne moze biti null
            Description opis = EM.find(Description.class, dto.getDescription());
            if (opis == null) {
                //dao.rollbackTransaction();
                throw new EntityNotFoundException(
                        Utils.getMessage("RecordInvoice.EntityNotFoundEx.Desc",
                                dto.getDescription()));
            }
            List<ApplicationUser> userList = EM.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME,
                    ApplicationUser.class)
                    .setParameter(1, dto.getUser())
                    .getResultList();
            if (userList.isEmpty() == true) {
                throw new EntityNotFoundException(
                        Utils.getMessage("RecordInvoice.EntityNotFoundEx.User",
                                dto.getUser())
                );
            }
            ApplicationUser user = userList.get(0);
            //add currency exchange rate code
            JournalEntry order = new JournalEntry();
            JournalEntryType tip = EM.find(JournalEntryType.class,
                    new JournalEntryTypePK(dto.getEntryOrderType(), dto.getClientId()));
            if (tip == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("RecordInvoice.EntityNotFound.OrderType",
                                dto.getEntryOrderType(), dto.getClientId())
                );
            }
            order.setClient(tip.getClient().getId());
            order.setType(tip.getId());
            order.setNumber(dto.getJournalEntryNumber());
            order.setRecordDate(dto.getRecordDate());
            order.setPosted(Boolean.FALSE);
            order.setBalanceDebit(BigDecimal.ZERO);
            order.setBalanceCredit(BigDecimal.ZERO);
            BigDecimal total = BigDecimal.ZERO;//zadužujem kupca
            BigDecimal price = BigDecimal.ZERO;
            BigDecimal rebate = BigDecimal.ZERO;
            BigDecimal vatGeneral = BigDecimal.ZERO;
            BigDecimal vatLower = BigDecimal.ZERO;
            for (InvoiceItem item : invoice.getUnmodifiableItemsSet()) {
                BigDecimal price1 = item.getNetPrice().multiply(item.getQuantity());
                price = price.add(item.getNetPrice().multiply(item.getQuantity()));
                BigDecimal rebate1 = item.getNetPrice().multiply(item.getRebatePercent())
                        .multiply(item.getQuantity());
                rebate = rebate.add(item.getNetPrice().multiply(item.getRebatePercent())
                        .multiply(item.getQuantity()));
                BigDecimal tempVat = item.getNetPrice().subtract
                        (item.getNetPrice().multiply(item.getRebatePercent()))
                        .multiply(item.getVatPercent()).multiply(item.getQuantity());
                total = total.add(price1.subtract(rebate1).multiply
                        (BigDecimal.ONE.add(item.getVatPercent())));
                switch(item.getArticleVAT()) {
                    case GENERAL_RATE : vatGeneral = vatGeneral.add(tempVat);break;
                    case LOWER_RATE : vatLower = vatLower.add(tempVat);break;
                }
            }
            if (invoice.isDomesticCurrency() == false) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.YEAR, invoice.getCreditRelationDate().getYear());
                calendar.set(Calendar.DAY_OF_YEAR, invoice.getCreditRelationDate().getDayOfYear());
                ExchangeRate rate = EM.find(ExchangeRate.class,
                        new ExchangeRatePK(LocalDate.now(),
                                invoice.getCurrencyISOCode()));
                if (rate == null) {
                    //dao.rollbackTransaction();
                    throw new EntityNotFoundException(
                            Utils.getMessage("RecordInvoice.EntityNotFoundEx.ExchangeRate",
                                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(invoice.getCreditRelationDate()),
                                    invoice.getCurrencyISOCode()));
                }
                total = total.multiply(rate.getMiddle());
                price = price.multiply(rate.getMiddle());
                rebate = rebate.multiply(rate.getMiddle());
                vatGeneral = vatGeneral.multiply(rate.getMiddle());
                vatLower = vatLower.multiply(rate.getMiddle());
            }
            int rbr = 1;
            if(invoice.getPartnerType() == InvoiceBusinessPartner.ABROAD) {
                if (total.compareTo(BigDecimal.ZERO) > 0) {
                    Account konto = EM.find(RecordInvoiceAccount.class, "customer_abroad")
                            .getAccount();
                    this.addEntryOrderDocument(order,
                            rbr,
                            orgUnit,
                            invoice.getCreditRelationDate(),
                            invoice.getValueDate(),
                            opis,
                            invoice.getDocument(),
                            konto,
                            partner,
                            ChangeType.DEBIT,
                            total.setScale(2, RoundingMode.HALF_UP),
                            user
                    );
                    rbr = rbr +1;
                }

                if (price.compareTo(BigDecimal.ZERO) > 0) {
                    Account konto = EM.find(RecordInvoiceAccount.class, "income_abroad")
                            .getAccount();
                    this.addEntryOrderDocument(order,
                            rbr,
                            orgUnit,
                            invoice.getCreditRelationDate(),
                            invoice.getValueDate(),
                            opis,
                            invoice.getDocument(),
                            konto,
                            null,
                            ChangeType.CREDIT,
                            price.setScale(2, RoundingMode.HALF_UP),
                            user
                    );
                    rbr = rbr +1;
                }
                if (rebate.compareTo(BigDecimal.ZERO) > 0) {
                    Account konto = EM.find(RecordInvoiceAccount.class, "income_abroad")
                            .getAccount();
                    this.addEntryOrderDocument(order,
                            rbr,
                            orgUnit,
                            invoice.getCreditRelationDate(),
                            invoice.getValueDate(),
                            opis,
                            invoice.getDocument(),
                            konto,
                            null,
                            ChangeType.DEBIT,
                            rebate.setScale(2, RoundingMode.HALF_UP),
                            user);
                    rbr = rbr +1;
                }
            } else {
                if (total.compareTo(BigDecimal.ZERO) > 0) {
                    Account konto = EM.find(RecordInvoiceAccount.class, "customer_domestic")
                            .getAccount();
                    this.addEntryOrderDocument(order,
                            rbr,
                            orgUnit,
                            invoice.getCreditRelationDate(),
                            invoice.getValueDate(),
                            opis,
                            invoice.getDocument(),
                            konto,
                            partner,
                            ChangeType.DEBIT,
                            total.setScale(2, RoundingMode.HALF_UP),
                            user
                    );
                    rbr = rbr +1;
                }

                if (price.compareTo(BigDecimal.ZERO) > 0) {
                    Account konto = EM.find(RecordInvoiceAccount.class, "income_domestic")
                            .getAccount();
                    this.addEntryOrderDocument(order,
                            rbr,
                            orgUnit,
                            invoice.getCreditRelationDate(),
                            invoice.getValueDate(),
                            opis,
                            invoice.getDocument(),
                            konto,
                            null,
                            ChangeType.CREDIT,
                            price.setScale(2, RoundingMode.HALF_UP),
                            user);
                    rbr = rbr +1;
                }
                if (rebate.compareTo(BigDecimal.ZERO) > 0) {
                    Account konto = EM.find(RecordInvoiceAccount.class, "income_domestic")
                            .getAccount();
                    this.addEntryOrderDocument(order,
                            rbr,
                            orgUnit,
                            invoice.getCreditRelationDate(),
                            invoice.getValueDate(),
                            opis,
                            invoice.getDocument(),
                            konto,
                            null,
                            ChangeType.DEBIT,
                            rebate.setScale(2, RoundingMode.HALF_UP),
                            user
                    );
                    rbr = rbr + 1;
                }
                if (vatGeneral.compareTo(BigDecimal.ZERO) > 0) {
                    Account konto = EM.find(RecordInvoiceAccount.class, "vat_general_rate")
                            .getAccount();
                    this.addEntryOrderDocument(order,
                            rbr,
                            orgUnit,
                            invoice.getCreditRelationDate(),
                            invoice.getValueDate(),
                            opis,
                            invoice.getDocument(),
                            konto,
                            null,
                            ChangeType.CREDIT,
                            vatGeneral.setScale(2, RoundingMode.HALF_UP),
                            user
                    );
                    rbr = rbr +1;
                }
                if (vatLower.compareTo(BigDecimal.ZERO) > 0) {
                    Account konto = EM.find(RecordInvoiceAccount.class, "vat_lower_rate")
                            .getAccount();
                    this.addEntryOrderDocument(order,
                            rbr,
                            orgUnit,
                            invoice.getCreditRelationDate(),
                            invoice.getValueDate(),
                            opis,
                            invoice.getDocument(),
                            konto,
                            null,
                            ChangeType.CREDIT,
                            vatLower.setScale(2, RoundingMode.HALF_UP),
                            user);
                    rbr = rbr +1;
                }
            }
            //RAVNOTEZA NALOGA**************************************************
            BigDecimal duguje = BigDecimal.ZERO;
            BigDecimal potrazuje = BigDecimal.ZERO;
            for (JournalEntryItem stavka : order.getItems()) {
                if (stavka.getChangeType() == ChangeType.DEBIT) {
                    duguje = duguje.add(stavka.getAmount());
                }
                if (stavka.getChangeType() == ChangeType.CREDIT) {
                    potrazuje = potrazuje.add(stavka.getAmount());
                }
            }
            if (duguje.compareTo(potrazuje) > 0) {
                Account konto = EM.find(RecordInvoiceAccount.class,
                        "journal_entry_inequailty")
                        .getAccount();
                this.addEntryOrderDocument(order,
                        rbr,
                        orgUnit,
                        invoice.getCreditRelationDate(),
                        invoice.getValueDate(),
                        opis,
                        invoice.getDocument(),
                        konto,
                        null,
                        ChangeType.CREDIT,
                        duguje.subtract(potrazuje),
                        user
                );
                rbr = rbr +1;
            }
            if(duguje.compareTo(potrazuje) < 0) {
                Account konto = EM.find(RecordInvoiceAccount.class,
                        "journal_entry_inequailty")
                        .getAccount();
                this.addEntryOrderDocument(order,
                        rbr,
                        orgUnit,
                        invoice.getCreditRelationDate(),
                        invoice.getValueDate(),
                        opis,
                        invoice.getDocument(),
                        konto,
                        null,
                        ChangeType.DEBIT,
                        potrazuje.subtract(duguje),
                        user
                );
            }
            invoice.setRecorded(Boolean.TRUE);
            EM.persist(order);
        } catch(EntityNotFoundException | PostedInvoiceException |
                ProformaInvoicePostingException | JournalEntryExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    getMessage("RecordInvoice.PersistenceEx.Record")
            );
        }
    }

    private void addEntryOrderDocument(JournalEntry nalog,
                                       Integer ordinal,
                                       OrgUnit orgUnit,
                                       LocalDate datumDPO,
                                       LocalDate valueDate,
                                       Description opis,
                                       String document,
                                       Account account,
                                       BusinessPartner partner,
                                       ChangeType type,
                                       BigDecimal amount,
                                       ApplicationUser user ) {
        JournalEntryItem item = new JournalEntryItem(nalog, ordinal);
        item.setOrgUnit(orgUnit);
        item.setDesc(opis);
        item.setCreditDebitRelationDate(datumDPO);
        item.setDocument(document);
        item.setAccount(account);
        item.setDetermination(account.getDetermination());
        item.setValueDate(valueDate);
        item.setInternalDocument("");
        item.setAmount(type,amount);
        item.setPartner(partner);
        item.setUser(user);
        switch(type) {
            case DEBIT :
                nalog.setBalanceDebit(nalog.getBalanceDebit().add(amount));
                break;
            case CREDIT :
                nalog.setBalanceCredit(nalog.getBalanceCredit().add(amount));
                break;
        }

        //dodaj korisnika        
        nalog.addItem(item);
    }

}