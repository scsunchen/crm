
import com.invado.core.exception.ApplicationException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author draganbob
 */
public class JournalEntryItemExistsException extends ApplicationException {

    public JournalEntryItemExistsException(String msg) {
        super(msg);
    }

    public JournalEntryItemExistsException() {
    }
}
