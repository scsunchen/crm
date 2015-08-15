package com.invado.core.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by NikolaB on 6/14/2015.
 */
@Entity
@Table(name = "c_job", schema = "devel")
public class Job implements Serializable {

    @TableGenerator(
            name = "JobTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Job",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "JobTab")
    @Id
    private Integer id;
    @NotNull(message = "{Job.Name.NotNull}")
    @Column(name="name")
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
}
