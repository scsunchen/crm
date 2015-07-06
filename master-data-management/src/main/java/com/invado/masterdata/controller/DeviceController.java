package com.invado.masterdata.controller;

import com.invado.core.domain.Article;
import com.invado.core.domain.Device;
import com.invado.core.domain.DeviceStatus;
import com.invado.core.domain.Township;
import com.invado.finance.service.ArticleService;
import com.invado.finance.service.MasterDataService;
import com.invado.masterdata.service.DeviceService;
import com.invado.masterdata.service.DeviceStatusService;
import com.invado.masterdata.service.TownshipService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MasterDataService masterDataService;



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

        List<DeviceStatus> deviceStatuses = deviceStatusService.readAll(null, null);
        model.put("deviceStatuses", deviceStatuses);
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
            System.out.println("Kod artikla je "+item.getArticleCode());
            item.setArticle(articleService.read(item.getArticleCode()));
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
        Device item = deviceService.read(id);
        model.put("item", item);
        return "device-grid";
    }

    @RequestMapping(value = "/device/{page}/update/{id}",
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

    @RequestMapping(value = "/device/read-item/{desc}")
    public @ResponseBody
    List<Article> findItemByDescription(@PathVariable String desc) {
        return masterDataService.readItemByDescription(desc);
    }
}
