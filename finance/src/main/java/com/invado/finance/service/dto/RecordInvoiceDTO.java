/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.time.MonthDay;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/** 
 *
 * @author bdragan
 */
public class RecordInvoiceDTO {
    
    @NotNull(message="{RecordInvoice.IllegalArgument.Client}")
    public Integer clientId;
    @NotNull(message="{RecordInvoice.IllegalArgument.OrgUnit}")
    public Integer orgUnitId;
    @NotBlank(message="{RecordInvoice.IllegalArgument.Document}")
    public String document;
    @NotNull(message="{RecordInvoice.IllegalArgument.JournalEntryDate}")
    public MonthDay entryMonthDay;    
    @NotNull(message="{RecordInvoice.IllegalArgument.JournalEntryNumber}")
    public Integer entryOrderType;
    @NotNull(message="{RecordInvoice.IllegalArgument.JournalEntryNumber}")
    public Integer entryOrderNumber;
    @NotNull(message="{RecordInvoice.IllegalArgument.Description}")
    public Integer description;
    public String user;
}
