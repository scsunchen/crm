/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author draganbob
 */
public class JournalEntryDTO {

    public BigDecimal balance;
    public BigDecimal balanceDebit;
    public BigDecimal balanceCredit;
    public Month month;
    public Integer day;
    public String typeName;
    public Integer clientId;
    public String clientName;
    public Integer typeId;
    public Integer typeNumber;
    public Integer journalEntryNumber;
    public Long numberOfItems;
    public Long version;
    public Boolean isPosted;
    public List<JournalEntryItemDTO> items = new ArrayList<>();
}
