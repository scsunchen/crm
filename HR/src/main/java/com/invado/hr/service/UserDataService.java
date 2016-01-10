/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.hr.service;

import com.invado.core.domain.ApplicationUser;
import com.invado.core.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bdragan
 */
@Service
public class UserDataService implements UserDetailsService{
    
    private static final Logger LOG = Logger.getLogger(UserDataService.class.getName());
    
    @PersistenceContext(name = "unit")
    private EntityManager dao;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        try {
            ApplicationUser user = dao.createNamedQuery(
                    ApplicationUser.READ_BY_USERNAME, 
                    ApplicationUser.class)
                    .setParameter(1, string)
                    .getSingleResult();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            }
            return new User(
                    string,
                    String.valueOf(user.getPassword()),
                    authorities);
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "", ex);
            throw new UsernameNotFoundException("Exception occured during during app user reading.", ex);
        }
    }
    
}
