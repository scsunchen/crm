package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.DeviceHolderPartnerDTO;
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
 * Created by Nikola on 06/12/2015.
 */
@Service
public class DeviceHolderPartnerService {

    private static final Logger LOG = Logger.getLogger(
            DeviceHolderPartner.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Inject
    private Validator validator;


    @Transactional(rollbackFor = Exception.class)
    public DeviceHolderPartner create(DeviceHolderPartnerDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateDeviceHolderPartnerPermission

        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentEx"));
        }
        if (a.getBusinessPartnerId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentException.Partner"));
        }
        if (a.getDeviceId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentException.Device"));
        }
        try {
            DeviceHolderPartner deviceHolderPartner = new DeviceHolderPartner();
            deviceHolderPartner.setDevice(dao.find(Device.class, a.getDeviceId()));
            deviceHolderPartner.setBusinessPartner(dao.find(BusinessPartner.class, a.getBusinessPartnerId()));
            deviceHolderPartner.setStartDate(a.getStartDate());
            deviceHolderPartner.setEndDate(a.getEndDate());
            deviceHolderPartner.setICCID(a.getICCID());
            deviceHolderPartner.setTelekomId(a.getTelekomId());
            deviceHolderPartner.setActivationDate(a.getActivationDate());
            deviceHolderPartner.setTransactionLimit(a.getTransactionLimit());
            deviceHolderPartner.setConnectionType(dao.find(ConnectionType.class, a.getConnectionTypeId()));
            deviceHolderPartner.setLimitPerDay(a.getLimitPerDay());
            deviceHolderPartner.setLimitPerMonth(a.getLimitPerMonth());
            deviceHolderPartner.setLimitPerDay(a.getLimitPerDay());
            deviceHolderPartner.setMSISDN(a.getMSISDN());
            deviceHolderPartner.setRefillType(dao.find(PrepaidRefillType.class, a.getRefillTypeId()));


            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(deviceHolderPartner);
            return deviceHolderPartner;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceHolderPartner.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public DeviceHolderPartner update(DeviceHolderPartnerDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateDeviceHolderPartnerPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentEx.Id"));
        }
        if (dto.getBusinessPartnerId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentException.Partner"));
        }
        if (dto.getDeviceId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentException.Device"));
        }
        try {
            DeviceHolderPartner item = dao.find(DeviceHolderPartner.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("DeviceHolderPartner.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);


            DeviceHolderPartner deviceHolderPartner = new DeviceHolderPartner();
            deviceHolderPartner.setDevice(dao.find(Device.class, dto.getDeviceId()));
            deviceHolderPartner.setBusinessPartner(dao.find(BusinessPartner.class, dto.getBusinessPartnerId()));
            deviceHolderPartner.setStartDate(dto.getStartDate());
            deviceHolderPartner.setEndDate(dto.getEndDate());
            deviceHolderPartner.setICCID(dto.getICCID());
            deviceHolderPartner.setTelekomId(dto.getTelekomId());
            deviceHolderPartner.setActivationDate(dto.getActivationDate());
            deviceHolderPartner.setTransactionLimit(dto.getTransactionLimit());
            deviceHolderPartner.setConnectionType(dao.find(ConnectionType.class, dto.getConnectionTypeId()));
            deviceHolderPartner.setLimitPerDay(dto.getLimitPerDay());
            deviceHolderPartner.setLimitPerMonth(dto.getLimitPerMonth());
            deviceHolderPartner.setLimitPerDay(dto.getLimitPerDay());
            deviceHolderPartner.setMSISDN(dto.getMSISDN());
            deviceHolderPartner.setRefillType(dao.find(PrepaidRefillType.class, dto.getRefillTypeId()));

            List<String> msgs = validator.validate(item).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            dao.flush();
            return item;
        } catch (ConstraintViolationException | EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        Utils.getMessage("DeviceHolderPartner.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("DeviceHolderPartner.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteDeviceHolderPartnerPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentEx.Code")
            );
        }
        try {
            DeviceHolderPartner service = dao.find(DeviceHolderPartner.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceHolderPartner.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public DeviceHolderPartnerDTO read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadDeviceHolderPartnerPermission
        //TODO : check ReadDeviceHolderPartnerPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("DeviceHolderPartner.IllegalArgumentEx.Code")
            );
        }
        try {
            DeviceHolderPartner DeviceHolderPartner = dao.find(DeviceHolderPartner.class, id);
            if (DeviceHolderPartner == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("DeviceHolderPartner.EntityNotFoundEx", id)
                );
            }
            return DeviceHolderPartner.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceHolderPartner.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<DeviceHolderPartnerDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadDeviceHolderPartnerPermission
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
                        Utils.getMessage("DeviceHolderPartner.PageNotExists", pageNumber));
            }
            ReadRangeDTO<DeviceHolderPartnerDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first DeviceHolderPartner = last page number * DeviceHolderPartners per page
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
                    Utils.getMessage("DeviceHolderPartner.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<DeviceHolderPartnerDTO> convertToDTO(List<DeviceHolderPartner> lista) {
        List<DeviceHolderPartnerDTO> listaDTO = new ArrayList<>();
        for (DeviceHolderPartner pr : lista) {
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
        Root<DeviceHolderPartner> root = c.from(DeviceHolderPartner.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();

        if (id != null) {
            criteria.add(cb.equal(root.get(DeviceHolderPartner_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (partnerId != null) {
            partner = dao.find(BusinessPartner.class, partnerId);
            criteria.add(cb.equal(root.get(DeviceHolderPartner_.businessPartner),
                    cb.parameter(BusinessPartner.class, "partner")));
        }
        if (deviceId != null) {
            device = dao.find(Device.class, deviceId);
            criteria.add(cb.equal(root.get(DeviceHolderPartner_.device),
                    cb.parameter(Device.class, "device")));
        }
        if (customCode != null && !customCode.isEmpty()) {
            Join<DeviceHolderPartner, Device> deviceQ = root.join(DeviceHolderPartner_.device);
            criteria.add(cb.like(deviceQ.get(Device_.customCode),
                    cb.parameter(String.class, "customCode")));
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);

        if (id != null) {
            q.setParameter("id", id);
        }
        if (partnerId != null) {
            q.setParameter("partner", partner);
        }
        if (deviceId != null) {
            q.setParameter("device", device);
        }
        if (customCode != null && !customCode.isEmpty()) {
            q.setParameter("customCode", "%"+customCode+"%");
        }

        return q.getSingleResult();
    }

    private List<DeviceHolderPartner> search(EntityManager em,
                                             Integer id,
                                             Integer deviceId,
                                             Integer partnerId,
                                             String customCode,
                                             int first,
                                             int pageSize) {

        BusinessPartner partner = null;
        Device device = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DeviceHolderPartner> query = cb.createQuery(DeviceHolderPartner.class);
        Root<DeviceHolderPartner> root = query.from(DeviceHolderPartner.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();

        if (id != null) {
            criteria.add(cb.equal(root.get(DeviceHolderPartner_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (partnerId != null) {
            partner = dao.find(BusinessPartner.class, partnerId);
            criteria.add(cb.equal(root.get(DeviceHolderPartner_.businessPartner),
                    cb.parameter(BusinessPartner.class, "partner")));
        }
        if (deviceId != null) {
            device = dao.find(Device.class, deviceId);
            criteria.add(cb.equal(root.get(DeviceHolderPartner_.device),
                    cb.parameter(Device.class, "device")));
        }
        if (customCode != null && !customCode.isEmpty()) {
            Join<DeviceHolderPartner, Device> deviceQ = root.join(DeviceHolderPartner_.device);
            criteria.add(cb.like(deviceQ.get(Device_.customCode),
                    cb.parameter(String.class, "customCode")));
        }
        query.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<DeviceHolderPartner> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (partnerId != null) {
            typedQuery.setParameter("partner", partner);
        }
        if (deviceId != null) {
            typedQuery.setParameter("device", device);
        }
        if (customCode != null && !customCode.isEmpty()) {
            typedQuery.setParameter("customCode", "%"+customCode+"%");
        }
        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<DeviceHolderPartnerDTO> readAll(Integer id,
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
                    Utils.getMessage("DeviceHolderPartner.PersistenceEx.ReadAll"), ex);
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
