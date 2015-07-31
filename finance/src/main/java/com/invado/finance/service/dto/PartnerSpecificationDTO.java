/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bdragan
 */
public class PartnerSpecificationDTO implements Serializable {

    public String clientName;
    public String accountNumber;
    public Integer orgUnitID;
    public String orgUnitName;
    public String partnerID;
    public String partnerName;
    public LocalDate creditDebitRelationDateFrom;
    public LocalDate creditDebitRelationDateTo;
    public LocalDate valueDateFrom;
    public LocalDate valueDateTo;
    public Integer numberOfPages;
    public BigDecimal debitTotal = BigDecimal.ZERO;
    public BigDecimal creditTotal = BigDecimal.ZERO;
    public BigDecimal balanceTotal = BigDecimal.ZERO;
    public List<StavkaSpecifikacijeDTO> items = new ArrayList<>();
}
