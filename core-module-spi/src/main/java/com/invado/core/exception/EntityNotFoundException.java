/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.invado.core.exception;

/**
 *
 * @author bdragan
 */
public class EntityNotFoundException  extends ApplicationException {
  public EntityNotFoundException(String msg) {
    super(msg);
  }
}
