/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;


import com.invado.core.domain.Client;
import com.invado.core.domain.LocalDateConverter;
import com.invado.finance.service.dto.JournalEntryDTO;
import com.invado.finance.service.dto.JournalEntryReportDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.ArrayList;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "f_journal_entry", schema="devel")
@NamedQueries({
    @NamedQuery(name = JournalEntry.READ_ALL_ORDER_BY_PRIMARY_KEY,
            query = "SELECT x FROM JournalEntry x ORDER BY x.pk.client, x.pk.type, x.pk.number"),
    @NamedQuery(name = JournalEntry.READ_BY_PRIMARY_KEY,
            query = "SELECT x FROM JournalEntry x WHERE x.pk.client = ?1 AND x.pk.type = ?2 AND x.pk.number = ?3"),
    @NamedQuery(name = JournalEntry.READ_BY_TYPE,
            query = "SELECT x FROM JournalEntry x WHERE x.pk.type = ?1 AND x.pk.client = ?2")
})
public class JournalEntry implements Serializable {

    public static final String READ_ALL_ORDER_BY_PRIMARY_KEY =
            "JournalEntry.ReadAllOrderByPK";
    public static final String READ_BY_PRIMARY_KEY =
            "JournalEntry.ReadByPK";
    public static final String READ_BY_TYPE =
            "JournalEntry.ReadByType";
    
    @EmbeddedId    
    @Valid
    private JournalEntryPK pk = new JournalEntryPK();
    
