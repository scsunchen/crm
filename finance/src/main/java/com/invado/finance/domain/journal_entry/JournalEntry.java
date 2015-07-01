/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;


import com.invado.core.domain.LocalDateConverter;
import com.invado.finance.service.dto.JournalEntryDTO;
import com.invado.finance.service.dto.JournalEntryReportDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.ArrayList;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "f_journal_entry")
@IdClass(JournalEntryPK.class)
@NamedQueries({
    @NamedQuery(name = JournalEntry.READ_ALL_ORDER_BY_PRIMARY_KEY,
            query = "SELECT x FROM JournalEntry x JOIN x.type t "
                    + "ORDER BY t.client.id, t.id, x.number"),
    @NamedQuery(name = JournalEntry.READ_BY_PRIMARY_KEY,
            query = "SELECT x FROM JournalEntry x WHERE x.type.client.id = ?1 "
                    + "AND x.type.id = ?2 AND x.number = ?3"),
    @NamedQuery(name = JournalEntry.READ_BY_TYPE,
            query = "SELECT x FROM JournalEntry x JOIN x.type t WHERE "
            + "t.id = ?1 AND t.client.id = ?2")
})
public class JournalEntry implements Serializable {

    public static final String READ_ALL_ORDER_BY_PRIMARY_KEY =
            "JournalEntry.ReadAllOrderByPK";
    public static final String READ_BY_PRIMARY_KEY =
            "JournalEntry.ReadByPK";
    public static final String READ_BY_TYPE =
            "JournalEntry.ReadByType";
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "journal_entry_type_id",
                referencedColumnName = "type_id"),
        @JoinColumn(name = "company_id",
                referencedColumnName = "company_id")
    })
    @NotNull(message = "{JournalEntry.Type.NotNull}")
    private JournalEntryType type;
    @Id
    @Column(name = "number")
    @NotNull(message = "{JournalEntry.Number.NotNull}")
    @DecimalMin(value = "1", message = "{JournalEntry.Number.Min}")
    //proverava se u SO da li je veci od broja u tipu naloga 
    private Integer number;
    @Temporal(TemporalType.DATE)
    @Column(name = "record_date")
    @NotNull(message = "{JournalEntry.Date.NotNull}")
    //u so proverava da li je u tekucoj godini poslovanja(Podesavanja.godina)
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
    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "journalEntry",
            fetch = FetchType.LAZY)
    private List<JournalEntryItem> items = new ArrayList<>();
    @Version
    @Column(name = "version")
    private Long version;

    public JournalEntry() {
    }

    public JournalEntry(Integer companyID, Integer typeID, Integer number) {
        this.type = new JournalEntryType(companyID, typeID);
        this.number = number;
    }

    public JournalEntry(JournalEntryType type, Integer number) {
        this.type = type;
        this.number = number;
    }

    public List<JournalEntryItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getNumberOfItems() {
        return items.size();
    }

    public void addItem(JournalEntryItem item) {
        items.add(item);
    }

    public void removeItem(JournalEntryItem item) {
        items.remove(item);
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
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public JournalEntryType getType() {
        return type;
    }

    public void setType(JournalEntryType type) {
        this.type = type;
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
        return type.getClientID();
    }

    public Integer getTypeId() {
        return type.getId();
    }

    public JournalEntryDTO getReadAllDTO() {
        JournalEntryDTO dto = new JournalEntryDTO();
        dto.typeId = type.getId();
        dto.clientId = type.getClientID();
        dto.clientName = type.getClientName();
        dto.typeNumber = type.getNumber();
        dto.journalEntryNumber = number;
        dto.month = recordDate.getMonth();
        dto.day = recordDate.getDayOfMonth();
        dto.typeName = type.getName();
        dto.balanceDebit = balanceDebit;
        dto.balanceCredit = balanceCredit;
        dto.balance = balanceDebit.subtract(balanceCredit);
        dto.isPosted = this.posted;
        dto.version = version;
        return dto;
    }

    public JournalEntryReportDTO getPrintJournalEntryDTO(String companyName) {
        JournalEntryReportDTO dto = new JournalEntryReportDTO();
        dto.clientName = companyName;
        dto.typeId = type.getId();
        dto.typeName = type.getName();
        dto.journalEntryNumber = number;
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

    // <editor-fold defaultstate="collapsed" desc="Object methods implementation">
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JournalEntry other = (JournalEntry) obj;
        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
            return false;
        }
        if (this.number != other.number && (this.number == null || !this.number.equals(other.number))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 43 * hash + (this.number != null ? this.number.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "JournalEntry{" + "type=" + type + ", number=" + number + '}';
    }
    // </editor-fold>
}