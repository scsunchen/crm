/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.math.BigDecimal;

/**
 *
 * @author root
 */
public class InvoiceItemDTO {
    
    public Integer clientId;
    public String clientDesc;
    public Integer unitId;
//    public String unitDesc;
    public String invoiceDocument;
    public Integer ordinal;
    public String articleCode;
    public String articleDesc;
    public BigDecimal VATPercent;
    public BigDecimal netPrice;    
    public BigDecimal quantity;
    public BigDecimal rabatPercent;
    public BigDecimal totalCost;
    public String username;
    public char[] password;
    public Long invoiceVersion;
}
