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
 * @author Bobic Dragan
 */

public class LedgerCardDTO {

    public Integer numberOfPages;
    public String clientName;
    public Integer orgUnitID;
    public String orgUnitName;
    public String accountNumber;
    public String accountName;
    public String status;
    public LocalDate creditDebitRelationDateFrom;
    public LocalDate creditDebitRelationDateTo;
    public LocalDate valueDateFrom;
    public LocalDate valueDateTo;
    public CurrencyTypeDTO currency;
    public String currencyISOCode;
    public String partnerRegistrationNumber;
    public String partnerName;
    public String partnerPlace;
    public String partnerStreet;
    public String partnerAccount;
    public BigDecimal debitTotal;
    public BigDecimal creditTotal;
    public BigDecimal balanceTotal;
    public LedgerCardTypeDTO type;
    public List<LedgerCardItemDTO> items = new ArrayList<>();    
    public List<AccountTotal> accountTotalItems = new ArrayList<>();

    public static class AccountTotal {

        public String accountCode;
        public BigDecimal debit;
        public BigDecimal credit;
        public BigDecimal balance;
    }    
}
