package com.invado.core.domain;



import com.invado.core.dto.InvoicingTransactionDTO;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Nikola on 12/09/2015.
 */
@Entity
@Table(name = "CRM_INVOICING_TRANSACTION")


@SqlResultSetMapping(name = "invoicingTransactions", entities = {@EntityResult(entityClass = InvoicingTransaction.class,
        fields = {
                @FieldResult(name = "id", column = "ID"),
                @FieldResult(name = "invoicingDate", column = "INVOICING_DATE"),
                @FieldResult(name = "invoicedFrom", column = "INVOICED_FROM"),
                @FieldResult(name = "invoicedTo", column = "INVOICED_TO")})})
@NamedNativeQueries({
        @NamedNativeQuery(name = InvoicingTransaction.LAST_INVOICING_TRANSACTION, query = " select *  " +
                "from (select * from crm_invoicing_transaction order by 3) where rownum=1",
                resultSetMapping = "invoicingTransactions"),
        @NamedNativeQuery(name = InvoicingTransaction.LAST_TRANSACTION, query = " select *  " +
                                " from (select * from crm_invoicing_transaction order by 3 desc) where rownum=1" ,
                resultClass = InvoicingTransaction.class)})


public class InvoicingTransaction {

    public static final String LAST_INVOICING_TRANSACTION = "InvoicingTransaction.LastInvoicingTransaction";
    public static final String LAST_TRANSACTION = "InvoicingTransaction.LastTransaction";

    @TableGenerator(
            name = "InvTransTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "InvPeriod",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "InvTransTab")
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "DISTRIBUTOR_ID")
    private Client ditributor;
    @Column(name = "INVOICING_DATE")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate invoicingDate;
    @Column (name = "INVOICED_FROM")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate invoicedFrom;
    @Column (name = "INVOICED_TO")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate invoicedTo;
    @Version
    private Long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getDitributor() {
        return ditributor;
    }

    public void setDitributor(Client ditributor) {
        this.ditributor = ditributor;
    }

    public LocalDate getInvoicingDate() {
        return invoicingDate;
    }

    public void setInvoicingDate(LocalDate invoicingDate) {
        this.invoicingDate = invoicingDate;
    }

    public LocalDate getInvoicedFrom() {
        return invoicedFrom;
    }

    public void setInvoicedFrom(LocalDate invoicedFrom) {
        this.invoicedFrom = invoicedFrom;
    }

    public LocalDate getInvoicedTo() {
        return invoicedTo;
    }

    public void setInvoicedTo(LocalDate invoicedTo) {
        this.invoicedTo = invoicedTo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public InvoicingTransactionDTO getDTO(){

        InvoicingTransactionDTO dto = new InvoicingTransactionDTO();
        dto.setDitributorId(this.getDitributor().getId());
        dto.setDistributorName(this.getDitributor().getName());
        dto.setId(this.getId());
        dto.setInvoicedFrom(this.getInvoicedFrom());
        dto.setInvoicedTo(this.getInvoicedTo());
        dto.setInvoicingDate(this.getInvoicingDate());
        dto.setVersion(this.getVersion());
        dto.setDisplayPeriod(this.getInvoicedFrom()+" - "+this.getInvoicedTo());
        return dto;
    }
}
