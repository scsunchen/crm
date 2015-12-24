package com.invado.masterdata.controller;

import com.invado.core.dto.BusinessPartnerDocumentDTO;
import com.invado.masterdata.service.BusinessPartnerDocumentService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
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
 * Created by Nikola on 24/12/2015.
 */
@Controller
public class BusinessPartnerDocumentController {
    @Inject
    private BusinessPartnerDocumentService service;


    @RequestMapping("/businesspartnerdocument/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<BusinessPartnerDocumentDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "businesspartnerdocument-view";
    }

    @RequestMapping(value = "/businesspartnerdocument/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new BusinessPartnerDocumentDTO());
        model.put("action", "create");
        return "businesspartnerdocument-grid";
    }

    @RequestMapping(value = "/businesspartnerdocument/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") BusinessPartnerDocumentDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "businesspartnerdocument-grid";
        } else {
            this.service.create(item);
            model.put("message", item.getId()+" "+item.getTypeDescription());
            status.setComplete();
        }
        //return "redirect:/businesspartnerdocument/{page}";
        return "redirect:/businesspartnerdocument/{page}/create";
    }

    @RequestMapping("/businesspartnerdocument/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/businesspartnerdocument/{page}";
    }

    @RequestMapping(value = "/businesspartnerdocument/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerDocumentDTO item = service.read(id).getDTO();
        model.put("item", item);
        return "businesspartnerdocument-grid";
    }

    @RequestMapping(value = "/businesspartnerdocument/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerDocumentDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "businesspartnerdocument-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/businesspartnerdocument/{page}";
    }
}
