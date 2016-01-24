/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.controller;


import com.invado.customer.relationship.service.UserDataService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author bdragan
 */
@Controller
public class LoginController {


    @Inject
    private UserDataService userDataService;
    @Inject
    private AuthenticationManager  authenticationManager;


    @RequestMapping("/")
    public String showHomePage1() {
        return "home";
    }

    @RequestMapping("/home")
    public String showHomePage() {
        return "home";
    }

    @RequestMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @RequestMapping("/auto-login.html")
    public String autoLogin(@RequestParam String username, @RequestParam String pass, HttpServletRequest request) {

        request.getSession();
        UserDetails userDetails = userDataService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(auth);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return "redirect:/home";
        }
        return "redirect:/login";
    }
}
