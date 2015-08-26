package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.customer.relationship.service.dto.TransactionDTO;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nikola on 19/08/2015.
 */
@Entity
@Table(name = "crm_transaction")
public class Transaction implements Serializable{

    @Id
    private Long id;
    private String statusId;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TransactionType type;
    private Integer amount;
    @ManyToOne
    @JoinColumn(name = "terminal_id")
    private Device terminal;
    /**
     * Point of sale - mesto prodaje, partner podređen poslovnom partneru koji je i pravno lice i sa kojim se sklapa ugovor o prodaji. esot na kojemu se nalazi terminal
     */
    @ManyToOne
    @JoinColumn(name = "business_partner_id")
    private BusinessPartner pointOfSale;
    /**
     * Distrubotor je zapravo klijent, odnosno kompanija korisnik aplikacije
     */
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client distributor;
    /**
     * Service provider je kompanija čija se usluga prodaje, odnoson mobilni provider
     */
    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private BusinessPartner serviceProvider;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Device getTerminal() {
        return terminal;
    }

    public void setTerminal(Device terminal) {
        this.terminal = terminal;
    }

    public BusinessPartner getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(BusinessPartner pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public Client getDistributor() {
        return distributor;
    }

    public void setDistributor(Client distributor) {
        this.distributor = distributor;
    }

    public BusinessPartner getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(BusinessPartner serviceProvider) {
        this.serviceProvider = serviceProvider;
    }


    public TransactionDTO getDTO(){

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(this.getId());
        transactionDTO.setStatusId (this.getStatusId());
        transactionDTO.setTypeId (this.getType().getId());
        transactionDTO.setTypeDescription(this.getType().getDescription());
        transactionDTO.setAmount (this.getAmount());
        transactionDTO.setTerminalId (this.getTerminal().getId());
        transactionDTO.setTerminalCustomCode(this.getTerminal().getCustomCode());
        transactionDTO.setPointOfSaleId(this.getPointOfSale().getId());
        transactionDTO.setPointOfSaleName(this.getPointOfSale().getName());
        transactionDTO.setDistributorId (this.getDistributor().getId());
        transactionDTO.setDistributorName(this.getDistributor().getName());
        transactionDTO.setServiceProviderId (this.getServiceProvider().getId());
        transactionDTO.setServiceProviderName(this.getServiceProvider().getName());

        return transactionDTO;
    }

    /*Overriden*/


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
