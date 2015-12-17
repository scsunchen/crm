/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import com.invado.core.dto.InvoiceDTO;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author root
 */
@Entity
@Table(name = "r_invoice", schema = "devel")
@NamedQueries({
        @NamedQuery(name = "Invoice.GetMaxOrdinalNumber",
                query = "SELECT MAX(x.ordinal) FROM InvoiceItem x WHERE "
                        + "x.invoice.document = :document AND x.invoice.orgUnitE = :orgUnit"),
        @NamedQuery(name = "Invoice.GetAll",
                query = "SELECT x FROM Invoice x ORDER BY x.orgUnit.client.id,"
                        + "x.orgUnit.id, x.document"),
        @NamedQuery(name = "Invoice.GetByOrgUnit",
                query = "SELECT x FROM Invoice x WHERE x.orgUnit = :orgUnit"),
        @NamedQuery(name = Invoice.READ_BY_INVOICING_TRANSACTION,
                query = "SELECT x FROM Invoice x WHERE x.invoicingTransaction = :invoicingTransaction"),
        @NamedQuery(name = Invoice.READ_MAX_DOCUMENT,
                query = "SELECT MAX(cast(x.document as integer))+1 FROM Invoice x WHERE "
                        + "x.client = :client AND x.orgUnitE = :orgUnit"),
        @NamedQuery(
                name = "Invoice.GetByPartner",
                query = "SELECT x FROM Invoice x WHERE x.partner.companyIdNumber = :companyIdNumber"
        )
})
@IdClass(InvoicePK.class)
public class Invoice implements Serializable {

    public static final String READ_MAX_DOCUMENT = "Invoice.GetMaxDocument";
    public static final String READ_BY_INVOICING_TRANSACTION = "Invoice.GetByInvoicingTransaction";

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
    @ManyToOne()
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
            @JoinColumn(name = "unit_id", referencedColumnName = "org_unit_id", insertable = false, updatable = false)
    })
    private OrgUnit orgUnitE;
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
    @ManyToOne
    @JoinColumn(name = "invoicing_transaction_id")
    private InvoicingTransaction invoicingTransaction;
    @Valid//FIXME : Lazy collection validation
    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "invoice",
            fetch = FetchType.LAZY)
    private List<InvoiceItem> items = new ArrayList<>();
    @Version()
    @Column(name = "version")
    private Long version;

    public Invoice() {
    }

    public Invoice(OrgUnit orgUnit, String document) {
        this.client = orgUnit.getClient();
        this.orgUnit = orgUnit.getId();
        this.document = document;
    }

    public void setIsDomesticCurrency(Boolean isDomesticCurrency) {
        this.isDomesticCurrency = isDomesticCurrency;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBank(BankCreditor bank) {
        this.bank = bank;
    }

    public Integer getBankID() {
        return bank.getId();
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

    public InvoiceDTO getDTO() {
        InvoiceDTO result = new InvoiceDTO();
        result.setClientId(this.getClientId());
        result.setClientDesc(this.getClientName());
        result.setOrgUnitId(this.getOrgUnitId());
        result.setOrgUnitDesc(orgUnitE.getName());
        result.setCreditRelationDate(this.getCreditRelationDate());
        result.setPartnerID(this.getPartnerID());
        result.setPartnerName(this.getPartnerName());
        result.setDocument(this.getDocument());
        result.setInvoiceDate(this.getInvoiceDate());
        result.setPaid(this.isPaid());
        result.setRecorded(this.isRecorded());
        result.setPrinted(this.isPrinted());
        result.setProForma(this.getInvoiceType());
        result.setPartnerType(this.getPartnerType());
        result.setValueDate(this.getValueDate());
        result.setCurrencyISOCode(this.getCurrencyISOCode());
        result.setCurrencyDesc(this.getCurrencyDescription());
        result.setVersion(this.getVersion());
        result.setIsDomesticCurrency(this.isDomesticCurrency());
        result.setContractNumber(this.getContractNumber());
        result.setContractDate(this.getContractDate());
        result.setBankID(this.getBankID());
        result.setBankName(bank.getName());
        return result;
    }

    public String getBankName() {
        return bank.getName();
    }

    public String getBankAccountNumber() {
        return bank.getAccount();
    }

    public Boolean isDomesticCurrency() {
        return isDomesticCurrency;
    }

    public String getClientName() {
        return client.getName();
    }

    public void setPartner(BusinessPartner partner) {
        this.partner = partner;
    }

    public BusinessPartner getPartner() {
        return partner;
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

    public Integer getPartnerID() {
        return partner.getId();
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

    public InvoicingTransaction getInvoicingTransaction() {
        return invoicingTransaction;
    }

    public void setInvoicingTransaction(InvoicingTransaction invoicingTransaction) {
        this.invoicingTransaction = invoicingTransaction;
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
        return Objects.equals(this.document, other.document);
    }

    @Override
    public String toString() {
        return "Invoice{" + "client=" + client + ", orgUnit=" + orgUnit + ", document=" + document + '}';
    }

}
