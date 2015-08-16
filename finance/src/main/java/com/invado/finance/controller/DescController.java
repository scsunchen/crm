/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.exception.SystemException;
import com.invado.finance.service.DescService;
import com.invado.finance.service.dto.DescDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.exception.DescriptionConstraintViolationException;
import com.invado.finance.service.exception.DescriptionExistsException;
import com.invado.finance.service.exception.DescriptionNotFoundException;
import java.util.Map;
import javax.inject.Inject;
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
public class DescController {
    
    @Inject 
    private DescService service;
    
    @RequestMapping("/desc/{page}")
    public String showItems(@PathVariable Integer page,
            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<DescDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "desc-view";
    }

    @RequestMapping("/desc/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/desc/{page}";
    }
    
    @RequestMapping(value = "/desc/{page}/create.html", 
                    method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, 
                                 Map<String, Object> model) {
        model.put("desc", new DescDTO());
        model.put("action", "create");
        model.put("page", page);
        return "desc-grid";
    }

    @RequestMapping(value = "/desc/{page}/create.html", 
                    method = RequestMethod.POST)
    public String processCreationForm(
            @ModelAttribute("desc") DescDTO item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("page", page);
            return "desc-grid";
        } 
        try {
            service.create(item);
            status.setComplete();
        } catch (SystemException | DescriptionConstraintViolationException ex) {
            model.put("action", "create");
            model.put("exception", ex);
            model.put("page", page);
            return "desc-grid";
        }
        return "redirect:/desc/{page}";
    }
    
    @RequestMapping(value = "/desc/{page}/{id}/update.html", 
                    method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String page, 
                                 @PathVariable Integer id,
                                 Map<String, Object> model) 
                                throws Exception {
        model.put("desc", service.read(id));
        model.put("action", "update");
        model.put("page", page);
        return "desc-grid";
    }

    @RequestMapping(value = "/desc/{page}/{id}/update.html", 
                    method = RequestMethod.POST)
    public String processUpdationForm(
            @ModelAttribute("desc") DescDTO item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "update");
            model.put("page", page);
            return "desc-grid";
        } 
        try {
            System.out.println(item.getVersion());
            service.update(item);

            status.setComplete();
        } catch (SystemException | DescriptionNotFoundException | 
                DescriptionConstraintViolationException ex) {
            model.put("action", "update");
            model.put("exception", ex);
            model.put("page", page);
            return "desc-grid";
        }
        return "redirect:/desc/{page}";
    }

}
