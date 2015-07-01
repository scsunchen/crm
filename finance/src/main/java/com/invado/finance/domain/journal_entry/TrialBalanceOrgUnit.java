/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.OrgUnit;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "f_trial_balance_orgunit")
@NamedQueries({
    @NamedQuery(name = TrialBalanceOrgUnit.READ_BY_ORGUNIT_AND_ACCOUNT,
            query = "SELECT x FROM TrialBalanceOrgUnit x JOIN x.orgUnit o "
            + "JOIN x.account k WHERE x.orgUnit.client.id= :clientID "
            + "AND x.orgUnit.id = :orgUnitID AND k.number = :accountNumber"),
    @NamedQuery(name = TrialBalanceOrgUnit.READ_BY_ORGUNIT_ORDER_BY_ACCOUNT,
            query = "SELECT x FROM TrialBalanceOrgUnit x JOIN x.orgUnit o "
            + "JOIN x.account k WHERE o.id = ?2 AND o.client.id = ?1 "
            + "ORDER BY k.number"),
    @NamedQuery(name = TrialBalanceOrgUnit.READ_BY_ACCOUNT,
            query = "SELECT x FROM TrialBalanceOrgUnit x JOIN x.account k "
            + "WHERE k.number = ?1"),
    @NamedQuery(name = TrialBalanceOrgUnit.READ_BY_ORGUNIT,
            query = "SELECT x FROM TrialBalanceOrgUnit x JOIN x.orgUnit o "
            + "WHERE o.id = ?1 AND o.client.id = ?2")
})
public class TrialBalanceOrgUnit extends TrialBalance implements Serializable {

    public static final String READ_BY_ORGUNIT_AND_ACCOUNT =
            "OrgUnitTrialBalance.ReadByOrgUnitAndAccount";
    public static final String READ_BY_ORGUNIT_ORDER_BY_ACCOUNT =
            "OrgUnitTrialBalance.ReadByOrgUnitOrderByAccount";
    public static final String READ_BY_ORGUNIT = "OrgUnitTrialBalance.ReadByOrgUnit";
    public static final String READ_BY_ACCOUNT = "OrgUnitTrialBalance.ReadByAccount";
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "org_unit_id", referencedColumnName = "org_unit_id"),
        @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    })
    private OrgUnit orgUnit;

    public TrialBalanceOrgUnit() {
    }

    public TrialBalanceOrgUnit(Long id) {
        //konstruktor se poziva prilikom testiranja aplikacije
        super(id);
    }

    public TrialBalanceOrgUnit(JournalEntryItem item) {
        setAccount(item.getAccount());
        orgUnit = item.getOrgUnit();
        setDetermination(item.getDetermination());
        setCurrentTurnoverDebit(BigDecimal.ZERO);
        setCurrentTurnoverCredit(BigDecimal.ZERO);
        setOpeningBalanceDebit(BigDecimal.ZERO);
        setOpeningBalanceCredit(BigDecimal.ZERO);
//        postaviDaneKasnjenja(0);
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgJedinica) {
        this.orgUnit = orgJedinica;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
