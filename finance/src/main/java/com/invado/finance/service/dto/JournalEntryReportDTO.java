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
 * @author Vlada
 */
public class JournalEntryReportDTO {

    public LocalDate date;
    public String typeName;
    public Integer typeId;
    public Integer journalEntryNumber;
    public String clientName;
    public BigDecimal totalCredit;
    public BigDecimal totalDebit;
    public BigDecimal totalBalance;
    public BigDecimal generalLedgerDebit;
    public BigDecimal generalLedgerCredit;
    public BigDecimal generalLedgerBalance;
    public BigDecimal supplierDebit;
    public BigDecimal supplierCredit;
    public BigDecimal supplierBalance;
    public BigDecimal customerDebit;
    public BigDecimal customerCredit;
    public BigDecimal customerBalance;
    public Boolean isPosted;
    public List<JournalEntryItemDTO> items = new ArrayList<>();
}
