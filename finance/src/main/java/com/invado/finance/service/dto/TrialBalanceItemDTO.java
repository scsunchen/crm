/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.math.BigDecimal;

/**
 *
 * @author bdragan
 */
public class TrialBalanceItemDTO {

    public String accountNumber;
    public String accountName;
    public BigDecimal openingBalanceDebit = BigDecimal.ZERO;
    public BigDecimal openingBalanceCredit = BigDecimal.ZERO;
    public BigDecimal currentTurnoverDebit = BigDecimal.ZERO;
    public BigDecimal currentTurnoverCredit = BigDecimal.ZERO;
    public BigDecimal totalDebit = BigDecimal.ZERO;
    public BigDecimal totalCredit = BigDecimal.ZERO;
    public BigDecimal balanceDebit = BigDecimal.ZERO;
    public BigDecimal balanceCredit = BigDecimal.ZERO;
}
