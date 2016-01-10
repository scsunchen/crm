package com.invado.core.domain;



import com.invado.core.dto.InvoicingTransactionDTO;
import java.io.Serializable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.format.annotation.DateTimeFormat;

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
@NamedQuery(name = InvoicingTransaction.READ_ALL_ORDERBY_INVOICING_DATE_DESC,
            query = "SELECT x FROM InvoicingTransaction x ORDER BY x.invoicingDate DESC")
public class InvoicingTransaction implements Serializable {

    public static final String LAST_INVOICING_TRANSACTION = "InvoicingTransaction.LastInvoicingTransaction";
    public static final String LAST_TRANSACTION = "InvoicingTransaction.LastTransaction";
    public static final String READ_ALL_ORDERBY_INVOICING_DATE_DESC = "InvoicingTransaction.ReadAllOrderByInvoicingDateDesc";
    
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
    @DateTimeFormat(style = "M-")
    private LocalDate invoicedFrom;
    @Column (name = "INVOICED_TO")
    @DateTimeFormat(style = "M-")
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
    
    public String getFormattedInvoicedFrom() {
        //Spring bug : JSP tag spring:eval does not apply @NumberFormat and @DateTimeFormat 
        //formatting on the expression when using the var attribute
        //https://jira.spring.io/browse/SPR-7509
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(LocaleContextHolder.getLocale())
                .format(invoicedFrom);
        
    }
    
    public String getFormattedInvoicedTo() {
       //Spring bug : JSP tag spring:eval does not apply @NumberFormat and @DateTimeFormat 
        //formatting on the expression when using the var attribute
        //https://jira.spring.io/browse/SPR-7509
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(LocaleContextHolder.getLocale())
                .format(invoicedTo);
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

        DateTimeFormatter format;
        format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        InvoicingTransactionDTO dto = new InvoicingTransactionDTO();
        dto.setDitributorId(this.getDitributor().getId());
        dto.setDistributorName(this.getDitributor().getName());
        dto.setId(this.getId());
        dto.setInvoicedFrom(this.getInvoicedFrom());
        dto.setInvoicedTo(this.getInvoicedTo());
        dto.setInvoicingDate(this.getInvoicingDate());
        dto.setVersion(this.getVersion());
        dto.setDisplayPeriod("Od "+dto.getInvoicedFrom().format(format) +" do "+dto.getInvoicedTo().format(format));
        return dto;
    }
}
