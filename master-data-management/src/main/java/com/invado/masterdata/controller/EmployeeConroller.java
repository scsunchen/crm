package com.invado.masterdata.controller;

import com.invado.core.domain.Employee;
import com.invado.masterdata.service.EmployeeService;
import com.invado.masterdata.service.JobService;
import com.invado.masterdata.service.OrgUnitService;
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

import java.util.List;
import java.util.Map;

/**
 * Created by NikolaB on 6/17/2015.
 */
@Controller
public class EmployeeConroller {

    @Autowired
    private EmployeeService service;
    @Autowired
    private JobService jobService;
    @Autowired
    private OrgUnitService orgUnitService;



    @RequestMapping("/employee/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<Employee> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "employee-view";
    }

    @RequestMapping(value = "/employee/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        System.out.println("u pravom smo kodu");
        model.put("item", new Employee());
        model.put("action", "create");
        return "employee-grid";
    }

    @RequestMapping(value = "/employee/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") Employee item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "employee-grid";
        } else {
            item.setJob(jobService.read(item.getJob().getId()));
            item.setOrgUnit(orgUnitService.read(item.getOrgUnit().getId()));
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/employee/{page}";
    }

    @RequestMapping("/employee/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/employee/{page}";
    }

    @RequestMapping(value = "/employee/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String code,
                                 Map<String, Object> model)
            throws Exception {
        Employee item = service.read(code);
        model.put("item", item);
        return "employee-grid";
    }

    @RequestMapping(value = "/employee/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") Employee item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "employee-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/employee/{page}";
    }
    
}
