/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class InvoiceReportDTO {

    public String document;
    public String clientName;
    public String clientPost;
    public String clientCity;
    public String clientAddress;
    public String clientPhone;
    public String clientTIN;
    public String clientVatCertificateNumber;
    public String clientBAC;
    public byte[] clientLogo;
    public String partnerID;
    public String partnerName;
    public String partnerCity;
    public String partnerPost;
    public String partnerAddress;
    public String partnerTIN;
    public String partnerAccount;
    public String partnerPhone;
    public String partnerFax;
    public String partnerEMail;
    public LocalDate invoiceDate;
    public LocalDate creditRelationDate;
    public LocalDate valueDate;
    public Boolean printed;
    public Boolean proforma;
    public Boolean isDomesticPartner;
    public Boolean isDomesticCurrency;
    public String currencyISOCode;
    public String currencyDesc;
    public List<Item> items = new ArrayList<>();
    public String clientMail;
    public BigDecimal netPriceTotal;
    public BigDecimal rebateTotal;
    public BigDecimal invoiceTotalAmount;
    public BigDecimal generalRateBasis;
    public BigDecimal generalRateTax;
    public BigDecimal generalRatePercent;
    public BigDecimal lowerRateBasis;
    public BigDecimal lowerRateTax;
    public BigDecimal lowerRatePercent;
    public String contractNumber;
    public LocalDate contractDate;
    public String bankName;
    public String bankAccount;
    public Long version;

    public static class Item {

        public Integer ordinal;
        public String serviceDesc;
        public String unitOfMeasure;
        public BigDecimal quantity;
        public BigDecimal netPrice;
        public BigDecimal VATPercent;
        public BigDecimal rebatePercent;
        public BigDecimal itemTotal;
    }

}
