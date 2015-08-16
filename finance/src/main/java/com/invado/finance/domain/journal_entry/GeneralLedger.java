/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.ApplicationUser;
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

/**
 *
 * @author Vlada
 */
@Entity
@Table(name = "f_general_ledger", schema="devel")
@NamedQueries({
//    @NamedQuery(name = GeneralLedger.REMOVE_BY_JOURNAL_ENTRY,
//            query = "DELETE FROM GeneralLedger x WHERE "
//            + "x.journalEntryType.client.id = :clientID AND "
//            + "x.journalEntryType.id = :typeID AND "
//            + "x.journalEntryNumber = :number"),
    @NamedQuery(name = GeneralLedger.READ_BY_ACCOUNT,
            query = "SELECT x FROM GeneralLedger x JOIN x.account k WHERE k.number = ?1"),
    @NamedQuery(name = GeneralLedger.READ_BY_USER,
            query = "SELECT x FROM GeneralLedger x JOIN x.user k WHERE k.id = ?1"),
    @NamedQuery(name = GeneralLedger.READ_BY_DESCRIPTION,
            query = "SELECT x FROM GeneralLedger x JOIN x.description o "
            + "WHERE o.id = ?1"),
    @NamedQuery(name = GeneralLedger.READ_BY_ORGUNIT,
            query = "SELECT x FROM GeneralLedger x JOIN x.orgUnit o "
            + "WHERE o.id = ?1 AND o.client.id = ?2"),
    @NamedQuery(name = GeneralLedger.READ_BY_JOURNAL_ENTRY_TYPE,
            query = "SELECT x FROM GeneralLedger x JOIN x.journalEntryType t WHERE "
            + "t.id = ?1 AND t.client.id = ?2")
})
public class GeneralLedger implements Serializable {

