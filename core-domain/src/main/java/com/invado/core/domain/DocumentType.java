package com.invado.core.domain;

import com.invado.core.dto.DocumentTypeDTO;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nikola on 23/12/2015.
 */
@Entity
@Table(name = "c_document_type")
public class DocumentType implements Serializable{
    @TableGenerator(
            name = "DocTypeTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "DocType",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DocTypeTab")
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

    public DocumentTypeDTO getDTO(){

        DocumentTypeDTO dto = new DocumentTypeDTO();

        dto.setId(this.getId());
        dto.setDescription(this.getDescription());

        return dto;
    }


}
