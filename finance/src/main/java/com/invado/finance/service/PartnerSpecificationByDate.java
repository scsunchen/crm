/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;


import com.invado.core.domain.*;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.*;
import com.invado.finance.service.dto.PartnerSpecificationDTO;
import com.invado.finance.service.dto.ReadSpecificationsDTO;
import com.invado.finance.service.dto.StavkaSpecifikacijeDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.service.dto.RequestPartnerSpecificationDTO;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartnerSpecificationByDate  {

    private static final Logger LOG = Logger.getLogger(
            PartnerSpecificationByDate.class.getName());
    
    @PersistenceContext(name = "unit")
    private EntityManager dao;
    
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReadSpecificationsDTO readPartnerSpecificationByDate(
            RequestPartnerSpecificationDTO DTO)
            throws EntityNotFoundException {
        if (DTO.getClientID() == null) {
            throw new EntityNotFoundException(
                    "PartnerSpecification.IllegalArgument.ClientId");
        }
        try {
            //Proveri da li entiteti postoje u bazi podataka********************
            Client client = dao.find(Client.class, DTO.getClientID());
            if (client == null) {
                throw new EntityNotFoundException(
                        getMessage("PartnerSpecification.ClientNotExists"));
            }
            if (DTO.getOrgUnitID() != null && 
                    dao.find(OrgUnit.class, DTO.getOrgUnitID()) == null) {
                throw new EntityNotFoundException(
                        getMessage("PartnerSpecification.OrgUnitNotExists",
                        DTO.getClientID(), DTO.getOrgUnitID()));
            }

            if (DTO.getAccountNumber() != null
                    && DTO.getAccountNumber().isEmpty() == false
                    && dao.find(Account.class, DTO.getAccountNumber()) == null) {
                throw new EntityNotFoundException(
                        getMessage("PartnerSpecification.AccountNotExists",
                        DTO.getAccountNumber()));
            }

            if (DTO.getPartnerRegNo() != null
                    && DTO.getPartnerRegNo().isEmpty() == false
                    && dao.find(BusinessPartner.class, DTO.getPartnerRegNo()) == null) {
                throw new EntityNotFoundException(
                        getMessage("PartnerSpecification.BusinessPartnerNotExists",
                        DTO.getPartnerRegNo()));
            }
            //******************************************************************

            List<Analytical> list = this.readAnalytics(dao,
                    DTO.getClientID(),
                    DTO.getOrgUnitID(),
                    DTO.getAccountNumber(),
                    DTO.getPartnerRegNo(),
                    DTO.getCreditDebitRelationDateFrom(),
                    DTO.getCreditDebitRelationDateTo(),
                    DTO.getValueDateFrom(),
                    DTO.getValueDateTo());
            Integer pageSize = dao.find(ApplicationSetup.class, 1).getPageSize();
            //LinkedHashMap will iterate in the order in which the entries were 
            //put into the map
            Map<String, List<Analytical>> accountNumberAnalytics =
                    new LinkedHashMap<>();
            /*
             * Store the previous item, and compare it to 
             * the current. if the previous is different from the current 
             * (using equals(..) then use list.subList(groupStart, currentIdx)                 
             */
            String previous = null;
            int groupStart = 0;
            for (int i = 0; i < list.size(); i++) {
                Analytical current = list.get(i);
                if (i == 0) {
                    previous = current.getAccountNumber();
                }
                if (current.getAccountNumber().equals(previous) == false) {
                    //ovde ne moze da bude situacija i = 0 
                    //prilikom poziva metode sublist poslednji u navedenom opsegu sublist(od,do)
                    //nije uklucen u listu i zato stavljam i a ne i-1                     
                    accountNumberAnalytics.put(previous,
                            list.subList(groupStart, i));
                    groupStart = i;
                }
                //ako je poslednji u nizu dodaj njegovu grupu bezuslovno
                //jer sledeci konto ne postoji
                if (i == list.size() - 1) {
                    //prilikom poziva metode sublist poslednji u navedenom opsegu sublist(od,do)
                    //nije uklucen u listu i zato stavljam i+1 a ne i                     
                    accountNumberAnalytics.put(current.getAccountNumber(),
                            list.subList(groupStart, i + 1));
                }
                previous = current.getAccountNumber();
            }

            ReadSpecificationsDTO result = new ReadSpecificationsDTO();
            List<PartnerSpecificationDTO> specList = new ArrayList<>();
            Integer numberOfPages = 0;
            for (String account : accountNumberAnalytics.keySet()) {
                PartnerSpecificationDTO dto = this.getPartnerSpecificationDTO(
                        dao,
                        DTO.getClientID(),
                        client.getName(),
                        DTO.getOrgUnitID(),
                        account,
                        DTO.getPartnerRegNo(),
                        DTO.getCreditDebitRelationDateFrom(),
                        DTO.getCreditDebitRelationDateTo(),
                        DTO.getValueDateFrom(),
                        DTO.getValueDateTo(),
                        pageSize,
                        accountNumberAnalytics.get(account));
                specList.add(dto);
                numberOfPages += dto.numberOfPages;
            }

            // number of pages is zero based so subtract 1
            result.numberOfPages = numberOfPages - 1;
            result.pageSize = pageSize;
            result.specifications = specList;
            return result;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new SystemException(getMessage("PartnerSpecification.Persistence"));
        } 
    }

    private PartnerSpecificationDTO getPartnerSpecificationDTO(
            EntityManager dao,
            Integer clientID,
            String clientName,
            Integer orgUnitID,
            String accountNumber,
            String partnerID,
            LocalDate creditDebitRelationDateFrom,
            LocalDate creditDebitRelationDateTo,
            LocalDate valueDateFrom,
            LocalDate valueDateTo,
            Integer pageSize,
            List<Analytical> items) {
        PartnerSpecificationDTO dto = new PartnerSpecificationDTO();
        dto.clientName = clientName;
        dto.accountNumber = accountNumber;
        dto.orgUnitID = orgUnitID;
        if (orgUnitID != null) {
            dto.orgUnitName = dao.find(OrgUnit.class,orgUnitID).getName();
        } else {
            dto.orgUnitName = "";
        }
        dto.partnerID = partnerID;
        if (partnerID != null && partnerID.isEmpty() == false) {
            dto.partnerName = dao.find(BusinessPartner.class, partnerID).getName();
        } else {
            dto.partnerName = "";
        }
        dto.creditDebitRelationDateFrom = creditDebitRelationDateFrom;
        dto.creditDebitRelationDateTo = creditDebitRelationDateTo;
        dto.valueDateFrom = valueDateFrom;
        dto.valueDateTo = valueDateTo;
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;
        List<Analytical> obradjeni = new ArrayList<>();
        for (Analytical analitika : items) {
            if (obradjeni.contains(analitika) == true) {
                continue;
            }
            BigDecimal debit = BigDecimal.ZERO;
            BigDecimal credit = BigDecimal.ZERO;
            for (Analytical analitika1 : items) {

                if (analitika.getAccountNumber().equals(analitika1.getAccountNumber()) == true
                        && analitika.getOrgUnitID().equals(analitika1.getOrgUnitID()) == true
                        && analitika.getPartnerCompanyID().equals(analitika1.getPartnerCompanyID()) == true) {
                    debit = debit.add(analitika1.getDebit());
                    credit = credit.add(analitika1.getCredit());
                    obradjeni.add(analitika1);
                }
            }
            debitTotal = debitTotal.add(debit);
            creditTotal = creditTotal.add(credit);

            StavkaSpecifikacijeDTO item = new StavkaSpecifikacijeDTO();
            item.idOrgJedinice = analitika.getOrgUnitID();
            item.businessPartnerName = analitika.getPartnerName();
            item.businessPartnerRegNo = analitika.getPartnerCompanyID();
            item.sifraKonta = analitika.getAccountNumber();
            item.credit = credit;
            item.debit = debit;
            item.balance = debit.subtract(credit);
            dto.items.add(item);
        }
        Integer numberOfItems = dto.items.size() + 1;
        dto.numberOfPages = ((numberOfItems != 0 && numberOfItems % pageSize == 0)
                ? numberOfItems / pageSize
                : numberOfItems / pageSize + 1);
        dto.debitTotal = debitTotal;
        dto.creditTotal = creditTotal;
        dto.balanceTotal = debitTotal.subtract(creditTotal);
        return dto;
    }

    public List<Analytical> readAnalytics(EntityManager EM,
            Integer clientId,
            Integer orgUnitId,
            String accountCode,
            String partnerRegNumber,
            LocalDate creditDebitRelationDateFrom,
            LocalDate creditDebitRelationDateTo,
            LocalDate valueDateFrom,
            LocalDate valueDateTo) {
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        CriteriaQuery<Analytical> query = cb.createQuery(Analytical.class);
        Root<Analytical> root = query.from(Analytical.class);
        Join<Analytical, OrgUnit> orgUnitJoin =
                root.join(Analytical_.orgUnit, JoinType.LEFT);
        query.select(root);
        Predicate criteria = cb.conjunction();
        criteria = cb.and(criteria, cb.equal(orgUnitJoin.get(OrgUnit_.client)
                .get(Client_.id),
                cb.parameter(Integer.class, "client")));
        if (orgUnitId != null) {
            criteria = cb.and(criteria, cb.equal(orgUnitJoin.get(OrgUnit_.id),
                    cb.parameter(Integer.class, "orgUnit")));
        }
        if (accountCode != null && accountCode.equals("") == false) {
            criteria = cb.and(criteria, cb.equal(root.get(Analytical_.account)
                    .get(Account_.number),
                    cb.parameter(String.class, "account")));
        }
        if (partnerRegNumber != null && partnerRegNumber.equals("") == false) {
            criteria = cb.and(criteria, cb.equal(root.get(Analytical_.partner)
                    .get(BusinessPartner_.companyIdNumber),
                    cb.parameter(String.class, "partnerRegNumber")));
        }
        if (creditDebitRelationDateFrom != null) {
            criteria = cb.and(criteria,
                    cb.greaterThanOrEqualTo(root.get(Analytical_.creditDebitRelationDate),
                    cb.parameter(LocalDate.class, "creditDebitRelationDateFrom")));
        }
        if (creditDebitRelationDateTo != null) {
            criteria = cb.and(criteria,
                    cb.lessThanOrEqualTo(root.get(Analytical_.creditDebitRelationDate),
                    cb.parameter(LocalDate.class, "creditDebitRelationDateTo")));
        }
        if (valueDateFrom != null) {
            criteria = cb.and(criteria,
                    cb.greaterThanOrEqualTo(root.get(Analytical_.valueDate),
                    cb.parameter(LocalDate.class, "valueDateFrom")));
        }
        if (valueDateTo != null) {
            criteria = cb.and(criteria,
                    cb.lessThanOrEqualTo(root.get(Analytical_.valueDate),
                    cb.parameter(LocalDate.class, "valueDateTo")));
        }
        query.where(criteria).orderBy(
                cb.asc(root.get(Analytical_.account).get(Account_.number)),
                cb.asc(orgUnitJoin.get(OrgUnit_.id)),
                cb.asc(root.get(Analytical_.partner).get(BusinessPartner_.companyIdNumber)));
        TypedQuery<Analytical> q = EM.createQuery(query);
        q.setParameter("client", clientId);
        if (orgUnitId != null) {
            q.setParameter("orgUnit", orgUnitId);
        }
        if (accountCode != null && accountCode.isEmpty() == false) {
            q.setParameter("account", accountCode);
        }
        if (partnerRegNumber != null && partnerRegNumber.isEmpty() == false) {
            q.setParameter("partnerRegNumber", partnerRegNumber);
        }
        if (creditDebitRelationDateFrom != null) {
            q.setParameter("creditDebitRelationDateFrom", creditDebitRelationDateFrom);
        }
        if (creditDebitRelationDateTo != null) {
            q.setParameter("creditDebitRelationDateTo", creditDebitRelationDateTo);
        }
        if (valueDateFrom != null) {
            q.setParameter("valueDateFrom", valueDateFrom);
        }
        if (valueDateTo != null) {
            q.setParameter("valueDateTo", valueDateTo);
        }
        return q.getResultList();
    }
    
}