package com.invado.masterdata.controller;

import com.invado.core.dto.BusinessPartnerStatusDTO;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.BusinessPartnerStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;

/**
 * Created by Nikola on 04/01/2016.
 */
@Controller
public class BusinessPartnerStatusController {


    @Autowired
    private BusinessPartnerStatusService service;


    @RequestMapping("/partnerstatus/read-page.html")
    public String showItems(@RequestParam Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<BusinessPartnerStatusDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "partnerstatus-view";
    }

    @RequestMapping(value = "/partnerstatus/create.html", method = RequestMethod.GET)
    public String initCreateForm(Map<String, Object> model) {
        model.put("item", new BusinessPartnerStatusDTO());
        model.put("action", "create");
        return "partnerstatus-grid";
    }

    @RequestMapping(value = "/partnerstatus/create.html", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") BusinessPartnerStatusDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "partnerstatus-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        //return "redirect:/partnerstatus/{page}/create";
        return "redirect:/partnerstatus/create.html";
    }

    @RequestMapping("/partnerstatus/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/partnerstatus/read-page.html?page=0";
    }

    @RequestMapping(value = "/partnerstatus/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam Integer id,
                                 @RequestParam Integer page,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerStatusDTO item = service.read(id);
        model.put("item", item);
        return "partnerstatus-grid";
    }

    @RequestMapping(value = "/partnerstatus/update.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerStatusDTO item,
                                      @RequestParam Integer id,
                                      @RequestParam Integer page,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "partnerstatus-grid";
        } else {
            service.update(item);
            status.setComplete();
        }
        //return "redirect:/partnerstatus/{page}";
        return "redirect:/partnerstatus/read-page.html?page=0";
    }
}
