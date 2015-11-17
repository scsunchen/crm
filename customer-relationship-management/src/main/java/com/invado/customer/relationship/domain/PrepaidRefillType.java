package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;

import javax.persistence.*;

/**
 * Created by Nikola on 10/11/2015.
 */
@Entity
@Table(name = "CRM_REFILL_TYPE")
public class PrepaidRefillType {
    /*TIP DOPUNE*/

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
    @ManyToOne
    @JoinColumn(name = "SERVICE_PROVIDER_ID")
    private BusinessPartner serviceProvider;

}
