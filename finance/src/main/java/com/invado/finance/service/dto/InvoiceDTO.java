/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import com.invado.finance.domain.InvoiceBusinessPartner;
import com.invado.finance.domain.InvoiceType;
import java.time.LocalDate;

/**
 *
 * @author root
 */
public class InvoiceDTO  {

    public Integer clientId;
    public String clientDesc;
    public Integer orgUnitId;
    public String orgUnitDesc;
    public String document;
    public String partnerID;
    public String partnerName;
    public LocalDate invoiceDate;
    public LocalDate creditRelationDate;
    public LocalDate valueDate;
    public Boolean recorded;
    public Boolean paid;
    public Boolean printed;
    public InvoiceType proForma;
    public InvoiceBusinessPartner partnerType;
    public Boolean isDomesticCurrency;
    public String currencyISOCode;
    public String currencyDesc;
    public String username;
    public char[] password;
    public String contractNumber;
    public LocalDate contractDate;
    public Integer bankID;
    public String bankName;
    public Long version;
}
