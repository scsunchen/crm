package com.invado.masterdata.service;

import com.invado.core.domain.*;
import com.invado.core.dto.BusinessPartnerDocumentDTO;
import com.invado.core.exception.*;
import com.invado.core.exception.EntityExistsException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.IllegalArgumentException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Nikola on 24/12/2015.
 */
@Service
public class BusinessPartnerDocumentService {
    private static final Logger LOG = Logger.getLogger(
            BusinessPartnerDocument.class.getName());

    @PersistenceContext(name = "baza")
    private EntityManager dao;

    @Inject
    private Validator validator;
    private final String username = "a";


    @Transactional(rollbackFor = Exception.class)
    public BusinessPartnerDocument create(BusinessPartnerDocumentDTO a) throws IllegalArgumentException,
            EntityExistsException, ConstraintViolationException {
        //check CreateBusinessPartnerDocumentPermission
        if (a == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx"));
        }
        if (a.getBusinessPartnerOwnerId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx.Owner"));
        }
        if (a.getTypeId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx.Type"));
        }
        if (a.getStatus() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx.Status"));
        }
        try {
            BusinessPartnerDocument businessPartnerDocument = new BusinessPartnerDocument();

            businessPartnerDocument.setStatus(a.getStatus());
            businessPartnerDocument.setDescription(a.getDescription());
            businessPartnerDocument.setBusinessPartnerOwner(dao.find(BusinessPartner.class, a.getBusinessPartnerOwnerId()));
            if (a.getTypeId() != null)
                businessPartnerDocument.setType(dao.find(DocumentType.class, a.getTypeId()));
            businessPartnerDocument.setInputDate(a.getInputDate());
            businessPartnerDocument.setValidUntil(a.getValidUntil());
            businessPartnerDocument.setFile(a.getFile());
            businessPartnerDocument.setFileName(a.getFileName());
            businessPartnerDocument.setFileContentType(a.getFileContentType());
            List<String> msgs = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (msgs.size() > 0) {
                throw new IllegalArgumentException("", msgs);
            }
            dao.persist(businessPartnerDocument);
            return businessPartnerDocument;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new com.invado.masterdata.service.exception.SystemException(
                    Utils.getMessage("BusinessPartnerDocument.PersistenceEx.Create", ex)
            );
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public BusinessPartnerDocument update(BusinessPartnerDocumentDTO dto) throws ConstraintViolationException,
            EntityNotFoundException, ReferentialIntegrityException {
        //check UpdateBusinessPartnerDocumentPermission
        if (dto == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx"));
        }
        if (dto.getBusinessPartnerOwnerId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx.Owner"));
        }
        if (dto.getTypeId() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx.Type"));
        }
        if (dto.getStatus() == null) {
            throw new ConstraintViolationException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx.Status"));
        }
        try {
            BusinessPartnerDocument item = dao.find(BusinessPartnerDocument.class,
                    dto.getId(),
                    LockModeType.OPTIMISTIC);
            if (item == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerDocument.EntityNotFoundEx",
                                dto.getId())
                );
            }

            item.setStatus(dto.getStatus());
            item.setDescription(dto.getDescription());
            item.setBusinessPartnerOwner(dao.find(BusinessPartner.class, dto.getBusinessPartnerOwnerId()));
            if (dto.getTypeId() != null)
                item.setType(dao.find(DocumentType.class, dto.getTypeId()));
            item.setInputDate(dto.getInputDate());
            item.setValidUntil(dto.getValidUntil());
            item.setFile(dto.getFile());
            item.setFileName(dto.getFileName());
            item.setFileContentType(dto.getFileContentType());
            dao.lock(item, LockModeType.OPTIMISTIC);
            item.setVersion(dto.getVersion());
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
                throw new com.invado.masterdata.service.exception.SystemException(
                        Utils.getMessage("BusinessPartnerDocument.OptimisticLockEx",
                                dto.getId()),
                        ex
                );
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new com.invado.masterdata.service.exception.SystemException(
                        Utils.getMessage("BusinessPartnerDocument.PersistenceEx.Update"),
                        ex);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws IllegalArgumentException,
            com.invado.masterdata.service.exception.ReferentialIntegrityException {
        //TODO : check DeleteBusinessPartnerDocumentPermission
        if (id == null) {
            throw new IllegalArgumentException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx.Id")
            );
        }
        try {
            BusinessPartnerDocument businessPartnerDocument = dao.find(BusinessPartnerDocument.class, id);
            if (businessPartnerDocument != null) {
                dao.remove(businessPartnerDocument);
                dao.flush();
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new com.invado.masterdata.service.exception.SystemException(
                    Utils.getMessage("BusinessPartnerDocument.PersistenceEx.Delete"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BusinessPartnerDocument read(Integer id) throws EntityNotFoundException {
        //TODO : check ReadBusinessPartnerDocumentPermission
        if (id == null) {
            throw new EntityNotFoundException(
                    Utils.getMessage("BusinessPartnerDocument.IllegalArgumentEx.Id")
            );
        }
        try {
            BusinessPartnerDocument BusinessPartnerDocument = dao.find(BusinessPartnerDocument.class, id);
            if (BusinessPartnerDocument == null) {
                throw new EntityNotFoundException(
                        Utils.getMessage("BusinessPartnerDocument.EntityNotFoundEx", id)
                );
            }
            return BusinessPartnerDocument;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new com.invado.masterdata.service.exception.SystemException(
                    Utils.getMessage("BusinessPartnerDocument.PersistenceEx.Read"),
                    ex);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReadRangeDTO<BusinessPartnerDocumentDTO> readPage(PageRequestDTO p)
            throws com.invado.masterdata.service.exception.PageNotExistsException {
        //TODO : check ReadBusinessPartnerDocumentPermission
        Integer id = null;
        DocumentType type = null;
        BusinessPartner businessPartnerOwner = null;
        BusinessPartnerDocument.DocumentStatus status = null;
        for (PageRequestDTO.SearchCriterion s : p.readAllSearchCriterions()) {
            if (s.getKey().equals("id") && s.getValue() instanceof Integer) {
                id = (Integer) s.getValue();
            }
            if (s.getKey().equals("type") && s.getValue() instanceof DocumentType) {
                type = (DocumentType) s.getValue();
            }
            if (s.getKey().equals("businessPartnerOwner")) {
                if (s.getValue() instanceof BusinessPartner) {
                    businessPartnerOwner = (BusinessPartner) s.getValue();
                } else if (s.getValue() instanceof Integer) {
                    businessPartnerOwner = dao.find(BusinessPartner.class, s.getValue());
                }
            }
            if (s.getKey().equals("status")) {
                if (s.getValue() instanceof BusinessPartnerDocument.DocumentStatus) {
                    status = (BusinessPartnerDocument.DocumentStatus) s.getValue();
                }
            }
        }

        try {
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();

            Long countEntities = this.count(dao, id, type, businessPartnerOwner, status);
            Long numberOfPages = (countEntities != 0 && countEntities % pageSize == 0)
                    ? (countEntities / pageSize - 1) : countEntities / pageSize;
            Integer pageNumber = p.getPage();
            if (pageNumber.compareTo(-1) == -1
                    || pageNumber.compareTo(numberOfPages.intValue()) == 1) {
                //page number cannot be less than -1 or greater than numberOfPages
                throw new com.invado.masterdata.service.exception.PageNotExistsException(
                        Utils.getMessage("BusinessPartnerDocument.PageNotExists", pageNumber));
            }
            ReadRangeDTO<BusinessPartnerDocumentDTO> result = new ReadRangeDTO<>();
            if (pageNumber.equals(-1)) {
                //if page number is -1 read last page
                //first BusinessPartnerDocument = last page number * BusinessPartnerDocuments per page
                int start = numberOfPages.intValue() * pageSize;

                result.setData(convertToDTO(this.search(dao, id, type, businessPartnerOwner, status, start, pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(numberOfPages.intValue());
            } else {
                result.setData(convertToDTO(this.search(dao, id, type, businessPartnerOwner, status,
                        p.getPage() * pageSize,
                        pageSize)));
                result.setNumberOfPages(numberOfPages.intValue());
                result.setPage(pageNumber);
            }
            return result;
        } catch (com.invado.masterdata.service.exception.PageNotExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new com.invado.masterdata.service.exception.SystemException(
                    Utils.getMessage("BusinessPartnerDocument.PersistenceEx.ReadPage", ex)
            );
        }
    }

    private List<BusinessPartnerDocumentDTO> convertToDTO(List<BusinessPartnerDocument> lista) {
        List<BusinessPartnerDocumentDTO> listaDTO = new ArrayList<>();
        for (BusinessPartnerDocument pr : lista) {
            listaDTO.add(pr.getDTO());
        }
        return listaDTO;
    }

    private Long count(
            EntityManager EM,
            Integer id,
            DocumentType type,
            BusinessPartner businessPartnerOwner,
            BusinessPartnerDocument.DocumentStatus status) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<BusinessPartnerDocument> root = c.from(BusinessPartnerDocument.class);
        c.select(cb.count(root));
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerDocument_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (status != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerDocument_.status),
                    cb.parameter(BusinessPartnerDocument.DocumentStatus.class, "status")));
        }
        if (businessPartnerOwner != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerDocument_.businessPartnerOwner),
                    cb.parameter(BusinessPartner.class, "businessPartnerOwner")));
        }
        if (type != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerDocument_.type),
                    cb.parameter(DocumentType.class, "type")));
        }
        c.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Long> q = EM.createQuery(c);
        if (id != null) {
            q.setParameter("id", id);
        }
        if (status != null) {
            q.setParameter("status", status);
        }
        if (businessPartnerOwner != null) {
            q.setParameter("businessPartnerOwner", businessPartnerOwner);
        }
        if (type != null) {
            q.setParameter("type", type);
        }


        return q.getSingleResult();
    }

    private List<BusinessPartnerDocument> search(EntityManager em,
                                                 Integer id,
                                                 DocumentType type,
                                                 BusinessPartner businessPartnerOwner,
                                                 BusinessPartnerDocument.DocumentStatus status,
                                                 int first,
                                                 int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusinessPartnerDocument> query = cb.createQuery(BusinessPartnerDocument.class);
        Root<BusinessPartnerDocument> root = query.from(BusinessPartnerDocument.class);
        query.select(root);
        List<Predicate> criteria = new ArrayList<>();
        if (id != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerDocument_.id),
                    cb.parameter(Integer.class, "id")));
        }
        if (status != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerDocument_.status),
                    cb.parameter(BusinessPartnerDocument.DocumentStatus.class, "status")));
        }
        if (businessPartnerOwner != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerDocument_.businessPartnerOwner),
                    cb.parameter(BusinessPartner.class, "businessPartnerOwner")));
        }
        if (type != null) {
            criteria.add(cb.equal(root.get(BusinessPartnerDocument_.type),
                    cb.parameter(DocumentType.class, "type")));
        }

        query.where(criteria.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(BusinessPartnerDocument_.id)));
        TypedQuery<BusinessPartnerDocument> typedQuery = em.createQuery(query);
        if (id != null) {
            typedQuery.setParameter("id", id);
        }
        if (status != null) {
            typedQuery.setParameter("status", status);
        }
        if (businessPartnerOwner != null) {
            typedQuery.setParameter("businessPartnerOwner", businessPartnerOwner);
        }
        if (type != null) {
            typedQuery.setParameter("type", type);
        }

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BusinessPartnerDocumentDTO> readAll(
            Integer id,
            DocumentType type,
            BusinessPartner businessPartnerOwner,
            BusinessPartnerDocument.DocumentStatus status) {
        try {
            return convertToDTO(this.search(dao, id,
                    type,
                    businessPartnerOwner,
                    status, 0, 0));
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "",
                    ex);
            throw new com.invado.masterdata.service.exception.SystemException(
                    Utils.getMessage("BusinessPartnerDocument.PersistenceEx.ReadAll"), ex);
        }
    }

    private void readFile() {
        /*
        File file = fc.getSelectedFile();
        path = file.toPath();
        InputStream pathInputStream = Files.newInputStream(path);
        BufferedImage image = ImageIO.read(pathInputStream);
        if (image == null) {
            Dialogs.showError(getWindowAncestor(ClientPanel.this),
                    ClientBundle.getFormattedString(
                            "ImageIOException",
                            path.toAbsolutePath()));
            return;
        }
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", bas);
        byte[] data = bas.toByteArray();
        UpdateLogoImageResultDTO result = Services
                .get(ClientServices.class)
                .updateClientLogo(getID(), data);
                */

    }

    public List<BusinessPartnerDocument.DocumentStatus> getDocumentStatuses() {
        return Arrays.asList(BusinessPartnerDocument.DocumentStatus.values());
    }

}
