package com.invado.masterdata.controller;

import com.invado.core.domain.Article;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Device;
import com.invado.core.dto.DeviceDTO;
import com.invado.core.dto.DeviceStatusDTO;
//import com.invado.finance.service.MasterDataService;
import com.invado.masterdata.service.*;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by NikolaB on 6/21/2015.
 */
@Controller
public class DeviceController {

    @Inject
    private DeviceService deviceService;
    @Inject
    private DeviceStatusService deviceStatusService;
    @Inject
    private TelekomWSClient telekomWSClient;
    @Inject
    private BPService businessPartnerService;

    @RequestMapping("/device/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<DeviceDTO> items = deviceService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "device-view";
    }

    @RequestMapping(value = "/device/create.html", method = RequestMethod.GET)
    public String initCreateForm( Map<String, Object> model) {

        model.put("item", new DeviceDTO());
        List<DeviceStatusDTO> deviceStatuses = deviceStatusService.readAll(null, null);
        model.put("deviceStatuses", deviceStatuses);
        model.put("action", "create");

        return "device-grid";
    }

    @RequestMapping(value = "/device/create.html", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") DeviceDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            String resultErrorMessages = null;
            model.put("action", "create");
            /*
            System.out.println(model.get("bank").toString());
            */
            for (ObjectError e : result.getAllErrors()) {
                resultErrorMessages += e.getDefaultMessage();
            }
            model.put("resulterror", resultErrorMessages);
            return "resulterror";
        } else {
            this.deviceService.create(item);
            status.setComplete();
        }
        return "redirect:/device/{page}";

    }


    @RequestMapping("/device/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        deviceService.delete(id);
        return "redirect:/device/{page}";
    }

    @RequestMapping(value = "/device/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        DeviceDTO item = deviceService.read(id);
        List<DeviceStatusDTO> deviceStatuses = deviceStatusService.readAll(null, null);
        model.put("deviceStatuses", deviceStatuses);
        model.put("item", item);
        return "device-grid";
    }

    @RequestMapping(value = "/device/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") DeviceDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "device-grid";
        } else {
            this.deviceService.update(item);
            status.setComplete();
        }
        return "redirect:/device/{page}";
    }

    /*
    map na WS za telekom...doprogramirati...

    <li><a href="/masterdata/device/registerTerminal">Registracija Terminala</a></li>
    <li><a href="/masterdata/partner/updateTerminal">Izmena Terminala</a></li>
    <li><a href="/masterdata/partner/updateTerminalStatus">Izmena Status Terminala</a></li>
    <li><a href="/masterdata/partner/cancelActivateTerminal">Otkazi/Akirajtiv</a></li>
    <li><a href="/masterdata/partner/checkTerminalStatus">Provera Status Terminala</a></li>

    */



    @RequestMapping(value = "/device/read-item/{desc}")
    public
    @ResponseBody
    List<Article> findItemByDescription(@PathVariable String desc) {
        return deviceService.readItemByDescription(desc);
    }


}