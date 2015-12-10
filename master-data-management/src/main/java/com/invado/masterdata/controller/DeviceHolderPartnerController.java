package com.invado.masterdata.controller;

import com.invado.core.domain.Device;
import com.invado.core.domain.DeviceHolderPartner;
import com.invado.core.dto.BusinessPartnerContactDetailsDTO;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.core.dto.DeviceHolderPartnerDTO;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.BPService;
import com.invado.masterdata.service.DeviceHolderPartnerService;
import com.invado.masterdata.service.TelekomWSClient;
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
 * Created by Nikola on 07/12/2015.
 */
@Controller
public class DeviceHolderPartnerController {

    @Inject
    private DeviceHolderPartnerService service;
    @Inject
    private BPService bpService;
    @Inject
    private TelekomWSClient telekomWSClient;


    @RequestMapping(value = "/device/device-assignment.html", method = RequestMethod.GET)
    public String showDetailDeviceAssignment(@RequestParam Integer page,
                                         @RequestParam(value = "deviceCustomCode") String deviceCustomCode,
                                         @RequestParam(value = "businessPartnerId", required = false) Integer businessPartnerId,
                                         @ModelAttribute DeviceHolderPartnerDTO deviceHolderPartnerDTO,
                                         Map<String, Object> model) throws Exception {

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("partnerId", businessPartnerId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("customCode", deviceCustomCode));
        ReadRangeDTO<DeviceHolderPartnerDTO> items = service.readPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        //model.put("page", page);
        model.put("numberOfPages", items.getNumberOfPages());

        return "device-assignment";
    }

