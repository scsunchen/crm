package com.invado.masterdata.service;

import com.invado.core.domain.ApplicationSetup;


import com.invado.core.domain.Device;
import com.invado.core.domain.DeviceStatus;
import com.invado.core.domain.DeviceStatus_;
import com.invado.core.dto.DeviceStatusDTO;

import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
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
public class DeviceStatusService {


    private static final Logger LOG = Logger.getLogger(
            DeviceStatus.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public DeviceStatus create(DeviceStatusDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateDeviceStatusPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("DeviceStatus.IllegalArgumentEx"));
        }
        if (a.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceStatus.IllegalArgumentException.Name"));
        }
        if (a.getDescription() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceStatus.IllegalArgumentException.Description"));
        }
        try {
            DeviceStatus deviceStatus = new DeviceStatus();
            deviceStatus.setName(a.getName());
            deviceStatus.setDescription(a.getDescription());

            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(deviceStatus);
            return deviceStatus;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceStatus.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public DeviceStatus update(DeviceStatusDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateDeviceStatusPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceStatus.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceStatus.IllegalArgumentEx.Code"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceStatus.IllegalArgumentException.Name"));
        }
        if (dto.getDescription() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("DeviceStatus.IllegalArgumentException.Description"));
        }
        try {
            DeviceStatus item = dao.find(DeviceStatus.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("DeviceStatus.EntityNotFoundEx",
                                dto.getId())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setName(dto.getName());
            item.setDescription(dto.getDescription());
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
                        Utils.getMessage("DeviceStatus.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("DeviceStatus.PersistenceEx.Update "),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer code) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteDeviceStatusPermission
        if (code == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("DeviceStatus.IllegalArgumentEx.Code")
            );
        }
        try {
            DeviceStatus service = dao.find(DeviceStatus.class, code);


            if (service != null) {
                if (dao.createNamedQuery(Device.READ_BY_STATUS)
                        .setParameter("status", service)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "DeviceStatus.ReferentialIntegrityEx.Device",
                            code)
                    );
                }
                dao.remove(service);
                dao.flush();
            }
        } catch (ReferentialIntegrityException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("DeviceStatus.PersistenceEx.Delete"),
                    ex);
        }
    }


@Transactional(readOnly = true, rollbackFor = Exception.class)
public DeviceStatusDTO read(Integer id)throws EntityNotFoundException{
        //TODO : check ReadDeviceStatusPermission
        if(id==null){
        throw new EntityNotFoundException(
        Utils.getMessage("DeviceStatus.IllegalArgumentEx.Code")
        );
        }
        try{
        DeviceStatus DeviceStatus=dao.find(DeviceStatus.class,id);
        if(DeviceStatus==null){
        throw new EntityNotFoundException(
        Utils.getMessage("DeviceStatus.EntityNotFoundEx",id)
        );
        }
        return DeviceStatus.getDTO();
        }catch(EntityNotFoundException ex){
        throw ex;
        }catch(Exception ex){
        LOG.log(Level.WARNING,"",ex);
        throw new SystemException(
        Utils.getMessage("DeviceStatus.PersistenceEx.Read"),
        ex);
        }
        }

