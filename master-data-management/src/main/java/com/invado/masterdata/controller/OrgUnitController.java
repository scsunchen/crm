package com.invado.masterdata.controller;

import com.invado.core.domain.Client;
import com.invado.core.domain.OrgUnit;
import com.invado.core.dto.ClientDTO;
import com.invado.core.dto.OrgUnitDTO;
import com.invado.masterdata.service.ClientService;
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
    @Autowired
    private ClientService clientService;


    @RequestMapping("/org-unit/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<OrgUnit> items = service.readPageHierarchy(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "orgunit-view";
    }

    @RequestMapping(value = "/org-unit/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {

        model.put("item", new OrgUnitDTO());
        List<ClientDTO> clients = clientService.readAll(null, null, null, null);
        model.put("clients", clients);
        model.put("action", "create");
        return "orgunit-grid";
    }

    @RequestMapping(value = "/org-unit/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") OrgUnitDTO item,
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
        return "redirect:/org-unit/{page}/create";
    }

    @RequestMapping("/org-unit/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/org-unit/{page}";
    }

    @RequestMapping(value = "/org-unit/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        model.put("action", "update");
        OrgUnitDTO item = service.read(id);
        model.put("item", item);
        List<ClientDTO> clients = clientService.readAll(null, null, null, null);
        model.put("clients", clients);
        return "orgunit-grid";
    }

    @RequestMapping(value = "/org-unit/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") OrgUnitDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "orgunit-grid";
        } else {
            System.out.println("evo ga id "+item.getId());
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/org-unit/{page}";
    }


    @RequestMapping(value = "/org-unit/read-orgunit/{name}")
    public @ResponseBody
    List<OrgUnit> findItemByDescription(@PathVariable String name) {
        return service.readOrgUnitByNameAndCustomId(name);
    }
}
