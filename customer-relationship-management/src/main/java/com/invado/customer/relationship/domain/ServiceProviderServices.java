/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.domain;

import com.invado.core.domain.Article;
import com.invado.core.domain.BusinessPartner;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bdragan
 */
@Entity
@Table(name = "CRM_SERVICE_PROVIDER_SERVICES")
public class ServiceProviderServices implements Serializable {
    
    @Id
    @TableGenerator(
            name = "ServiceProviderServicesGenerator",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "ServiceProviderServices",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, 
                    generator = "ServiceProviderServicesGenerator")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "BUSINESS_PARTNER_ID")    
    @NotNull(message = "{ServiceProviderServices.ServiceProvider.NotNull}")
    private BusinessPartner serviceProvider;
    @NotBlank(message = "{ServiceProviderServices.Description.NotNull}")
    private String description;
    @Column(name="MANDATORY_ACTIVATION")
    private Boolean mandatoryActivation;
    @NotNull(message = "{ServiceProviderServices.Item.NotNull}")
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")    
    private Article service;
    @Column(name="DATE_FROM")
    private LocalDate dateFrom;
    @Column(name="DATE_TO")
    private LocalDate dateTo;

    public ServiceProviderServices() {
    }

    public ServiceProviderServices(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BusinessPartner getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(BusinessPartner serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMandatoryActivation() {
        return mandatoryActivation;
    }

    public void setMandatoryActivation(Boolean mandatoryActivation) {
        this.mandatoryActivation = mandatoryActivation;
    }

    public Article getService() {
        return service;
    }

    public void setService(Article service) {
        this.service = service;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final ServiceProviderServices other = (ServiceProviderServices) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ServiceProviderServices{" + "description=" + description + '}';
    }
            
}
