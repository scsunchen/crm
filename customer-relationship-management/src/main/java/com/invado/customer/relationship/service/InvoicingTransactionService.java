package com.invado.customer.relationship.service;

import com.invado.core.domain.InvoicingTransaction;
import com.invado.core.domain.InvoicingTransaction_;
import com.invado.core.dto.InvoicingTransactionDTO;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikola on 16.12.2015.
 */
@Service
public class InvoicingTransactionService {

    @PersistenceContext(name = "unit")
    private EntityManager em;

    public List<InvoicingTransactionDTO> getAllPeriods(){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvoicingTransaction> query = cb.createQuery(InvoicingTransaction.class);
        Root<InvoicingTransaction> root = query.from(InvoicingTransaction.class);
        query.select(root);
        query.orderBy(cb.desc(root.get(InvoicingTransaction_.invoicingDate)));
        TypedQuery<InvoicingTransaction> typedQuery = em.createQuery(query);
        List<InvoicingTransaction> list =  typedQuery.getResultList();
        List<InvoicingTransactionDTO> listDTO = new ArrayList<InvoicingTransactionDTO>();
        for (InvoicingTransaction item : list){
            listDTO.add(item.getDTO());
        }

        return listDTO;
    }
}
