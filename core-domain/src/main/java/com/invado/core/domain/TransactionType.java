package com.invado.core.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nikola on 19/08/2015.
 */
@Entity
@Table(name = "crm_transaction_type")
@NamedQueries({
        @NamedQuery(name = TransactionType.READ_BY_ID,
                query = "SELECT x FROM TransactionType x WHERE id LIKE  :id"),
        @NamedQuery(name = TransactionType.READ_BY_TYPE,
                query = "SELECT x FROM TransactionType x WHERE UPPER(x.type) LIKE  :type"),
        @NamedQuery(name = TransactionType.READ_BY_DESCRIPTION,
                query = "SELECT x FROM TransactionType x WHERE UPPER(x.description) LIKE :name ORDER BY x.description")
})
public class TransactionType implements Serializable {

    public static final String READ_BY_TYPE = "TransactionType.ReadByType";
    public static final String READ_BY_DESCRIPTION = "TransactionType.ReadByDescription";
    public static final String READ_BY_ID = "TransactionType.ReadById";

    @TableGenerator(
            name = "ClientTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Client",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ClientTab")
    @Id
    @Column(name = "id")
    private Integer id;
    private String type;
    private String description;
    private Boolean invoiceable;
    private String invoicingStatuses;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getInvoiceable() {
        return invoiceable;
    }

    public void setInvoiceable(Boolean invoiceable) {
        this.invoiceable = invoiceable;
    }

    public String getInvoicingStatuses() {
        return invoicingStatuses;
    }

    public void setInvoicingStatuses(String invoicingStatuses) {
        this.invoicingStatuses = invoicingStatuses;
    }
}
