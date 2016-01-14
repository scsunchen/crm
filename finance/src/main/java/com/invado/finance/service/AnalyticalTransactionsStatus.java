/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service;


import com.invado.core.exception.SystemException;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.domain.journal_entry.Analytical;
import static com.invado.finance.domain.journal_entry.ChangeType.CREDIT;
import static com.invado.finance.domain.journal_entry.ChangeType.DEBIT;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bobic Dragan
 */
@Service
public class AnalyticalTransactionsStatus {
    
    private static final Logger LOG = Logger.getLogger(AnalyticalTransactionsStatus.class.getName());
    
    @PersistenceContext(name = "unit")
    EntityManager EM;

    @Transactional(rollbackFor = Exception.class)
    public void openAllItems()  {
        try {
            List<Analytical> list = EM.createNamedQuery(
                    Analytical.READ_ALL_ORDERBY_ID).getResultList();
            for (Analytical a : list) {
                EM.lock(a, LockModeType.OPTIMISTIC);
            }
            for (Analytical analytic : list) {
                analytic.setStatus(Analytical.Status.OPEN);
            }
        } catch (Exception e) {
            if (e instanceof OptimisticLockException
                    || e.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        getMessage("JournalEntry.OptimisticLock.OpenItems"));
            } else {
                LOG.log(Level.WARNING, "", e);
                throw new SystemException(
                        getMessage("JournalEntry.Persistence.OpenItems"));
            }
        } 
    }

    @Transactional(rollbackFor = Exception.class)
    public void closeItems()  {
        try {
            List<Analytical> list = EM.createNamedQuery(Analytical.READ_BY_STATUS, 
                                                        Analytical.class)
                    .setParameter(1, Analytical.Status.OPEN)
                    .getResultList();
            for (Analytical analytic : list) {
                EM.lock(analytic, LockModeType.OPTIMISTIC);
            }
            this.closeItems(EM, list);
        } catch (Exception ex) {
            if (ex instanceof OptimisticLockException
                    || ex.getCause() instanceof OptimisticLockException) {
                throw new SystemException(
                        getMessage("JournalEntry.OptimisticLock.CloseStatements"));
            } else {
                LOG.log(Level.WARNING, "", ex);
                throw new SystemException(
                        getMessage("JournalEntry.Persistence.CloseItems"));
            }
        } 
    }

    private void closeItems(EntityManager EM, List<Analytical> list) {
        List<Analytical> closedItems = new ArrayList<>();
        List<Analytical> group = null;
        for (int i = 0, n = list.size(); i < n; i++) {
            if (closedItems.contains(list.get(i))) {
                continue;
            }
            group = new ArrayList<>();
            group.add(list.get(i));
            for (int j = i + 1; j < n; j++) {
                if (this.equals(list.get(i), list.get(j))) {
                    group.add(list.get(j));
                }
            }
            closedItems.addAll(group);
            if (this.balanceIsEqualToZero(group)) {
                for (Analytical a1 : group) {
                    EM.find(Analytical.class, a1.getId());
                    a1.setStatus(Analytical.Status.CLOSED);
                }
            }
        }
    }

    private boolean balanceIsEqualToZero(List<Analytical> grupa) {
        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;

        for (Analytical a1 : grupa) {
            switch (a1.getChangeType()) {
                case DEBIT:
                    debit = debit.add(a1.getAmount());
                    break;
                case CREDIT:
                    credit = credit.add(a1.getAmount());
                    break;
            }
        }
        return debit.compareTo(credit) == 0;
    }

    private boolean equals(Analytical a, Analytical a1) {
        if (a.equals(a1)) {
            return false;
        }
        if (a.getAccountNumber().equals(a1.getAccountNumber())
                && a.getPartnerCompanyID().equals(a1.getPartnerCompanyID())
                && a.getDocument().equals(a1.getDocument())) {
            return true;
        }
        return false;
    }
    
}