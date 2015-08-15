/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.LocalDateConverter;
import com.invado.core.domain.OrgUnit;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Vlada
 */
@Entity
@Table( name = "f_analytical", schema="devel")
@NamedQueries({
//    @NamedQuery(name = Analytical.REMOVE_BY_JOURNAL_ENTRY,
//            query = "DELETE FROM Analytical x WHERE "
//            + "x.journalEntryType.client.id = :clientID AND "
//            + "x.journalEntryType.id = :typeID AND x.journalEntryNumber = :number"),
    @NamedQuery(name = Analytical.READ_BY_STATUS,
            query = "SELECT x FROM Analytical x WHERE x.status = ?1"),
    @NamedQuery(name = Analytical.READ_BY_ACCOUNT,
            query = "SELECT x FROM Analytical x JOIN x.account k "
            + "WHERE k.number = ?1"),
    @NamedQuery(name = Analytical.READ_BY_USER,
            query = "SELECT x FROM Analytical x JOIN x.user k WHERE k.id = ?1"),
    @NamedQuery(name = Analytical.READ_BY_DESCRIPTION,
            query = "SELECT x FROM Analytical x JOIN x.description o "
            + "WHERE o.id = ?1"),
    @NamedQuery(name = Analytical.READ_BY_ORGUNIT,
            query = "SELECT x FROM Analytical x JOIN x.orgUnit o WHERE o.id = ?1 "
            + "AND o.client.id = ?2"),
    @NamedQuery(name = Analytical.READ_BY_JOURNAL_ENTRY_TYPE,
            query = "SELECT x FROM Analytical x JOIN x.journalEntryType t WHERE "
            + "t.id = ?1 AND t.client.id = ?2"),
    @NamedQuery(name = Analytical.READ_BY_BUSINESS_PARTNER,
            query = "SELECT x FROM Analytical x JOIN x.partner s WHERE "
            + "s.companyIdNumber = ?1"),
    @NamedQuery(name = Analytical.READ_ALL_ORDERBY_ID,
            query = "SELECT x FROM Analytical x ORDER BY x.id")
})
public class Analytical implements Serializable {

