/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.invado.customer.relationship.service.exception;

import com.invado.core.exception.ApplicationException;

/**
 *
 * @author bdragan
 */
public class OrgUnitNotExistsEx  extends ApplicationException {
  public OrgUnitNotExistsEx(String msg) {
    super(msg);
  }
}