    @RequestMapping(value = "/deviceholder/create.html", method = RequestMethod.GET)
    public String initCreateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 Map<String, Object> model) {

        DeviceHolderPartnerDTO deviceHolderPartnerDTO = new DeviceHolderPartnerDTO();

        BusinessPartnerDTO businessPartner = null;
        if (pointOfSaleId != null) {
            businessPartner = bpService.read(pointOfSaleId);
            deviceHolderPartnerDTO.setBusinessPartnerId(businessPartner.getId());
            deviceHolderPartnerDTO.setBusinessPartnerName(businessPartner.getName());
        }


        model.put("connectionTypes", service.getConnectionTypes());
        model.put("refillTypes", service.getRefillTypes());
        model.put("item", deviceHolderPartnerDTO);
        model.put("action", "create");
        return "device-assignement-grid";
    }

    @RequestMapping(value = "/deviceholder/create.html", method = RequestMethod.POST)
    public String processCreateForm(@RequestParam String page,
                                    @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                    @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                    @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                    @ModelAttribute("item") DeviceHolderPartnerDTO item,
                                    BindingResult result,
                                    SessionStatus status,
                                    Map<String, Object> model) throws Exception {

        if (result.hasErrors()) {
            model.put("action", "create");
            return "business-partner-device-grid";
        } else {
            service.create(item);
            status.setComplete();
        }
        return "redirect:/deviceholder/create.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&pointOfSaleId=" + pointOfSaleId + "&page=" + 0;
    }

    @RequestMapping(value = "/deviceholder/create-detail.html", method = RequestMethod.GET)
    public String initCreateDetailForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 Map<String, Object> model) {

        DeviceHolderPartnerDTO deviceHolderPartnerDTO = new DeviceHolderPartnerDTO();

        BusinessPartnerDTO businessPartner = null;
        if (pointOfSaleId != null) {
            businessPartner = bpService.read(pointOfSaleId);
            deviceHolderPartnerDTO.setBusinessPartnerId(businessPartner.getId());
            deviceHolderPartnerDTO.setBusinessPartnerName(businessPartner.getName());
        }


        model.put("connectionTypes", service.getConnectionTypes());
        model.put("refillTypes", service.getRefillTypes());
        model.put("item", deviceHolderPartnerDTO);
        model.put("action", "create");
        return "business-partner-device-grid";
    }


    @RequestMapping(value = "/deviceholder/create-detail.html", method = RequestMethod.POST)
    public String processCreateDetailForm(@RequestParam String page,
                                    @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                    @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                    @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                    @ModelAttribute("item") DeviceHolderPartnerDTO item,
                                    BindingResult result,
                                    SessionStatus status,
                                    Map<String, Object> model) throws Exception {

        if (result.hasErrors()) {
            model.put("action", "create");
            return "business-partner-device-grid";
        } else {
            service.create(item);
            status.setComplete();
        }
        return "redirect:/deviceholder/create-detail.html?masterPartnerId=&masterPartnerName=&pointOfSaleId=&page=0";
    }

    @RequestMapping(value = "/device/update-assignment.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 @RequestParam(value = "id") Integer id,
                                 Map<String, Object> model)
            throws Exception {

        DeviceHolderPartnerDTO item = service.read(id);

        model.put("connectionTypes", service.getConnectionTypes());
        model.put("refillTypes", service.getRefillTypes());
        model.put("item", item);
        return "device-assignement-grid";
    }

    @RequestMapping(value = "/device/update-assignment.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") DeviceHolderPartnerDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "business-partner-contact-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "device-assignment";
    }

    @RequestMapping(value = "/deviceholder/registerTerminal")
    public String registerTerminal(@ModelAttribute DeviceHolderPartnerDTO deviceHolderPartnerDTO) throws Exception{

        /*Ovo nije završeno jer rerutn statement nije dobar potrebni su parametri....i slično*/
        deviceHolderPartnerDTO.setTelekomId(telekomWSClient.terminalRegistration(deviceHolderPartnerDTO));
        service.update(deviceHolderPartnerDTO);
        return "redirect:/deviceholder/update.html";
    }



    /*
    @RequestMapping(value = "/deviceholder/create.html", method = RequestMethod.POST)
    public String processCreationForm(@RequestParam String page,
                                      @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                      @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                      @ModelAttribute("item") BusinessPartnerContactDetailsDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "business-partner-contact-grid";
        } else {
            service.create(item);
            model.put("message", item.getId() + " " + item.getName());
            status.setComplete();
        }
        //return "redirect:/township/{page}";
        return "redirect:/deviceholder/create.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }


   @RequestMapping("/deviceholder/{page}")
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

        ReadRangeDTO<BusinessPartnerContactDetailsDTO> items = service.readPage(request);

        modelMap.addAttribute("filterObjects", filterList);
        modelMap.addAttribute("data", items.getData());
        modelMap.addAttribute("page", items.getPage());
        modelMap.addAttribute("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "business-partner-contact-view";
    }

    @RequestMapping(value = "/contact/create.html", method = RequestMethod.GET)
    public String initCreateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 Map<String, Object> model) {
        BusinessPartnerContactDetailsDTO businessPartnerContactDetailsDTO = new BusinessPartnerContactDetailsDTO();
        BusinessPartnerDTO businessPartner = null;
        if (pointOfSaleId != null) {
            businessPartner = bpService.read(pointOfSaleId);
        } else {
            businessPartner = bpService.read(masterPartnerId);
        }
        if (businessPartner.getParentBusinessPartnerId() != null) {
            businessPartnerContactDetailsDTO.setMerchantId(businessPartner.getParentBusinessPartnerId());
            businessPartnerContactDetailsDTO.setMerchantName(businessPartner.getParentBusinesspartnerName());
            businessPartnerContactDetailsDTO.setPointOfSaleId(businessPartner.getId());
            businessPartnerContactDetailsDTO.setPointOfSaleName(businessPartner.getName());
        } else {
            businessPartnerContactDetailsDTO.setMerchantId(businessPartner.getId());
            businessPartnerContactDetailsDTO.setMerchantName(businessPartner.getName());
        }
        model.put("item", businessPartnerContactDetailsDTO);
        model.put("action", "create");
        return "business-partner-contact-grid";
    }

    @RequestMapping(value = "/contact/create.html", method = RequestMethod.POST)
    public String processCreationForm(@RequestParam String page,
                                      @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                      @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                      @ModelAttribute("item") BusinessPartnerContactDetailsDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "business-partner-contact-grid";
        } else {
            service.create(item);
            model.put("message", item.getId() + " " + item.getName());
            status.setComplete();
        }
        //return "redirect:/township/{page}";
        return "redirect:/contact/create.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }

    @RequestMapping("/contact/delete.html")
    public String delete(@RequestParam Integer id, @RequestParam String page,
                         @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                         @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName) throws Exception {
        service.delete(id);
        //return "redirect:/contact/{page}";
        return "redirect:/partner/read-contactsdetals-page.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }

    @RequestMapping(value = "/contact/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 @RequestParam(value = "id") Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerContactDetailsDTO item = service.read(id).getDTO();
        model.put("item", item);
        return "business-partner-contact-grid";
    }

    @RequestMapping(value = "/contact/update.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerContactDetailsDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "business-partner-contact-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/contact/update.html";
    }
*/
}
