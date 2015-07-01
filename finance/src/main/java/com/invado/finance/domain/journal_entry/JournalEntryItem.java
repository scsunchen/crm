/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.LocalDateConverter;
import com.invado.core.domain.OrgUnit;
import com.invado.finance.service.dto.JournalEntryItemDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "f_journal_entry_item")
@IdClass(JournalEntryItemPK.class)
@NamedQueries({
    @NamedQuery(name = JournalEntryItem.COUNT_BY_JOURNAL_ENTRY,
            query = "SELECT COUNT(x) FROM JournalEntryItem x JOIN "
            + "x.journalEntry n WHERE n.type.client.id = ?1 AND n.type.id = ?2 "
            + "AND n.number = ?3"),
    @NamedQuery(name = JournalEntryItem.READ_BY_JOURNAL_ENTRY_ORDER_BY_ORDINAL,
            query = "SELECT x FROM JournalEntryItem x JOIN x.journalEntry n "
            + "WHERE n.type.client.id = ?1 AND n.type.id = ?2 AND n.number = ?3 "
            + "ORDER BY x.ordinalNumber"),
    @NamedQuery(name = JournalEntryItem.READ_BY_ACCOUNT,
            query = "SELECT x FROM JournalEntryItem x JOIN x.account k WHERE "
            + "k.number = ?1"),
    @NamedQuery(name = JournalEntryItem.READ_BY_USER,
            query = "SELECT x FROM JournalEntryItem x JOIN x.user k WHERE "
            + "k.id = ?1"),
    @NamedQuery(name = JournalEntryItem.READ_BY_DESCRIPTION,
            query = "SELECT x FROM JournalEntryItem x JOIN x.desc o "
            + "WHERE o.id = ?1"),
    @NamedQuery(name = JournalEntryItem.READ_BY_ORGUNIT,
            query = "SELECT x FROM JournalEntryItem x JOIN x.orgUnit o "
            + "WHERE o.id = ?1 AND o.client.id = ?2"),
    @NamedQuery(name = JournalEntryItem.READ_JOURNAL_ENTRY_MAX_ITEM_ORDINAL,
            query = "SELECT MAX(x.ordinalNumber) FROM JournalEntryItem x "
            + "WHERE x.journalEntry.type.client.id = ?1 AND "
            + "x.journalEntry.type.id = ?2 AND x.journalEntry.number = ?3"),
    @NamedQuery(name = JournalEntryItem.READ_BY_BUSINESS_PARTNER,
            query = "SELECT x FROM JournalEntryItem x JOIN x.partner s "
            + "WHERE s.companyIdNumber = ?1")
})
public class JournalEntryItem implements Serializable, Comparable {

