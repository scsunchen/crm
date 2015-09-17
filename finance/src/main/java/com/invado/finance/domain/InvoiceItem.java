/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.invado.core.domain.Article;
import com.invado.core.domain.VatPercent;
import com.invado.finance.service.dto.InvoiceItemDTO;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author root
 */

@NamedQueries({@NamedQuery(
        name = InvoiceItem.READ_BY_ARTICLE,
        query = "SELECT x FROM InvoiceItem x WHERE x.article.code = :code"),
        @NamedQuery(
                name = InvoiceItem.READ_ITEMS_BY_INVOICE_AND_ARTICLE,
                query = " SELECT x FROM InvoiceItem x " +
                        " WHERE x.invoice.document = :document " +
                        " and x.invoice.orgUnit = :orgUnit " +
                        " and x.invoice.client.id = :clientId " +
                        " and x.article.code = :code ")
})

@Entity
@Table(name = "r_invoice_item", schema = "devel")
@IdClass(InvoiceItemPK.class)


public class InvoiceItem implements Serializable, Comparable<InvoiceItem> {

    public static final String READ_ITEMS_BY_INVOICE_AND_ARTICLE = "InvoiceItem.GetByInvoiceAndArticle";
    public static final String READ_BY_ARTICLE = "InvoiceItem.GetByArticle";
    @Id
    @NotNull(message = "{InvoiceItem.Invoice.NotNull}")
    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "company_id",
                    referencedColumnName = "company_id"),
            @JoinColumn(name = "unit_id",
                    referencedColumnName = "unit_id"),
            @JoinColumn(name = "invoice_document",
                    referencedColumnName = "document")
    })
    private Invoice invoice;
    @Id
    @NotNull(message = "{InvoiceItem.Ordinal.NotNull}")
    @DecimalMin(value = "1", message = "{InvoiceItem.Ordinal.Min}")
    @Column(name = "ordinal")
    private Integer ordinal;
    @ManyToOne
    @JoinColumn(name = "item_code")
    @NotNull(message = "{InvoiceItem.Article.NotNull}")
    private Article article;
    @NotBlank(message = "{InvoiceItem.ItemDescription.NotBlank}")
    @Size(max = 100, message = "{InvoiceItem.ItemDescription.Size}")
    @Column(name = "item_desc")
    private String itemDescription;
    @NotBlank(message = "{InvoiceItem.UnitOfMeasure.NotBlank}")
    @Size(max = 3, message = "{InvoiceItem.UnitOfMeasure.Size}")
    @Column(name = "unit_of_measure")
    private String unitOfMeasure;
    @NotNull(message = "{InvoiceItem.NetPrice.NotNull}")
    @Digits(integer = 9, fraction = 2, message = "{InvoiceItem.NetPrice.Digits}")
    @Column(name = "net_price")
    private BigDecimal netPrice;
    @NotNull(message = "{InvoiceItem.VatPercent.NotNull}")
    @DecimalMin(value = "0", message = "{InvoiceItem.VatPercent.Min}")
    @DecimalMax(value = "1", message = "{InvoiceItem.VatPercent.Max}")
    @Digits(integer = 1, fraction = 4, message = "{InvoiceItem.VatPercent.Digits}")
    @Column(name = "vat_percent")
    private BigDecimal vatPercent;
    @NotNull(message = "{InvoiceItem.VatIdentifier.NotNull}")
    @Column(name = "vat_id")
    private VatPercent articleVAT;
    @NotNull(message = "{InvoiceItem.RabatPercent.NotNull}")
    @DecimalMin(value = "0", message = "{InvoiceItem.RabatPercent.Min}")
    @DecimalMax(value = "1", message = "{InvoiceItem.RabatPercent.Max}")
    @Digits(integer = 1, fraction = 4, message = "{InvoiceItem.RabatPercent.Digits}")
    @Column(name = "rabat_percent")
    private BigDecimal rabatPercent;
    @NotNull(message = "{InvoiceItem.TotalCost.NotNull}")
    @Digits(integer = 13, fraction = 2, message = "{InvoiceItem.TotalCost.Digits}")
    @Column(name = "total_cost")
    private BigDecimal totalCost;
    @NotNull(message = "{InvoiceItem.Quantity.NotNull}")
    @DecimalMin(value = "0", message = "{InvoiceItem.Quantity.Min}")
    @Digits(integer = 7, fraction = 2, message = "{InvoiceItem.Quantity.Digits}")
    @Column(name = "quantity")
    private BigDecimal quantity;

    public InvoiceItem() {
    }

    public InvoiceItem(Invoice invoice, Integer ordinal) {
        this.invoice = invoice;
        this.ordinal = ordinal;
    }

    public String getInvoiceDocument() {
        return invoice.getDocument();
    }

    //
    public Integer getOrgUnitId() {
        return invoice.getOrgUnitId();
    }

    public Integer getClientId() {
        return invoice.getClientId();
    }

//    public String getOrgUnitName() {
//        return invoice.getOrgUnitName();
//    }


    public String getClientName() {
        return invoice.getClientName();
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getItemCode() {
        return article.getCode();
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal price) {
        this.netPrice = price;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal cost) {
        this.totalCost = cost;
    }

    public BigDecimal getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(BigDecimal vatPercent) {
        this.vatPercent = vatPercent;
    }

    public BigDecimal getRebatePercent() {
        return rabatPercent;
    }

    public void setRabatPercent(BigDecimal rabatPercent) {
        this.rabatPercent = rabatPercent;
    }

    public VatPercent getArticleVAT() {
        return articleVAT;
    }

    public void setArticleVAT(VatPercent articleVAT) {
        this.articleVAT = articleVAT;
    }

    public InvoiceItemDTO getInvoiceItemDTO(){

        InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO();

        invoiceItemDTO.setClientId(this.getClientId());
        invoiceItemDTO.setArticleCode(this.getArticle().getCode());
        invoiceItemDTO.setArticleDesc(this.getArticle().getDescription());
        invoiceItemDTO.setClientDesc(this.getClientName());
        invoiceItemDTO.setInvoiceDocument(this.getInvoiceDocument());
        invoiceItemDTO.setNetPrice(this.getNetPrice());
        invoiceItemDTO.setOrdinal(this.getOrdinal());
        invoiceItemDTO.setQuantity(this.getQuantity());
        invoiceItemDTO.setRabatPercent(this.getRebatePercent());
        invoiceItemDTO.setTotalCost(this.getTotalCost());
        invoiceItemDTO.setVATPercent(this.getVatPercent());
        invoiceItemDTO.setUnitId(this.getOrgUnitId());
        return invoiceItemDTO;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InvoiceItem other = (InvoiceItem) obj;
        if (this.invoice != other.invoice && (this.invoice == null
                || !this.invoice.equals(other.invoice))) {
            return false;
        }
        return !(this.ordinal != other.ordinal && (this.ordinal == null
                || !this.ordinal.equals(other.ordinal)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.invoice != null ? this.invoice.hashCode() : 0);
        hash = 37 * hash + (this.ordinal != null ? this.ordinal.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "InvoiceItem{" + "invoice=" + invoice + ", ordinal=" + ordinal + '}';
    }

    @Override
    public int compareTo(InvoiceItem other) {
        if (other.invoice.equals(invoice)) {
            return ordinal.compareTo(other.ordinal);
        } else {
            return invoice.getDocument().compareTo(other.getInvoiceDocument());
        }
    }
}