/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Article;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.SystemException;
import com.invado.customer.relationship.Utils;
import com.invado.customer.relationship.domain.ServiceProviderServices;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.exception.ArticleNotFoundException;
import com.invado.customer.relationship.service.exception.BusinessPartnerIsNotServiceProviderException;
import com.invado.customer.relationship.service.exception.BusinessPartnerNotFoundException;
import com.invado.customer.relationship.service.exception.ServiceProviderServicesConstraintViolationException;
import com.invado.customer.relationship.service.exception.ServiceProviderServicesIsNotUniqueException;
import com.invado.customer.relationship.service.exception.ServiceProviderServicesNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author bdragan
 */
@Service
public class ServiceProviderService {

    private static final Logger LOG = Logger.getLogger(ServiceProviderService.class.getName());

    @PersistenceContext(name = "unit")
    private EntityManager entityManager;
    @Inject
    private Validator validator;

    @Transactional(rollbackFor = Exception.class)
    public void create(ServiceProviderServices services)
            throws ServiceProviderServicesConstraintViolationException,
            ServiceProviderServicesIsNotUniqueException,
            ArticleNotFoundException,
            BusinessPartnerNotFoundException,
            BusinessPartnerIsNotServiceProviderException {
        try {
            if(services.getService() != null && services.getService().getCode() != null
                    && services.getService().getCode().isEmpty() == false) {
                Article service = entityManager.find(Article.class,
                        services.getService().getCode());
                if (service == null) {
                    throw new ArticleNotFoundException(Messages.ARTICLE_NOT_FOUND.get());
                }
                services.setService(service);                
            }else {
                services.setService(null);
            }
            if(services.getServiceProvider() != null && services.getServiceProvider().getId()!= null) {
                BusinessPartner serviceProvider = entityManager.find(
                        BusinessPartner.class,
                        services.getServiceProvider().getId());
                if (serviceProvider == null) {
                    throw new BusinessPartnerNotFoundException(
                            Messages.BUSINESS_PARTNER_NOT_FOUND.get()
                    );
                }
                if (serviceProvider.getType() != BusinessPartner.Type.SERVICE_PROVIDER) {
                    throw new BusinessPartnerIsNotServiceProviderException(
                            Messages.BUSINESS_PARTNER_IS_NOT_SERVICE_PROVIDER.get()
                    );
                }
                services.setServiceProvider(serviceProvider);                
            }else {
                services.setServiceProvider(null);
            }
            List<String> messages = validator.validate(services).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (messages.isEmpty() == false) {
                throw new ServiceProviderServicesConstraintViolationException(
                        Messages.SERVICE_PROVIDER_SERVICES_CONSTRAINT_VIOLATION.get(),
                        messages
                );
            }
            if(entityManager.createNamedQuery(
                    ServiceProviderServices.READ_BY_BUSINESSPARTNER_SERVICE,
                    ServiceProviderServices.class)
                    .setParameter("service", services.getService())
                    .setParameter("provider", services.getServiceProvider())
                    .getResultList()
                    .isEmpty() == false) {
                throw new ServiceProviderServicesIsNotUniqueException(
                        Messages.SERVICE_PROVIDER_SERVICES_NOT_UNIQUE.get(
                                services.getServiceProvider().getName(),
                                services.getService().getDescription()
                                )
                );
            }
            entityManager.persist(services);
        } catch (ServiceProviderServicesConstraintViolationException 
                | ServiceProviderServicesIsNotUniqueException
                | ArticleNotFoundException 
                | BusinessPartnerNotFoundException 
                | BusinessPartnerIsNotServiceProviderException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Messages.CREATE_SYSTEM_EXCEPTION.get(), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ServiceProviderServices services)
            throws ServiceProviderServicesConstraintViolationException,
//            ServiceProviderServicesIsNotUniqueException,
            ServiceProviderServicesNotFoundException,
            ArticleNotFoundException,
            BusinessPartnerNotFoundException,
            BusinessPartnerIsNotServiceProviderException {
        try {
            ServiceProviderServices servicesFromDB = entityManager.find(
                    ServiceProviderServices.class,
                    services.getId());
            if (servicesFromDB == null) {
                throw new ServiceProviderServicesNotFoundException(
                        Messages.SERVICE_PROVIDER_SERVICES_NOT_FOUND.get()
                );
            }
            if(services.getService() != null && services.getService().getCode() != null
                    && services.getService().getCode().isEmpty() == false) {
                Article service = entityManager.find(Article.class,
                        services.getService().getCode());
                if (service == null) {
                    throw new ArticleNotFoundException(Messages.ARTICLE_NOT_FOUND.get());
                }
                services.setService(service);                
            }else {
                services.setService(null);
            }
            if(services.getServiceProvider() != null && services.getServiceProvider().getId()!= null) {
                BusinessPartner serviceProvider = entityManager.find(
                        BusinessPartner.class,
                        services.getServiceProvider().getId());
                if (serviceProvider == null) {
                    throw new BusinessPartnerNotFoundException(
                            Messages.BUSINESS_PARTNER_NOT_FOUND.get()
                    );
                }
                if (serviceProvider.getType() != BusinessPartner.Type.SERVICE_PROVIDER) {
                    throw new BusinessPartnerIsNotServiceProviderException(
                            Messages.BUSINESS_PARTNER_IS_NOT_SERVICE_PROVIDER.get()
                    );
                }
                services.setServiceProvider(serviceProvider);                
            }else {
                services.setServiceProvider(null);
            }
            servicesFromDB.setDateFrom(services.getDateFrom());
            servicesFromDB.setDateTo(services.getDateTo());
            servicesFromDB.setDescription(services.getDescription());
            servicesFromDB.setMandatoryActivation(services.getMandatoryActivation());
            List<String> messages = validator.validate(servicesFromDB).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (messages.isEmpty() == false) {
                throw new ServiceProviderServicesConstraintViolationException(
                        Messages.SERVICE_PROVIDER_SERVICES_CONSTRAINT_VIOLATION.get(),
                        messages
                );
            }
            if(services.getVersion().compareTo(servicesFromDB.getVersion()) != 0) {
                throw new OptimisticLockException();
            }
        } catch (ServiceProviderServicesConstraintViolationException 
//                | ServiceProviderServicesIsNotUniqueException
                | ArticleNotFoundException 
                | BusinessPartnerNotFoundException 
                | BusinessPartnerIsNotServiceProviderException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(Messages.OPTIMISTIC_LOCK.get(),ex);
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(Messages.UPDATE_SYSTEM_EXCEPTION.get(), ex);
            }
        }
    }

