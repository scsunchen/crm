/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.Currency;
import com.invado.core.domain.OrgUnit;
import com.invado.finance.service.dto.InvoiceDTO;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author root
 */
@Entity
@Table(name = "r_invoice", schema="devel")
@NamedQueries({
    @NamedQuery(name = "Invoice.GetMaxOrdinalNumber",
            query = "SELECT MAX(x.ordinal) FROM InvoiceItem x WHERE "
            + "x.invoice.document = :document AND x.invoice.orgUnit = :orgUnit"),
    @NamedQuery(name = "Invoice.GetAll",
            query = "SELECT x FROM Invoice x ORDER BY x.orgUnit.client.id,"
            + "x.orgUnit.id, x.document")
})
@IdClass(InvoicePK.class)
public class Invoice implements Serializable {
    
    @Id
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    @NotNull(message = "{Invoice.Client.NotNull}")
    private Client client;
    @Id
    @Column(name = "unit_id")
    @NotNull(message = "{Invoice.OrgUnit.NotNull}")
    private Integer orgUnit;
    //Bug : https://hibernate.atlassian.net/browse/HHH-8333
    //AssertionFailure: Unexpected nested component on the referenced entity when mapping a @MapsId
//    @NotNull(message = "{Invoice.OrgUnit.NotNull}")
//    @ManyToOne
//    @JoinColumns({
//        @JoinColumn(name = "company_id", referencedColumnName = "company_id"),
//        @JoinColumn(name = "unit_id", referencedColumnName = "org_unit_id")
//    })
//    private OrgUnit orgUnitE;
    @NotBlank(message = "{Invoice.Document.NotBlank}")
    @Id
    @Column(name = "document")
    @Size(max = 35, message = "{Invoice.Document.Size}")
    private String document;
    @NotNull(message = "{Invoice.Partner.NotNull}")
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private BusinessPartner partner;
    @NotNull(message = "{Invoice.InvoiceDate.NotNull}")
    @Column(name = "invoice_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate invoiceDate;
    @NotNull(message = "{Invoice.CreditRelationDate.NotNull}")
    @Column(name = "credit_relation_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate creditRelationDate;
    @NotNull(message = "{Invoice.ValueDate.NotNull}")
    @Column(name = "value_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate valueDate;
    @NotNull(message = "{Invoice.Paid.NotNull}")
    @Column(name = "paid")
    private Boolean paid;
    @NotNull(message = "{Invoice.Recorded.NotNull}")
    @Column(name = "recorded")
    private Boolean recorded;
    @NotNull(message = "{Invoice.Printed.NotNull}")
    @Column(name = "printed")
    private Boolean printed;
    @NotNull(message = "{Invoice.ProForma.NotNull}")
    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private InvoiceType type;
    @NotNull(message = "{Invoice.PartnerType.NotNull}")
    @Column(name = "partner_type")
    @Enumerated(EnumType.ORDINAL)
    private InvoiceBusinessPartner partnerType;
    @NotNull(message = "{Invoice.User.NotNull}")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
    @NotNull(message = "{Invoice.Currency.NotNull}")
    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;
    @NotNull(message = "{Invoice.IsDomesticCurrency.NotNull}")
    @Column(name = "is_domestic_currency")
    private Boolean isDomesticCurrency;
    @Column(name = "contract_number")
    @Size(max = 20, message = "{Invoice.ContractNumber.Size}")
    private String contractNumber;
    @Column(name = "contract_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate contractDate;
    @NotNull(message = "{Invoice.Bank.NotNull}")
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private BankCreditor bank;
    @Valid//FIXME : Lazy collection validation
    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "invoice",
            fetch = FetchType.LAZY)    
    private List<InvoiceItem> items = new ArrayList<>();
    @Version()
    @Column(name="version")
    private Long version;

    public Invoice() {
    }

    public Invoice(OrgUnit orgUnit, String document) {
        this.orgUnit = orgUnit.getID();
        this.document = document;
    }

    public void setBank(BankCreditor bank) {
        this.bank = bank;
    }

    public Integer getBankID() {
        return bank.getId();
    }

    public String getBankAccountNumber() {
        return bank.getAccount();
    }

    public String getBankName() {
        return bank.getName();
    }

    public Boolean isDomesticCurrency() {
        return isDomesticCurrency;
    }

    public void setIsDomesticCurrency(Boolean isDomesticCurrency) {
        this.isDomesticCurrency = isDomesticCurrency;
    }

    public InvoiceDTO getDTO() {
        InvoiceDTO result = new InvoiceDTO();
        result.clientId = this.getClientId();
        result.clientDesc = this.getClientName();
        result.orgUnitId = this.getOrgUnitId();
//        result.orgUnitDesc = this.getOrgUnitName();
        result.creditRelationDate = this.getCreditRelationDate();
        result.partnerID = this.getPartnerID();
        result.partnerName = this.getPartnerName();
        result.document = this.getDocument();
        result.invoiceDate = this.getInvoiceDate();
        result.paid = this.isPaid();
        result.recorded = this.isRecorded();
        result.printed = this.isPrinted();
        result.proForma = this.getInvoiceType();
        result.partnerType = this.getPartnerType();
        result.valueDate = this.getValueDate();
        result.currencyISOCode = this.getCurrencyISOCode();
        result.currencyDesc = this.getCurrencyDescription();
        result.version = this.getVersion();
        result.isDomesticCurrency = this.isDomesticCurrency();
        result.contractNumber = this.getContractNumber();
        result.contractDate = this.getContractDate();
        result.bankID = this.getBankID();
        result.bankName = this.getBankName();
        return result;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public Integer getOrgUnitId() {
        return orgUnit;
    }

    public Integer getClientId() {
        return client.getId();
    }
//
//    public String getOrgUnitName() {
//        return orgUnitE.getName();
//    }
//
    public String getClientName() {
        return client.getName();
    }

    public void setPartner(BusinessPartner partner) {
        this.partner = partner;
    }

    public String getCurrencyISOCode() {
        return currency.getISOCode();
    }

    public String getCurrencyDescription() {
        return currency.getDescription();
    }

    public LocalDate getCreditRelationDate() {
        return creditRelationDate;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getDocument() {
        return document;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public String getPartnerStreet() {
        return partner.getStreet();
    }

    public String getPartnerPost() {
        return partner.getPostCode();
    }

    public String getPartnerTID() {
        return partner.getTIN();
    }

    public String getPartnerAccount() {
        return partner.getCurrentAccount();
    }

    public String getPartnerPhone() {
        return partner.getPhone();
    }

    public String getPartnerFax() {
        return partner.getFax();
    }

    public String getPartnerEMail() {
        return partner.getEMail();
    }

    public String getPartnerCity() {
        return partner.getPlace();
    }

    public String getPartnerID() {
        return partner.getCompanyIdNumber();
    }

    public String getPartnerName() {
        return partner.getName();
    }

    public Boolean isPaid() {
        return paid;
    }

    public Boolean isPrinted() {
        return printed;
    }

    public InvoiceType getInvoiceType() {
        return type;
    }

    public void setCreditRelationDate(LocalDate creditRelationDate) {
        this.creditRelationDate = creditRelationDate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public void setPrinted(Boolean printed) {
        this.printed = printed;
    }

    public void setInvoiceType(InvoiceType proforma) {
        this.type = proforma;
    }

    public InvoiceBusinessPartner getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(InvoiceBusinessPartner partnerType) {
        this.partnerType = partnerType;
    }

    public Long getVersion() {
        return version;
    }

    public char[] getPassword() {
        return user.getPassword();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getUserFullName() {
        return user.getDescription();
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<InvoiceItem> getUnmodifiableItemsSet() {
        Collections.sort(items);
        return Collections.unmodifiableList(items);
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
    }

    public boolean removeItem(InvoiceItem item) {
        return items.remove(item);
    }

    public int getItemsSize() {
        return items.size();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.client);
        hash = 79 * hash + Objects.hashCode(this.orgUnit);
        hash = 79 * hash + Objects.hashCode(this.document);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Invoice other = (Invoice) obj;
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        if (!Objects.equals(this.orgUnit, other.orgUnit)) {
            return false;
        }
        if (!Objects.equals(this.document, other.document)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Invoice{" + "client=" + client + ", orgUnit=" + orgUnit + ", document=" + document + '}';
    }

   
}