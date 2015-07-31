/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.exception.SystemException;
import com.invado.finance.domain.journal_entry.AccountDetermination;
import com.invado.finance.domain.journal_entry.AccountType;
import com.invado.finance.service.AccountService;
import com.invado.finance.service.dto.AccountDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.exception.AccountConstraintViolationException;
import com.invado.finance.service.exception.AccountExistsException;
import java.util.Map;
import javax.inject.Inject;
import javax.security.auth.login.AccountNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author bdragan
 */
@Controller
public class AccountController {
    
    @Inject 
    private AccountService service;
    
    @RequestMapping("/account/{page}")
    public String showItems(
            @PathVariable Integer page,
            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<AccountDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "account-view";
    }

    @RequestMapping("/account/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/account/{page}";
    }
    
    @RequestMapping(value = "/account/{page}/create.html", 
                    method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, 
                                 Map<String, Object> model) {
        model.put("account", new AccountDTO());
        model.put("action", "create");
        model.put("page", page);
        model.put("accountDeterminations", AccountDetermination.values());
        model.put("accountTypes", AccountType.values());
        return "account-grid";
    }

    @RequestMapping(value = "/account/{page}/create.html", 
                    method = RequestMethod.POST)
    public String processCreationForm(
            @ModelAttribute("account") AccountDTO account,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("page", page);
            model.put("accountDeterminations", AccountDetermination.values());
            model.put("accountTypes", AccountType.values());
            return "account-grid";
        } 
        try {
            service.create(account);
            status.setComplete();
        } catch (SystemException | AccountConstraintViolationException 
                | AccountExistsException ex) {
            model.put("action", "create");
            model.put("exception", ex);
            model.put("page", page);
            model.put("accountDeterminations", AccountDetermination.values());
            model.put("accountTypes", AccountType.values());
            return "account-grid";
        }
        return "redirect:/account/{page}";
    }
    
    @RequestMapping(value = "/account/{page}/{code}/update.html", 
                    method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String page, 
                                 @PathVariable String code, 
                                 Map<String, Object> model) throws Exception {
        model.put("account", service.read(code));
        model.put("action", "update");
        model.put("page", page);
        model.put("accountDeterminations", AccountDetermination.values());
        model.put("accountTypes", AccountType.values());
        return "account-grid";
    }
    
    @RequestMapping(value = "/account/{page}/{code}/update.html", 
                    method = RequestMethod.POST)
    public String processUpdateForm(
            @ModelAttribute("account") AccountDTO account,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("action", "update");
            model.put("page", page);
            model.put("accountDeterminations", AccountDetermination.values());
            model.put("accountTypes", AccountType.values());
            return "account-grid";
        } 
        try {
            service.update(account);
            status.setComplete();
        } catch (SystemException | AccountConstraintViolationException 
                | AccountNotFoundException ex) {
            model.put("action", "update");
            model.put("exception", ex);
            model.put("page", page);
            model.put("accountDeterminations", AccountDetermination.values());
            model.put("accountTypes", AccountType.values());
            return "account-grid";
        }
        return "redirect:/account/{page}";
    }
    
}
