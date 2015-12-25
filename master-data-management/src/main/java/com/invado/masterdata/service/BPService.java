package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.finance.service.dto.BusinessPartnerSpecificationItemDTO;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
import com.invado.core.exception.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import telekomWS.client.AddressServiceClient;
import telekomWS.client.ServiceClient;
import telekomWS.client.exceptions.WSException;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.*;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Created by NikolaB on 6/2/2015.
 */
@Service
public class BPService {

    private static final Logger LOG = Logger.getLogger(
            BusinessPartner.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Inject
    private Validator validator;
    private final String username = "a";

    private ServiceClient telekomWSClient = new ServiceClient();

    private AddressServiceClient addressWSClient = new AddressServiceClient();


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartner createDetail(BusinessPartnerDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateBusinessPartnerPermission

        BusinessPartner parentBusinessPartner = null;

        String msgg = Utils.getMessage("Client.Legal_Entity");
        if (a == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx"));
        }
        if (a.getParentBusinessPartnerId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.ParentPartner"));
        }
        if (a.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.Name"));
        }
        if (a.getParentBusinessPartnerId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.Parent"));
        }

        try {
            BusinessPartner businessPartner = new BusinessPartner();

            if (a.getParentBusinessPartnerId() != null) {
                parentBusinessPartner = dao.find(BusinessPartner.class, a.getParentBusinessPartnerId());
                businessPartner.setParentBusinessPartner(parentBusinessPartner);
            }
            businessPartner.setCompanyIdNumber(parentBusinessPartner.getCompanyIdNumber());
            businessPartner.setName(a.getName());
            businessPartner.setName1(a.getName1());
            businessPartner.setAddress(new Address(a.getCountry(), a.getPlace(), a.getStreet(), a.getPostCode()));
            a.settAddressCode(addressWSClient.getPAK(a.gettHouseNumberCode()));
            businessPartner.setTelekomAddress(new TelekomAddress(a.gettPlace(), a.gettPlaceCode(), a.getPostCode(), a.getStreet(), a.gettStreetCode(), a.gettHouseNumber(), a.gettHouseNumberCode(),
                    a.gettAddressCode()));
            businessPartner.setPhone(a.getPhone());
            businessPartner.setFax(a.getStreet());
            businessPartner.setEMail(a.getEMail());
            businessPartner.setTIN(parentBusinessPartner.getTIN());
            businessPartner.setCurrencyDesignation(a.getCurrencyDesignation());
            businessPartner.setCurrentAccount(a.getCurrentAccount());
            businessPartner.setActivityCode(a.getActivityCode());
            businessPartner.setRebate(a.getRebate());
            businessPartner.setInterestFreeDays(a.getInterestFreeDays());
            businessPartner.setVAT(a.getVAT());
            if (a.getTypeT() != null)
                a.setType(a.getTypeT());
            businessPartner.setType(a.getType());
            businessPartner.setTelekomStatus(a.getTelekomStatus());
            businessPartner.setTelekomId(a.getTelekomId());
            if (a.getPosTypeId() != null)
                businessPartner.setPosType(dao.find(POSType.class, a.getPosTypeId()));
            businessPartner.setPosType(dao.find(POSType.class, a.getPosTypeId().intValue()));
            businessPartner.setContactPerson(new ContactPerson(a.getContactPersoneName(), a.getContactPersonePhone(), a.getContactPersoneFunction(), a.getEMail()));
            List<String> msgs = validator.validate(businessPartner).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }

            dao.persist(businessPartner);
            return businessPartner;
        } catch (ConstraintViolationException | EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            ex.printStackTrace();
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.Create" + ex.getMessage(), ex)

            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BusinessPartner create(BusinessPartnerDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateBusinessPartnerPermission

        String msgg = Utils.getMessage("Client.Legal_Entity");
        if (a == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx"));
        }
        if (a.getCompanyIdNumber() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.CompanyIdNumber"));
        }
        if (a.getTIN() == null || a.getTIN().isEmpty() || a.getTIN() == "") {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.TIN"));
        }
        if (a.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.Name"));
        }

        try {
            BusinessPartner businessPartner = new BusinessPartner();

            businessPartner.setCompanyIdNumber(a.getCompanyIdNumber());
            businessPartner.setName(a.getName());
            businessPartner.setName1(a.getName1());
            businessPartner.setAddress(new Address(a.getCountry(), a.getPlace() == null ? a.gettPlace() : a.gettPlace(),
                    a.getStreet() == null ? a.gettStreet() : a.getStreet(), a.getPostCode(), a.gettHouseNumber() == null ? a.gettHouseNumber() : a.gettHouseNumber()));
            a.settAddressCode(addressWSClient.getPAK(a.gettHouseNumberCode()));
            businessPartner.setTelekomAddress(new TelekomAddress(a.gettPlace(), a.gettPlaceCode(), a.getPostCode(), a.getStreet(), a.gettStreetCode(), a.gettHouseNumber(), a.gettHouseNumberCode(),
                    a.gettAddressCode()));
            businessPartner.setPhone(a.getPhone());
            businessPartner.setFax(a.getStreet());
            businessPartner.setEMail(a.getEMail());
            businessPartner.setTIN(a.getTIN());
            businessPartner.setCurrencyDesignation(a.getCurrencyDesignation());
            businessPartner.setCurrentAccount(a.getCurrentAccount());
            businessPartner.setActivityCode(a.getActivityCode());
            businessPartner.setRebate(a.getRebate());
            businessPartner.setInterestFreeDays(a.getInterestFreeDays());
            businessPartner.setVAT(a.getVAT());
            if (a.getTypeT() != null)
                a.setType(a.getTypeT());
            businessPartner.setType(a.getType());
            businessPartner.setTelekomStatus(a.getTelekomStatus());
            businessPartner.setTelekomId(a.getTelekomId());
            if (a.getPosTypeId() != null)
                businessPartner.setPosType(dao.find(POSType.class, a.getPosTypeId()));
            if (a.getParentBusinessPartnerId() != null)
                businessPartner.setParentBusinessPartner(dao.find(BusinessPartner.class, a.getParentBusinessPartnerId()));
            businessPartner.setContactPerson(new ContactPerson(a.getContactPersoneName(), a.getContactPersonePhone(), a.getContactPersoneFunction(), a.getEMail()));
            List<String> msgs = validator.validate(businessPartner).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new ConstraintViolationException("", msgs);
            }

            dao.persist(businessPartner);
            return businessPartner;
        } catch (ConstraintViolationException | EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            ex.printStackTrace();
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.Create" + ex.getMessage(), ex)

            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartner update(BusinessPartnerDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateBusinessPartnerPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Code"));
        }
        if (dto.getCompanyIdNumber() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.CompanyIdNumber"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentException.Name"));
        }
        try {
            BusinessPartner item = dao.find(BusinessPartner.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartner.EntityNotFoundEx",
                                dto.getCompanyIdNumber())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setName(dto.getName());
            item.setName1(dto.getName1());
            if (dto.getCountry() != null)
                item.setAddress(new Address(dto.getCountry(), dto.getPlace(), dto.getStreet(), dto.getPostCode()));
            if (dto.getContactPersoneName() != null)
                item.setContactPerson(new ContactPerson(dto.getContactPersoneName(), dto.getContactPersonePhone(), dto.getContactPersoneFunction(), dto.getEMail()));
            item.setCurrentAccount(dto.getCurrentAccount());
            dto.settAddressCode(addressWSClient.getPAK(dto.gettHouseNumberCode()));
            item.setTelekomAddress(new TelekomAddress(dto.gettPlace(), dto.gettPlaceCode(), dto.getPostCode(), dto.gettStreet(), dto.gettStreetCode(),
                    dto.gettHouseNumber(), dto.gettHouseNumberCode(), dto.gettAddressCode()));
            item.setEMail(dto.getEMail());
            item.setFax(dto.getFax());
            item.setPhone(dto.getPhone());
            item.setRebate(dto.getRebate());
            item.setVersion(dto.getVersion());
            item.setActivityCode(dto.getActivityCode());
            item.setInterestFreeDays(dto.getInterestFreeDays());
            item.setTIN(dto.getTIN());
            item.setCompanyIdNumber(dto.getCompanyIdNumber());
            item.setCurrencyDesignation(dto.getCurrencyDesignation());
            if (dto.getPosTypeId() != null)
                item.setPosType(dao.find(POSType.class, dto.getPosTypeId()));
            if (dto.getTypeT() != null)
                dto.setType(dto.getTypeT());
            item.setType(dto.getType());
            item.setTelekomStatus(dto.getTelekomStatus());
            item.setTelekomId(dto.getTelekomId());

            if (dto.getParentBusinessPartnerId() != null)
                item.setParentBusinessPartner(dao.find(BusinessPartner.class, dto.getParentBusinessPartnerId()));

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
                        Utils.getMessage("BusinessPartner.OptimisticLockEx",
                                dto.getCompanyIdNumber()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("BusinessPartner.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional
    public List<BusinessPartner> readParentPartners() {
        Query parent = dao.createNamedQuery(BusinessPartner.READ_PARENT);
        List<BusinessPartner> parents = parent.getResultList();
        return parents;
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteBusinessPartnerPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Code")
            );
        }
        try {
            BusinessPartner service = dao.find(BusinessPartner.class, id);
            if (service != null) {
                if (dao.createNamedQuery("Invoice.GetByPartner")
                        .setParameter("companyIdNumber", id)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .getResultList().isEmpty() == false) {
                    throw new ReferentialIntegrityException(Utils.getMessage(
                            "BusinessPartner.ReferentialIntegrityEx.InvoiceItem",
                            id)
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
                    Utils.getMessage("BusinessPartner.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerDTO read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("BusinessPartner.IllegalArgumentEx.Id")
            );
        }
        try {
            BusinessPartner businessPartner = dao.find(BusinessPartner.class, id);
            if (businessPartner == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartner.EntityNotFoundEx", id)
                );
            }
            return businessPartner.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BusinessPartnerDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadBusinessPartnerPermission
        Integer id = null;
        String companyIdNumber = null;
        String name = null;
        String TIN = null;
        BusinessPartner.Type type = null;
        BusinessPartner parentBusinessPartner = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("companyIdNumber") && s.getValue() instanceof String) {
                companyIdNumber = (String) s.getValue();
            }
            if (s.getKey().equals("name") && s.getValue() instanceof String) {
                name = (String) s.getValue();
            }
            if (s.getKey().equals("TIN") && s.getValue() instanceof String) {
                TIN = (String) s.getValue();
            }
            if (s.getKey().equals("type")) {

                if (s.getValue() instanceof String && !((String) s.getValue()).isEmpty()) {
                    type = BusinessPartner.Type.getEnum((String) s.getValue());
                } else if (s.getValue() instanceof String && ((String) s.getValue()).isEmpty()) {
                    type = null;
                } else {
                    type = (BusinessPartner.Type) s.getValue();
                }
            }
            if (s.getKey().equals("parentBusinessPartner")) {
                if (s.getValue() instanceof BusinessPartner) {
                    parentBusinessPartner = (BusinessPartner) s.getValue();
                } else if (s.getValue() instanceof Integer) {
                    parentBusinessPartner = dao.find(BusinessPartner.class, s.getValue());
                }
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 2).getPageSize();

            Long countEntities = this.count(dao,
                    id,
                    companyIdNumber,
                    name,
                    TIN,
                    type,
                    parentBusinessPartner);
            System.out.println("broj entiteta je " + countEntities);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("BusinessPartner.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BusinessPartnerDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartner = last page number * BusinessPartners per page
                int start = numberOfPages.intValue() * pageSize;
                List<BusinessPartnerDTO> businessPartnerDTOList = convertToDTO(this.search(dao, id, companyIdNumber, name, TIN, type, parentBusinessPartner, start, pageSize));
                result.setData(businessPartnerDTOList);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<BusinessPartnerDTO> businessPartnerDTOList = convertToDTO(this.search(dao, id, companyIdNumber, name, TIN, type, parentBusinessPartner, p.getPage() * pageSize, pageSize));
                result.setData(businessPartnerDTOList);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (
                PageNotExistsException ex
                )

        {
            throw ex;
        } catch (
                Exception ex
                )

        {
            System.out.println(ex.getStackTrace());
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.ReadPage", ex)
            );
        }

    }

    private List<BusinessPartnerDTO> convertToDTO(List<BusinessPartner> lista) {
        List<BusinessPartnerDTO> listaDTO = new ArrayList<>();
        for (BusinessPartner pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            String companyIdNumber,
            String name,
            String TIN,
            BusinessPartner.Type type,
            BusinessPartner parentBusinessPartner) {

        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartner> root = c.from(BusinessPartner.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();

        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartner_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.companyIdNumber)),
                    cb.parameter(String.class, "companyIdNumber")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.name)),
                    cb.parameter(String.class, "name")));
        }
        if (TIN != null && TIN.isEmpty() == false) {
            criteria.add(cb.like(
                            root.get(BusinessPartner_.TIN),
                            cb.parameter(String.class, "TIN"))
            );
        }
        if (type != null) {
            criteria.add(cb.equal(root.get(BusinessPartner_.type),
                    cb.parameter(BusinessPartner.Type.class, "type")));
        }
        if (parentBusinessPartner != null) {
            criteria.add(cb.equal(root.get(BusinessPartner_.parentBusinessPartner),
                    cb.parameter(BusinessPartner.class, "parentBusinessPartner")));
        }


        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            q.setParameter("companyIdNumber", companyIdNumber.toUpperCase() + "%");
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", "%" + name.toUpperCase() + "%");
        }
        if (TIN != null && TIN.isEmpty() == false) {
            q.setParameter("TIN", TIN);
        }
        if (type != null) {
            q.setParameter("type", type);
        }
        if (parentBusinessPartner != null) {
            q.setParameter("parentBusinessPartner", parentBusinessPartner);
        }
        return q.getSingleResult();
    }

    private List<BusinessPartner> search(EntityManager em,
                                         Integer id,
                                         String companyIdNumber,
                                         String name,
                                         String TIN,
                                         BusinessPartner.Type type,
                                         BusinessPartner parentBusinessPartner,
                                         int first,
                                         int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusinessPartner> query = cb.createQuery(BusinessPartner.class);
        Root<BusinessPartner> root = query.from(BusinessPartner.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartner_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.companyIdNumber)),
                    cb.parameter(String.class, "companyIdNumber")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(BusinessPartner_.name)),
                    cb.parameter(String.class, "desc")));
        }
        if (TIN != null && TIN.isEmpty() == false) {
            criteria.add(cb.like(root.get(BusinessPartner_.TIN),
                    cb.parameter(String.class, "TIN")));
        }
        if (type != null) {
            criteria.add(cb.equal(root.get(BusinessPartner_.type),
                    cb.parameter(BusinessPartner.Type.class, "type")));
        }
        if (parentBusinessPartner != null) {
            criteria.add(cb.equal(root.get(BusinessPartner_.parentBusinessPartner),
                    cb.parameter(BusinessPartner.class, "parentBusinessPartner")));
        }
        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BusinessPartner_.companyIdNumber)));
        query.orderBy(cb.asc(root.get(BusinessPartner_.name)));
        TypedQuery<BusinessPartner> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (companyIdNumber != null && companyIdNumber.isEmpty() == false) {
            typedQuery.setParameter("companyIdNumber", companyIdNumber.toUpperCase() + "%");
        }
        if (name != null && name.isEmpty() == false) {
            typedQuery.setParameter("desc", "%" + name.toUpperCase() + "%");
        }
        if (TIN != null && TIN.isEmpty() == false) {
            typedQuery.setParameter("TIN", TIN);
        }
        if (type != null) {
            typedQuery.setParameter("type", type);
        }
        if (parentBusinessPartner != null) {
            typedQuery.setParameter("parentBusinessPartner", parentBusinessPartner);
        }
        System.out.println("first " + first + " a ps je " + pageSize);
        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        List<BusinessPartner> tempList = typedQuery.getResultList();
        return tempList;
    }


    /*MERCHANT*/
    @Transactional(rollbackFor = Exception.class)
    public void merchantRegistration(BusinessPartnerDTO businessPartnerDTO) throws WSException {
        if (businessPartnerDTO.getTIN() == null || businessPartnerDTO.getCompanyIdNumber() == null || businessPartnerDTO.getName() == null) {
            throw new WSException(
                    Utils.getMessage("WSReference.MerchantRegistration.IllegalArgumentEx"));
        }
        if (businessPartnerDTO.getType() != BusinessPartner.Type.MERCHANT) {
            throw new WSException(
                    Utils.getMessage("WSReference.MerchantRegistration.IllegalType"));
        }
        try {
            businessPartnerDTO.setTelekomId(Integer.valueOf(telekomWSClient.poslovniPartnerUnos(businessPartnerDTO.getName(), Integer.valueOf(businessPartnerDTO.getTIN()).intValue(), businessPartnerDTO.getCompanyIdNumber(),
                    businessPartnerDTO.getPlace(), businessPartnerDTO.getStreet(), businessPartnerDTO.getContactPersoneName(), businessPartnerDTO.getPhone(),
                    businessPartnerDTO.getEMail())));
            businessPartnerDTO.setTelekomStatus(BusinessPartner.TelekomStatus.ACTIVE);
        } catch (WSException ex) {
            throw ex;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void merchantUpdate(BusinessPartnerDTO businessPartnerDTO) throws WSException {
        if (businessPartnerDTO.getTelekomId() == null) {
            throw new WSException(
                    Utils.getMessage("WSReference.MerchantUpdate.NonRegisteredMerchant"));
        }
        try {
            businessPartnerDTO.setTelekomId(Integer.valueOf(telekomWSClient.poslovniPartnerIzmena(businessPartnerDTO.getName(), Integer.valueOf(businessPartnerDTO.getTIN()).intValue(), businessPartnerDTO.getCompanyIdNumber(),
                    businessPartnerDTO.getPlace(), businessPartnerDTO.getStreet(), businessPartnerDTO.getContactPersoneName(), businessPartnerDTO.getPhone(),
                    businessPartnerDTO.getEMail())));
        } catch (WSException ex) {
            throw ex;
        }
    }

    public void merchantDeactivation(BusinessPartnerDTO businessPartnerDTO) throws Exception {

        if (businessPartnerDTO.getTelekomId() == null) {
            throw new WSException(
                    Utils.getMessage("WSReference.MerchantUpdate.NonRegisteredMerchant"));
        }
        try {
            businessPartnerDTO.setTelekomId(telekomWSClient.poslovniPartnerDeaktivacija(businessPartnerDTO.getTelekomId().intValue()));
            businessPartnerDTO.setTelekomStatus(BusinessPartner.TelekomStatus.DEACTIVATED);
        } catch (WSException ex) {
            throw ex;
        }

    }


    /*POS*/
    @Transactional(rollbackFor = Exception.class)
    public void pointOfSaleRegistration(BusinessPartnerDTO businessPartnerDTO) throws WSException {

        Integer parentPartnerTelekomId = dao.find(BusinessPartner.class, businessPartnerDTO.getParentBusinessPartnerId()).getTelekomId();
        if (parentPartnerTelekomId == null) {
            throw new WSException(
                    Utils.getMessage("WSReference.POSRegistration.NonRegisteredMerchant"));
        }
        if (businessPartnerDTO.getPOStype() != null || businessPartnerDTO.getName() == null) {
            throw new WSException(
                    Utils.getMessage("WSReference.POSRegistration.IllegalArgumentEx"));
        }
        if (businessPartnerDTO.getType() != BusinessPartner.Type.POINT_OF_SALE) {
            throw new WSException(
                    Utils.getMessage("WSReference.POSRegistration.IllegalType"));
        }
        try {

            /*ulica, broj, grad, kontaktOsoba, telefon i eMail*/

            businessPartnerDTO.setTelekomId(Integer.valueOf(telekomWSClient.prodajnoMestoUnos(parentPartnerTelekomId,
                    businessPartnerDTO.getName(),
                    businessPartnerDTO.gettPlace(),
                    businessPartnerDTO.gettHouseNumberCode(),
                    businessPartnerDTO.getPosTypeId(),
                    businessPartnerDTO.getContactPersoneName(),
                    businessPartnerDTO.getPhone(),
                    businessPartnerDTO.getEMail())));
            businessPartnerDTO.setTelekomStatus(BusinessPartner.TelekomStatus.ACTIVE);
        } catch (WSException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }


    public void pointOfSaleUpdate(BusinessPartnerDTO businessPartnerDTO) throws WSException {

        if (businessPartnerDTO.getTelekomId() == null) {
            throw new WSException(Utils.getMessage("WSReference.POSRegistration.NonRegisteredPOS"));
        }
        BusinessPartner parentBusinesspartner = dao.find(BusinessPartner.class, businessPartnerDTO.getParentBusinessPartnerId());
        try {
            businessPartnerDTO.setTelekomId(Integer.valueOf(telekomWSClient.prodajnoMestoIzmena(businessPartnerDTO.getTelekomId(), businessPartnerDTO.getName(),
                    Integer.parseInt(parentBusinesspartner.getTIN()), businessPartnerDTO.gettAddressCode(), businessPartnerDTO.getContactPersoneName(), businessPartnerDTO.getPosTypeId(),
                    businessPartnerDTO.getPhone(), businessPartnerDTO.getEMail())));
        } catch (WSException ex) {
            throw ex;
        }
    }

    public void pointOfSaleDeactivation(BusinessPartnerDTO businessPartnerDTO) throws WSException {
        if (businessPartnerDTO.getTelekomId() == null) {
            throw new WSException(Utils.getMessage("WSReference.POSRegistration.NonRegisteredPOS"));
        }
        try {
            businessPartnerDTO.setTelekomId(Integer.valueOf(telekomWSClient.prodajnoMestoDeaktivacija(businessPartnerDTO.getTelekomId())));
            businessPartnerDTO.setTelekomStatus(BusinessPartner.TelekomStatus.DEACTIVATED);
        } catch (WSException ex) {
            throw ex;
        }
    }


    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BusinessPartner> readAll(Integer id,
                                         String companyIdNumber,
                                         String name,
                                         String TIN,
                                         BusinessPartner.Type type,
                                         BusinessPartner parentBusinessPartner) {
        try {
            return this.search(dao, id, companyIdNumber, name, TIN, type, parentBusinessPartner, 0, 0);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("BusinessPartner.PersistenceEx.ReadAll"), ex);
        }
    }

    @Transactional(readOnly = true)
    public List<BusinessPartner> readPartnerByName(String name) {
        try {
            System.out.println("evo ga servis " + name);
            List<BusinessPartner> list = dao.createNamedQuery(
                    BusinessPartner.READ_BY_NAME_ORDERBY_NAME,
                    BusinessPartner.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList();
            return list;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "BusinessPartner.Exception.ReadItemByDescription"),
                    ex);
        }
    }

    @Transactional(readOnly = true)
    public List<BusinessPartner> readMerchantByName(String name) {
        try {
            System.out.println("evo ga servis " + name);
            List<BusinessPartner> list = dao.createNamedQuery(
                    BusinessPartner.READ_MERCHANT_BY_NAME_ORDERBY_NAME,
                    BusinessPartner.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList();
            return list;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "BusinessPartner.Exception.ReadItemByDescription"),
                    ex);
        }
    }


    @Transactional(readOnly = true)
    public List<BusinessPartner> readPointOfSaleByName(String name) {
        try {
            System.out.println("evo ga servis " + name);
            List<BusinessPartner> list = dao.createNamedQuery(
                    BusinessPartner.READ_POINT_OF_SALE_BY_NAME_ORDERBY_NAME,
                    BusinessPartner.class)
                    .setParameter("name", ("%" + name + "%").toUpperCase())
                    .getResultList();
            return list;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(Utils.getMessage(
                    "BusinessPartner.Exception.ReadItemByDescription"),
                    ex);
        }
    }

    public List<BusinessPartner.Type> getTypes() {
        return Arrays.asList(BusinessPartner.Type.values());
    }

    public List<BusinessPartner.TelekomStatus> getTelekomStatuses() {
        return Arrays.asList(BusinessPartner.TelekomStatus.values());
    }

    public BusinessPartner readById(Integer id) {
        return dao.find(BusinessPartner.class, id);
    }

}


