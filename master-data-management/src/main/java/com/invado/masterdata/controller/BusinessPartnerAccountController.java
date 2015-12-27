package com.invado.masterdata.controller;


import com.invado.core.dto.BusinessPartnerAccountDTO;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.masterdata.service.BPService;
import com.invado.masterdata.service.BusinessPartnerAccountService;
import com.invado.masterdata.service.dto.FilterObjectsList;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by Nikola on 24/12/2015.
 */
@Controller
public class BusinessPartnerAccountController {

    @Inject
    private BusinessPartnerAccountService service;
    @Inject
    private BPService bpService;


    @RequestMapping("/account/{page}")
    public String showItems(@PathVariable Integer page,
                            @ModelAttribute("filterObjectsList") FilterObjectsList filterList,
                            ModelMap modelMap, Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        if (filterList.getBusinessPartnerDTOs().size() > 0) {
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("merchantId", filterList.getBusinessPartnerDTOs().get(0).getId()));
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("pointOfSaleId", filterList.getBusinessPartnerDTOs().get(1).getId()));
        } else {
            filterList.getBusinessPartnerDTOs().add(new BusinessPartnerDTO());
            filterList.getBusinessPartnerDTOs().add(new BusinessPartnerDTO());
        }

        ReadRangeDTO<BusinessPartnerAccountDTO> items = service.readPage(request);

        modelMap.addAttribute("filterObjects", filterList);
        modelMap.addAttribute("data", items.getData());
        modelMap.addAttribute("page", items.getPage());
        modelMap.addAttribute("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "business-partner-account-view";
    }

    @RequestMapping(value = "/account/create.html", method = RequestMethod.GET)
    public String initCreateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 Map<String, Object> model) {
        BusinessPartnerAccountDTO businessPartnerAccountDTO = new BusinessPartnerAccountDTO();
        BusinessPartnerDTO businessPartner = null;
        if (pointOfSaleId != null) {
            businessPartner = bpService.read(pointOfSaleId);
        } else {
            businessPartner = bpService.read(masterPartnerId);
        }

        businessPartnerAccountDTO.setAccountOwnerId(businessPartner.getParentBusinessPartnerId());
        businessPartnerAccountDTO.setAccountOwnerName(businessPartner.getParentBusinesspartnerName());

        model.put("item", businessPartnerAccountDTO);
        model.put("action", "create");
        return "business-partner-account-grid";
    }

    @RequestMapping(value = "/account/create.html", method = RequestMethod.POST)
    public String processCreationForm(@RequestParam String page,
                                      @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                      @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                      @ModelAttribute("item") BusinessPartnerAccountDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "business-partner-account-grid";
        } else {
            service.create(item);
            model.put("message", item.getId() + " " + item.getAccount());
            status.setComplete();
        }
        //return "redirect:/township/{page}";
        return "redirect:/account/create.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }

    @RequestMapping("/account/delete.html")
    public String delete(@RequestParam Integer id, @RequestParam String page,
                         @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                         @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName) throws Exception {
        service.delete(id);
        //return "redirect:/contact/{page}";
        return "redirect:/partner/read-accounts-page.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }

    @RequestMapping(value = "/account/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 @RequestParam(value = "id") Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerAccountDTO item = service.read(id).getDTO();
        model.put("item", item);
        return "business-partner-account-grid";
    }

    @RequestMapping(value = "/account/update.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerAccountDTO item,
                                      BindingResult result,
                                      @RequestParam String page,
                                      @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                      @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                      @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                      @RequestParam(value = "id") Integer id,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "business-account-account-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/account/update.html";
    }
}
