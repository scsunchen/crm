/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

//import com.sproduct.finko.dto.TrialBalanceItemDTO;
import com.invado.finance.service.dto.TrialBalanceItemDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author bdragan
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TrialBalance implements Serializable {

    @Id
    @GeneratedValue(generator = "BrutoStanjeTab")
    @TableGenerator(name = "BrutoStanjeTab",
    table = "id_generator",
    pkColumnName = "idime",
    valueColumnName = "idvrednost",
    pkColumnValue = "BrutoStanje",
    allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "determination")
    @NotNull(message = "{TrialBalance.Determination.NotNull}")
    private Determination determination;
    @NotNull(message = "{TrialBalance.OpeningBalanceDebit.NotNull}")
    @Column(name = "opening_balance_debit")
    @Digits(integer = 18, fraction = 2, message = "{TrialBalance.OpeningBalanceDebit.Digits}")
    private BigDecimal openingBalanceDebit;
    @NotNull(message = "{TrialBalance.OpeningBalanceCredit.NotNull}")
    @Column(name = "opening_balance_credit")
    @Digits(integer = 18, fraction = 2, message = "{TrialBalance.OpeningBalanceCredit.Digits}")
    private BigDecimal openingBalanceCredit;
    @NotNull(message = "{TrialBalance.CurrentTurnoverDebit.NotNull}")
    @Column(name = "current_turnover_debit")
    @Digits(integer = 18, fraction = 2, message = "{TrialBalance.CurrentTurnoverDebit.Digits}")
    private BigDecimal currentTurnoverDebit;
    @Column(name = "current_turnover_credit")
    @NotNull(message = "{TrialBalance.CurrentTurnoverCredit.NotNull}")
    @Digits(integer = 18, fraction = 2, message = "{TrialBalance.CurrentTurnoverCredit.Digits}")
    private BigDecimal currentTurnoverCredit;
    @NotNull(message = "{TrialBalance.Account.NotNull}")
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account account;
    @Version
    @Column(name = "version")
    private Long version;

    
    public TrialBalance() {
    }

    public TrialBalance(Long id) {
        //konstruktor se poziva prilikom testiranja aplikacije
        this.id = id;
    }

    /**
     * Dodaje iznos stavke bruto stanju. Ako je <code>tipNaloga</code> 98 iznos stavke se
     * dodaje na stanje, a za sve drugo iznos se dodaje prometu.
     * @param item stavka knjizenja
     * @param journalEntreyTypeID tip naloga
     */
    public void add(JournalEntryItem item, Integer journalEntreyTypeID) {
        if (journalEntreyTypeID == 98) {
            switch (item.getChangeType()) {
                case DEBIT:
                    openingBalanceDebit = openingBalanceDebit.add(item.getAmount());
                    break;
                case CREDIT:
                    openingBalanceCredit = openingBalanceCredit.add(item.getAmount());
                    break;
            }
            return;
        }
        switch (item.getChangeType()) {
            case DEBIT:
                currentTurnoverDebit = currentTurnoverDebit.add(item.getAmount());
                break;
            case CREDIT:
                currentTurnoverCredit = currentTurnoverCredit.add(item.getAmount());
                break;
        }
    }

    public TrialBalanceItemDTO getDTO() {
        TrialBalanceItemDTO dto = new TrialBalanceItemDTO();
        dto.accountNumber = account.getNumber();
        dto.accountName = account.getDescription();
        dto.openingBalanceDebit = openingBalanceDebit;
        dto.openingBalanceCredit = openingBalanceCredit;
        dto.currentTurnoverDebit = currentTurnoverDebit;
        dto.currentTurnoverCredit = currentTurnoverCredit;
        dto.totalDebit = openingBalanceDebit.add(currentTurnoverDebit);
        dto.totalCredit = openingBalanceCredit.add(currentTurnoverCredit);
        BigDecimal saldo = getBalance();
        if (saldo.compareTo(BigDecimal.ZERO) < 0) {
            dto.balanceCredit = saldo.abs();
        } else {
            dto.balanceDebit = saldo.abs();
        }
        return dto;
    }

    private BigDecimal getBalance() {
        return (openingBalanceDebit.add(currentTurnoverDebit)).subtract(openingBalanceCredit.add(currentTurnoverCredit));
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account a) {
        this.account = a;
    }

    public String getAccountNumber() {
        return account.getNumber();
    }

    public Determination getDetermination() {
        return determination;
    }

    public void setDetermination(Determination d) {
        this.determination = d;
    }
    
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getCurrentTurnoverDebit() {
        return currentTurnoverDebit;
    }

    public void setCurrentTurnoverDebit(BigDecimal prometDuguje) {
        this.currentTurnoverDebit = prometDuguje;
    }

    public BigDecimal getCurrentTurnoverCredit() {
        return currentTurnoverCredit;
    }
    
    public void setCurrentTurnoverCredit(BigDecimal prometPotrazuje) {
        this.currentTurnoverCredit = prometPotrazuje;
    }
    
    public BigDecimal getOpeningBalanceDebit() {
        return openingBalanceDebit;
    }

    public void setOpeningBalanceDebit(BigDecimal stanjeDuguje) {
        this.openingBalanceDebit = stanjeDuguje;
    }

    public void setOpeningBalanceCredit(BigDecimal stanjePotrazuje) {
        this.openingBalanceCredit = stanjePotrazuje;
    }

    public BigDecimal getOpeningBalanceCredit() {
        return openingBalanceCredit;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrialBalance other = (TrialBalance) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "TrialBalance{" + "id=" + id + '}';
    }    
}