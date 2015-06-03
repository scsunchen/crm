/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.masterdata.service.exception;

import java.util.Collection;

/**
 * Thrown to indicate that a method has been passed an illegal or 
 * inappropriate argument.
 *
 * @author bdragan
 */
public class IllegalArgumentException extends ApplicationException {

    private String[] constraintViolation;

    /**
     * Constructs a new exception with the specified constraint violation message.  The
     * cause and detail message is not initialized.
     *
     * @param   message   the constraint violation message. The constraint violation message is saved for 
     *          later retrieval by the {@link #getConstraintViolation()} method.
     */
    public IllegalArgumentException(String constraintViolationMessage) {
        this.constraintViolation = new String[]{constraintViolationMessage};
    }

    /**
     * Constructs a new exception with the specified detail message and constraint violation messages.  The
     * cause is not initialized.
     *
     * @param   msg   the detail message. The constraint violation message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     * @param constraintViolation the constraint violation messages. The constraint violation messages is saved for 
     *          later retrieval by the {@link #getConstraintViolation()} method.
     */
    public IllegalArgumentException(String msg, String[] constraintViolation) {
        super(msg);
        this.constraintViolation = constraintViolation;
    }
    
    /**
     * Constructs a new exception with the specified constraint violation messages.  The
     * cause and detail message is not initialized.
     *
     * @param   message   the constraint violation messages. The constraint violation messages is saved for 
     *          later retrieval by the {@link #getConstraintViolation()} method.
     */
    public IllegalArgumentException(String[] constraintViolation) {
        this.constraintViolation = constraintViolation;
    }

    /**
     * Constructs a new exception with the specified detail message and constraint violation messages.  The
     * cause is not initialized.
     *
     * @param   msg   the detail message. The constraint violation message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     * @param constraintViolation the constraint violation messages. The constraint violation messages is saved for 
     *          later retrieval by the {@link #getConstraintViolation()} method.
     */
    public IllegalArgumentException(String msg, Collection<String> constraintViolation) {
        super(msg);
        this.constraintViolation = constraintViolation.toArray(new String[]{});
    }
    
    /**
     * Returns the constraint violation message array of strings of this throwable.
     *
     * @return  the constraint violation message array of strings of this <tt>Throwable</tt> instance
     *          (which may be <tt>null</tt>).
     */
    public String[] getConstraintViolation() {
        return constraintViolation;
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage(); 
    }

    @Override
    public String getMessage() {
        String result = (super.getMessage() != null && super.getMessage().length() > 0) ? 
                super.getMessage()+"." : "";
        for (String violation : constraintViolation) {
            if(violation.length() > 0 )
                result += String.format(" %s.", violation);
        }
        return result; 
    }
    
}