    public static final String REMOVE_BY_JOURNAL_ENTRY = "GeneralLedger.RemoveByJournalEntry";
    public static final String READ_BY_ACCOUNT = "GeneralLedger.ReadByAccount";
    public static final String READ_BY_USER = "GeneralLedger.ReadByUser";
    public static final String READ_BY_DESCRIPTION = "GeneralLedger.ReadByDescription";
    public static final String READ_BY_ORGUNIT = "GeneralLedger.ReadByOrgUnit";
    public static final String READ_BY_JOURNAL_ENTRY_TYPE = "GeneralLedger.ReadByJournalEntryType";
    @Id
    @GeneratedValue(generator = "GlavnaKnjigaTab")
    @TableGenerator(name = "GlavnaKnjigaTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "GlavnaKnjiga",
            allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "{GeneralLedger.JournalEntryType.NotNull}")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(
                name = "journal_entry_type_id",
                referencedColumnName = "type_id"),
        @JoinColumn(
                name = "journal_entry_type_company",
                referencedColumnName = "company_id")
    })
    private JournalEntryType journalEntryType;
    @NotNull(message = "{GeneralLedger.JournalEntryNumber.NotNull}")
    @Column(name = "journal_entry_number")
    @DecimalMin(value = "1", message = "{GeneralLedger.JournalEntryNumber.Min}")
    private Integer journalEntryNumber;
    @NotNull(message = "{GeneralLedger.JournalEntryItemOrdinal.NotNull}")
    @DecimalMin(value = "1",
            message = "{GeneralLedger.JournalEntryItemOrdinal.Min}")
    @Column(name = "ordinal")
    private Integer journalEntryItemOrdinalNumber;
    @NotNull(message = "{GeneralLedger.JournalEntryDate.NotNull}")
    @Column(name = "journal_entry_record_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate recordDate;
    @ManyToOne
    @JoinColumn(name = "org_unit_id")
    @NotNull(message = "{GeneralLedger.OrgUnit.NotNull}")
    private OrgUnit orgUnit;
    @NotNull(message = "{GeneralLedger.CreditDebitRelationDate.NotNull}")
    @Column(name = "credit_relation_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate creditDebitRelationDate;
    @Size(max = 35, message = "{GeneralLedger.Document.Size}")
    @Column(name = "document")
    private String document;
    @ManyToOne
    @JoinColumn(name = "desc_id")
    private Description description;
    @NotNull(message = "{GeneralLedger.Account.NotNull}")
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account account;
    @Size(max = 35, message = "{GeneralLedger.InterDocument.Size}")
    @Column(name = "internal_document")
    private String internalDocument;
    @NotNull(message = "{GeneralLedger.ValueDate.NotNull}")
    @Column(name = "value_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate valueDate;
    @NotNull(message = "{GeneralLedger.Determination.NotNull}")
    @Column(name = "determination")
    private AccountDetermination determination;
    @NotNull(message = "{GeneralLedger.ChangeType.NotNull}")
    @Column(name = "change_type")
    private ChangeType changeType;
    @NotNull(message = "{GeneralLedger.Amount.NotNull}")
    @Column(name = "amount")
    @Digits(integer = 15, fraction = 2, message = "{GeneralLedger.Amount.Digits}")
    private BigDecimal amount;
    @NotNull(message = "{GeneralLedger.User.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
    @Version
    @Column(name = "version")
    private Long version;

    //************************************************************************//    
    // CONSTRUCTORS //
    //************************************************************************//
    public GeneralLedger() {
    }

    public GeneralLedger(Long id) {
        this.id = id;
    }

    public GeneralLedger(JournalEntryItem stavka) {
        journalEntryType = stavka.getJournalEntryTypeG();
        journalEntryNumber = stavka.getJournalEntryNumber();
        journalEntryItemOrdinalNumber = stavka.getOrdinalNumber();
        recordDate = stavka.getJournalEntry().getRecordDate();
        orgUnit = stavka.getOrgUnit();
        creditDebitRelationDate = stavka.getCreditDebitRelationDate();
        document = stavka.getDocument();
        description = stavka.getDesc();
        account = stavka.getAccount();
        internalDocument = stavka.getInternalDocument();
        valueDate = stavka.getValueDate();
        changeType = stavka.getChangeType();
        determination = stavka.getDetermination();
        amount = stavka.getAmount();
        user = stavka.getUser();
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

    public Integer getJournalEntryItemOrdinal() {
        return journalEntryItemOrdinalNumber;
    }

    public void setJournalEntryItemOrdinal(Integer ordinal) {
        this.journalEntryItemOrdinalNumber = ordinal;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate d) {
        this.recordDate = d;
    }

    public void setOrgUnit(OrgUnit o) {
        this.orgUnit = o;
    }

    public OrgUnit getOrgUnit() {
        return this.orgUnit;
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

    public Description getDescription() {
        return this.description;
    }

    public void setDescription(Description desc) {
        this.description = desc;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public String getInternalDocument() {
        return internalDocument;
    }

    public void setInternalDocument(String d) {
        this.internalDocument = d;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate d) {
        this.valueDate = d;
    }

    public void setDetermination(AccountDetermination d) {
        this.determination = d;
    }

    public AccountDetermination getDetermination() {
        return this.determination;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

//***************************************************************************//
    public void setAmount(ChangeType type, BigDecimal amount) {
        this.amount = amount;
        this.changeType = type;
    }

    public void setGLSumItem(JournalEntryItem stavka) {
        journalEntryType = stavka.getJournalEntryTypeG();
        journalEntryNumber = stavka.getJournalEntryNumber();
        journalEntryItemOrdinalNumber = stavka.getOrdinalNumber();
        recordDate = stavka.getJournalEntry().getRecordDate();
        orgUnit = stavka.getOrgUnit();
        creditDebitRelationDate = stavka.getCreditDebitRelationDate();
        account = stavka.getAccount();
        changeType = stavka.getChangeType();
        determination = stavka.getDetermination();
        valueDate = stavka.getValueDate();//valuta je jednaka datumu dospeca
        user = stavka.getUser();
        amount = stavka.getAmount();
    }

    public String getAccountNumber() {
        return account.getNumber();
    }

    public Integer getJournalEntryTypeID() {
        return journalEntryType.getId();
    }

    public boolean isDescriptionNull() {
        return (description == null);
    }

    public String getDescriptionName() {
        return description.getName();
    }

    public Integer getOrgUnitID() {
        return orgUnit.getId();
    }

    public Integer getOrgUnitClientID() {
        return orgUnit.getClientID();
    }

    public void addToAmount(BigDecimal va) {
        amount = amount.add(va);
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
        final GeneralLedger other = (GeneralLedger) obj;
        if (this.id != other.id && (this.id == null
                || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "GeneralLedger{" + "id=" + id + '}';
    }
}