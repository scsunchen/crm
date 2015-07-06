/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.invado.core.exception;

/**
 *
 * @author bdragan
 */
public class EntityExistsException  extends ApplicationException {
  public EntityExistsException(String msg) {
    super(msg);
  }
}
