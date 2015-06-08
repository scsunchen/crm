package com.invado.masterdata.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.masterdata.service.BusinessPartnerService;
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

import java.util.Arrays;
import java.util.Map;


/**
 * Created by NikolaB on 6/2/2015.
 */
@Controller
public class BusinessPartnerController {

    @Autowired
    private BusinessPartnerService service;

    @RequestMapping("/home")
    public String showHomePage() {
        System.out.println("ide na home page sada");
        return "home";
    }

    @RequestMapping("/partner/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        System.out.println("Ovo radi za razliku od onog drugog!");
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<BusinessPartner> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "partner-view";
    }

    @RequestMapping(value = "/partner/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        System.out.println("u pravom smo kodu");
        model.put("item", new BusinessPartner());
        model.put("action", "create");
        return "partner-grid";
    }

    @RequestMapping(value = "/partner/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") BusinessPartner item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        System.out.println("jeste našao metodu kontrolera"+" "+item.getCompanyIdNumber()+" "+item.getName());
        if (result.hasErrors()) {
            model.put("action", "create");
            return "partner-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/partner/{page}";
    }

    @RequestMapping("/partner/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/partner/{page}";
    }

    @RequestMapping(value = "/partner/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String code,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartner item = service.read(code);
        model.put("item", item);
        return "partner-grid";
    }

    @RequestMapping(value = "/item/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartner item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "partner-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/partner/{page}";
    }
}
