/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;

import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.Analytical;
import com.invado.finance.domain.journal_entry.ChangeType;
import com.invado.finance.domain.journal_entry.GeneralLedger;
import com.invado.finance.domain.journal_entry.JournalEntry;
import com.invado.finance.domain.journal_entry.JournalEntryItem;
import com.invado.finance.domain.journal_entry.JournalEntryPK;
import com.invado.finance.service.exception.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import static com.invado.finance.Utils.getMessage;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Bobic Dragan
 */
@Service
public class RecordJournalEntryService {

    private static final Logger LOG = Logger.getLogger(RecordJournalEntryService.class.getName());

    @PersistenceContext(name = "unit")
    EntityManager EM;
    @Inject
    private Validator validator;
    
    @Transactional(rollbackFor = Exception.class)
    public void recordJournalEntry(Integer clientID,
            Integer typeID,
            Integer number,
            Long version)
            throws JournalEntryNotFoundException,
            JournalEntryAlreadyPostedException,
            JournalEntryNotBalancedException {
        try {
            JournalEntry temp = EM.find(JournalEntry.class,
                    new JournalEntryPK(clientID, typeID, number)
                    , LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (temp == null) {
                throw new JournalEntryNotFoundException(
                        getMessage("JournalEntry.JournalEntryNotExists",
                                clientID,
                                typeID,
                                number));
            }
            temp.setVersion(version);
            if (temp.getPosted() == true) {
                throw new JournalEntryAlreadyPostedException(
                        getMessage("JournalEntry.JournalEntryAlreadyPosted",
                                clientID,
                                typeID,
                                number));
            }
            this.isJournalEntryBalanced(temp);
            this.recordJournalEntry(EM, temp);
            temp.setPosted(Boolean.TRUE);
        } catch (PostingConstraintViolationException ex) {
            LOG.log(Level.WARNING,
                    "JournalEntry.Persistence.Record. Message :" + ex.getMessage()
                    + ", report : " + ex.getReport(), ex);
            throw new SystemException(getMessage("JournalEntry.Persistence.Record"));
        } catch (JournalEntryAlreadyPostedException | JournalEntryNotFoundException | JournalEntryNotBalancedException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(getMessage("JournalEntry.OptimisticLock",
                        clientID,
                        typeID,
                        number));
            } else {
                LOG.log(Level.WARNING,
                        "JournalEntry.Persistence.Record",
                        ex);
                throw new SystemException(getMessage("JournalEntry.Persistence.Record"));
            }
        } 
    }

    private void isJournalEntryBalanced(JournalEntry journalEntry)
            throws JournalEntryNotBalancedException {
        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;
        for (JournalEntryItem item : journalEntry.getItems()) {
            if (item.getChangeType() == ChangeType.DEBIT) {
                debit = debit.add(item.getAmount());
            }

            if (item.getChangeType() == ChangeType.CREDIT) {
                credit = credit.add(item.getAmount());
            }
        }
        if (debit.compareTo(credit) != 0) {
            throw new JournalEntryNotBalancedException(
                    getMessage("JournalEntry.AccountingInequality"));
        }
    }

    private void recordJournalEntry(EntityManager EM, JournalEntry journalEntry) {
        List<JournalEntryItem> itemsList = journalEntry.getItems();

        for (int i = 0, n = itemsList.size(); i < n; i++) {
            this.createAnalyticAndGLItems(EM, itemsList.get(i));
        }

        List<JournalEntryItem> zbirneStavke = new ArrayList<>();
        for (int i = 0, n = itemsList.size(); i < n; i++) {
            this.createGLSumItems(EM,
                    itemsList.get(i),
                    journalEntry.getItems(),
                    zbirneStavke);
        }

    }

    private void createAnalyticAndGLItems(EntityManager EM,
            JournalEntryItem stavka) {
        if (stavka.isGL()) {
            GeneralLedger GL = new GeneralLedger(stavka);
            List<String> listaGK = validator.validate(GL).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            if (!listaGK.isEmpty()) {
                //poruka ide samo u log
                throw new PostingConstraintViolationException(
                        "GenaralLedger constraint violation", listaGK);
            }
            EM.persist(GL);
            return;
        }
        Analytical analytic = new Analytical(stavka);
        List<String> list = validator.validate(analytic).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
        if (!list.isEmpty()) {
            //poruka ide samo u log
            throw new PostingConstraintViolationException("Analytics constraint violation",
                    list);
        }
        EM.persist(analytic);
    }

    //Tested
    private void createGLSumItems(EntityManager EM,
            JournalEntryItem journalEntryItem,
            List<JournalEntryItem> itemsList,
            List<JournalEntryItem> zbirneStavke) {
        if (zbirneStavke.contains(journalEntryItem)
                || journalEntryItem.isGL()) {
            return;
        }
        GeneralLedger GL = new GeneralLedger();
        GL.setGLSumItem(journalEntryItem);
        for (int i = 0, n = itemsList.size(); i < n; i++) {
            //ako nije ista stavka i ako je isto troskovno mesto, konto,
            //tip promene i datumDPO. Obzirom da je uvek isti nalog knjizenja ne
            //proveravam idtipa naloga i broj naloga
            if (!itemsList.get(i).isGL() &&//nije pripadnost stavke gl. knjiga
                    !journalEntryItem.equals(itemsList.get(i))
                    && journalEntryItem.isEqualForGLSumItem(itemsList.get(i))) {
                GL.addToAmount(itemsList.get(i).getAmount());
                zbirneStavke.add(itemsList.get(i));
            }
        }
        List<String> list = validator.validate(GL).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
        if (!list.isEmpty()) {
            //poruka ide samo u log
            throw new PostingConstraintViolationException("Leneral ledger sum item "
                    + "contraint violation", list);
        }
        EM.persist(GL);
    }

}
