package com.invado.core.domain;


import com.invado.core.dto.POSTypeDTO;

import javax.persistence.*;

/**
 * Created by Nikola on 10/11/2015.
 */
@Entity
@Table(name = "CRM_POS_TYPE")
public class POSType {
/*TIp prodajnog objekta*/

    @TableGenerator(
            name = "RefillTypeTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "RefillType",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RefillTypeTab")
    @Id
    private Integer id;
    @Column(name = "DESCRIPTION")
    private String description;
    @Version
    private Long version;

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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public POSTypeDTO getDTO(){

        POSTypeDTO posTypeDTO = new POSTypeDTO();

        posTypeDTO.setId(this.id);
        posTypeDTO.setDescription(this.getDescription());
        posTypeDTO.setVersion(this.version);

        return posTypeDTO;
    }
}