    public static final String COUNT_BY_JOURNAL_ENTRY =
            "JournalEntryItem.CountByJournalEntry";
    public static final String READ_BY_JOURNAL_ENTRY_ORDER_BY_ORDINAL =
            "JournalEntryItem.ReadByJournalEntryOrderByOrdinal";
    public static final String READ_BY_ACCOUNT = "JournalEntryItem.ReadByAccount";
    public static final String READ_BY_USER = "JournalEntryItem.ReadByUser";
    public static final String READ_BY_DESCRIPTION = 
            "JournalEntryItem.ReadByDescription";
    public static final String READ_BY_ORGUNIT = "JournalEntryItem.ReadByOrgUnit";
    public static final String READ_JOURNAL_ENTRY_MAX_ITEM_ORDINAL =
            "JournalEntryItem.ReadJournalEntryMaxOrdinal";
    public static final String READ_BY_BUSINESS_PARTNER =
            "JournalEntryItem.ReadByBusinessPartner";
    @Id
    @ManyToOne
    @NotNull(message = "{JournalEntryItem.JournalEntry.NotNull}")
    @JoinColumns({
        @JoinColumn(name = "journal_entry_company",//idpreduzeca_naloga
                referencedColumnName = "company_id"),//idpreduzeca
        @JoinColumn(name = "journal_entry_type",//idtipanaloga
                referencedColumnName = "journal_entry_type_id"),//idtipanaloga
        @JoinColumn(name = "journal_entry_number",//brojnaloga
                referencedColumnName = "number")//brojnaloga
    })
    private JournalEntry journalEntry;
    @Id
    @Column(name = "ordinal")
    @NotNull(message = "{JournalEntryItem.Ordinal.NotNull}")
    @DecimalMin(value = "1", message = "{JournalEntryItem.Ordinal.Min}")//>=1
    private Integer ordinalNumber;
    @NotNull(message = "{JournalEntryItem.OrgUnit.NotNull}")
    @ManyToOne
    @JoinColumns({
        //idorgjedinice
        @JoinColumn(name = "org_unit_id",
                referencedColumnName = "org_unit_id"),
        @JoinColumn(name = "org_unit_company_id",
                referencedColumnName = "company_id")
    })
    private OrgUnit orgUnit;
    @NotNull(message = "{JournalEntryItem.DebitCreditRelationDate.NotNull}")
    @Temporal(TemporalType.DATE)
    @Column(name = "credit_relation_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate creditDebitRelationDate;
    @NotBlank(message = "{JournalEntryItem.Document.NotBlank}")
    @Size(max = 35, message = "{JournalEntryItem.Document.Size}")
    @Column(name = "document")
    private String document;
    @NotNull(message = "{JournalEntryItem.Desc.NotNull}")
    @ManyToOne
    @JoinColumn(name = "description_id",
            referencedColumnName = "id",
            nullable = false)
    private Description desc;
    @NotNull(message = "{JournalEntryItem.Account.NotNull}")
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "business_partner_regnumber")
    private BusinessPartner partner;
    @Size(max = 35, message = "{JournalEntryItem.InternalDocument.Size}")
    @Column(name = "internal_document")
    private String internalDocument;//15
    @NotNull(message = "{JournalEntryItem.ValueDate.NotNull}")
    @Temporal(TemporalType.DATE)
    @Column(name = "value_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate valueDate;//greater than datumDPO
    @NotNull(message = "{JournalEntryItem.Determination.NotNull}")
    @Column(name = "determination")
    private Determination determination;
    @NotNull(message = "{JournalEntryItem.ChangeType.NotNull}")
    @Column(name = "change_type")
    private ChangeType changeType;
    @Digits(integer = 15, fraction = 2, message = "{JournalEntryItem.Amount.Digits}")
    @NotNull(message = "{JournalEntryItem.Amount.NotNull}")
    @Column(name = "amount")
    // no decimalmin constraint
    private BigDecimal amount;
    @NotNull(message = "{JournalEntryItem.User.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_user_id")
    private ApplicationUser user;

    //************************************************************************//    
    // CONSTRUCTORS //
    //************************************************************************//
    public JournalEntryItem() {
    }

    public JournalEntryItem(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
    }

    public JournalEntryItem(JournalEntry journalEntry, Integer ordinalNumber) {
        this.journalEntry = journalEntry;
        this.ordinalNumber = ordinalNumber;
    }

    public JournalEntryItem(Integer companyID,
            Integer typeID,
            Integer number,
            Integer ordinal) {
        this.journalEntry = new JournalEntry(companyID, typeID, number);
        this.ordinalNumber = ordinal;
    }

    //************************************************************************//    
    // GET/SET METHODS //
    //************************************************************************//
    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgJedinica) {
        this.orgUnit = orgJedinica;
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

    public Description getDesc() {
        return desc;
    }

    public void setDesc(Description desc) {
        this.desc = desc;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account konto) {
        this.account = konto;
    }

    public BusinessPartner getPartner() {
        return partner;
    }

    public void setPartner(BusinessPartner partner) {
        this.partner = partner;
    }

    public String getInternalDocument() {
        return internalDocument;
    }

    public void setInternalDocument(String interDokument) {
        this.internalDocument = interDokument;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valuta) {
        this.valueDate = valuta;
    }

    public Determination getDetermination() {
        return determination;
    }

    public void setDetermination(Determination determination) {
        this.determination = determination;
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
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public void setAmount(ChangeType type, BigDecimal amount) {
        this.changeType = type;
        this.amount = amount;
    }
    //************************************************************************//    
    // DELEGATE METHODS //
    //************************************************************************//

    public String getAccountNumber() {
        return account.getNumber();
    }

    public Integer getJournalEntryNumber() {
        return journalEntry.getNumber();
    }

    public Integer getOrgUnitID() {
        return orgUnit.getId();
    }

    public Integer getOrgUnitClientID() {
        return orgUnit.getClientID();
    }

    public Client getOrgUnitClient() {
        return orgUnit.getClient();
    }

    public Integer getTypeID() {
        return journalEntry.getTypeId();
    }

    public JournalEntryType getJournalEntryType() {
        return journalEntry.getType();
    }

    public String getPartnerID() {
        return partner.getCompanyIdNumber();
    }

    public Integer getDescID() {
        return desc.getId();
    }

//******************************************************************************
    /**
     * Vraca true ako stavke imaju iste sledece atribute :
     *
     * <ul> <li> Troskovno mesto</li> <li> Account </li> <li> Datum doseca
     * i,</li>
     * <li> Tip promene.</li> </ul>
     *
     * @param druga stavka knjizenja
     * @return true/false
     */
    public boolean isEqualForGLSumItem(JournalEntryItem druga) {
        //ista idpreduzeca i idorgjedinice
        if (orgUnit.equals(druga.orgUnit)
                && //isti id konta
                account.equals(druga.account)
                && creditDebitRelationDate.equals(druga.creditDebitRelationDate)
                && changeType.equals(druga.changeType)) {
            return true;
        }
        return false;
    }

    public boolean isEqualForPartnerSpecification(JournalEntryItem druga) {

        if (orgUnit.equals(druga.orgUnit)
                && account.equals(druga.account)
                //ovo poredjenje poslovnih partnera radi jer JPA ima istu vrednost 
                //referencne promenljive za iste partnere 
                //tj. JPA kreira samo jednu instancu!!
                && partner == druga.partner)//saradnik moze biti null tako da se ne poziva metod equals
        {
            return true;
        }
        return false;
    }

    public boolean isGL() {
        if (determination == Determination.GENERAL_LEDGER) {
            return true;
        }
        return false;
    }

    public JournalEntryItemDTO getDTO() {
        JournalEntryItemDTO rezultat = new JournalEntryItemDTO();
        rezultat.clientId = journalEntry.getClientID();
        rezultat.journalEntryNumber = journalEntry.getNumber();
        rezultat.typeId = journalEntry.getTypeId();
        rezultat.ordinalNumber = this.ordinalNumber;
        rezultat.creditDebitRelationDate = this.creditDebitRelationDate;
        rezultat.document = this.document;
        desc.setJournalEntryItemDTO(rezultat);
        if (orgUnit != null) {
            rezultat.unitId = orgUnit.getId();
            rezultat.unitName = orgUnit.getName();
        }
        rezultat.type = this.changeType;
        rezultat.internalDocument = this.internalDocument;
//        rezultat.note = this.note;
        rezultat.accountCode = account.getNumber();
        rezultat.accountName = account.getDescription();
        if (partner != null) {
            rezultat.partnerCompanyId = partner.getCompanyIdNumber();
            rezultat.partnerName = partner.getName();
        }
        rezultat.valueDate = this.valueDate;
        rezultat.amount = this.amount;
        rezultat.username = user.getUsername();
        rezultat.password = user.getPassword();
        rezultat.determination = determination;
        return rezultat;
    }

    public void set(JournalEntryItemDTO dto,
            JournalEntry nalog,
            Description o,
            OrgUnit oj,
            BusinessPartner s,
            Account k,
            ApplicationUser kor) {
        this.journalEntry = nalog;
        this.ordinalNumber = dto.ordinalNumber;
        this.creditDebitRelationDate = dto.creditDebitRelationDate;
        this.document = dto.document;
        this.desc = o;
        this.orgUnit = oj;
        this.changeType = dto.type!= null ? dto.type : null;
        this.internalDocument = dto.internalDocument;
        this.partner = s;
//        this.note = dto.note;
        this.account = k;
        if (k != null) {
            this.determination = k.getDetermination();
        }
        this.valueDate = dto.valueDate;
        this.amount = dto.amount;
        this.user = kor;
    }

    @Override
    public int compareTo(Object o) {
        JournalEntryItem stavka = (JournalEntryItem) o;
        return this.ordinalNumber.compareTo(stavka.ordinalNumber);
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
        final JournalEntryItem other = (JournalEntryItem) obj;
        if (this.journalEntry != other.journalEntry && (this.journalEntry == null || !this.journalEntry.equals(other.journalEntry))) {
            return false;
        }
        if (this.ordinalNumber != other.ordinalNumber && (this.ordinalNumber == null || !this.ordinalNumber.equals(other.ordinalNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.journalEntry != null ? this.journalEntry.hashCode() : 0);
        hash = 83 * hash + (this.ordinalNumber != null ? this.ordinalNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "JournalEntryItem{" + "journalEntry=" + journalEntry
                + ", ordinalNumber=" + ordinalNumber + '}';
    }
}