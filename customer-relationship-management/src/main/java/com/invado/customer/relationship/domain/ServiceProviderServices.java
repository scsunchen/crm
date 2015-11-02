
package com.invado.customer.relationship.domain;

import com.invado.core.domain.Article;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.LocalDateConverter;
import com.invado.core.domain.LocalDateTimeConverter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;

/**
 * @author bdragan
 */
@Entity
@Table(name = "CRM_SERVICE_PROVIDER_SERVICES")
public class ServiceProviderServices implements Serializable {

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
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "SERVICE_PROVIDER")
    private BusinessPartner serviceProvider;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "MANDATORY_ACTIVATION")
    private Boolean mandatoryActivation;
    @ManyToOne
    @JoinColumn(name = "SERVICE_ID")
    private Article service;
    @Column(name = "DATE_FROM")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dateFrom;
    @Column(name = "DATE_TO")
    @Convert(converter = LocalDateConverter.class)
    private LocalDateTime dateTo;
    @Version
    private Long version;

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

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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