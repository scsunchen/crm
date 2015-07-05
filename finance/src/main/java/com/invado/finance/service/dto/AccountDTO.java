/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.Determination;


/**
 *
 * @author bdragan
 */
public class AccountDTO {

    private String number;
    private String name;
    private Determination determination;
    private Account.Type type;
    private Long version;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Determination getDetermination() {
        return determination;
    }

    public void setDetermination(Determination determination) {
        this.determination = determination;
    }

    public Account.Type getType() {
        return type;
    }

    public void setType(Account.Type type) {
        this.type = type;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
    
}