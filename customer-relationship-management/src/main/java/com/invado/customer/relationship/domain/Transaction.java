package com.invado.customer.relationship.domain;

import com.invado.core.domain.*;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Nikola on 19/08/2015.
 */
@Entity
@Table(name = "CRM_TRANSACTION")
@NamedNativeQueries({
        @NamedNativeQuery(name = Transaction.INVOICING_CANDIDATES_PER_POS, query = " select trans.client_id as distributorId, " +
                "      distributor.name as distributorName, trans.business_partner_id as merchantId, merchant.name as merchantName," +
                "      trans.point_Of_Sale_id as posId, pos.name as posName, trans.terminal_id as treminalId, terminal.custom_code as treminalName," +
                "      a.code as service, service.description as serviceDescription, sum(trans.amount) as amount " +
                "         from crm_Transaction_Type type, crm_Transaction trans, c_client distributor, c_business_partner merchant, " +
                "             c_business_partner pos, crm_device terminal, crm_Service_Provider_Services service, r_article a  " +
                "         where type.invoiceable = 1 " +
                "         and type.invoicingStatuses like '%'||trans.statusId||'%' " +
                "         and trans.statusId is not null " +
                "         and trans.invoicing_Status = 0 " +
                "         and trans.response_Time  between :invoicingDateFrom and :invoicingDateTo " +
                "         and (trans.distributor_id = :distributorId or :distributorId is null) " +
                "         and trans.type_id = type.id" +
                "         and trans.terminal_id = terminal.id " +
                "         and trans.client_id = distributor.id" +
                "         and trans.service_Provider_id = service.service_Provider" +
                "         and trans.business_partner_id = merchant.id" +
                "         and trans.point_of_sale_id  =  pos.id " +
                "         and service.service_id = a.code " +
                "         group by trans.client_id, distributor.name, trans.business_partner_id, merchant.name, trans.point_Of_Sale_id, pos.name, " +
                "                    trans.terminal_id, terminal.custom_Code, service.service_id, service.description, a .code " +
                "         order by 1, 2, 3, 4, 5 "),
        @NamedNativeQuery(name = Transaction.COUNT_INVOICING_CANDIDATES_PER_POS, query = " select count(*) from (select 1 " +
                "      service.id as service, service.description as serviceDescription, sum(trans.amount) as amount " +
                "         from crm_Transaction_Type type, crm_Transaction trans, c_client distributor, c_business_partner merchant, " +
                "             c_business_partner pos, crm_device terminal, crm_Service_Provider_Services service " +
                "         where type.invoiceable = 1 " +
                "         and type.invoicingStatuses like '%'||trans.statusId||'%' " +
                "         and trans.statusId is not null " +
                "         and trans.invoicing_Status = 0 " +
                "         and trans.response_Time  between :invoicingDateFrom and :invoicingDateTo " +
                "         and (trans.distributor_id = :distributorId or :distributorId is null) " +
                "         and trans.type_id = type.id" +
                "         and trans.terminal_id = terminal.id " +
                "         and trans.client_id = distributor.id" +
                "         and trans.service_Provider_id = service.service_Provider" +
                "         and trans.business_partner_id = merchant.id" +
                "         and trans.point_of_sale_id  =  pos.id " +
                "         and service.service_id = a.code " +
                "         group by trans.client_id, distributor.name, trans.business_partner_id, merchant.name, trans.point_Of_Sale_id, pos.name, " +
                "                    trans.terminal_id, terminal.custom_Code, service.service_id, service.description, a .code )"),
        @NamedNativeQuery(name = Transaction.INVOICING_SUM_PER_MERCHANT, query = " select trans.client_id as distributorId, " +
                "                     distributor.name as distributorName, trans.business_partner_id as merchantId, merchant.name as merchantName," +
                "                     service.id as service, a.code as articleCode, service.description as serviceDescription, sum(trans.amount) as amount, " +
                "                     pos.id posId, pos.name posName, terminal.id terminalId, terminal.custom_code terminalName, trunc(trans.response_time) responseTime, null as transactionId " +
                "                     from crm_Transaction_Type type, crm_Transaction trans, c_client distributor, c_business_partner merchant, " +
                "                          crm_Service_Provider_Services service, r_article a, c_business_partner pos, crm_device terminal " +
                "                     where type.invoiceable = 1 " +
                "                     and type.invoicingStatuses like '%'||trans.statusId||'%' " +
                "                     and trans.statusId is not null " +
                "                     and trans.invoicing_Status = 0 " +
                "                     and trans.response_Time  between :invoicingDateFrom and :invoicingDateTo " +
                "                     and (trans.client_id = :distributorId or :distributorId = 0) " +
                "                     and trans.type_id = type.id" +
                "                     and trans.client_id = distributor.id" +
                "                     and trans.service_Provider_id = service.service_Provider" +
                "                     and trans.business_partner_id = merchant.id" +
                "                     and trans.point_of_sale_id = pos.id " +
                "                     and service.service_id = a.code " +
                "                     and trans.terminal_id = terminal.id " +
                "                     group by trans.client_id, distributor.name, trans.business_partner_id, merchant.name,service.id, a.code, service.description, null," +
                "                               pos.id, pos.name, terminal.id, terminal.custom_code,trunc(trans.response_time) " +
                "                     order by 1, 3, 14, 5 "),
        @NamedNativeQuery(name = Transaction.COUNT_INVOICING_SUM_PER_MERCHANT, query = " select count(*) " +
                " from (select trans.client_id as distributorId, " +
                "                                     distributor.name as distributorName, trans.business_partner_id as merchantId, merchant.name as merchantName," +
                "                                     service.id as service, a.code as articleCode, service.description as serviceDescription, sum(trans.amount) as amount, " +
                "                                     pos.id posId, pos.name posName, terminal.id terminalId, terminal.custom_code terminalName, trunc(trans.response_time) responseTime, null as transactionId" +
                "                                     from crm_Transaction_Type type, crm_Transaction trans, c_client distributor, c_business_partner merchant, " +
                "                                          crm_Service_Provider_Services service, r_article a, c_business_partner pos, crm_device terminal " +
                "                                     where type.invoiceable = 1 " +
                "                                     and type.invoicingStatuses like '%'||trans.statusId||'%' " +
                "                                     and trans.statusId is not null " +
                "                                     and trans.invoicing_Status = 0 " +
                "                                     and trans.response_Time between :invoicingDateFrom and :invoicingDateTo " +
                "                                     and (trans.client_id = :distributorId or :distributorId = 0) " +
                "                                     and trans.type_id = type.id" +
                "                                     and trans.client_id = distributor.id" +
                "                                     and trans.service_Provider_id = service.service_Provider" +
                "                                     and trans.business_partner_id = merchant.id" +
                "                                     and trans.point_of_sale_id = pos.id " +
                "                                     and service.service_id = a.code " +
                "                                     and trans.terminal_id = terminal.id " +
                "                                     group by trans.client_id, distributor.name, trans.business_partner_id, merchant.name,service.id, a.code, service.description, null," +
                "                                               pos.id, pos.name, terminal.id, terminal.custom_code, trunc(trans.response_time))"),
        @NamedNativeQuery(name = Transaction.INVOICING_CANDIDATES_PER_MERCHANT, query = " select trans.client_id as distributorId, " +
                "      distributor.name as distributorName, trans.business_partner_id as merchantId, merchant.name as merchantName," +
                "      service.id as service, a.code as articleCode, service.description as serviceDescription, trans.amount as amount, null posId, null posName," +
                "      null terminalId, null terminalName, response_time reponseTime, trans.id as transactionId " +
                "         from crm_Transaction_Type type, crm_Transaction trans, c_client distributor, c_business_partner merchant, " +
                "             crm_Service_Provider_Services service, r_article a " +
                "         where type.invoiceable = 1 " +
                "         and type.invoicingStatuses like '%'||trans.statusId||'%' " +
                "         and trans.statusId is not null " +
                "         and trans.invoicing_Status = 0 " +
                "         and trans.response_Time between :invoicingDateFrom and :invoicingDateTo  " +
                "         and (trans.client_id = :distributorId or :distributorId = 0) " +
                "         and trans.type_id = type.id" +
                "         and trans.client_id = distributor.id" +
                "         and trans.service_Provider_id = service.service_Provider" +
                "         and trans.business_partner_id = merchant.id" +
                "         and service.service_id = a.code" +
                "         order by trans.business_partner_id "),
        @NamedNativeQuery(name = Transaction.COUNT_INVOICING_CANDIDATES_PER_MERCHANT, query = " select count(*) from (select 1 " +
                "         from crm_Transaction_Type type, crm_Transaction trans, c_client distributor, c_business_partner merchant, " +
                "             crm_Service_Provider_Services service, r_article a " +
                "         where type.invoiceable = 1 " +
                "         and type.invoicingStatuses like '%'||trans.statusId||'%' " +
                "         and trans.statusId is not null " +
                "         and trans.invoicing_Status = 0 " +
                "         and trans.response_Time  between :invoicingDateFrom and :invoicingDateTo " +
                "         and (trans.client_id = :distributorId or :distributorId = 0) " +
                "         and trans.type_id = type.id" +
                "         and trans.client_id = distributor.id" +
                "         and trans.service_Provider_id = service.service_Provider" +
                "         and trans.business_partner_id = merchant.id" +
                "         and service.service_id = a.code) ")

})

