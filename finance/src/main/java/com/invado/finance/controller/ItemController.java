/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.domain.Article;
import com.invado.finance.domain.UnitOfMeasure;
import com.invado.core.domain.VatPercent;
import com.invado.finance.service.ArticleService;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import java.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author bdragan
 */
@Controller
public class ItemController {

    @Inject
    private ArticleService service;

    @RequestMapping("/home")
    public String showHomePage() {
        return "home";
    }

    @RequestMapping("/item/{page}")
    public String showItems(@PathVariable Integer page,
            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<Article> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "item-view";
    }

    @RequestMapping("/item/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/item/{page}";
    }

    @RequestMapping(value = "/item/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new Article());
        model.put("action", "create");
        model.put("VATOptions", Arrays.asList(VatPercent.values()));
        model.put("unitOfMeasure", Arrays.asList(UnitOfMeasure.values()));
        model.put("page", page);
        return "item-grid";
    }

    @RequestMapping(value = "/item/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(
            @ModelAttribute("item") Article item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("VATOptions", Arrays.asList(VatPercent.values()));
            model.put("unitOfMeasure", Arrays.asList(UnitOfMeasure.values()));
            model.put("page", page);
            return "item-grid";
        } 
        try {
            item.setUserDefinedUnitOfMeasure(Boolean.FALSE);
            this.service.create(item);
            status.setComplete();
        } catch (Exception ex) {
            model.put("action", "create");
            model.put("exception", ex);
            model.put("VATOptions", Arrays.asList(VatPercent.values()));
            model.put("unitOfMeasure", Arrays.asList(UnitOfMeasure.values()));
            model.put("page", page);
            return "item-grid";
        }
        return "redirect:/item/{page}";
    }

    @RequestMapping(value = "/item/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(
            @PathVariable String code,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        Article item = service.read(code);
        model.put("item", item);
        model.put("action", "update");
        model.put("VATOptions", Arrays.asList(VatPercent.values()));
        model.put("unitOfMeasure", Arrays.asList(UnitOfMeasure.values()));
        model.put("page", page);
        return "item-grid";
    }

    @RequestMapping(value = "/item/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(
            @ModelAttribute("item") Article item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("VATOptions", Arrays.asList(VatPercent.values()));
            model.put("unitOfMeasure", Arrays.asList(UnitOfMeasure.values()));
            model.put("page", page);
            return "item-grid";
        } 
        try {
            item.setUserDefinedUnitOfMeasure(Boolean.FALSE);
            this.service.update(item);
            status.setComplete();            
        } catch(Exception ex) {
            model.put("exception", ex);
            model.put("VATOptions", Arrays.asList(VatPercent.values()));
            model.put("unitOfMeasure", Arrays.asList(UnitOfMeasure.values()));
            model.put("page", page);
            return "item-grid";
        }
        return "redirect:/item/{page}";
    }

}
