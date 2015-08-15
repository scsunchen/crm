/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "c_application_setup", schema = "devel")
public class ApplicationSetup implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "year")
    private Integer year;
    @Size(max = 100)
    //User can't insert
    @Column(name = "company_name")
    private String accountingAgencyName;
    @Column(name = "app_version")
    private ApplicationVersion applicationVersion;
    @Column(name = "application_user")
    private ApplicationUser applicationUser;
    @Column(name = "page_size") 
    private Integer pageSize;
    
    //************************************************************************//    
                           // CONSTRUCTORS //
    //************************************************************************//
    
    public ApplicationSetup() {}

    public ApplicationSetup(Integer id) {
        this.id = id;
    }
    
    //************************************************************************//    
                           // GET/SET METHODS //
    //************************************************************************//
    
    public Integer getId() {
        return id;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getAccountingAgencyName() {
        return accountingAgencyName;
    }    

    public void setAccountingAgencyName(String accountingAgencyName) {
        this.accountingAgencyName = accountingAgencyName;
    }
    
    public ApplicationVersion getApplicationVersion() {
        return applicationVersion;
    }
    
    public void setApplicationVersion(ApplicationVersion v) {
        this.applicationVersion = v;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    //************************************************************************//    
                           // OVERRIDEN OBJECT METHODS  //
    //************************************************************************//
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final ApplicationSetup other = (ApplicationSetup) obj;
        return !(this.id != other.id && (this.id == null || !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "ApplicationSetup{" + "id=" + id + '}';
    }
    
    public enum ApplicationVersion implements Serializable {

        ACCOUNTING_AGENCY,
        COMPANY_ACCOUNTING
    }
    
    public enum ApplicationUser implements Serializable {

        PRVI, NULTI
        //UNDEFINED;
    }

}