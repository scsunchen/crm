/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author draganbob
 */
@Entity
@Table(name = "c_application_user", schema = "devel")
@NamedQueries({
    @NamedQuery(name = ApplicationUser.READ_BY_USERNAME_AND_PASSWORD,
            query = "SELECT x FROM ApplicationUser x WHERE x.username = ?1 AND x.password = ?2"),
    @NamedQuery(name = ApplicationUser.READ_BY_USERNAME,
            query = "SELECT x FROM ApplicationUser x WHERE x.username = ?1")
})
public class ApplicationUser implements Serializable {

    public static final String READ_BY_USERNAME = "ApplicationUser.ReadByUsername";
    public static final String READ_BY_USERNAME_AND_PASSWORD =
            "ApplicationUser.ReadByUsernameAndPassword";
    @Id
    @GeneratedValue(generator = "KorisnikTab")
    @TableGenerator(name = "KorisnikTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Korisnik",
            allocationSize = 1)
    @Column(name = "id")
    private Integer id;
    @NotBlank(message = "{User.Username.NotBlank}")
    @Column(name = "username")
    @Size(max = 20, message = "{User.Username.Size}")
    private String username;
    @Size(min = 1, max = 20, message = "{User.Password.Size}")
    @Column(name = "password")
    private char[] password;
    @NotBlank(message = "{User.Desc.NotBlank}")
    @Size(max = 100, message = "{User.Desc.Size}")
    @Column(name = "description")
    private String description;
    @Version
    private Long version;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "c_user_role",schema = "devel",
            joinColumns =
            @JoinColumn(name = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "role_name"))
    private Set<Role> roles = new LinkedHashSet<>();

    //************************************************************************//    
    // CONSTRUCTORS //
    //************************************************************************//
    public ApplicationUser() {
    }

    public ApplicationUser(Integer id) {
        this.id = id;
    }

    public ApplicationUser(String username, char[] password) {
        this.username = username;
        this.password = password;
    }

    //************************************************************************//    
    // GET/SET METHODS //
    //************************************************************************//
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean add(Role e) {
        return roles.add(e);
    }

    public boolean remove(Role o) {
        return roles.remove(o);
    }

    public boolean contains(Role o) {
        return roles.contains(o);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    //************************************************************************//    
    // OVERRIDEN OBJECT METHODS  //
    //************************************************************************//
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ApplicationUser other = (ApplicationUser) obj;
        return !(this.id != other.id
                && (this.id == null || !this.id.equals(other.id)));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" + "id=" + id + ", username=" + username
                + ", password=" + password + ", description=" + description + '}';
    }
}
