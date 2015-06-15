package com.invado.masterdata.controller;

import com.invado.core.domain.BankCreditor;
import com.invado.masterdata.service.BankCreditorService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;

/**
 * Created by NikolaB on 6/10/2015.
 */
@Controller
public class BankCreditorController {

    @Autowired
    private BankCreditorService service;


    @RequestMapping("/bank/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<BankCreditor> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "bank-view";
    }

    @RequestMapping(value = "/bank/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new BankCreditor());
        model.put("action", "create");
        return "bank-grid";
    }

    @RequestMapping(value = "/bank/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") BankCreditor item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {

        if (result.hasErrors()) {
            model.put("action", "create");
            return "bank-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/bank/{page}";
    }

    @RequestMapping("/bank/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/bank/{page}";
    }

    @RequestMapping(value = "/bank/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BankCreditor item = service.read(id);
        model.put("item", item);
        return "bank-grid";
    }

    @RequestMapping(value = "/bank/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BankCreditor item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "bank-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/bank/{page}";
    }
}