    @Transactional
    public void delete(Integer id) {
        try {
            ServiceProviderServices services = entityManager.find(
                    ServiceProviderServices.class,
                    id);
            if (services != null) {
                entityManager.remove(services);
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Messages.DELETE_SYSTEM_EXCEPTION.get(), ex);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ServiceProviderServices read(Integer id)
            throws EntityNotFoundException {
        try {
            ServiceProviderServices result = entityManager.find(
                    ServiceProviderServices.class,
                    id);
            if (result == null) {
                throw new EntityNotFoundException(
                        Messages.SERVICE_PROVIDER_SERVICES_NOT_FOUND.get()
                );
            }
            return result;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Messages.READ_SYSTEM_EXCEPTION.get(), ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<ServiceProviderServices> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        try {
            Integer pageSize = entityManager.find(ApplicationSetup.class, 1)
                    .getPageSize();
            Long countEntities = entityManager.createNamedQuery(
                    ServiceProviderServices.COUNT_ALL,
                    Long.class).getSingleResult();
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                throw new PageNotExistsException(Messages.PAGE_NOT_FOUND.get(p.getPage()));
            }
            ReadRangeDTO<ServiceProviderServices> result = new ReadRangeDTO<>();
            //if page number is -1 read last page
            if (pageNumber.equals(-1)) {
                int start = numberOfPages.intValue() * pageSize;
                List<ServiceProviderServices> data = entityManager.createNamedQuery(
                        ServiceProviderServices.READ_ALL_ORDERBY_SERVICE_PROVIDER_DESCRIPTION,
                        ServiceProviderServices.class)
                        .setFirstResult(start)
                        .setMaxResults(pageSize)
                        .getResultList();
                result.setData(data);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<ServiceProviderServices> data = entityManager.createNamedQuery(
                        ServiceProviderServices.READ_ALL_ORDERBY_SERVICE_PROVIDER_DESCRIPTION,
                        ServiceProviderServices.class)
                        .setFirstResult(pageNumber * pageSize)
                        .setMaxResults(pageSize)
                        .getResultList();
                result.setData(data);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Messages.READ_SYSTEM_EXCEPTION.get(),ex);
        } 
    }
    
    
    
    enum Messages {

        OPTIMISTIC_LOCK("ServiceProviderServices.OptimisticLock"),
        ARTICLE_NOT_FOUND("ServiceProviderServices.ArticleNotFound"),
        SERVICE_PROVIDER_SERVICES_CONSTRAINT_VIOLATION(
            "ServiceProviderServices.ServiceProviderServicesConstraintViolation"),
        BUSINESS_PARTNER_NOT_FOUND("ServiceProviderServices.BusinessPartnerNotFound"),
        BUSINESS_PARTNER_IS_NOT_SERVICE_PROVIDER(
            "ServiceProviderServices.BusinessPartnerIsNotServiceProvider"),
        SERVICE_PROVIDER_SERVICES_NOT_UNIQUE("ServiceProviderServices.ServiceProviderServicesNotUnique"),
        SERVICE_PROVIDER_SERVICES_NOT_FOUND(
            "ServiceProviderServices.ServiceProviderServicesNotFound"),
        PAGE_NOT_FOUND("ServiceProviderServices.PageNotFound"),
        CREATE_SYSTEM_EXCEPTION("ServiceProviderServices.CreateSystemException"),
        UPDATE_SYSTEM_EXCEPTION("ServiceProviderServices.UpdateSystemException"),
        DELETE_SYSTEM_EXCEPTION("ServiceProviderServices.DeleteSystemException"),
        READ_SYSTEM_EXCEPTION("ServiceProviderServices.ReadSystemException");
        
        private String key;

        Messages(String key) {
            this.key = key;
        }

        String get() {
            return Utils.getMessage(key);
        }
        
        String get(Object... args) {
            return Utils.getMessage(key, args);
        }
    }
    
}