    //Bug : https://hibernate.atlassian.net/browse/HHH-8333
    //AssertionFailure: Unexpected nested component on the referenced entity when mapping a @MapsId
    //    @NotNull(message = "{Invoice.OrgUnit.NotNull}")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "journal_entry_type_id",
                referencedColumnName = "type_id", 
                insertable = false, updatable = false),
        @JoinColumn(name = "company_id",
                referencedColumnName = "company_id", 
                insertable = false, updatable = false)
    })
    private JournalEntryType typeG;
    @Column(name = "record_date")
    @NotNull(message = "{JournalEntry.Date.NotNull}")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate recordDate;
    @Digits(integer = 18, fraction = 2,
            message = "{JournalEntry.BalanceDebit.Digits}")
    @Column(name = "balance_debit")
    private BigDecimal balanceDebit;
    @Digits(integer = 18, fraction = 2,
            message = "{JournalEntry.BalanceCredit.Digits}")
    @Column(name = "balance_credit")
    private BigDecimal balanceCredit;
    @NotNull(message = "{JournalEntry.IsPosted.NotNull}")
    @Column(name = "is_posted")
    private Boolean posted;
    @OneToMany(
            cascade = {CascadeType.ALL},
            mappedBy="journalEntry",
            fetch = FetchType.LAZY)
    private List<JournalEntryItem> items = new ArrayList<>();
    @Version
    @Column(name = "version")
    private Long version;

    public JournalEntry() {
    }

    public JournalEntry(JournalEntryType type, Integer number) {
        this.pk = new JournalEntryPK(type.getClientID(),type.getId(), number);
    }

    public List<JournalEntryItem> getItems() {
        return items;
    }

    public int getNumberOfItems() {
        return items.size();
    }

    public void addItem(JournalEntryItem item) {
        items.add(item);
    }

    public boolean removeItem(JournalEntryItem item) {
        return items.remove(item);
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean isPosted) {
        this.posted = isPosted;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getNumber() {
        return pk.getNumber();
    }

    public void setNumber(Integer number) {
        this.pk.setNumber(number);
    }

    public JournalEntryType getTypeG() {
        return typeG;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long verzija) {
        this.version = verzija;
    }

    public BigDecimal getBalanceDebit() {
        return balanceDebit;
    }

    public void setBalanceDebit(BigDecimal balanceDebit) {
        this.balanceDebit = balanceDebit;
    }

    public BigDecimal getBalanceCredit() {
        return balanceCredit;
    }

    public void setBalanceCredit(BigDecimal balanceCredit) {
        this.balanceCredit = balanceCredit;
    }

    public Integer getClientID() {
        return pk.getClient();
    }

    public Integer getType() {
        return pk.getType();
    }

    public void setType(Integer type) {
        this.pk.setType(type);
    }
    
    public void setClient(Integer clientId) {
        this.pk.setClient(clientId);
    }
    
    public Client getClient() {
        return typeG.getClient();
    }

    public JournalEntryDTO getReadAllDTO() {
        JournalEntryDTO dto = new JournalEntryDTO();
        dto.setTypeId(typeG.getId());
        dto.setClientId(typeG.getClientID());
        dto.setClientName(typeG.getClientName());
        dto.setTypeNumber(typeG.getNumber());
        dto.setJournalEntryNumber(pk.getNumber());
        dto.setRecordDate(recordDate);
        dto.setTypeName(typeG.getName());
        dto.setBalanceDebit(balanceDebit);
        dto.setBalanceCredit(balanceCredit);
        dto.setBalance(balanceDebit.subtract(balanceCredit));
        dto.setIsPosted(this.posted);
        dto.setVersion(version);
        return dto;
    }

    public JournalEntryReportDTO getPrintJournalEntryDTO(String companyName) {
        JournalEntryReportDTO dto = new JournalEntryReportDTO();
        dto.clientName = companyName;
        dto.typeId = typeG.getId();
        dto.typeName = typeG.getName();
        dto.journalEntryNumber = pk.getNumber();
        dto.date = this.recordDate;
        dto.generalLedgerDebit = BigDecimal.ZERO;
        dto.generalLedgerCredit = BigDecimal.ZERO;
        dto.generalLedgerBalance = BigDecimal.ZERO;
        dto.supplierDebit = BigDecimal.ZERO;
        dto.supplierCredit = BigDecimal.ZERO;
        dto.supplierBalance = BigDecimal.ZERO;
        dto.customerDebit = BigDecimal.ZERO;
        dto.customerCredit = BigDecimal.ZERO;
        dto.customerBalance = BigDecimal.ZERO;
        dto.isPosted = this.posted;
        Collections.sort(items);
        dto.items = new ArrayList<>();
        for (JournalEntryItem stavka : items) {
            dto.items.add(stavka.getDTO());
            switch (stavka.getDetermination()) {
                case SUPPLIERS:
                    addSupplier(stavka, dto);
                    break;
                case CUSTOMERS:
                    addCustomer(stavka, dto);
                    break;
                case GENERAL_LEDGER:
                    addGL(stavka, dto);
                    break;
            }
        }
        dto.totalDebit = dto.supplierDebit.add(dto.customerDebit).add(dto.generalLedgerDebit);
        dto.totalCredit = dto.supplierCredit.add(dto.customerCredit).add(dto.generalLedgerCredit);
        dto.totalBalance = dto.totalDebit.subtract(dto.totalCredit);
        dto.supplierBalance = dto.supplierDebit.subtract(dto.supplierCredit);
        dto.generalLedgerBalance = dto.generalLedgerDebit.subtract(dto.generalLedgerCredit);
        dto.customerBalance = dto.customerDebit.subtract(dto.customerCredit);
        return dto;
    }

    private void addGL(JournalEntryItem stavka, JournalEntryReportDTO dto) {
        switch (stavka.getChangeType()) {
            case DEBIT:
                dto.generalLedgerDebit = dto.generalLedgerDebit.add(stavka.getAmount());
                break;
            case CREDIT:
                dto.generalLedgerCredit = dto.generalLedgerCredit.add(stavka.getAmount());
                break;
        }
    }

    private void addSupplier(JournalEntryItem stavka, JournalEntryReportDTO dto) {
        switch (stavka.getChangeType()) {
            case DEBIT:
                dto.supplierDebit = dto.supplierDebit.add(stavka.getAmount());
                break;
            case CREDIT:
                dto.supplierCredit = dto.supplierCredit.add(stavka.getAmount());
                break;
        }
    }

    private void addCustomer(JournalEntryItem stavka, JournalEntryReportDTO dto) {
        switch (stavka.getChangeType()) {
            case DEBIT:
                dto.customerDebit = dto.customerDebit.add(stavka.getAmount());
                break;
            case CREDIT:
                dto.customerCredit = dto.customerCredit.add(stavka.getAmount());
                break;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final JournalEntry other = (JournalEntry) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JournalEntry{" + "pk=" + pk + '}';
    }
    
    
    
}