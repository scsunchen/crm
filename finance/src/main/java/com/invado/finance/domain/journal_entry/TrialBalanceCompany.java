/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.Client;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "f_trial_balance_company")
@NamedQueries({
    @NamedQuery(name = TrialBalanceCompany.READ_BY_COMPANY_AND_ACCOUNT,
            query = "SELECT x FROM TrialBalanceCompany x JOIN x.client p "
                    + "JOIN x.account k WHERE p.id = :clientID AND "
                    + "k.number = :accountNumber"),
    @NamedQuery(name = TrialBalanceCompany.READ_BY_COMPANY_ORDER_BY_ACCOUNT,
            query = "SELECT x FROM TrialBalanceCompany x JOIN x.account k WHERE "
                    + "x.client.id = ?1 ORDER BY k.number"),
    @NamedQuery(name = TrialBalanceCompany.READ_BY_ACCOUNT,
            query = "SELECT x FROM TrialBalanceCompany x JOIN x.account k WHERE "
        + "k.number = ?1"),
    @NamedQuery(name = TrialBalanceCompany.READ_BY_COMPANY, 
        query="SELECT x FROM TrialBalanceCompany x JOIN x.client p WHERE p.id=?1")

})
public class TrialBalanceCompany extends TrialBalance implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String READ_BY_COMPANY_AND_ACCOUNT =
            "CompanyTrialBalance.ReadByCompanyAndAccount";
    public static final String READ_BY_COMPANY_ORDER_BY_ACCOUNT =
            "CompanyTrialBalance.ReadByCompanyOrderByAccount";
    public static final String READ_BY_ACCOUNT = "CompanyTrialBalance.ReadByAccount";
    public static final String READ_BY_COMPANY="CompanyTrialBalance.ReadByCompany";    
    
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Client client;

    public TrialBalanceCompany() {
    }

    public TrialBalanceCompany(Long id) {
        super(id);
    }

    public TrialBalanceCompany(JournalEntryItem item) {
        setAccount(item.getAccount());
        client = item.getOrgUnitClient();
        setDetermination(item.getDetermination());
        setCurrentTurnoverDebit(BigDecimal.ZERO);
        setCurrentTurnoverCredit(BigDecimal.ZERO);
        setOpeningBalanceDebit(BigDecimal.ZERO);
        setOpeningBalanceCredit(BigDecimal.ZERO);
//        postaviDaneKasnjenja(0);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client c) {
        this.client = c;
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
