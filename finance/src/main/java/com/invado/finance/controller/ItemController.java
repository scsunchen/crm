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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author bdragan
 */
@Controller
public class ItemController {

    @Autowired
    private ArticleService service;

    @RequestMapping("/home")
    public String showHomePage(){
        System.out.println("ide na home page iz itema");
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
        //return "item-table";
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
        return "item-grid";
    }

    @RequestMapping(value = "/item/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(Article item,
            BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
        } else {
            item.setUserDefinedUnitOfMeasure(Boolean.FALSE);
            this.service.create(item);
        }
        return "redirect:/item/{page}";
    }

    @RequestMapping(value = "/item/{page}/update/{code}",
                    method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String code,
                                 Map<String, Object> model)
                                 throws Exception {
        Article item = service.read(code);
        model.put("item", item);
        model.put("action", "update");
        model.put("VATOptions", Arrays.asList(VatPercent.values()));
        model.put("unitOfMeasure", Arrays.asList(UnitOfMeasure.values()));
        return "item-grid";
    }

    @RequestMapping(value = "/item/{page}/update/{code}",
                    method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") Article item,
            BindingResult result,
            SessionStatus status,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("VATOptions", Arrays.asList(VatPercent.values()));
            model.put("UnitOfMeasure", Arrays.asList(UnitOfMeasure.values())
                    .stream().map(p -> p.getDescription())
                    .collect(Collectors.toList()));
            return "item-grid";
        } else {
            //disable id
            item.setUserDefinedUnitOfMeasure(Boolean.FALSE);
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/item/{page}";
    }

}
