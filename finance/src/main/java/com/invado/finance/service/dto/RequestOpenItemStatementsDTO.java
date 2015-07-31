/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import static com.invado.finance.Utils.getMessage;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author bdragan
 */
public class RequestOpenItemStatementsDTO  {

    @DateTimeFormat(style = "M-")
    private LocalDate valueDate;
    @DateTimeFormat(style = "M-")
    private LocalDate printDate;
    private Integer clientID;
    private String clientName;
    private Integer orgUnitID;
    private String orgUnitName;
    private String accountNumber;
    private String partnerID;
    private String partnerName;
    private Amount i;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal min;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal max;
    private Prikaz p;

    public static enum Amount implements Serializable {

        ALL,
        OPSEG
    }

    public static enum Prikaz implements Serializable {

        ANALYTIC,
        BALANCE;

        public String getDescription() {
            switch (this) {
                case ANALYTIC:
                    return getMessage("OpenStatements.Label.Analytic");
                case BALANCE:
                    return getMessage("OpenStatements.Label.Balance");
                default:
                    return "";
            }
        }

    }

    public LocalDate getPrintDate() {
        return printDate;
    }

    public void setPrintDate(LocalDate printDate) {
        this.printDate = printDate;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Integer getOrgUnitID() {
        return orgUnitID;
    }

    public void setOrgUnitID(Integer orgUnitID) {
        this.orgUnitID = orgUnitID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

    public Amount getI() {
        return i;
    }

    public void setI(Amount i) {
        this.i = i;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public Prikaz getP() {
        return p;
    }

    public void setP(Prikaz p) {
        this.p = p;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

}
