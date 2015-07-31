/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.exception.SystemException;
import com.invado.finance.service.JournalEntryTypeService;
import com.invado.finance.service.dto.JournalEntryTypeDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.exception.ClientNotFoundException;
import com.invado.finance.service.exception.JournalEntryTypeConstraintViolationException;
import com.invado.finance.service.exception.JournalEntryTypeNotFoundException;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author bdragan
 */

@Controller
public class JournalEntryTypeController {
    
    @Inject 
    private JournalEntryTypeService service;
    
    @RequestMapping("/journal-entry-type/{page}")
    public String showItems(
            @PathVariable Integer page,
            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<JournalEntryTypeDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "journal-entry-type-view";
    }

    @RequestMapping("/journal-entry-type/{page}/{clientId}/{typeId}/delete.html")
    public String delete(@PathVariable Integer clientId, 
                         @PathVariable Integer typeId) 
                         throws Exception {
        service.delete(clientId, typeId);
        return "redirect:/journal-entry-type/{page}";
    }
    
    @RequestMapping(value = "/journal-entry-type/{page}/create.html", 
                    method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, 
                                 Map<String, Object> model) {
        JournalEntryTypeDTO dto = new JournalEntryTypeDTO();
        dto.setJournalEntryNumber(0);
        model.put("type", dto);
        model.put("action", "create");
        model.put("page", page);
        return "journal-entry-type-grid";
    }
    
    @RequestMapping(value = "/journal-entry-type/{page}/create.html", 
                    method = RequestMethod.POST)
    public String processCreateForm(
            @ModelAttribute("type") JournalEntryTypeDTO item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("page", page);
            return "journal-entry-type-grid";
        } 
        try {
            service.create(item);
            status.setComplete();
        } catch (ClientNotFoundException | JournalEntryTypeConstraintViolationException | SystemException ex) {
            model.put("action", "create");
            model.put("exception", ex);
            model.put("page", page);
            return "journal-entry-type-grid";
        }
        return "redirect:/journal-entry-type/{page}";
    }
    
    @RequestMapping(value = "/journal-entry-type/{page}/{clientId}/{typeId}/update.html", 
                    method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String page, 
                                 @PathVariable Integer clientId, 
                                 @PathVariable Integer typeId, 
                                 Map<String, Object> model) throws Exception {
        JournalEntryTypeDTO dto = service.read(clientId, typeId);
        model.put("type", dto);
        model.put("action", "update");
        model.put("page", page);
        return "journal-entry-type-grid";
    }
    
    @RequestMapping(value = "/journal-entry-type/{page}/{clientId}/{typeId}/update.html", 
                    method = RequestMethod.POST)
    public String processUpdateForm(
            @ModelAttribute("type") JournalEntryTypeDTO item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "update");
            model.put("page", page);
            return "journal-entry-type-grid";
        } 
        try {
            service.update(item);
            status.setComplete();
        } catch (JournalEntryTypeNotFoundException 
                | JournalEntryTypeConstraintViolationException 
                | SystemException ex) {
            model.put("action", "update");
            model.put("exception", ex);
            model.put("page", page);
            return "journal-entry-type-grid";
        }
        return "redirect:/journal-entry-type/{page}";
    }
    
    @RequestMapping(value = "/read-journal-entry-type/{name}/{clientId}")
    public @ResponseBody List<JournalEntryTypeDTO> findTypeByNameAndClientId(
            @PathVariable String name,
            @PathVariable Integer clientId) {
        return service.readJournalEntryTypeByNameAndClient(clientId, name);
    }
    
    @RequestMapping(value = "/read-journal-entry-type/{name}")
    public @ResponseBody List<JournalEntryTypeDTO> findTypeByName(
            @PathVariable String name) {
        return service.readJournalEntryTypeByName(name);
    }
}
