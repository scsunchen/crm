package com.invado.core.dto;

/**
 * Created by Nikola on 10/11/2015.
 */
public class POSTypeDTO {

    private Integer id;
    private String description;
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
}