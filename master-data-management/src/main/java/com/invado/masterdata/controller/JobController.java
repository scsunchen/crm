package com.invado.masterdata.controller;

import com.invado.core.domain.Job;
import com.invado.masterdata.service.JobService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;

/**
 * Created by NikolaB on 6/14/2015.
 */
public class JobController {
    @Autowired
    private JobService service;


    @RequestMapping("/Job/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<Job> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "Job-view";
    }

    @RequestMapping(value = "/Job/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new Job());
        model.put("action", "create");
        return "Job-grid";
    }

    @RequestMapping(value = "/Job/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") Job item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "Job-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/Job/{page}";
    }

    @RequestMapping("/Job/{page}/{code}/delete.html")
    public String delete(@PathVariable String code) throws Exception {
        service.delete(code);
        return "redirect:/Job/{page}";
    }

    @RequestMapping(value = "/Job/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        Job item = service.read(id);
        model.put("item", item);
        return "Job-grid";
    }

    @RequestMapping(value = "/Job/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") Job item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "Job-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/Job/{page}";
    }
}
