package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;
import com.invado.core.domain.Device;
import com.invado.core.domain.DeviceStatus;
import com.invado.core.domain.Device_;;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
import com.invado.masterdata.service.exception.IllegalArgumentException;
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
    private final String username = "a";

    @Autowired
    private ArticleSharedService articleService;

    @Transactional(rollbackFor = Exception.class)
    public Device create(Device a) throws IllegalArgumentException,
            javax.persistence.EntityExistsException {
        //check CreateDevicePermission

        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Device.IllegalArgumentEx"));
        }
        try {
            if (dao.find(Device.class, a.getId()) != null) {
                throw new javax.persistence.EntityExistsException(
                        Utils.getMessage("Device.EntityExistsEx", a.getId())
                );
            }
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            a.setArticle(articleService.read(a.getArticle().getCode()));
            dao.persist(a);
            return a;
        } catch (IllegalArgumentException | javax.persistence.EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Device.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Device update(Device dto) throws ConstraintViolationException,
            javax.persistence.EntityNotFoundException,
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
        try {
            Device item = dao.find(Device.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("Device.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setCreationDate(dto.getCreationDate());
            item.setCustomCode(dto.getCustomCode());
            item.setInstalledSoftwareVersion(dto.getInstalledSoftwareVersion());
            item.setSerialNumber(dto.getSerialNumber());
            item.setStatus(dto.getStatus());
            item.setVersion(dto.getVersion());
            item.setWorkingEndTime(dto.getWorkingEndTime());
            item.setWorkingStartTime(dto.getWorkingStartTime());
            List<String> msgs = validator.validate(item).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }
            dao.flush();
            return item;
        } catch (ConstraintViolationException | javax.persistence.EntityNotFoundException ex) {
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
    public void delete(String companyIDNumber) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteDevicePermission
        if (companyIDNumber == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Device.IllegalArgumentEx.Code")
            );
        }
        try {
            Device service = dao.find(Device.class, companyIDNumber);
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
    public Device read(String companyIDNumber) throws javax.persistence.EntityNotFoundException {
        //TODO : check ReadDevicePermission
        if (companyIDNumber == null) {
            throw new javax.persistence.EntityNotFoundException(
                    Utils.getMessage("Device.IllegalArgumentEx.Code")
            );
        }
        try {
            Device Device = dao.find(Device.class, companyIDNumber);
            if (Device == null) {
                throw new javax.persistence.EntityNotFoundException(
                        Utils.getMessage("Device.EntityNotFoundEx", companyIDNumber)
                );
            }
            return Device;
        } catch (javax.persistence.EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Device.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<Device> readPage(PageRequestDTO p)
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
            ReadRangeDTO<Device> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Device = last page number * Devices per page
                int start = numberOfPages.intValue() * pageSize;
                result.setData(this.search(dao,
                        id,
                        customCode,
                        serialNumber,
                        status,
                        installedSoftwareVersion,
                        start,
                        pageSize));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(this.search(dao,
                        id,
                        customCode,
                        serialNumber,
                        status,
                        installedSoftwareVersion,
                        p.getPage() * pageSize,
                        pageSize));
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
    public List<Device> readAll( Integer id,
                                 String customCode,
                                 String serialNumber,
                                 DeviceStatus status,
                                 String installedSoftwareVersion) {
        try {
            return this.search(dao, id, customCode, serialNumber, status, installedSoftwareVersion, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Device.PersistenceEx.ReadAll"), ex);
        }
    }
}
