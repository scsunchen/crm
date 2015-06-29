package com.invado.masterdata.controller;

import com.invado.core.domain.DeviceStatus;
import com.invado.masterdata.service.DeviceStatusService;
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
 * Created by NikolaB on 6/21/2015.
 */
@Controller
public class DeviceStatusController {
    @Autowired
    private DeviceStatusService service;


    @RequestMapping("/devicestatus/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<DeviceStatus> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "devicestatus-view";
    }

    @RequestMapping(value = "/devicestatus/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new DeviceStatus());
        model.put("action", "create");
        return "devicestatus-grid";
    }

    @RequestMapping(value = "/devicestatus/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") DeviceStatus item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "devicestatus-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/devicestatus/{page}";
    }

    @RequestMapping("/devicestatus/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/devicestatus/{page}";
    }

    @RequestMapping(value = "/devicestatus/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        DeviceStatus item = service.read(id);
        model.put("item", item);
        return "devicestatus-grid";
    }

    @RequestMapping(value = "/devicestatus/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") DeviceStatus item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "devicestatus-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/devicestatus/{page}";
    }
}
