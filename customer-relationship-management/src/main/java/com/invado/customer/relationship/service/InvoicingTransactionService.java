package com.invado.customer.relationship.service;

import com.invado.core.domain.*;
import com.invado.core.dto.InvoiceReportDTO;
import com.invado.core.dto.InvoicingTransactionDTO;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.SystemException;
import com.invado.customer.relationship.Utils;
import com.invado.customer.relationship.domain.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nikola on 16.12.2015.
 */
@Service
public class InvoicingTransactionService {

    private static final Logger LOG = Logger.getLogger(
            Transaction.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager dao;

    public List<InvoicingTransactionDTO> getAllPeriods(){

        CriteriaBuilder cb = dao.getCriteriaBuilder();
        CriteriaQuery<InvoicingTransaction> query = cb.createQuery(InvoicingTransaction.class);
        Root<InvoicingTransaction> root = query.from(InvoicingTransaction.class);
        query.select(root);
        query.orderBy(cb.desc(root.get(InvoicingTransaction_.invoicingDate)));
        TypedQuery<InvoicingTransaction> typedQuery = dao.createQuery(query);
        List<InvoicingTransaction> list =  typedQuery.getResultList();
        List<InvoicingTransactionDTO> listDTO = new ArrayList<InvoicingTransactionDTO>();
        for (InvoicingTransaction item : list){
            listDTO.add(item.getDTO());
        }

        return listDTO;
    }

    public InvoicingTransactionDTO getbyId(Integer id){
        return dao.find(InvoicingTransaction.class, id).getDTO();
    }



    @Transactional(rollbackFor = Exception.class)
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
            if (temp.getInvoiceType() == InvoiceType.INVOICE) {
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
}
