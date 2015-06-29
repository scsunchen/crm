package com.invado.masterdata.controller;

import com.invado.core.domain.Device;
import com.invado.core.domain.DeviceStatus;
import com.invado.core.domain.Township;
import com.invado.masterdata.service.DeviceService;
import com.invado.masterdata.service.DeviceStatusService;
import com.invado.masterdata.service.TownshipService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by NikolaB on 6/21/2015.
 */
@Controller
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceStatusService deviceStatusService;


    @RequestMapping("/device/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<Device> items = deviceService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "device-view";
    }

    @RequestMapping(value = "/device/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {

        model.put("item", new Device());

        List<DeviceStatus> statuses = deviceStatusService.readAll(null, null);
        model.put("statuses", statuses);
        model.put("action", "create");

        return "device-grid";
    }

    @RequestMapping(value = "/device/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") Device item,
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
            for( ObjectError e:result.getAllErrors()) {
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


    @RequestMapping("/device/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        deviceService.delete(code);
        return "redirect:/device/{page}";
    }

    @RequestMapping(value = "/device/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String code,
                                 Map<String, Object> model)
            throws Exception {
        Device item = deviceService.read(code);
        model.put("item", item);
        return "device-grid";
    }

    @RequestMapping(value = "/device/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") Device item,
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
}
