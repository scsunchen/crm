package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.DeviceServiceProviderRegistrationDTO;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.IllegalArgumentException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.PageNotExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.*;
import javax.validation.Validator;

import javax.persistence.*;
import javax.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Created by Nikola on 01/01/2016.
 */
@Service
public class DeviceServiceProviderRegistrationService {


    private static final Logger LOG = Logger.getLogger(
            DeviceServiceProviderRegistration.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Inject
    private Validator validator;


    @Transactional(rollbackFor = Exception.class)
    public DeviceServiceProviderRegistration create(DeviceServiceProviderRegistrationDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateDeviceServiceProviderRegistrationPermission

        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentEx"));
        }
        if (a.getServiceProviderId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentException.Partner"));
        }
        if (a.getDeviceId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentException.Device"));
        }
        try {
            DeviceServiceProviderRegistration serviceProviderReg = new DeviceServiceProviderRegistration();
            serviceProviderReg.setDevice(dao.find(Device.class, a.getDeviceId()));
            serviceProviderReg.setServiceProvider(dao.find(BusinessPartner.class, a.getServiceProviderId()));
            serviceProviderReg.setICCID(a.getICCID());
            serviceProviderReg.setActivationDate(a.getActivationDate());
            serviceProviderReg.setTransactionLimit(a.getTransactionLimit());
            if (a.getConnectionTypeId() != null)
                serviceProviderReg.setConnectionType(dao.find(ConnectionType.class, a.getConnectionTypeId()));
            serviceProviderReg.setLimitPerDay(a.getLimitPerDay());
            serviceProviderReg.setLimitPerMonth(a.getLimitPerMonth());
            serviceProviderReg.setLimitPerDay(a.getLimitPerDay());
            serviceProviderReg.setMSISDN(a.getMSISDN());
            serviceProviderReg.setWorkingStartTime(a.getWorkingStartTime());
            serviceProviderReg.setWorkingEndTime(a.getWorkingEndTime());
            if (a.getRefillTypeId() != null)
                serviceProviderReg.setRefillType(dao.find(PrepaidRefillType.class, a.getRefillTypeId()));
            if (a.getDeviceStatusId() != null)
                serviceProviderReg.setDeviceStatus(dao.find(DeviceStatus.class, a.getDeviceStatusId()));
            serviceProviderReg.setRegistration(a.getRegistrationId());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(serviceProviderReg);
            return serviceProviderReg;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceServiceProviderRegistration.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public DeviceServiceProviderRegistration update(DeviceServiceProviderRegistrationDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateDeviceServiceProviderRegistrationPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentEx.Id"));
        }
        if (dto.getServiceProviderId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentException.Partner"));
        }
        if (dto.getDeviceId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentException.Device"));
        }
        try {
            DeviceServiceProviderRegistration serviceProviderReg = dao.find(DeviceServiceProviderRegistration.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (serviceProviderReg == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("DeviceServiceProviderRegistration.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(serviceProviderReg, LockModeType.OPTIMISTIC);

            serviceProviderReg.setDevice(dao.find(Device.class, dto.getDeviceId()));
            serviceProviderReg.setServiceProvider(dao.find(BusinessPartner.class, dto.getServiceProviderId()));
            serviceProviderReg.setICCID(dto.getICCID());
            serviceProviderReg.setActivationDate(dto.getActivationDate());
            serviceProviderReg.setTransactionLimit(dto.getTransactionLimit());
            if (dto.getConnectionTypeId() != null)
                serviceProviderReg.setConnectionType(dao.find(ConnectionType.class, dto.getConnectionTypeId()));
            serviceProviderReg.setLimitPerDay(dto.getLimitPerDay());
            serviceProviderReg.setLimitPerMonth(dto.getLimitPerMonth());
            serviceProviderReg.setLimitPerDay(dto.getLimitPerDay());
            serviceProviderReg.setMSISDN(dto.getMSISDN());
            serviceProviderReg.setWorkingStartTime(dto.getWorkingStartTime());
            serviceProviderReg.setWorkingEndTime(dto.getWorkingEndTime());
            if (dto.getRefillTypeId() != null)
                serviceProviderReg.setRefillType(dao.find(PrepaidRefillType.class, dto.getRefillTypeId()));
            if (dto.getDeviceStatusId() != null)
                serviceProviderReg.setDeviceStatus(dao.find(DeviceStatus.class, dto.getDeviceStatusId()));
            serviceProviderReg.setRegistration(dto.getRegistrationId());

            List<String> msgs = validator.validate(serviceProviderReg).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }

            dao.flush();
            return serviceProviderReg;
        } catch (ConstraintViolationException | EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("DeviceServiceProviderRegistration.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("DeviceServiceProviderRegistration.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteDeviceServiceProviderRegistrationPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentEx.Code")
            );
        }
        try {
            DeviceServiceProviderRegistration service = dao.find(DeviceServiceProviderRegistration.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceServiceProviderRegistration.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public DeviceServiceProviderRegistrationDTO read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadDeviceServiceProviderRegistrationPermission
        //TODO : check ReadDeviceServiceProviderRegistrationPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("DeviceServiceProviderRegistration.IllegalArgumentEx.Code")
            );
        }
        try {
            DeviceServiceProviderRegistration DeviceServiceProviderRegistration = dao.find(DeviceServiceProviderRegistration.class, id);
            if (DeviceServiceProviderRegistration == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("DeviceServiceProviderRegistration.EntityNotFoundEx", id)
                );
            }
            return DeviceServiceProviderRegistration.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceServiceProviderRegistration.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<DeviceServiceProviderRegistrationDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadDeviceServiceProviderRegistrationPermission
        Integer id = null;
        Integer deviceId = null;
        Integer partnerId = null;
        String customCode = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("deviceId") && s.getValue() instanceof Integer) {
                deviceId = (Integer) s.getValue();
            }
            if (s.getKey().equals("partnerId") && s.getValue() instanceof Integer) {
                partnerId = (Integer) s.getValue();
            }
            if (s.getKey().equals("customCode") && s.getValue() instanceof String) {
                customCode = (String) s.getValue();
            }

        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao,
                    id,
                    deviceId,
                    partnerId,
                    customCode);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("DeviceServiceProviderRegistration.PageNotExists", pageNumber));
            }
            ReadRangeDTO<DeviceServiceProviderRegistrationDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first DeviceServiceProviderRegistration = last page number * DeviceServiceProviderRegistrations per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(convertToDTO(this.search(dao,
                        id,
                        deviceId,
                        partnerId,
                        customCode,
                        start,
                        pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao,
                        id,
                        deviceId,
                        partnerId,
                        customCode,
                        p.getPage() * pageSize,
                        pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceServiceProviderRegistration.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<DeviceServiceProviderRegistrationDTO> convertToDTO(List<DeviceServiceProviderRegistration> lista) {
        List<DeviceServiceProviderRegistrationDTO> listaDTO = new ArrayList<>();
        for (DeviceServiceProviderRegistration pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            Integer deviceId,
            Integer partnerId,
            String customCode) {
/*CriteriaQuery<Pet> cq = cb.createQuery(Pet.class);

Root<Pet> pet = cq.from(Pet.class);
Join<Pet, Owner> owner = pet.join(Pet_.owners);*/

        BusinessPartner partner = null;
        Device device = null;
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<DeviceServiceProviderRegistration> root = c.from(DeviceServiceProviderRegistration.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();

        if (id != null) {
            criteria.add(cb.equal(root.get(DeviceServiceProviderRegistration_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (partnerId != null) {
            partner = dao.find(BusinessPartner.class, partnerId);
            criteria.add(cb.equal(root.get(DeviceServiceProviderRegistration_.serviceProvider),
                    cb.parameter(BusinessPartner.class, "serviceProvider")));
        }
        if (deviceId != null) {
            device = dao.find(Device.class, deviceId);
            criteria.add(cb.equal(root.get(DeviceServiceProviderRegistration_.device),
                    cb.parameter(Device.class, "device")));
        }
        if (customCode != null && !customCode.isEmpty()) {
            Join<DeviceServiceProviderRegistration, Device> deviceQ = root.join(DeviceServiceProviderRegistration_.device);
            criteria.add(cb.like(deviceQ.get(Device_.customCode),
                    cb.parameter(String.class, "customCode")));
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);

        if (id != null) {
            q.setParameter("id", id);
        }
        if (partnerId != null) {
            q.setParameter("serviceProvider", partner);
        }
        if (deviceId != null) {
            q.setParameter("device", device);
        }
        if (customCode != null && !customCode.isEmpty()) {
            q.setParameter("customCode", "%" + customCode + "%");
        }

        return q.getSingleResult();
    }

    private List<DeviceServiceProviderRegistration> search(EntityManager em,
                                                           Integer id,
                                                           Integer deviceId,
                                                           Integer partnerId,
                                                           String customCode,
                                                           int first,
                                                           int pageSize) {

        BusinessPartner partner = null;
        Device device = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DeviceServiceProviderRegistration> query = cb.createQuery(DeviceServiceProviderRegistration.class);
        Root<DeviceServiceProviderRegistration> root = query.from(DeviceServiceProviderRegistration.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();

        if (id != null) {
            criteria.add(cb.equal(root.get(DeviceServiceProviderRegistration_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (partnerId != null) {
            partner = dao.find(BusinessPartner.class, partnerId);
            criteria.add(cb.equal(root.get(DeviceServiceProviderRegistration_.serviceProvider),
                    cb.parameter(BusinessPartner.class, "serviceProvider")));
        }
        if (deviceId != null) {
            device = dao.find(Device.class, deviceId);
            criteria.add(cb.equal(root.get(DeviceServiceProviderRegistration_.device),
                    cb.parameter(Device.class, "device")));
        }
        if (customCode != null && !customCode.isEmpty()) {
            Join<DeviceServiceProviderRegistration, Device> deviceQ = root.join(DeviceServiceProviderRegistration_.device);
            criteria.add(cb.like(deviceQ.get(Device_.customCode),
                    cb.parameter(String.class, "customCode")));
        }
        query.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<DeviceServiceProviderRegistration> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (partnerId != null) {
            typedQuery.setParameter("serviceProvider", partner);
        }
        if (deviceId != null) {
            typedQuery.setParameter("device", device);
        }
        if (customCode != null && !customCode.isEmpty()) {
            typedQuery.setParameter("customCode", "%" + customCode + "%");
        }
        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<DeviceServiceProviderRegistrationDTO> readAll(Integer id,
                                                              Integer partnerId,
                                                              Integer deviceid,
                                                              String customCode) {
        try {
            return convertToDTO(this.search(dao, id, partnerId, deviceid, customCode, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("DeviceServiceProviderRegistration.PersistenceEx.ReadAll"), ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<PrepaidRefillType> getRefillTypes() {

        Query query = dao.createQuery("SELECT e FROM PrepaidRefillType e");
        return (List<PrepaidRefillType>) query.getResultList();
    }


    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<ConnectionType> getConnectionTypes() {

        Query query = dao.createQuery("SELECT e FROM ConnectionType e");
        return (List<ConnectionType>) query.getResultList();
    }
}
