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
public class ClientNotExistsEx  extends ApplicationException {
  public ClientNotExistsEx(String msg) {
    super(msg);
  }
}
