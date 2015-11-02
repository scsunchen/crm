/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.masterdata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author bdragan
 */
@Controller
public class LoginController {

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
}
