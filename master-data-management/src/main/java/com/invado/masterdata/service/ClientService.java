package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.ClientDTO;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
import com.invado.masterdata.service.exception.EntityExistsException;
import com.invado.masterdata.service.exception.EntityNotFoundException;
import com.invado.core.exception.IllegalArgumentException;
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
 * Created by NikolaB on 6/9/2015.
 */
@Service
public class ClientService {

    private static final Logger LOG = Logger.getLogger(
            Client.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Autowired
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public Client create(ClientDTO dto) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateClientPermission

        if (dto == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Client.IllegalArgumentEx"));
        }
        if (dto.getTIN() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentException.TIN"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentException.Name"));
        }
        if (dto.getTownshipCode() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentException.Township"));
        }
        try {
            try {
                Client temp = dao.createNamedQuery(Client.READ_BY_ID, Client.class)
                        .setParameter("id", dto.getId())
                        .getSingleResult();
                if (temp != null) {
                    throw new EntityExistsException(
                            Utils.getMessage("OrgUnit.EntityExistsException", dto.getId()));
                }
            } catch (NoResultException ex) {
                System.out.println("Nema problema " + ex.getMessage());
            }

            Client client = new Client();
            client.setName(dto.getName());
            client.setPlace(dto.getPlace());
            client.setStreet(dto.getStreet());
            client.setBank(dao.find(BankCreditor.class, dto.getBankId()));
            client.setBankAccount(dto.getBankAccount());
            client.setBusinessActivityCode(dto.getBusinessActivityCode());
            client.setCompanyIDNumber(dto.getCompanyIDNumber());
            client.setCountry(dto.getCountry());
            client.setEMail(dto.getEMail());
            client.setEmployee(dto.getEmployee());
            client.setFax(dto.getFax());
            client.setInitialCapital(dto.getInitialCapital());
            client.setLogo(dto.getLogo());
            client.setPhone(dto.getPhone());
            client.setTownship(dao.find(Township.class, dto.getTownshipCode()));
            client.setTIN(dto.getTIN());
            client.setStatus(dto.getStatus());
            client.setPostCode(dto.getPostCode());
            client.setType(dto.getType());
            client.setCompanyIDNumber(dto.getCompanyIDNumber());
            client.setRegistrationNumber(dto.getRegistrationNumber());
            client.setVatCertificateNumber(dto.getVatCertificateNumber());


            List<String> msgs = validator.validate(client).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(client);
            return client;
        } catch (IllegalArgumentException | EntityExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Client update(ClientDTO dto) throws ConstraintViolationException,
            EntityNotFoundException,
            ReferentialIntegrityException {
        //check UpdateClientPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentEx"));
        }
        if (dto.getId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentEx.Id"));
        }
        if (dto.getTIN() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentException.TIN"));
        }
        if (dto.getName() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentException.Name"));
        }
        if (dto.getTownshipCode() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("Client.IllegalArgumentException.Township"));
        }
        try {
            Client item = dao.find(Client.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Client.EntityNotFoundEx",
                                dto.getCompanyIDNumber())
                );
            }
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setName(dto.getName());
            item.setCountry(dto.getCountry());
            item.setPlace(dto.getPlace());
            item.setTownship(dao.find(Township.class, dto.getTownshipCode()));
            item.setStreet(dto.getStreet());
            item.setPostCode(dto.getPostCode());
            item.setEMail(dto.getEMail());
            item.setFax(dto.getFax());
            item.setPhone(dto.getPhone());
            item.setBank(dao.find(BankCreditor.class, dto.getBankId()));
            item.setStatus(dto.getStatus());
            item.setVersion(dto.getVersion());
            item.setType(dto.getType());
            item.setBankAccount(dto.getBankAccount());
            item.setBusinessActivityCode(dto.getBusinessActivityCode());
            item.setCompanyIDNumber(dto.getCompanyIDNumber());
            item.setEmployee(dto.getEmployee());
            item.setFax(dto.getFax());
            item.setInitialCapital(dto.getInitialCapital());
            item.setLogo(dto.getLogo());
            item.setTIN(dto.getTIN());
            item.setStatus(dto.getStatus());
            item.setType(dto.getType());
            item.setCompanyIDNumber(dto.getCompanyIDNumber());
            item.setRegistrationNumber(dto.getRegistrationNumber());


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
                        Utils.getMessage("Client.OptimisticLockEx",
                                dto.getCompanyIDNumber()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        Utils.getMessage("Client.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            ReferentialIntegrityException {
        //TODO : check DeleteClientPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("Client.IllegalArgumentEx.Id")
            );
        }
        try {
            Client service = dao.find(Client.class, id);
            if (service != null) {
                dao.remove(service);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ClientDTO read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadClientPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("Client.IllegalArgumentEx.Id")
            );
        }
        try {
            Client client = dao.find(Client.class, id);
            if (client == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("Client.EntityNotFoundEx", id)
                );
            }
            return client.getDTO();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<ClientDTO> readPage(PageRequestDTO p)
            throws PageNotExistsException {
        //TODO : check ReadClientPermission
        Integer id = null;
        String companyIDNumber = null;
        String name = null;
        String TIN = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("companyIDNumber") && s.getValue() instanceof String) {
                companyIDNumber = (String) s.getValue();
            }
            if (s.getKey().equals("name") && s.getValue() instanceof String) {
                name = (String) s.getValue();
            }
            if (s.getKey().equals("TIN") && s.getValue() instanceof String) {
                TIN = (String) s.getValue();
            }
        }
        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao,
                    id,
                    companyIDNumber,
                    name,
                    TIN);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new PageNotExistsException(
                        Utils.getMessage("Client.PageNotExists", pageNumber));
            }
            ReadRangeDTO<ClientDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first Client = last page number * Clients per page
                int start = numberOfPages.intValue() * pageSize;
                List<ClientDTO> listClient = convertToDTO(this.search(dao, id, companyIDNumber, name, TIN, start, pageSize));

                result.setData(listClient);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                List<ClientDTO> listClient = convertToDTO(this.search(dao, id, companyIDNumber, name, TIN, p.getPage() * pageSize, pageSize));
                result.setData(listClient);
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<ClientDTO> convertToDTO(List<Client> lista) {
        List<ClientDTO> listaDTO = new ArrayList<>();
        for (Client pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            String companyIDNumber,
            String name,
            String TIN) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Client> root = c.from(Client.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Client_.id),
                    cb.parameter(String.class, "id")));
        }
        if (companyIDNumber != null && companyIDNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Client_.companyIDNumber)),
                    cb.parameter(String.class, "companyIDNumber")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Client_.name)),
                    cb.parameter(String.class, "desc")));
        }
        if (TIN != null && TIN.isEmpty() == false) {
            criteria.add(cb.like(
                            root.get(Client_.TIN),
                            cb.parameter(String.class, "TIN"))
            );
        }

        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (companyIDNumber != null && companyIDNumber.isEmpty() == false) {
            q.setParameter("companyIDNumber", companyIDNumber.toUpperCase() + "%");
        }
        if (name != null && name.isEmpty() == false) {
            q.setParameter("name", name.toUpperCase() + "%");
        }
        if (TIN != null && TIN.isEmpty() == false) {
            q.setParameter("TIN", TIN);
        }

        return q.getSingleResult();
    }

    private List<Client> search(EntityManager em,
                                Integer id,
                                String companyIDNumber,
                                String name,
                                String TIN,
                                int first,
                                int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(Client_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (companyIDNumber != null && companyIDNumber.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Client_.companyIDNumber)),
                    cb.parameter(String.class, "companyIDNumber")));
        }
        if (name != null && name.isEmpty() == false) {
            criteria.add(cb.like(cb.upper(root.get(Client_.name)),
                    cb.parameter(String.class, "desc")));
        }
        if (TIN != null && TIN.isEmpty() == false) {
            criteria.add(cb.like(root.get(Client_.TIN),
                    cb.parameter(String.class, "TIN")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(Client_.companyIDNumber)));
        TypedQuery<Client> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (companyIDNumber != null && companyIDNumber.isEmpty() == false) {
            typedQuery.setParameter("companyIDNumber", companyIDNumber.toUpperCase() + "%");
        }
        if (name != null && name.isEmpty() == false) {
            typedQuery.setParameter("desc", name.toUpperCase() + "%");
        }
        if (TIN != null && TIN.isEmpty() == false) {
            typedQuery.setParameter("TIN", TIN);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<ClientDTO> readAll(Integer id,
                                   String companyIDNumber,
                                   String name,
                                   String TIN) {
        try {
            return convertToDTO(this.search(dao, id, companyIDNumber, name, TIN, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new SystemException(
                    Utils.getMessage("Client.PersistenceEx.ReadAll"), ex);
        }
    }
}


