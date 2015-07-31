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
import java.util.Objects;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "f_journal_entry_item", schema="devel")
@NamedQueries({
    @NamedQuery(name = JournalEntryItem.COUNT_BY_JOURNAL_ENTRY,
            query = "SELECT COUNT(x) FROM JournalEntryItem x WHERE x.pk.client = ?1 AND x.pk.type = ?2 AND x.pk.number = ?3"),
    @NamedQuery(name = JournalEntryItem.READ_BY_JOURNAL_ENTRY_ORDER_BY_ORDINAL,
            query = "SELECT x FROM JournalEntryItem x  WHERE x.pk.client = ?1 AND x.pk.type = ?2 AND x.pk.number = ?3 ORDER BY x.pk.ordinalNumber"),
    @NamedQuery(name = JournalEntryItem.READ_BY_ACCOUNT,
            query = "SELECT x FROM JournalEntryItem x JOIN x.account k WHERE k.number = ?1"),
    @NamedQuery(name = JournalEntryItem.READ_BY_USER,
            query = "SELECT x FROM JournalEntryItem x JOIN x.user k WHERE k.id = ?1"),
    @NamedQuery(name = JournalEntryItem.READ_BY_DESCRIPTION,
            query = "SELECT x FROM JournalEntryItem x JOIN x.desc o WHERE o.id = ?1"),
    @NamedQuery(name = JournalEntryItem.READ_BY_ORGUNIT,
            query = "SELECT x FROM JournalEntryItem x JOIN x.orgUnit o WHERE o.id = ?1 AND o.client.id = ?2"),
    @NamedQuery(name = JournalEntryItem.READ_JOURNAL_ENTRY_MAX_ITEM_ORDINAL,
            query = "SELECT MAX(x.pk.ordinalNumber) FROM JournalEntryItem x WHERE x.pk.client = ?1 AND x.pk.type = ?2 AND x.pk.number = ?3"),
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
    @EmbeddedId
    @Valid
    private JournalEntryItemPK pk = new JournalEntryItemPK();
    
    @ManyToOne
    @NotNull(message = "{JournalEntryItem.JournalEntry.NotNull}")
    @JoinColumns({
        @JoinColumn(name = "journal_entry_company",//idpreduzeca_naloga
                referencedColumnName = "company_id",
                insertable = false, updatable = false),//idpreduzeca
        @JoinColumn(name = "journal_entry_type",//idtipanaloga
                referencedColumnName = "journal_entry_type_id",
                insertable = false, updatable = false),//idtipanaloga
        @JoinColumn(name = "journal_entry_number",//brojnaloga
                referencedColumnName = "number",
                insertable = false, updatable = false)//brojnaloga
    })
    private JournalEntry journalEntry;
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
    @Column(name = "value_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate valueDate;//greater than datumDPO
    @NotNull(message = "{JournalEntryItem.Determination.NotNull}")
    @Column(name = "determination")
    private AccountDetermination determination;
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
        this.pk.setClient(journalEntry.getClientID());
        this.pk.setType(journalEntry.getType());
        this.pk.setNumber(journalEntry.getNumber());
    }

    public JournalEntryItem(JournalEntry journalEntry, Integer ordinalNumber) {
        this.journalEntry = journalEntry;
        this.pk.setClient(journalEntry.getClientID());
        this.pk.setType(journalEntry.getType());
        this.pk.setNumber(journalEntry.getNumber());
        this.pk.setOrdinalNumber(ordinalNumber);
    }

    //************************************************************************//    
    // GET/SET METHODS //
    //************************************************************************//
    
    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    public Integer getOrdinalNumber() {
        return pk.getOrdinalNumber();
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

    public AccountDetermination getDetermination() {
        return determination;
    }

    public void setDetermination(AccountDetermination determination) {
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
        return pk.getNumber();
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
        return pk.getType();
    }

    public JournalEntryType getJournalEntryTypeG() {
        return journalEntry.getTypeG();
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
        if (determination == AccountDetermination.GENERAL_LEDGER) {
            return true;
        }
        return false;
    }

    public JournalEntryItemDTO getDTO() {
        JournalEntryItemDTO rezultat = new JournalEntryItemDTO();
        rezultat.setClientId(journalEntry.getClientID());
        rezultat.setJournalEntryNumber(journalEntry.getNumber());
        rezultat.setTypeId(journalEntry.getType());
        rezultat.setOrdinalNumber(this.pk.getOrdinalNumber());
        rezultat.setCreditDebitRelationDate(this.creditDebitRelationDate);
        rezultat.setDocument(this.document);
        desc.setJournalEntryItemDTO(rezultat);
        if (orgUnit != null) {
            rezultat.setUnitId(orgUnit.getId());
            rezultat.setUnitName(orgUnit.getName());
        }
        rezultat.setInternalDocument(this.internalDocument);
//        rezultat.note = this.note;
        rezultat.setAccountCode(account.getNumber());
        rezultat.setAccountName(account.getDescription());
        if (partner != null) {
            rezultat.setPartnerCompanyId(partner.getCompanyIdNumber());
            rezultat.setPartnerName( partner.getName() );
        }
        rezultat.setValueDate(this.valueDate);
        switch (changeType) {
            case DEBIT:
                rezultat.setDebit(this.amount);
                rezultat.setCredit(BigDecimal.ZERO);
                break;
            case CREDIT:
                rezultat.setDebit(BigDecimal.ZERO);
                rezultat.setCredit(this.amount);
                break;

        }
        rezultat.setUsername(user.getUsername());
        rezultat.setDetermination(determination);
        return rezultat;
    }

    @Override
    public int compareTo(Object o) {
        JournalEntryItem stavka = (JournalEntryItem) o;
        return this.pk.getOrdinalNumber().compareTo(stavka.pk.getOrdinalNumber());
    }

    //************************************************************************//    
    // OVERRIDEN OBJECT METHODS  //
    //************************************************************************//

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pk);
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
        final JournalEntryItem other = (JournalEntryItem) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JournalEntryItem{" + "pk=" + pk + '}';
    }
    
    
}