public class Transaction implements Serializable {

    public static final String INVOICING_CANDIDATES_PER_POS = "Transaction.ReadInvoicingCandidatesTransactionsPerPOS";
    public static final String COUNT_INVOICING_CANDIDATES_PER_POS = "Transaction.CountInvoincingCandidatesSetPerPOS";
    public static final String INVOICING_SUM_PER_MERCHANT = "Transaction.ReadInvoicingSumTransactionsPerMerchant";
    public static final String COUNT_INVOICING_SUM_PER_MERCHANT = "Transaction.CountInvoincingSumSetPerMerchant";
    public static final String INVOICING_CANDIDATES_PER_MERCHANT = "Transaction.ReadInvoicingCandidatesTransactionsPerMerchant";
    public static final String COUNT_INVOICING_CANDIDATES_PER_MERCHANT = "Transaction.CountInvoincingCandidatesSetPerMerchant";
    @Id
    private Long id;
    @Column(name = "TRANSACTION_STATUS")
    private String statusId;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TransactionType type;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "terminal_id")
    private Device terminal;
    /**
     * Point of sale - mesto prodaje, partner podređen poslovnom partneru koji je i pravno lice i sa kojim se sklapa ugovor o prodaji. esot na kojemu se nalazi terminal
     */
    @ManyToOne
    @JoinColumn(name = "point_of_sale_id")
    private BusinessPartner pointOfSale;
    /**
     * Distrubotor je zapravo klijent, odnosno kompanija korisnik aplikacije
     */
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client distributor;
    /**
     * Merchant je prodavac
     */
    @ManyToOne
    @JoinColumn(name = "business_partner_id")
    private BusinessPartner merchant;
    /**
     * Service provider je kompanija čija se usluga prodaje, odnoson mobilni provider
     */
    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private BusinessPartner serviceProvider;

    @Column(name = "request_time")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime requestTime;

    @Column(name = "response_time")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime responseTime;

    @Column(name = "invoicing_status")
    private Boolean invoicingStatus;


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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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


    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public Boolean isInvoicingStatus() {
        return invoicingStatus;
    }

    public void setInvoicingStatus(Boolean invoicingStatus) {
        this.invoicingStatus = invoicingStatus;
    }

    public BusinessPartner getMerchant() {
        return merchant;
    }

    public void setMerchant(BusinessPartner businessPartner) {
        this.merchant = businessPartner;
    }

    public Boolean getInvoicingStatus() {
        return invoicingStatus;
    }

    public TransactionDTO getDTO() {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(this.getId());
        transactionDTO.setStatusId(this.getStatusId());
        transactionDTO.setTypeId(this.getType().getId());
        transactionDTO.setTypeDescription(this.getType().getDescription());
        transactionDTO.setAmount(this.getAmount());
        transactionDTO.setTerminalId(this.getTerminal().getId());
        transactionDTO.setTerminalCustomCode(this.getTerminal().getCustomCode());
        transactionDTO.setPointOfSaleId(this.getPointOfSale().getId());
        transactionDTO.setPointOfSaleName(this.getPointOfSale().getName());
        transactionDTO.setDistributorId(this.getDistributor().getId());
        transactionDTO.setDistributorName(this.getDistributor().getName());
        transactionDTO.setMerchantId(this.getMerchant().getId());
        transactionDTO.setMerchantName(this.getMerchant().getName());
        transactionDTO.setServiceProviderId(this.getServiceProvider().getId());
        transactionDTO.setServiceProviderName(this.getServiceProvider().getName());
        transactionDTO.setRequestTime(this.getRequestTime());
        transactionDTO.setResponseTime(this.getResponseTime());
        transactionDTO.setInvoicingStatus(this.isInvoicingStatus());

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
