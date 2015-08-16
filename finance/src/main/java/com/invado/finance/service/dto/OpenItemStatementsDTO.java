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
public class OpenItemStatementsDTO  {

    public String accountNumber;
    public String accountName;
    //poverilac
    public String creditorCompanyID;//maticni broj
    public String creditorTIN;
    public String creditorName;
    public String creditorStreet;
    public String creditorPlace;
    public String creditorPostCode;
    public String creditorPhone;
    public String creditorCurrentAccount;
//******************************************************************************
    public String debtorCompanyID;//maticni broj
    public String debtorTIN;
    public String debtorName;
    public String debtorStreet;
    public String debtorPlace;
    public String debtorPostCode;
    public List<Item> items = new ArrayList<>();
    public BigDecimal totalDebit = BigDecimal.ZERO;
    public BigDecimal totalCredit = BigDecimal.ZERO;
    public BigDecimal balanceDebit = BigDecimal.ZERO;
    public BigDecimal balanceCredit = BigDecimal.ZERO;

    public static class Item implements Serializable {

        public Integer ordinalNumber;
        public String document;
        public LocalDate valueDate;
        public BigDecimal debit;
        public BigDecimal credit;
    }
}