/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "r_article", schema = "devel")
@NamedQueries({
@NamedQuery(name=Article.READ_ALL_ORDERBY_CODE, query = "SELECT x FROM Article x ORDER BY x.code"),
@NamedQuery(name=Article.READ_BY_DESCRIPTION_ORDERBY_DESCRIPTION, 
        query = "SELECT x FROM Article x WHERE UPPER(x.description) LIKE :desc ORDER BY x.description")
})
public class Article implements Serializable {

    public static final String READ_ALL_ORDERBY_CODE = "Article.ReadAllOrderByCode";
    public static final String READ_BY_DESCRIPTION_ORDERBY_DESCRIPTION = 
            "Article.ReadByDescriptionOrderByDescription";
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "code")
    @NotBlank(message = "{Article.Code.NotBlank}")
    @Size(max = 13, message = "{Article.Code.Size}")
    private String code;
    @NotBlank(message = "{Article.Description.NotBlank}")
    @Size(max = 34, message = "{Article.Description.Size}")
    @Column(name = "description")
    private String description;
    @NotNull(message = "{Article.VatRate.NotNull}")
    @Column(name = "vat_key")
    private VatPercent VATRate;
    @NotNull(message="{Article.UserDefinedUnitOfMeasure.NotNull}")
    @Column(name = "user_defined_unit_of_measure")
    private Boolean userDefinedUnitOfMeasure;
    @NotBlank(message="{Article.UnitOfMeasure.NotBlank}")
    @Size(max = 3, message="{Article.UnitOfMeasure.Size}")
    @Column(name = "unit_of_measure_code")
    private String unitOfMeasureCode;
    @DecimalMin(value = "0", message = "{Article.SalePriceWithVAT.Min}")
    @Digits(integer=11, fraction=2, message="{Article.SalePriceWithVAT.Digits}")
    @Column(name = "sale_price_with_vat")
    //FIXME : set currency number format 
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.##")
    private BigDecimal salePriceWithVAT;
    @DecimalMin(value = "0", message = "{Article.SalePrice.Min}")
    @Digits(integer=11, fraction=2, message="{Article.SalePrice.Digits}")
    @Column(name = "sale_price")
    //FIXME : set currency number format 
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.##")
    private BigDecimal salePrice;
    @DecimalMin(value = "0", message = "{Article.PurchasePrice.Min}")
    @Digits(integer=11, fraction=2, message="{Article.PurchasePrice.Digits}")
    @Column(name = "purchase_price")
    //FIXME : set currency number format 
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.##")
    private BigDecimal purchasePrice;
    @NotNull(message="{Article.UnitsInStock.NotNull}")
    @Digits(integer=11, fraction=2, message="{Article.UnitsInStock.Digits}")
    @Column(name = "units_in_stock")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private BigDecimal unitsInStock;
    @NotNull(message = "{Article.LastUpdateBy.NotNull}")
    @ManyToOne
    @JoinColumn(name = "last_update_by")
    private ApplicationUser lastUpdateBy;
    @NotNull(message = "{Article.Updated.NotNull}")
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(style = "M-")
    private LocalDate updated;
    @Version
    private Long version;

    public Article() {
    }

    public Article(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VatPercent getVATRate() {
        return VATRate;
    }

    public void setVATRate(VatPercent VATRate) {
        this.VATRate = VATRate;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSalePriceWithVAT() {
        return salePriceWithVAT;
    }

    public void setSalePriceWithVAT(BigDecimal salePriceWithVAT) {
        this.salePriceWithVAT = salePriceWithVAT;
    }

    public BigDecimal getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(BigDecimal unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public String getUnitOfMeasureCode() {
        return unitOfMeasureCode;
    }

    public void setUnitOfMeasureCode(String unitOfMeasureCode) {
        this.unitOfMeasureCode = unitOfMeasureCode;
    }

    public Boolean getUserDefinedUnitOfMeasure() {
        return userDefinedUnitOfMeasure;
    }

    public void setUserDefinedUnitOfMeasure(Boolean userDefinedUnitOfMeasure) {
        this.userDefinedUnitOfMeasure = userDefinedUnitOfMeasure;
    }

    /**
     * Returns the version property that serves as optimistic lock value for
     * this entity.
     *
     * @see javax.persistence.Version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version property that serves as optimistic lock value for this
     * entity.
     *
     * @see javax.persistence.Version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Return application user who last updated this article.
     * @see ApplicationUser
     */
    public ApplicationUser getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**
     * Set application user who last updated this article.
     */
    public void setLastUpdateBy(ApplicationUser updatedBy) {
        this.lastUpdateBy = updatedBy;
    }

    /**
     * Return date of last article data update.
     */
    public LocalDate getUpdated() {
        return updated;
    }

    /**
     * Set date of last article data update.
     */
    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.code != null ? this.code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Article other = (Article) obj;
        if ((this.code == null) ? (other.code != null)
                : !this.code.equals(other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Article{code=" + code + '}';
    }
}