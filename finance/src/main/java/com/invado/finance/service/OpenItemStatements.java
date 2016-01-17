/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.OrgUnit;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.service.dto.OpenItemStatementsDTO;
import com.invado.finance.service.dto.RequestOpenItemStatementsDTO;
import static com.invado.finance.service.dto.RequestOpenItemStatementsDTO.Amount.OPSEG;
import static com.invado.finance.service.dto.RequestOpenItemStatementsDTO.Prikaz.ANALYTIC;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.domain.journal_entry.AccountDetermination;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
/**
 *
 * @author Bobic Dragan
 */
@Service
public class OpenItemStatements {

    private static final Logger LOG = Logger.getLogger(OpenItemStatements.class.getName());

    @PersistenceContext(name = "unit")
    EntityManager dao;

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<OpenItemStatementsDTO> readOpenItemStatements(RequestOpenItemStatementsDTO dto)
            throws EntityNotFoundException,
            ConstraintViolationException {
        this.checkInputArgument(dto);
        List<OpenItemStatementsDTO> listDTO = null;
        try {
            if (dto.getOrgUnitID() != null && dao.find(OrgUnit.class, dto.getOrgUnitID()) == null) {
                throw new EntityNotFoundException(
                        getMessage("OpenItem.OrgUnitNotExists",
                                dto.getClientID(),
                                dto.getOrgUnitID()));
            }
            Account account = dao.find(Account.class, dto.getAccountNumber());
            if (account == null) {
                throw new EntityNotFoundException(
                        getMessage("OpenItem.AccountNotExists", dto.getAccountNumber()));
            }
            if (account.getDetermination() == AccountDetermination.GENERAL_LEDGER) {
                throw new EntityNotFoundException(
                        getMessage("OpenItem.GeneralLedgerAccount"));
            }
            if (dto.getPartnerID() != null && dao.find(BusinessPartner.class,
                    dto.getPartnerID()) == null) {
                throw new EntityNotFoundException(
                        getMessage("OpenItem.BusinessPartnerNotExists",
                                dto.getPartnerID()));
            }
            List<Analytical> list = this.readOpenItemsFromDB(dao, dto);
            for (Analytical analytic : list) {
                dao.lock(analytic, LockModeType.OPTIMISTIC);
            }

            listDTO = this.convertAnalyticsIntoDTO(dao, dto, list);
            return listDTO;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(getMessage("OpenItem.OptimisticLock"));
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(getMessage("OpenItem.Persistence"));
            }
        }
    }

    private void checkInputArgument(RequestOpenItemStatementsDTO dto)
            throws ConstraintViolationException {
        List<String> lista = new ArrayList<>();
        if (dto.getClientID() == null) {
            lista.add(getMessage("OpenItem.IllegalArgument.ClientId"));
        }
        if (dto.getValueDate() == null) {
            lista.add(getMessage("OpenItem.IllegalArgument.ValueDate"));
        }
        if (dto.getAccountNumber() == null || dto.getAccountNumber().equals("")) {
            lista.add(getMessage("OpenItem.IllegalArgument.Account"));
        }
        if (dto.getI() == OPSEG && (dto.getMax() == null || dto.getMin() == null)) {
            lista.add(getMessage("OpenItem.IllegalArgument.Amount"));
        }
        if (dto.getP() == null) {
            lista.add(getMessage("OpenItem.IllegalArgument.ReadType"));
        }
        if (!lista.isEmpty()) {
            throw new ConstraintViolationException(
                    getMessage("OpenItem.IllegalArgument"),
                    lista.toArray(new String[0]));
        }
    }

    private List<Analytical> readOpenItemsFromDB(EntityManager EM, RequestOpenItemStatementsDTO dto) {
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Unos konta je obavezan(u formi)
        String queryCondition = "WHERE x.status = ?1 AND o.client.id = "
                + dto.getClientID() + " AND k.number ='"
                + escapeSQLInjectionCharacters(dto.getAccountNumber()) + "'";
        if (dto.getOrgUnitID() != null) {
            queryCondition = queryCondition + " AND o.id = "
                    + dto.getOrgUnitID();
        }
        if (dto.getPartnerID() != null ) {
            queryCondition = queryCondition + " AND s.id = "+ dto.getPartnerID() ;
        }
        if (dto.getValueDate() != null) {
            queryCondition = queryCondition + " AND x.valueDate <=  ?2";
        }
        if (dto.getI() == OPSEG) {
            queryCondition = queryCondition + " AND x.amount >= " + dto.getMin()
                    + " AND x.amount <= " + dto.getMax();
        }
        return this.vratiStavke(EM, queryCondition, Analytical.Status.OPEN, dto.getValueDate());
    }

    private List<Analytical> vratiStavke(EntityManager EM, String uslov, Object... s) {
        // FIXME : use Criteria API
//        String upit1  = "SELECT x FROM Analytical x JOIN x.account k JOIN x.partner s JOIN x.orgUnit o  " + uslov
//                + " ORDER BY s.companyIdNumber, k.number, x.recordDate";
//        System.out.println(upit1);
        Query upit = EM.createQuery("SELECT x FROM Analytical x JOIN x.account k JOIN x.partner s JOIN x.orgUnit o  " + uslov
                + " ORDER BY s.id, k.number, x.recordDate");
        for (int i = 0; i < s.length; i++) {
            upit.setParameter(i + 1, s[i]);
        }
        return upit.getResultList();
    }

    private String escapeSQLInjectionCharacters(String value) {
        String result = new String(value);
        result = result.replaceAll("\\\\", "\\\\\\\\");
        result = result.replaceAll("\\'", "\\\\'");
        result = result.replaceAll("\\\"", "\\\\\"");
        result = result.replaceAll("\\;", "\\\\;");
        result = result.replaceAll("\\?", "\\\\?");
        return result;
    }

