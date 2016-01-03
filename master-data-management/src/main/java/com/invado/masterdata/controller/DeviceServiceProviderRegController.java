package com.invado.masterdata.controller;

import com.invado.core.domain.DeviceStatus;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.core.dto.DeviceServiceProviderRegistrationDTO;
import com.invado.core.dto.DeviceServiceProviderRegistrationDTO;
import com.invado.core.dto.DeviceStatusDTO;
import com.invado.masterdata.service.BPService;
import com.invado.masterdata.service.DeviceServiceProviderRegistrationService;
import com.invado.masterdata.service.DeviceStatusService;
import com.invado.masterdata.service.TelekomWSClient;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 01/01/2016.
 */
@Controller
public class DeviceServiceProviderRegController {


    @Inject
    private DeviceServiceProviderRegistrationService service;
    @Inject
    private BPService bpService;
    @Inject
    private TelekomWSClient telekomWSClient;
    @Inject
    private DeviceStatusService statusService;


    @RequestMapping(value = "/deviceservprovider/device-serv-provider.html", method = RequestMethod.GET)
    public String showDetailDeviceServiceProvider(@RequestParam Integer page,
                                                  @ModelAttribute DeviceServiceProviderRegistrationDTO deviceServiceProviderRegistrationDTO,
                                                  Map<String, Object> model) throws Exception {

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);


        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("deviceId", deviceServiceProviderRegistrationDTO.getDeviceId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("partnerId", deviceServiceProviderRegistrationDTO.getServiceProviderId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("customCode", deviceServiceProviderRegistrationDTO.getDeviceCustomCode()));

        ReadRangeDTO<DeviceServiceProviderRegistrationDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());

        //model.put("page", page);
        model.put("numberOfPages", items.getNumberOfPages());

        return "device-service-provider";
    }

    @RequestMapping(value = "/deviceservprovider/create.html", method = RequestMethod.GET)
    public String initCreateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 Map<String, Object> model) {

        DeviceServiceProviderRegistrationDTO deviceHolderPartnerDTO = new DeviceServiceProviderRegistrationDTO();

        BusinessPartnerDTO businessPartner = null;

        model.put("connectionTypes", service.getConnectionTypes());
        model.put("deviceStatuses", statusService.readAll(null, null));
        model.put("refillTypes", service.getRefillTypes());
        model.put("item", deviceHolderPartnerDTO);
        model.put("action", "create");

        return "device-service-provider-grid";
    }

    @RequestMapping(value = "/deviceservprovider/create.html", method = RequestMethod.POST)
    public String processCreateForm(@RequestParam String page,
                                    @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                    @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                    @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                    @ModelAttribute("item") DeviceServiceProviderRegistrationDTO item,
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

        if (pointOfSaleId != null)
            return "redirect:/deviceservprovider/create.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&pointOfSaleId=" + pointOfSaleId + "&page=" + 0;

        return "redirect:/deviceservprovider/create.html?masterPartnerId=&masterPartnerName=&pointOfSaleId=&page=0";
    }

    @RequestMapping(value = "/deviceservprovider/create-detail.html", method = RequestMethod.GET)
    public String initCreateDetailForm(@RequestParam String page,
                                       @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                       @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                       @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                       Map<String, Object> model) {

        DeviceServiceProviderRegistrationDTO deviceHolderPartnerDTO = new DeviceServiceProviderRegistrationDTO();

        BusinessPartnerDTO businessPartner = null;
        if (pointOfSaleId != null) {
            businessPartner = bpService.read(pointOfSaleId);
            deviceHolderPartnerDTO.setServiceProviderId(businessPartner.getId());
            deviceHolderPartnerDTO.setServiceProviderName(businessPartner.getName());
        }


        model.put("connectionTypes", service.getConnectionTypes());
        model.put("refillTypes", service.getRefillTypes());
        model.put("item", deviceHolderPartnerDTO);
        model.put("action", "create");
        return "business-partner-device-grid";
    }


    @RequestMapping(value = "/deviceservprovider/create-detail.html", method = RequestMethod.POST)
    public String processCreateDetailForm(@RequestParam String page,
                                          @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                          @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                          @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                          @ModelAttribute("item") DeviceServiceProviderRegistrationDTO item,
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
        return "redirect:/deviceservprovider/create-detail.html?masterPartnerId=&masterPartnerName=&pointOfSaleId=&page=0";
    }

    @RequestMapping(value = "/deviceservprovider/update-serv-provider.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 @RequestParam(value = "id") Integer id,
                                 Map<String, Object> model)
            throws Exception {

        DeviceServiceProviderRegistrationDTO item = service.read(id);

        model.put("connectionTypes", service.getConnectionTypes());
        model.put("deviceStatuses", statusService.readAll(null, null));
        model.put("refillTypes", service.getRefillTypes());

        model.put("item", item);
        return "device-service-provider-grid";
    }

    @RequestMapping(value = "/deviceservprovider/update-serv-provider.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") DeviceServiceProviderRegistrationDTO item,
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
        return "redirect:/deviceservprovider/device-serv-provider.html?page=0";
    }


    @RequestMapping("/deviceservprovider/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/deviceservprovider/device-serv-provider.html?page=0";
    }


    @RequestMapping(value = "/deviceservprovider/terminal/register")
    public String processTelekomTermnalRegistration(@ModelAttribute("item") DeviceServiceProviderRegistrationDTO item) throws Exception {

        if (item.getId() != null)
            service.create(item);
        item.setRegistrationId(telekomWSClient.terminalRegistration(item));
        service.update(item);

        return "partner-grid";
    }


    @RequestMapping(value = "/deviceservprovider/terminal/update")
    public String processTelekomTerminalUpdate(@ModelAttribute("item") DeviceServiceProviderRegistrationDTO item) throws Exception {

        if (item.getId() != null)
            service.create(item);
        item.setRegistrationId(telekomWSClient.terminalUpdate(item));
        service.update(item);

        return "partner-grid";
    }

    @RequestMapping(value = "/deviceservprovider/terminal/update-status")
    public String processTelekomTerminalUpdateStatus(@ModelAttribute("item") DeviceServiceProviderRegistrationDTO item) throws Exception {

        if (item.getId() != null)
            service.create(item);
        item.setRegistrationId(telekomWSClient.terminalStatusUpdate(item));
        service.update(item);

        return "partner-grid";
    }


    @RequestMapping(value = "/deviceservprovider/terminal/cancel-activate")
    public String processTelekomTerminalCancleActivate(@ModelAttribute("item") DeviceServiceProviderRegistrationDTO item) throws Exception {

        if (item.getId() != null)
            service.create(item);
        item.setRegistrationId(telekomWSClient.terminalCancelActivate(item));
        service.update(item);

        return "partner-grid";
    }
}
