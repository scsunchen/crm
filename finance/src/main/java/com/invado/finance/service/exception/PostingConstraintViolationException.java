package com.invado.finance.service.exception;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.invado.core.exception.SystemException;
import java.util.List;

/**
 *
 * @author bdragan
 */
public class PostingConstraintViolationException extends SystemException {

    private List<String> report;

    public PostingConstraintViolationException(String msg) {
        super(msg);
    }

    /**
     * Kreira izvestaj o prestupu ogranicenja
     *
     * @param msg poruka o gresci
     * @param report poruke o ogranicenjima koja su narusena
     */
    public PostingConstraintViolationException(String msg, 
                                               List<String> report) {
        super(msg);
        this.report = report;
    }

    public String getReport() {
        String i = new String();
        for (String object : report) {
            i += (object + "\n");
        }
        return i;
    }
}