//****************************KREIRAJ DTO*************************************//
    private List<OpenItemStatementsDTO> convertAnalyticsIntoDTO(
            EntityManager dao,
            RequestOpenItemStatementsDTO dto,
            List<Analytical> analytics) {
        List<OpenItemStatementsDTO> result = new ArrayList<>();
        Client client = dao.find(Client.class, dto.getClientID());
        Account account = dao.find(Account.class, dto.getAccountNumber());

        if (dto.getPartnerID() != null) {
            //pravi se izvod za jednog poslovnog partnera      
            BusinessPartner businessPartner = dao.find(BusinessPartner.class,
                    dto.getPartnerID());
            OpenItemStatementsDTO DTO = getOpenItemStatementsDTO(
                    account,
                    businessPartner,
                    client);
            this.getItems(DTO, dto.getP(), analytics);
            result.add(DTO);
            return result;
        }

        //pravi se izvod za sve poslovne partnere
        Map<Integer, List<Analytical>> map = groupItemsByBusinessPartner(analytics);
        Iterator<Integer> it = map.keySet().iterator();
        while (it.hasNext()) {
            Integer partnerID = it.next();
            OpenItemStatementsDTO DTO = getOpenItemStatementsDTO(account,
                    dao.find(BusinessPartner.class, partnerID),
                    client);
            this.getItems(DTO, dto.getP(), map.get(partnerID));
            result.add(DTO);
        }
        return result;
    }

    //Postavlja podatke za konto, poverioca i duznika (poslovni partneri)
    private OpenItemStatementsDTO getOpenItemStatementsDTO(Account account,
            BusinessPartner partnerID,
            Client client) {
        OpenItemStatementsDTO dto = new OpenItemStatementsDTO();
        dto.accountNumber = account.getNumber();
        dto.accountName = account.getDescription();
        dto.debtorCompanyID = partnerID.getCompanyIdNumber();
        dto.debtorName = partnerID.getName();
        dto.debtorTIN = partnerID.getTIN();
        dto.debtorStreet = partnerID.getStreet();
        dto.debtorPlace = partnerID.getPlace();
        dto.debtorPostCode = partnerID.getPostCode();
        dto.creditorCompanyID = client.getCompanyIDNumber();
        dto.creditorTIN = client.getTIN();
        dto.creditorName = client.getName();
        dto.creditorPlace = client.getPlace();
        dto.creditorStreet = client.getStreet();
        dto.creditorPostCode = client.getPostCode();
        dto.creditorPhone = client.getPhone();
        dto.creditorCurrentAccount = client.getBankAccount();
        return dto;
    }

    private Map<Integer, List<Analytical>> groupItemsByBusinessPartner(
            List<Analytical> stavke) {
        Map<Integer, List<Analytical>> map = new HashMap<>();
        for (Analytical a : stavke) {
            if (!map.containsKey(a.getPartner().getId())) {
                List<Analytical> lista = new ArrayList<>();
                lista.add(a);
                for (Analytical a1 : stavke) {
                    if (a.getPartner().getId().equals(a1.getPartner().getId()) && !a.equals(a1)) {
                        lista.add(a1);
                    }
                }
                map.put(a.getPartner().getId(), lista);
            }
        }
        return map;
    }

    private void getItems(OpenItemStatementsDTO dto,
            RequestOpenItemStatementsDTO.Prikaz prikaz,
            List<Analytical> lista) {
        List<OpenItemStatementsDTO.Item> result = new ArrayList<>();
        for (int i = 0, n = lista.size(); i < n; i++) {
            if (prikaz == ANALYTIC) {
                Analytical analytic = lista.get(i);
                OpenItemStatementsDTO.Item openItem = new OpenItemStatementsDTO.Item();
                openItem.ordinalNumber = (i + 1);
                openItem.document = analytic.getDocument();
                openItem.valueDate = analytic.getValueDate();
                if (analytic.getChangeType().equals(ChangeType.DEBIT)) {
                    openItem.debit = analytic.getAmount();
                    openItem.credit = BigDecimal.ZERO;
                } else {
                    openItem.credit = analytic.getAmount();
                    openItem.debit = BigDecimal.ZERO;
                }
                result.add(openItem);
            }
            //postaviUkupno
            dto.totalDebit = dto.totalDebit.add(lista.get(i).getDebit());
            dto.totalCredit = dto.totalCredit.add(lista.get(i).getCredit());
        }
        if (dto.totalDebit.compareTo(dto.totalCredit) > 0) {
            dto.balanceDebit = dto.totalDebit.subtract(dto.totalCredit);
        }
        if (dto.totalDebit.compareTo(dto.totalCredit) < 0) {
            dto.balanceCredit = dto.totalCredit.subtract(dto.totalDebit);
        }
        dto.items = result;
    }

}
