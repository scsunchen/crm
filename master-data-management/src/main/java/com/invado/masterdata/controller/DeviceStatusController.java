package com.invado.masterdata.controller;

import com.invado.core.dto.DeviceStatusDTO;
import com.invado.masterdata.service.DeviceStatusService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;

/**
 * Created by NikolaB on 6/21/2015.
 */
@Controller
public class DeviceStatusController {

    @Autowired
    private DeviceStatusService service;


    @RequestMapping("/devicestatus/read-page.html")
    public String showItems(@RequestParam Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<DeviceStatusDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "devicestatus-view";
    }

    @RequestMapping(value = "/devicestatus/create.html", method = RequestMethod.GET)
    public String initCreateForm(Map<String, Object> model) {
        model.put("item", new DeviceStatusDTO());
        model.put("action", "create");
        return "devicestatus-grid";
    }

    @RequestMapping(value = "/devicestatus/create.html", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") DeviceStatusDTO item,
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
        //return "redirect:/devicestatus/{page}/create";
        return "redirect:/devicestatus/create.html";
    }

    @RequestMapping("/devicestatus/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/devicestatus/{page}";
    }

    @RequestMapping(value = "/devicestatus/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam Integer id,
                                 @RequestParam Integer page,
                                 Map<String, Object> model)
            throws Exception {
        DeviceStatusDTO item = service.read(id);
        model.put("item", item);
        return "devicestatus-grid";
    }

    @RequestMapping(value = "/devicestatus/update.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") DeviceStatusDTO item,
                                      @RequestParam Integer id,
                                      @RequestParam Integer page,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "devicestatus-grid";
        } else {
            service.update(item);
            status.setComplete();
        }
        //return "redirect:/devicestatus/{page}";
        return "redirect:/devicestatus/read-page.html?page=0";
    }
}
