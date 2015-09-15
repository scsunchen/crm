package com.invado.customer.relationship.domain;

import com.invado.core.domain.Client;
import org.springframework.cglib.core.Local;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Nikola on 12/09/2015.
 */
@Entity
@Table(name = "CRM_INVOICING_TRANSACTION")
public class InvoincingTransactions {

    @TableGenerator(
            name = "InvTransTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Device",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "InvTransTab")
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "DISTRIBUTOR_ID")
    private Client ditributor;
    @Column(name = "INVOICING_DATE")
    private LocalDate invoicingDate;
    @Column (name = "INVOICED_FROM ")
    private LocalDate invoicedFrom;
    @Column (name = "INVOICED_TO")
    private LocalDate invoicedTo;

}
