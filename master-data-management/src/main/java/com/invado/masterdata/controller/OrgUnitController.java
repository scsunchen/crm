package com.invado.masterdata.controller;

import com.invado.core.domain.OrgUnit;
import com.invado.masterdata.service.OrgUnitService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by NikolaB on 6/7/2015.
 */
@Controller
public class OrgUnitController {
    @Autowired
    private OrgUnitService service;


    @RequestMapping("/org-unit/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<OrgUnit> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "orgunit-view";
    }

    @RequestMapping(value = "/org-unit/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new OrgUnit());
        model.put("action", "create");
        return "orgunit-grid";
    }

    @RequestMapping(value = "/org-unit/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") OrgUnit item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "orgunit-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/orgunit/{page}";
    }

    @RequestMapping("/org-unit/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/orgunit/{page}";
    }

    @RequestMapping(value = "/org-unit/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        OrgUnit item = service.read(id);
        model.put("item", item);
        return "orgunit-grid";
    }

    @RequestMapping(value = "/org-unit/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") OrgUnit item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "orgunit-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/orgunit/{page}";
    }

    @RequestMapping(value = "/orgunit/read-orgunit/{name}")
    public @ResponseBody
    List<OrgUnit> findItemByDescription(@PathVariable String name) {
        return service.readOrgUnitByNameAndCustomId(name);
    }
}
