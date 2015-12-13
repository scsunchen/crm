/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.service;

import com.invado.core.domain.*;
import com.invado.core.dto.DeviceDTO;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.SystemException;
import com.invado.customer.relationship.Utils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<BusinessPartner> readPointOfSaleByName(String name) {
        try {
            List<BusinessPartner> list =  dao.createNamedQuery(BusinessPartner.READ_POINT_OF_SALE_BY_NAME_ORDERBY_NAME,
                    BusinessPartner.class)
                    .setParameter("name", ("%"+name+"%").toUpperCase())
                    .getResultList();
            return list;
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadBusinessPartnerByName"),
                    ex
            );
        }
    }


    @Transactional(readOnly = true)
    public BusinessPartner readPointOfSaleById(Integer id) {
        try {
           return  dao.createNamedQuery(BusinessPartner.READ_POINT_OF_SALE_BY_ID,
                    BusinessPartner.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadBusinessPartnerByName"),
                    ex
            );
        }
    }
    @Transactional(readOnly = true)
    public List<BusinessPartner> readServiceProviderByName(String name) {
        try {
            List<BusinessPartner> list =  dao.createNamedQuery(BusinessPartner.READ_SERVICE_PROVIDER_BY_NAME_ORDERBY_NAME,
                    BusinessPartner.class)
                    .setParameter("name", ("%"+name+"%").toUpperCase())
                    .getResultList();
            return list;
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadBusinessPartnerByName"),
                    ex
            );
        }
    }
    
    @Transactional(readOnly = true)
    public List<BusinessPartner> readBusinessPartnerByNameAndType(
            String name, 
            BusinessPartner.Type type) {
        try {
            List<BusinessPartner> list =  dao.createNamedQuery(BusinessPartner.READBY_NAME_TYPE_ORDERBY_NAME,
                    BusinessPartner.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .setParameter("type", type)
                    .getResultList();
            return list;
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadBusinessPartnerByName"),
                    ex
            );
        }
    }

    @Transactional(readOnly = true)
    public BusinessPartner readServiceProviderById(Integer id) {
        try {
           return  dao.createNamedQuery(BusinessPartner.READ_SERVICE_PROVIDER_BY_ID,
                    BusinessPartner.class)
                    .setParameter("id", id)
                    .getSingleResult();
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
    public Client readClientById(Integer id) {
        try {
            return dao.createNamedQuery(Client.READ_BY_ID, Client.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadClientByName"),
                    ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Client> readClientMinSetByName(String name) {
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
    public List<OrgUnit> readOrgUnitByClientAndName(Integer clientId, String name) {
        try {
            return dao.createNamedQuery(OrgUnit.READ_BY_CLIENT_AND_NAME_ORDERBY_NAME, OrgUnit.class)
                    .setParameter("clientId", clientId)
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
                    "MasterDataService.Exception.ReadItem"),
                    ex);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Article> readItemByCode(String code) {
        try {
            return dao.createNamedQuery(
                    Article.READ_BY_CODE_ORDERBY_CODE, 
                    Article.class)
                    .setParameter("code", "%"+code+"%")
                    .getResultList();
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "MasterDataService.Exception.ReadItem"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public DeviceDTO readDevice(Integer id) throws EntityNotFoundException {
        //TODO : check ReadDevicePermission
        //TODO : check ReadDevicePermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("Device.IllegalArgumentEx.Code")
            );
        }
        try {
            Device Device = dao.find(Device.class, id);
            if (Device == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Device.EntityNotFoundEx", id)
                );
            }
            return Device.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Device.PersistenceEx.Read", id),
                    ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Device> readDeviceByCustomCode(String name) {
        try {
            return dao.createNamedQuery(Device.READ_BY_CUSTOM_CODE, Device.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "Device.Exception.ReadByCusotmCode"),
                    ex);
        }
    }
}
