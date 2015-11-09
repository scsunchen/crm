package com.invado.masterdata.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.masterdata.service.BPService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 26/08/2015.
 */
@Controller
public class BusinessPartnerController {

    @Autowired
    private BPService service;

    @RequestMapping("/partner/{page}")
    public String showItems(HttpServletRequest httpServletRequest, @PathVariable Integer page,
                            @ModelAttribute BusinessPartnerDTO businessPartnerDTO, Map<String, Object> model)
            throws Exception {


        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);

        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("id", httpServletRequest.getParameter("id")));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("name", httpServletRequest.getParameter("name")));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type",  httpServletRequest.getParameter("type")));
        businessPartnerDTO.setTypeValue(httpServletRequest.getParameter("type"));
        ReadRangeDTO<BusinessPartnerDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("bussinesPartnerDTO", businessPartnerDTO);
        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "partner-view";
    }

    @RequestMapping(value = "/partner/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new BusinessPartnerDTO());
        List<BusinessPartner> parents = service.readParentPartners();
        List<BusinessPartner.Type> types = Arrays.asList(BusinessPartner.Type.values());
        model.put("types", types);
        model.put("parents", parents);
        model.put("action", "create");
        return "partner-grid";
    }

    @RequestMapping(value = "/partner/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") BusinessPartnerDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "partner-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/partner/{page}";
    }

    @RequestMapping("/partner/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/partner/{page}";
    }

    @RequestMapping(value = "/partner/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerDTO item = service.read(id);
        model.put("item", item);
        return "partner-grid";
    }

    @RequestMapping(value = "/partner/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerDTO item,
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

    @RequestMapping(value = "/partner/read-partner/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findItemByDescription(@PathVariable String name) {
        return service.readPartnerByName(name);
    }

}
