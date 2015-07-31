/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import static com.invado.finance.Utils.getMessage;

//*****************************************************************************//

public enum AccountType {
    
    SINTETICKI, 
    ANALITICKI;

    public String getDescription() {
        switch (this) {
            case ANALITICKI:
                return getMessage("Account.Type.Analytical");
            case SINTETICKI:
                return getMessage("Account.Type.Synthetical");
            default:
                return "";
        }
    }
    
}
