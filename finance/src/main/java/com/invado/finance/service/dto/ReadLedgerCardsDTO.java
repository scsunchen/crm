/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.util.List;

/**
 *
 * @author Bobic Dragan
 */
public class ReadLedgerCardsDTO  {
    
    public Integer pageSize;
    public Integer numberOfPages;
    public List<LedgerCardDTO> ledgerCards;
}
