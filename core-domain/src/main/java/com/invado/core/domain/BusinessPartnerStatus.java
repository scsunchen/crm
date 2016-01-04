package com.invado.core.domain;

import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.core.dto.BusinessPartnerStatusDTO;

import javax.persistence.*;

/**
 * Created by Nikola on 04/01/2016.
 */
@Entity
public class BusinessPartnerStatus {

    @TableGenerator(
            name = "PartnerStatusTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "PartnerStatus",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PartnerStatusTab")
    @Id
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Version
    private Long version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BusinessPartnerStatusDTO getDTO(){

        BusinessPartnerStatusDTO dto = new BusinessPartnerStatusDTO();

        dto.setId(this.getId());
        dto.setDescription(this.getDescription());
        dto.setName(this.getName());
        dto.setVersion(this.getVersion());

        return dto;
    }

    @Override
    public String toString() {
        return "BusinessPartnerStatus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                '}';
    }
}
