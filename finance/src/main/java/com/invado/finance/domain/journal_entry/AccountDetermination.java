  /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import static com.invado.finance.Utils.getMessage;
/**
 *
 * @author Vlada
 */
public enum AccountDetermination {

    GENERAL_LEDGER,
    SUPPLIERS,
    CUSTOMERS;

    public String getDescription() {
        switch(this) {
            case CUSTOMERS : return getMessage("Account.Determination.Customer");
            case GENERAL_LEDGER : return getMessage("Account.Determination.Ledger");
            case SUPPLIERS : return getMessage("Account.Determination.Supplier");
            default : return "";
        }
    }
    
}
