/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bdragan
 */
public class ReadSpecificationsDTO implements Serializable {

    public List<PartnerSpecificationDTO> specifications = new ArrayList<>();
    public Integer numberOfPages;
    public Integer pageSize;
}
