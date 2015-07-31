/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.core.utils.Utils;
import com.invado.finance.service.dto.AccountDTO;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "f_account", schema="devel")
@NamedQueries({
    @NamedQuery(name = Account.READ_ALL_ORDERBY_NUMBER, 
                query = "SELECT x FROM Account x ORDER BY x.number"),
    @NamedQuery(name = Account.READ_BY_NUMBER_ORDERBY_NUMBER, 
                query = "SELECT x FROM Account x WHERE x.number LIKE UPPER(:number) ORDER BY x.number")
    
})
public class Account implements Serializable {

    public static final String READ_ALL_ORDERBY_NUMBER = 
            "Account.ReadAllOrderByNumber";
    public static final String READ_BY_NUMBER_ORDERBY_NUMBER = "Account.ReadByNumberOrderByNumber";
    
    @Id
    @Column(name = "number")
    @Size(max = 10, message = "{Account.Id.Size}")
    @NotBlank(message = "{Account.Id.NotBlank}")
    private String number;
    @NotBlank(message = "{Account.Name.NotBlank}")
    @Size(max = 100, message = "{Account.Name.Size}")
    @Column(name = "description")
    private String description;
    @NotNull(message = "{Account.Determination.NotBlank}")
    @Column(name = "determination")
    private AccountDetermination determination;
    @NotNull(message = "{Account.Type.NotBlank}")
    @Column(name = "type")
    private AccountType type;
    @Version
    @Column(name="version")
    private Long version;
    
    //************************************************************************//    
                           // CONSTRUCTORS //
    //************************************************************************//
    
    public Account() {
    }

    public Account(String number) {
        this.number = number;
    }

    public Account(String number, 
                   String desc, 
                   AccountDetermination determination, 
                   AccountType type) {
        this.number = number;
        this.description = desc;
        this.determination = determination;
        this.type = type;
    }
    
    //************************************************************************//    
                           // GET/SET METHODS //
    //************************************************************************//
    
    public String getNumber() {
        return number;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Metoda vraca pripadnost konta. Account moze pripadati : <ul><li>Glavnoj knjizi</li>
     * <li>Dobavljacima, ili</li> <li> Kupcima</li></ul>. Metod se poziva prilikom
     * izracunavanja strukture naloga knjizenja ( u sistemskoj operaciji <code> StampaNaloga</code>).
     * @return pripadnost konta
     */
    public AccountDetermination getDetermination() {
        return determination;
    }

    public void setDetermination(AccountDetermination determination) {
        this.determination = determination;
    }
    
    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
    
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
    
    public boolean jeSinteticki() {
        if (type.equals(AccountType.SINTETICKI)) {
            return true;
        }
        return false;
    }    

    public AccountDTO getDTO() {
        AccountDTO dto = new AccountDTO();
        dto.setNumber(number);
        dto.setName(description);
        dto.setDetermination(determination);
        dto.setType(type);
        dto.setVersion(version);
        return dto;
    }

    public void set(AccountDTO dto) {
        number = dto.getNumber();
        description = dto.getName();
        determination = dto.getDetermination();
        type = dto.getType();
        version = dto.getVersion();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if ((this.number == null) ? (other.number != null) : !this.number.equals(other.number)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.number != null ? this.number.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Account{" + "number=" + number + '}';
    }
    
}