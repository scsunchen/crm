package com.invado.customer.relationship.service.dto;

import java.math.BigDecimal;

/**
 * Created by Nikola on 14/01/2016.
 */
public class TransactionSummaryElementsDTO {

    private Long count;
    private BigDecimal amount;


    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
