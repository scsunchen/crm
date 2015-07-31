/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class LedgerCardItemDTO implements Serializable {

    public LocalDate journalEntryDate;
    public Integer typeID;
    public Integer journalEntryNumber;
    public Integer ordinal;
    public Integer orgUnitID;
    public LocalDate creditDebitRelationDate;
    public LocalDate valueDate;
    public String accountNumber;
    public String description;
    public String document;
    public String internalDocument;
    public BigDecimal debit;
    public BigDecimal credit;    
    public BigDecimal balance;
}