@Transactional(readOnly = true, rollbackFor = Exception.class)
public ReadRangeDTO<DeviceStatusDTO>readPage(PageRequestDTO p)
        throws PageNotExistsException{
        //TODO : check ReadDeviceStatusPermission
        Integer code=null;
        String name=null;
        for(PageRequestDTO.SearchCriterion s:p.readAllSearchCriterions()){
        if(s.getKey().equals("code")&&s.getValue()instanceof String){
        code=(Integer)s.getValue();
        }
        if(s.getKey().equals("name")&&s.getValue()instanceof String){
        name=(String)s.getValue();
        }
        }
        try{
        Integer pageSize=dao.find(ApplicationSetup.class,1).getPageSize();

        Long countEntities=this.count(dao,code,name);
        Long numberOfPages=(countEntities!=0&&countEntities%pageSize==0)
        ?(countEntities/pageSize-1):countEntities/pageSize;
        Integer pageNumber=p.getPage();
        if(pageNumber.compareTo(-1)==-1
        ||pageNumber.compareTo(numberOfPages.intValue())==1){
        //page number cannot be less than -1 or greater than numberOfPages
        throw new PageNotExistsException(
        Utils.getMessage("DeviceStatus.PageNotExists",pageNumber));
        }
        ReadRangeDTO<DeviceStatusDTO>result=new ReadRangeDTO<>();
        if(pageNumber.equals(-1)){
        //if page number is -1 read last page
        //first DeviceStatus = last page number * DeviceStatuss per page
        int start=numberOfPages.intValue()*pageSize;
        result.setData(convertToDTO(this.search(dao,code,name,start,pageSize)));
        result.setNumberOfPages(numberOfPages.intValue());
        result.setPage(numberOfPages.intValue());
        }else{
        result.setData(convertToDTO(this.search(dao,code,name,
        p.getPage()*pageSize,
        pageSize)));
        result.setNumberOfPages(numberOfPages.intValue());
        result.setPage(pageNumber);
        }
        return result;
        }catch(PageNotExistsException ex){
        throw ex;
        }catch(Exception ex){
        LOG.log(Level.WARNING,"",ex);
        throw new SystemException(
        Utils.getMessage("DeviceStatus.PersistenceEx.ReadPage",ex)
        );
        }
        }

private List<DeviceStatusDTO>convertToDTO(List<DeviceStatus>lista){
        List<DeviceStatusDTO>listaDTO=new ArrayList<>();
        for(DeviceStatus pr:lista){
        listaDTO.add(pr.getDTO());
        }
        return listaDTO;
        }

private Long count(
        EntityManager EM,
        Integer code,
        String name){
        CriteriaBuilder cb=EM.getCriteriaBuilder();
        CriteriaQuery<Long>c=cb.createQuery(Long.class);
        Root<DeviceStatus>root=c.from(DeviceStatus.class);
        c.select(cb.count(root));
        List<Predicate>criteria=new ArrayList<>();
        if(code!=null){
        criteria.add(cb.equal(root.get(DeviceStatus_.id),
        cb.parameter(Integer.class,"code")));
        }
        if(name!=null&&name.isEmpty()==false){
        criteria.add(cb.like(cb.upper(root.get(DeviceStatus_.name)),
        cb.parameter(String.class,"name")));
        }


        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long>q=EM.createQuery(c);
        if(code!=null){
        q.setParameter("code",code);
        }
        if(name!=null&&name.isEmpty()==false){
        q.setParameter("name",name.toUpperCase()+"%");
        }


        return q.getSingleResult();
        }

private List<DeviceStatus>search(EntityManager em,
        Integer code,
        String name,
        int first,
        int pageSize){
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<DeviceStatus>query=cb.createQuery(DeviceStatus.class);
        Root<DeviceStatus>root=query.from(DeviceStatus.class);
        query.select(root);
        List<Predicate>criteria=new ArrayList<>();
        if(code!=null){
        criteria.add(cb.equal(root.get(DeviceStatus_.id),
        cb.parameter(Integer.class,"code")));
        }
        if(name!=null&&name.isEmpty()==false){
        criteria.add(cb.like(root.get(DeviceStatus_.name),
        cb.parameter(String.class,"name")));
        }

        query.where(criteria.toArray(new Predicate[0]))
        .orderBy(cb.asc(root.get(DeviceStatus_.id)));
        TypedQuery<DeviceStatus>typedQuery=em.createQuery(query);
        if(code!=null){
        typedQuery.setParameter("code",code);
        }
        if(name!=null&&name.isEmpty()==false){
        typedQuery.setParameter("name",name.toUpperCase()+"%");
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
        }

@Transactional(readOnly = true, rollbackFor = Exception.class)
public List<DeviceStatusDTO>readAll(
        Integer code,
        String name){
        try{
        return convertToDTO(this.search(dao,code,name,0,0));
        }catch(Exception ex){
        LOG.log(Level.WARNING,
        "",
        ex);
        throw new SystemException(
        Utils.getMessage("DeviceStatus.PersistenceEx.ReadAll"),ex);
        }
        }
        }


