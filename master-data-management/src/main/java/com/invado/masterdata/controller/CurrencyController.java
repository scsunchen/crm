package com.invado.masterdata.controller;

import com.invado.core.domain.Currency;
import com.invado.masterdata.service.CurrencyService;
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
 * Created by NikolaB on 6/28/2015.
 */
@Controller
public class CurrencyController {
    @Autowired
    private CurrencyService service;


    @RequestMapping("/currency/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<Currency> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "currency-view";
    }

    @RequestMapping(value = "/currency/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new Currency());
        model.put("action", "create");
        return "currency-grid";
    }

    @RequestMapping(value = "/currency/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") Currency item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "currency-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/currency/{page}/create";
    }

    @RequestMapping("/currency/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/currency/{page}";
    }

    @RequestMapping(value = "/currency/{page}/update/{ISOCode}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String ISOCode,
                                 Map<String, Object> model)
            throws Exception {
        Currency item = service.read(ISOCode);
        model.put("item", item);
        return "currency-grid";
    }

    @RequestMapping(value = "/currency/{page}/update/{ISOCode}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") Currency item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "currency-grid";
        } else {

            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/currency/{page}";
    }
}
