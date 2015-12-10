package com.invado.core.domain;

import com.invado.core.domain.BusinessPartner;

import javax.persistence.*;

/**
 * Created by Nikola on 10/11/2015.
 */
@Entity
@Table(name = "CRM_CONNECTION_TYPE")
public class ConnectionType {
    /*TIP KONEKCIJE*/
    @TableGenerator(
            name = "ConnectionTypeTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "ConnectionType",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ConnectionTypeTab")
    @Id
    private Integer id;
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToOne
    @JoinColumn(name = "SERVICE_PROVIDER_ID")
    private BusinessPartner serviceProvider;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BusinessPartner getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(BusinessPartner serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
