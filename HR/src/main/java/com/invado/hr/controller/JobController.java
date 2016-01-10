package com.invado.hr.controller;

import com.invado.core.domain.Job;
import com.invado.core.dto.JobDTO;
import com.invado.hr.service.JobService;
import com.invado.hr.service.dto.PageRequestDTO;
import com.invado.hr.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by NikolaB on 6/14/2015.
 */
@Controller
public class JobController {
    
    @Inject
    private JobService service;

    @RequestMapping("/job/read-page.html")
    public String showItems(@RequestParam Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<JobDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "job-view";
    }

    @RequestMapping(value = "/job/create.html", method = RequestMethod.GET)
    public String initCreateForm(Map<String, Object> model) {
        model.put("action", "create");
        model.put("item", new JobDTO());
        return "job-grid";
    }

    @RequestMapping(value = "/job/create.html", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") JobDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "job-grid";
        } else {
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/job/create.html";
    }

    @RequestMapping("/job/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/job/read-page.html?page=0";
    }

    @RequestMapping(value = "/job/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam Integer id,
                                 Map<String, Object> model)
            throws Exception {
        JobDTO item = service.read(id);
        model.put("item", item);
        return "job-grid";
    }

    @RequestMapping(value = "/job/update.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@RequestParam Integer id,
                                      @ModelAttribute("item") JobDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "job-grid";
        } else {
            if (item.getId() == null)
                item.setId(id);
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/job/read-page.html?page=0";
    }
}
