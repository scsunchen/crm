/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.invado.finance.service.exception;

import com.invado.core.exception.ApplicationException;

/**
 *
 * @author bdragan
 */
public class JournalEntryTypeExistsException extends ApplicationException {

  public JournalEntryTypeExistsException(String msg) {
    super(msg);
  }

  public JournalEntryTypeExistsException() {
  }

}
