/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.domain.journal_entry.Determination;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author draganbob
 */
public class JournalEntryItemDTO {

    public Integer clientId;
    public Integer typeId;
    public Integer journalEntryNumber;
    public Integer ordinalNumber;
    public Integer unitId;
    public String unitName;
    public LocalDate creditDebitRelationDate;
    public String document;
    public Integer descId;
    public String descName;
    public String accountCode;
    public String accountName;
    public String partnerCompanyId;
    public String partnerName;
    public String internalDocument;
    public LocalDate valueDate;
    public ChangeType type;
    public BigDecimal amount;
    public String note;
    public String username;
    public char[] password;
    public Integer userId;
    public Determination determination;
}
