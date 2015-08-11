/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.hr.service;

import com.invado.core.domain.*;
import com.invado.core.exception.SystemException;
import com.invado.hr.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bdragan
 */
@Service
public class MasterDataService {
    
    private static final Logger LOG = Logger.getLogger(
            MasterDataService.class.getName());
    
    @PersistenceContext(name = "unit")
    private EntityManager dao;   
    
    @Transactional(readOnly = true)
    public List<BusinessPartner> readBusinessPartnerByName(String name) {
        try {
            return dao.createNamedQuery(BusinessPartner.READ_BY_NAME_ORDERBY_NAME,
                                        BusinessPartner.class)
                    .setParameter("name", ("%"+name+"%").toUpperCase())
                    .getResultList();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadBusinessPartnerByName"),
                    ex
            );
        }
    }
    
    @Transactional(readOnly = true)
    public List<Client> readClientByName(String name) {
        try {
            return dao.createNamedQuery(Client.READ_BY_NAME_ORDERBY_NAME, Client.class)
                    .setParameter("name", ("%"+name+"%").toUpperCase())
                    .getResultList();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadClientByName"),
                    ex);
        }
    }
    
    @Transactional(readOnly = true)
    public List<OrgUnit> readOrgUnitByName(String name) {
        try {
            return dao.createNamedQuery(OrgUnit.READ_BY_NAME_ORDERBY_NAME, OrgUnit.class)
                    .setParameter("name", ("%"+name+"%").toUpperCase())
                    .getResultList();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadOrgUnitByName"),
                    ex);
        }
    }
    
    @Transactional(readOnly = true)
    public List<BankCreditor> readBankByName(String name) {
        try {
            return dao.createNamedQuery(BankCreditor.READ_BY_NAME_ORDERBY_NAME, BankCreditor.class)
                    .setParameter("name", ("%"+name+"%").toUpperCase())
                    .getResultList();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadBankByName"),
                    ex);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Currency> readCurrencyByISO(String iso) {
        try {
            return dao.createNamedQuery(Currency.READ_BY_ISOCODE_ORDERBY_ISOCODE, Currency.class)
                    .setParameter("iso", ("%"+iso+"%").toUpperCase())
                    .getResultList();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadCurrencyByISO"),
                    ex);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Article> readItemByDescription(String desc) {
        try {
            return dao.createNamedQuery(
                    Article.READ_BY_DESCRIPTION_ORDERBY_DESCRIPTION, 
                    Article.class)
                    .setParameter("desc", ("%"+desc+"%").toUpperCase())
                    .getResultList();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadItemByDescription"),
                    ex);
        }
    }
}
