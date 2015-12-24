package com.invado.core.domain;

import com.invado.core.dto.BusinessPartnerAccountDTO;

import javax.persistence.*;

/**
 * Created by Nikola on 23/12/2015.
 */
@Entity
@Table(name = "c_business_partner_account")
public class BusinessPartnerAccount {


    @TableGenerator(
            name = "AccountTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Account",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AccountTab")
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "ACCOUNT")
    private String account;
    @ManyToOne
    @JoinColumn(name = "BUSINESS_PARTNER_OWNER_ID")
    private BusinessPartner accountOwner;
    @ManyToOne
    @JoinColumn(name = "BANK_ID")
    private BusinessPartner bank;
    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID")
    private Currency currency;
    @Version
    private Long version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BusinessPartner getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(BusinessPartner accountOwner) {
        this.accountOwner = accountOwner;
    }

    public BusinessPartner getBank() {
        return bank;
    }

    public void setBank(BusinessPartner bank) {
        this.bank = bank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BusinessPartnerAccountDTO getDTO() {

        BusinessPartnerAccountDTO dto = new BusinessPartnerAccountDTO();

        dto.setId(this.getId());
        dto.setAccount(this.getAccount());
        dto.setAccountOwner(this.getAccountOwner());
        dto.setAccountOwnerId(this.getAccountOwner().getId());
        dto.setAccountOwnerName(this.getAccountOwner().getName());
        dto.setBank(this.getBank());
        dto.setBankId(this.getBank().getId());
        dto.setBankName(this.getBank().getName());
        dto.setCurrency(this.getCurrency());
        if (this.getCurrency() != null)
            dto.setISOCode(this.getCurrency().getISOCode());

        return dto;
    }
}
