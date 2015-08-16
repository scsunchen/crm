/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import com.invado.finance.domain.journal_entry.AccountType;
import com.invado.finance.domain.journal_entry.AccountDetermination;


/**
 *
 * @author bdragan
 */
public class AccountDTO {

    private String number;
    private String name;
    private AccountDetermination determination;
    private AccountType type;
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

    public AccountDetermination getDetermination() {
        return determination;
    }

    public void setDetermination(AccountDetermination determination) {
        this.determination = determination;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
    
}