    public static final String REMOVE_BY_JOURNAL_ENTRY =
            "Analytical.RemoveByJournalEntry";
    public static final String READ_BY_STATUS = "Analytical.ReadByStatus";
    public static final String READ_BY_ACCOUNT = "Analytical.ReadByAccount";
    public static final String READ_BY_USER = "Analytical.ReadByUser";
    public static final String READ_BY_DESCRIPTION = "Analytical.ReadByDescription";
    public static final String READ_BY_ORGUNIT = "Analytical.ReadByOrgUnit";
    public static final String READ_BY_JOURNAL_ENTRY_TYPE =
            "Analytical.ReadByJournalEntryType";
    public static final String READ_BY_BUSINESS_PARTNER =
            "Analytical.ReadByBusinessPartner";
    public static final String READ_ALL_ORDERBY_ID =
            "Analytical.ReadAllOrderById";
    //UPIT PRILIKOM PRAVLJENJA IOS LISTA U AnalitikaDAOImpl 
    //NE MOZE DA BUDE NAMEDQUERY!!!!!!!!
    @Id
    @GeneratedValue(generator = "AnalitikaTab")
    @TableGenerator(
            name = "AnalitikaTab",
            table = "id_generator",            
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Analitika",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;
    @NotNull(message = "{Analytical.JournalEntryType.NotNull}")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "journal_entry_type_id", 
                    referencedColumnName = "type_id"),
        @JoinColumn(name = "journal_entry_type_company", 
                    referencedColumnName = "company_id")
    })
    private JournalEntryType journalEntryType;
    @NotNull(message = "{Analytical.JournalEntryNumber.NotNull}")
    @DecimalMin(value = "1", message = "{Analytical.JournalEntryNumber.Min}")
    @Column(name = "journal_entry_number")
    private Integer journalEntryNumber;
    @NotNull(message = "{Analytical.JournalEntryItemOrdinal.NotNull}")
    @Column(name = "ordinal")
    @DecimalMin(value = "1", message = "{Analytical.JournalEntryItemOrdinal.Min}")//>=1
    private Integer journalEntryItemOrdinalNumber;
    @NotNull(message = "{Analytical.JournalEntryDate.NotNull}")
    @Column(name = "journal_entry_record_date")
    @Convert(converter = LocalDateConverter.class)
    //u so proverava da li je u tekucoj godini poslovanja(Podesavanja.godina)
    private LocalDate recordDate;
    @ManyToOne
    @JoinColumn(name = "org_unit_id")
    @NotNull(message = "{Analytical.OrgUnit.NotNull}")
    private OrgUnit orgUnit;
    @NotNull(message = "{Analytical.CreditDebitRelationDate.NotNull}")
    @Column(name = "credit_relation_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate creditDebitRelationDate;
    @NotBlank(message = "{Analytical.Document.NotBlank}")
    @Column(name = "document")
    @Size(max = 35, message = "{Analytical.Document.Size}")
    private String document;
    @NotNull(message = "{Analytical.Determination.NotNull}")
    @Column(name = "determination")
    private AccountDetermination determination;
    @NotNull(message = "{Analytical.Desc.NotNull}")
    @ManyToOne
    @JoinColumn(name = "desc_id")
    private Description description;
    @NotNull(message = "{Analytical.Account.NotNull}")
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account account;
    @NotNull(message = "{Analytical.BusinessPartner.NotNull}")
    @ManyToOne
    @JoinColumn(name = "business_partner_id")
    private BusinessPartner partner;
    @Column(name = "internal_document")
    @Size(max = 35, message = "{Analytical.InterDocument.Size}")
    private String internalDocument;
    @NotNull(message = "{Analytical.ValueDate.NotNull}")
    @Column(name = "value_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate valueDate;
    @NotNull(message = "{Analytical.ChangeType.NotNull}")
    @Column(name = "change_type")
    private ChangeType changeType;
    @NotNull(message = "{Analytical.Amount.NotNull}")
    @Column(name = "amount")
    @Digits(integer = 15, fraction = 2, message = "{Analytical.Amount.Digits}")
    private BigDecimal amount;
    @NotNull(message = "{Analytical.User.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
    @NotNull(message = "{Analytical.Status.NotNull}")
    @Column(name = "status")
    private Status status;
    @Version
    @Column(name = "version")
    private Long version;

    //************************************************************************//    
    // CONSTRUCTORS //
    //************************************************************************//
    public Analytical() {
    }

    public Analytical(Long id) {
        this.id = id;
    }

    public Analytical(Long id, JournalEntryItem s) {
        this(s);
        this.id = id;
    }

    public Analytical(JournalEntryItem item) {
        journalEntryType = item.getJournalEntryTypeG();
        journalEntryNumber = item.getJournalEntryNumber();
        journalEntryItemOrdinalNumber = item.getOrdinalNumber();
        recordDate = item.getJournalEntry().getRecordDate();
        orgUnit = item.getOrgUnit();
        creditDebitRelationDate = item.getCreditDebitRelationDate();
        document = item.getDocument();
        description = item.getDesc();
        account = item.getAccount();
        internalDocument = item.getInternalDocument();
        partner = item.getPartner();
        valueDate = item.getValueDate();
        changeType = item.getChangeType();
        determination = item.getDetermination();
        amount = item.getAmount();
        status = Status.OPEN;
        user = item.getUser();
    }

    //************************************************************************//    
    // GET/SET METHODS //
    //************************************************************************//
    public Long getId() {
        return id;
    }

    public JournalEntryType getJournalEntryType() {
        return journalEntryType;
    }

    public void setJournalEntryType(JournalEntryType type) {
        this.journalEntryType = type;
    }

    public Integer getJournalEntryNumber() {
        return journalEntryNumber;
    }

    public void setJournalEntryNumber(Integer number) {
        this.journalEntryNumber = number;
    }

    public Integer getJournalEntryItemOrdinalNumber() {
        return journalEntryItemOrdinalNumber;
    }

    public void setJournalEntryItemOrdinalNumber(Integer ordinal) {
        this.journalEntryItemOrdinalNumber = ordinal;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate date) {
        this.recordDate = date;
    }

    public OrgUnit getOrgUnit() {
        return this.orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public LocalDate getCreditDebitRelationDate() {
        return creditDebitRelationDate;
    }

    public void setCreditDebitRelationDate(LocalDate date) {
        this.creditDebitRelationDate = date;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public AccountDetermination getDetermination() {
        return this.determination;
    }

    public void setDetermination(AccountDetermination d) {
        this.determination = d;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description desc) {
        this.description = desc;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BusinessPartner getPartner() {
        return this.partner;
    }

    public void setPartner(BusinessPartner s) {
        this.partner = s;
    }

    public String getInternalDocument() {
        return internalDocument;
    }

    public void setInternalDocument(String internal) {
        this.internalDocument = internal;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate date) {
        this.valueDate = date;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ApplicationUser getUser() {
        return this.user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status s) {
        this.status = s;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    //************************************************************************//    
    // DELEGATE METHODS //
    //************************************************************************//
    public String getPartnerTIN() {
        return partner.getTIN();
    }

    public String getPartnerName() {
        return partner.getName();
    }

    public String getAccountNumber() {
        return account.getNumber();
    }

    public String getPartnerCompanyID() {
        return partner.getCompanyIdNumber();
    }

    public Integer getDescriptionId() {
        return description.getId();
    }

    public BigDecimal getDebit() {
        if (changeType == ChangeType.DEBIT) {
            return amount;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getCredit() {
        if (changeType == ChangeType.CREDIT) {
            return amount;
        }
        return BigDecimal.ZERO;
    }

    public Integer getJournalEntryTypeID() {
        return journalEntryType.getId();
    }

    public Integer getOrgUnitID() {
        return orgUnit.getId();
    }

    public Integer getOrgUnitCompanyID() {
        return orgUnit.getClientID();
    }

    public String getDescriptionName() {
        return description.getName();
    }

    public void setAmount(ChangeType type, BigDecimal amount) {
        this.changeType = type;
        this.amount = amount;
    }

    //************************************************************************//    
    // OVERRIDEN OBJECT METHODS  //
    //************************************************************************//
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Analytical other = (Analytical) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Analytical{" + "id=" + id + '}';
    }

    public static enum Status {

        OPEN,
        CLOSED;
    }
}