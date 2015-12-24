package com.invado.masterdata.controller;

import com.invado.core.domain.Township;
import com.invado.core.dto.TownshipDTO;
import com.invado.masterdata.service.TownshipService;
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

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by NikolaB on 6/10/2015.
 */
@Controller
public class TownshipController {

    @Inject
    private TownshipService service;


    @RequestMapping("/township/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<TownshipDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "township-view";
    }

    @RequestMapping(value = "/township/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new TownshipDTO());
        model.put("action", "create");
        return "township-grid";
    }

    @RequestMapping(value = "/township/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") TownshipDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "township-grid";
        } else {
            this.service.create(item);
            model.put("message", item.getCode()+" "+item.getName());
            status.setComplete();
        }
        //return "redirect:/township/{page}";
        return "redirect:/township/{page}/create";
    }

    @RequestMapping("/township/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/township/{page}";
    }

    @RequestMapping(value = "/township/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String code,
                                 Map<String, Object> model)
            throws Exception {
        TownshipDTO item = service.read(code).getDTO();
        model.put("item", item);
        return "township-grid";
    }

    @RequestMapping(value = "/township/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") TownshipDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "township-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/township/{page}";
    }
}
