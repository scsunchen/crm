/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.invado.finance.service.exception;

/**
 * 
 * @author bdragan
 */
public class SystemException extends RuntimeException {

  /**
   * Constructs an instance of <code>SystemException</code> with the specified cause.
   * @param cause the cause(A null value is permitted, and indicates that the cause is nonexistent or unknown).
   */
  public SystemException(Throwable cause) {
    super(cause);
  }

 /**
  * Constructs an instance of <code>SystemException</code> with the specified
  * detail message and cause.
  * @param msg the detail message.
  * @param cause the cause(A null value is permitted, and indicates that the cause is nonexistent or unknown).
  */
  public SystemException(String message, Throwable cause) {
    super(message,cause);
  }
//
  /**
   * Constructs an instance of <code>SystemException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public SystemException(String message) {
    super(message);
  }

  /**
   * Creates a new instance of <code>SystemException</code> without detail message.
   */
  public SystemException() {
    super();
  }

}
