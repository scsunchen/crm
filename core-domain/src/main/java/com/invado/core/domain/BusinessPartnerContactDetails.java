package com.invado.core.domain;


import com.invado.core.dto.BusinessPartnerContactDetailsDTO;
import com.invado.core.dto.BusinessPartnerDTO;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Nikola
 * @version 1.0
 * @created 18-Oct-2015 18:13:41
 */
@Entity
@Table(name = "c_business_partner_contacts", schema = "devel")
public class BusinessPartnerContactDetails {


    @TableGenerator(
            name = "PartnerContactsTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "PartnerContact",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PartnerContactsTab")
    @Id
    private int id;
    @Embedded
    private ContactPerson contactPerson;
    @Embedded
    private Address address;
    @Column(name = "date_from")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate dateFrom;
    @Column(name = "date_to")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate dateTo;
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    public BusinessPartner merchant;
    @ManyToOne
    @JoinColumn(name = "pos_id")
    public BusinessPartner pointOfSale;
    @Version
    @Column(name = "version")
    private Long version;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public BusinessPartner getMerchant() {
        return merchant;
    }

    public void setMerchant(BusinessPartner merchant) {
        this.merchant = merchant;
    }

    public BusinessPartner getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(BusinessPartner pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BusinessPartnerContactDetailsDTO getDTO() {
        BusinessPartnerContactDetailsDTO businessPartnerContactDetailsDTO = new BusinessPartnerContactDetailsDTO();

        businessPartnerContactDetailsDTO.setId(this.getId());
        businessPartnerContactDetailsDTO.setDateTo(this.dateTo);
        businessPartnerContactDetailsDTO.setDateFrom(this.dateFrom);
        if (this.address != null) {
            businessPartnerContactDetailsDTO.setCountry(this.address.getCountry());
            businessPartnerContactDetailsDTO.setPlace(this.address.getPlace());
            businessPartnerContactDetailsDTO.setPostCode(this.address.getPostCode());
            businessPartnerContactDetailsDTO.setStreet(this.address.getStreet());
        }
        if (this.contactPerson != null) {
            businessPartnerContactDetailsDTO.setName(this.contactPerson.getName());
            businessPartnerContactDetailsDTO.setPhone(this.contactPerson.getPhone());
            businessPartnerContactDetailsDTO.setEmail(this.contactPerson.getEmail());
            businessPartnerContactDetailsDTO.setFunction(this.contactPerson.getFunction());
        }
        if (this.merchant != null) {
            businessPartnerContactDetailsDTO.setMerchantId(this.getMerchant().getId());
            businessPartnerContactDetailsDTO.setMerchantName(this.getMerchant().getName());
        }
        if (this.pointOfSale != null) {
            businessPartnerContactDetailsDTO.setPointOfSaleId(this.getPointOfSale().getId());
            businessPartnerContactDetailsDTO.setPointOfSaleName(this.getPointOfSale().getName());
        }
        businessPartnerContactDetailsDTO.setVersion(this.getVersion());

        return businessPartnerContactDetailsDTO;

    }
}