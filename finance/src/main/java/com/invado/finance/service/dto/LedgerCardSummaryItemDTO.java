/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.sproduct.finance.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Bobic Dragan
 */
public class LedgerCardSummaryItemDTO implements Serializable {

    public String account;
    public BigDecimal debit;
    public BigDecimal credit;
    public BigDecimal balance;
}
