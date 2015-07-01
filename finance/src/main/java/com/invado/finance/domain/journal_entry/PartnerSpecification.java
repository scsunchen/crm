/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.OrgUnit;
import com.invado.finance.service.dto.BusinessPartnerSpecificationItemDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "f_partner_specification")
@NamedQueries({
    @NamedQuery(name = PartnerSpecification.READ_BY_ORGUNIT_AND_ACCOUNT_AND_PARTNER,
            query = "SELECT x FROM PartnerSpecification x JOIN x.orgUnit o "
            + "JOIN x.account k JOIN x.partner s WHERE o.client.id = :clientID "
            + "AND o.id = :orgUnitID AND k.number = :accountNumber "
            + "AND s.companyIdNumber = :partnerID"),
    @NamedQuery(name = PartnerSpecification.READ_BY_ACCOUNT,
            query = "SELECT x FROM PartnerSpecification x JOIN x.account o "
                    + "WHERE o.number = ?1"),
    @NamedQuery(name = PartnerSpecification.READ_BY_ORGUNIT,
            query = "SELECT x FROM PartnerSpecification x JOIN x.orgUnit o "
                    + "WHERE o.id = ?1 AND o.client.id = ?2"),
    @NamedQuery(name = PartnerSpecification.READ_BY_BUSINESS_PARTNER,
            query = "SELECT x FROM PartnerSpecification x JOIN x.partner s "
        + "WHERE s.companyIdNumber = ?1")
})
public class PartnerSpecification implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String READ_BY_ORGUNIT_AND_ACCOUNT_AND_PARTNER =
            "Stanje.ReadByOrgUnitAndAccountAndPartner";
    public static final String READ_BY_ACCOUNT =
            "BusinessPartnerSpecification.ReadByAccount";
    public static final String READ_BY_ORGUNIT =
            "BusinessPartnerSpecification.ReadByOrgUnit";
    public static final String READ_BY_BUSINESS_PARTNER =
            "BusinessPartnerSpecification.ReadByBusinessPartner";
    @Id
    @GeneratedValue(generator = "StanjeTab")
    @TableGenerator(name = "StanjeTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Stanje",
            allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "{PartnerSpecification.Determination.NotNull}")
    @Column(name = "determination")
    private Determination determination;
    @NotNull(message = "{PartnerSpecification.OpeningBalanceDebit.NotNull}")
    @Digits(integer = 18, fraction = 2, message = "{PartnerSpecification.OpeningBalanceDebit.Digits}")
    @Column(name = "opening_balance_debit")
    private BigDecimal openingBalanceDebit;
    @NotNull(message = "{PartnerSpecification.OpeningBalanceCredit.NotNull}")
    @Digits(integer = 18, fraction = 2, message = "{PartnerSpecification.OpeningBalanceCredit.Digits}")
    @Column(name = "opening_balance_credit")
    private BigDecimal openingBalanceCredit;
    @NotNull(message = "{PartnerSpecification.CurrentTurnoverDebit.NotNull}")
    @Digits(integer = 18, fraction = 2, message = "{PartnerSpecification.CurrentTurnoverDebit.Digits}")
    @Column(name = "current_turnover_debit")
    private BigDecimal currentTurnoverDebit;
    @NotNull(message = "{PartnerSpecification.CurrentTurnoverCredit.NotNull}")
    @Digits(integer = 18, fraction = 2, message = "{PartnerSpecification.CurrentTurnoverCredit.Digits}")
    @Column(name = "current_turnover_credit")
    private BigDecimal currentTurnoverCredit;
    @Column(name = "days")
    private Integer daysLate;//ne koristi se za sad
    @NotNull(message = "{PartnerSpecification.Account.NotNull}")
    @JoinColumn(name = "account_number")
    @ManyToOne
    private Account account;
    @NotNull(message = "{PartnerSpecification.OrgUnit.NotNull}")
    @JoinColumns({
        @JoinColumn(name = "org_unit_id", referencedColumnName = "org_unit_id"),
        @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    })
    @ManyToOne
    private OrgUnit orgUnit;
    @JoinColumn(name = "business_partner_regnumber")
    @ManyToOne
    private BusinessPartner partner;
    @Version
    @Column(name = "version")
    private Long version;

    public PartnerSpecification() {
    }

    public PartnerSpecification(Long id) {
        this.id = id;
    }

    /**
     * Kreira objekat stanja za prosledjenu stavku knjizenja. Na osnovu
     * vrednosti stavke popunjavaju se organizaciona jed., konto, saradnik i
     * pripadnost. Na osnovu prosledjenog tipa i stavke postavlja se promet i
     * stanje. Ako je identifikator tipa naloga jednak nuli stavka se odnosi na
     * pocetno stanje a za sve ostale vrednosti stavka se odnosi na promet a
     * pocetno stanje je nula.
     *
     */
    public PartnerSpecification(JournalEntryItem stavka) {
        //id i verzija se ne postavljaju
        orgUnit = stavka.getOrgUnit();
        account = stavka.getAccount();
        if (stavka.getPartner() != null) {
            partner = stavka.getPartner();
        }
        determination = stavka.getDetermination();
        //pocetna vrednost stanja i prometa je nula
        currentTurnoverDebit = new BigDecimal(0);
        currentTurnoverCredit = new BigDecimal(0);
        openingBalanceDebit = new BigDecimal(0);
        openingBalanceCredit = new BigDecimal(0);
        daysLate = 0;//    
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public Determination getDetermination() {
        return determination;
    }

    public void setDetermination(Determination determination) {
        this.determination = determination;
    }

    public BigDecimal getCurrentTurnoverDebit() {
        return currentTurnoverDebit;
    }

    public void setCurrentTurnoverDebit(BigDecimal currentTurnoverDebit) {
        this.currentTurnoverDebit = currentTurnoverDebit;
    }

    public BigDecimal getCurrentTurnoverCredit() {
        return currentTurnoverCredit;
    }

    public void setCurrentTurnoverCredit(BigDecimal currentTurnoverCredit) {
        this.currentTurnoverCredit = currentTurnoverCredit;
    }

    public BusinessPartner getPartner() {
        return partner;
    }

    public void setPartner(BusinessPartner partner) {
        this.partner = partner;
    }

    public BigDecimal getOpeningBalanceDebit() {
        return openingBalanceDebit;
    }

    public void setOpeningBalanceDebit(BigDecimal openingBalanceDebit) {
        this.openingBalanceDebit = openingBalanceDebit;
    }

    public BigDecimal getOpeningBalanceCredit() {
        return openingBalanceCredit;
    }

    public void setOpeningBalanceCredit(BigDecimal openingBalanceCredit) {
        this.openingBalanceCredit = openingBalanceCredit;
    }

    public Integer getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(Integer d) {
        this.daysLate = d;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Ispituje identifikator tipa naloga i ako je jednak 98(pocetno stanje)
     * povecava stanje suprotno povecava promet.
     * <b>Metoda se moze pozvati jedino ako se instanca stanja kreira pomocu
     * kontsruktora
     * <code> PartnerSpecification(JournalEntryItem stavka) </code></b>.
     *
     * @param item stavka knjiznog naloga
     * @param journalEntryTypeID tip naloga
     */
    public void add(JournalEntryItem item, Integer journalEntryTypeID) {
        if (journalEntryTypeID == 98) {
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
        //daniKasnjenja   
    }

    public BusinessPartnerSpecificationItemDTO getDTO() {
        BusinessPartnerSpecificationItemDTO dto = new BusinessPartnerSpecificationItemDTO();
        dto.accountCode = account.getNumber();
        dto.unitId = orgUnit.getId();
        if (partner != null) {
            dto.businessPartnerRegNo = partner.getCompanyIdNumber();
            dto.businessPartnerName = partner.getName();
        } else {
            dto.businessPartnerRegNo = "nema";
            dto.businessPartnerName = "nema";
        }
        dto.debit = openingBalanceDebit.add(currentTurnoverDebit);
        dto.credit = openingBalanceCredit.add(currentTurnoverCredit);
        dto.balance = openingBalanceDebit.add(currentTurnoverDebit).subtract(
                openingBalanceCredit.add(currentTurnoverCredit));
        return dto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PartnerSpecification)) {
            return false;
        }
        PartnerSpecification other = (PartnerSpecification) object;
        if ((this.id == null && other.id != null) || (this.id != null
                && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PartnerSpecification{" + "id=" + id + '}';
    }
}