/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.math.BigDecimal;

/**
 *
 * @author Bobic Dragan
 */
public class UpdateJournalEntryResultDTO  {

    public BigDecimal creditBalance;
    public BigDecimal debitBalance;
    public BigDecimal balance;
    public Long version;
}
