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
public class StavkaSpecifikacijeDTO {

    public Integer idOrgJedinice;
    public String sifraKonta;
    public String businessPartnerRegNo;
    public String businessPartnerName;
    public BigDecimal debit;
    public BigDecimal credit;
    public BigDecimal balance;
}
