/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author draganbob
 */
public class ReadAllDTO<T> implements Serializable {

    public List<T> list;
    public String clientName;

    public ReadAllDTO(List<T> lista, String clientName) {
        this.list = lista;
        this.clientName = clientName;
    }
}
