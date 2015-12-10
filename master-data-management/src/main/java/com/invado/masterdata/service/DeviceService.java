package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.DeviceDTO;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.*;

import com.invado.core.exception.IllegalArgumentException;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by NikolaB on 6/21/2015.
 */
@Service
public class DeviceService {
    private static final Logger LOG = Logger.getLogger(
            Device.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;


    @Transactional(rollbackFor = Exception.class)
    public Device create(DeviceDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateDevicePermission

        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Device.IllegalArgumentEx"));
        }
        if (a.getDeviceStatusId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Device.IllegalArgumentException.Status"));
        }
        try {
            Device device = new Device();
            device.setSerialNumber(a.getSerialNumber());
            device.setInstalledSoftwareVersion(a.getInstalledSoftwareVersion());
            device.setSerialNumber(a.getSerialNumber());
            device.setCreationDate(a.getCreationDate());
            device.setCustomCode(a.getCustomCode());
            device.setStatus(dao.find(DeviceStatus.class, a.getDeviceStatusId()));
            device.setWorkingEndTime(a.getWorkingEndTime());
            device.setWorkingStartTime(a.getWorkingStartTime());
            device.setInstalledSoftwareVersion(a.getInstalledSoftwareVersion());
            device.setArticle(dao.find(Article.class, a.getArticleCode()));
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(device);
            return device;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Device.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Device update(DeviceDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateDevicePermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Device.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Device.IllegalArgumentEx.Code"));
        }
        if (dto.getDeviceStatusId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Device.IllegalArgumentException.Status"));
        }
        try {
            Device item = dao.find(Device.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Device.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);


            item.setCreationDate(dto.getCreationDate());
            item.setCustomCode(dto.getCustomCode());
            item.setInstalledSoftwareVersion(dto.getInstalledSoftwareVersion());
            item.setSerialNumber(dto.getSerialNumber());
            item.setStatus(dao.find(DeviceStatus.class, dto.getDeviceStatusId()));
            item.setVersion(dto.getVersion());
            item.setWorkingEndTime(dto.getWorkingEndTime());
            item.setWorkingStartTime(dto.getWorkingStartTime());
            item.setArticle(dao.find(Article.class, dto.getArticleCode()));

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
                        Utils.getMessage("Device.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Device.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteDevicePermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Device.IllegalArgumentEx.Code")
            );
        }
        try {
            Device service = dao.find(Device.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Device.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public DeviceDTO read(Integer id) throws EntityNotFoundException {
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

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<DeviceDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadDevicePermission
        Integer id = null;
        String customCode = null;
        String serialNumber = null;
        DeviceStatus status = null;
        String installedSoftwareVersion = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("customCode") && s.getValue() instanceof String) {
                customCode = (String) s.getValue();
            }
            if (s.getKey().equals("serialNumber") && s.getValue() instanceof String) {
                serialNumber = (String) s.getValue();
            }
            if (s.getKey().equals("status") && s.getValue() instanceof DeviceStatus) {
                status = (DeviceStatus) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao,
                    id,
                    customCode,
                    serialNumber,
                    status,
                    installedSoftwareVersion);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Device.PageNotExists", pageNumber));
            }
            ReadRangeDTO<DeviceDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Device = last page number * Devices per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(convertToDTO(this.search(dao,
                        id,
                        customCode,
                        serialNumber,
                        status,
                        installedSoftwareVersion,
                        start,
                        pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao,
                        id,
                        customCode,
                        serialNumber,
                        status,
                        installedSoftwareVersion,
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
                    Utils.getMessage("Device.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<DeviceDTO> convertToDTO(List<Device> lista) {
        List<DeviceDTO> listaDTO = new ArrayList<>();
        for (Device pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            String customCode,
            String serialNumber,
            DeviceStatus status,
            String installedSoftwareVersion) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Device> root = c.from(Device.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Device_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (customCode != null && customCode.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Device_.customCode)),
                    cb.parameter(String.class, "customCode")));
        }
        if (serialNumber != null && serialNumber.isEmpty() == false) {
            criteria.add(cb.like(
                            cb.upper(root.get(Device_.serialNumber)),
                            cb.parameter(String.class, "serialNumber"))
            );
        }
        if (status != null) {
            criteria.add(cb.equal(root.get(Device_.status),
                            cb.parameter(Device.class, "status"))
            );
        }
        if (installedSoftwareVersion != null && installedSoftwareVersion.isEmpty() == false) {
            criteria.add(cb.like(
                            cb.upper(root.get(Device_.installedSoftwareVersion)),
                            cb.parameter(String.class, "installedSoftwareVersion"))
            );
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (customCode != null && customCode.isEmpty() == false) {
            q.setParameter("customCode", customCode.toUpperCase() + "%");
        }
        if (serialNumber != null && serialNumber.isEmpty() == false) {
            q.setParameter("serialNumber", serialNumber);
        }
        if (status != null) {
            q.setParameter("status", status);
        }


        if (installedSoftwareVersion != null && installedSoftwareVersion.isEmpty() == false) {
            q.setParameter("installedSoftwareVersion", installedSoftwareVersion);
        }
        return q.getSingleResult();
    }

    private List<Device> search(EntityManager em,
                                Integer id,
                                String customCode,
                                String serialNumber,
                                DeviceStatus status,
                                String installedSoftwareVersion,
                                int first,
                                int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Device> query = cb.createQuery(Device.class);
        Root<Device> root = query.from(Device.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Device_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (customCode != null && customCode.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Device_.customCode)),
                    cb.parameter(String.class, "customCode")));
        }
        if (serialNumber != null && serialNumber.isEmpty() == false) {
            criteria.add(cb.like(
                            cb.upper(root.get(Device_.serialNumber)),
                            cb.parameter(String.class, "serialNumber"))
            );
        }
        if (status != null) {
            criteria.add(cb.equal(root.get(Device_.status),
                            cb.parameter(Device.class, "status"))
            );
        }
        if (installedSoftwareVersion != null && installedSoftwareVersion.isEmpty() == false) {
            criteria.add(cb.like(
                            cb.upper(root.get(Device_.installedSoftwareVersion)),
                            cb.parameter(String.class, "installedSoftwareVersion"))
            );
        }

        query.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Device> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (customCode != null && customCode.isEmpty() == false) {
            typedQuery.setParameter("customCode", customCode.toUpperCase() + "%");
        }
        if (serialNumber != null && serialNumber.isEmpty() == false) {
            typedQuery.setParameter("serialNumber", serialNumber);
        }
        if (status != null) {
            typedQuery.setParameter("status", status);
        }


        if (installedSoftwareVersion != null && installedSoftwareVersion.isEmpty() == false) {
            typedQuery.setParameter("installedSoftwareVersion", installedSoftwareVersion);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<DeviceDTO> readAll(Integer id,
                                   String customCode,
                                   String serialNumber,
                                   DeviceStatus status,
                                   String installedSoftwareVersion) {
        try {
            return convertToDTO(this.search(dao, id, customCode, serialNumber, status, installedSoftwareVersion, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Device.PersistenceEx.ReadAll"), ex);
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
                    "Device.PersistenceEx.ReadItemByDescription"),
                    ex);
        }
    }


    @Transactional(readOnly = true)
    public List<Device> readDeviceByCustomCodeAnassigned(String name) {
        try {
            return dao.createNamedQuery(Device.READ_BY_CUSTOM_CODE_ANASSIGNED, Device.class)
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
