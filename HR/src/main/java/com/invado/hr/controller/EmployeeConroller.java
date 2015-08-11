package com.invado.hr.controller;

import com.invado.core.domain.Employee;
import com.invado.core.domain.Job;
import com.invado.core.domain.OrgUnit;
import com.invado.hr.service.EmployeeService;
import com.invado.hr.service.JobService;
import com.invado.hr.service.MasterDataService;
import com.invado.hr.service.dto.PageRequestDTO;
import com.invado.hr.service.dto.ReadRangeDTO;
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
    private MasterDataService masterDataService;


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
        List<Job> jobs = jobService.readAll(null, null);
        model.put("jobs", jobs);
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
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/employee/{page}/create";
    }

    @RequestMapping("/employee/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/employee/{page}";
    }

    @RequestMapping(value = "/employee/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        Employee item = service.read(id);
        model.put("item", item);
        return "employee-grid";
    }

    @RequestMapping(value = "/employee/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@PathVariable Integer id,
                                      @ModelAttribute("item") Employee item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "employee-grid";
        } else {
            System.out.println("ovo je poruka iz "+item.getId());
            if (item.getId() == null)
                item.setId(id);
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/employee/{page}";
    }

}
