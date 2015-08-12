package com.invado.masterdata.controller;

import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.Client;
import com.invado.core.domain.Township;
import com.invado.core.dto.ClientDTO;
import com.invado.masterdata.service.BankCreditorService;
import com.invado.masterdata.service.ClientService;
import com.invado.masterdata.service.TownshipService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by NikolaB on 6/9/2015.
 */
@Controller
public class ClientController {

    @Autowired
    private ClientService service;
    @Autowired
    private TownshipService townshipService;
    @Autowired
    private BankCreditorService bankCreditorService;



    @RequestMapping("/client/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<ClientDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "client-view";
    }

    @RequestMapping(value = "/client/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {

        model.put("item", new ClientDTO());

        List<Client.Status> statuses = Arrays.asList(Client.Status.values());
        model.put("statuses", statuses);

        List<Township> townships = townshipService.readAll(null, null);
        model.put("townships", townships);

        List<BankCreditor> bankCreditors = bankCreditorService.readAll(null, null);
        model.put("banks", bankCreditors);

        model.put("action", "create");

        return "client-grid";
    }

    @RequestMapping(value = "/client/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") ClientDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            String resultErrorMessages = null;
            model.put("action", "create");
            for( ObjectError e:result.getAllErrors()) {
                resultErrorMessages += e.getDefaultMessage();
            }
            model.put("resulterror", resultErrorMessages);
            return "resulterror";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/client/{page}/create";
    }


    @RequestMapping("/client/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/client/{page}";
    }

    @RequestMapping(value = "/client/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        List<Client.Status> statuses = Arrays.asList(Client.Status.values());
        model.put("statuses", statuses);

        List<Township> townships = townshipService.readAll(null, null);
        model.put("townships", townships);

        List<BankCreditor> bankCreditors = bankCreditorService.readAll(null, null);
        model.put("banks", bankCreditors);

        ClientDTO item = service.read(id);
        model.put("item", item);
        return "client-grid";
    }

    @RequestMapping(value = "/client/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") ClientDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "client-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/client/{page}";
    }
}
