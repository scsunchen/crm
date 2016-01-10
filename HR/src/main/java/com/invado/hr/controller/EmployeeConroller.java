package com.invado.hr.controller;

import com.invado.core.domain.Employee;
import com.invado.core.domain.Job;
import com.invado.core.domain.OrgUnit;
import com.invado.core.dto.EmployeeDTO;
import com.invado.core.dto.JobDTO;
import com.invado.core.dto.OrgUnitDTO;
import com.invado.hr.service.EmployeeService;
import com.invado.hr.service.JobService;
import com.invado.hr.service.MasterDataService;
import com.invado.hr.service.dto.PageRequestDTO;
import com.invado.hr.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
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


    @RequestMapping("/employee/read-page.html")
    public String showItems(@RequestParam Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<EmployeeDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "employee-view";
    }

    @RequestMapping(value = "/employee/create.html", method = RequestMethod.GET)
    public String initCreateForm(Map<String, Object> model) {
        List<JobDTO> jobs = jobService.readAll(null, null);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        model.put("jobs", jobs);
        List<OrgUnit> orgUnits = masterDataService.readAllOrgUnits();
        model.put("orgUnits", orgUnits);
        model.put("action", "create");
        model.put("item", employeeDTO);
        return "employee-grid";
    }

    @RequestMapping(value = "/employee/create.html", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") EmployeeDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      @RequestParam (required = false) CommonsMultipartFile[] fileUpload,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "employee-grid";
        } else {
            if (fileUpload != null && fileUpload.length > 0) {
                for (CommonsMultipartFile aFile : fileUpload){
                    System.out.println("Saving file: " + aFile.getOriginalFilename());

                    item.setPictureName(aFile.getOriginalFilename());
                    item.setPicture(aFile.getBytes());
                    item.setPictureContentType(aFile.getContentType());
                }
            }
            this.service.create(item);
            status.setComplete();
        }
        return "redirect:/employee/create.html";
    }

    @RequestMapping("/employee/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/employee/read-page.html?page=0";
    }

    @RequestMapping(value = "/employee/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam Integer id,
                                 Map<String, Object> model)
            throws Exception {
        List<JobDTO> jobs = jobService.readAll(null, null);
        model.put("jobs", jobs);
        EmployeeDTO item = service.read(id);
        model.put("item", item);
        return "employee-grid";
    }

    @RequestMapping(value = "/employee/update.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@RequestParam Integer id,
                                      @ModelAttribute("item") EmployeeDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      @RequestParam (required = false) CommonsMultipartFile[] fileUpload,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "employee-grid";
        } else {
            if (fileUpload != null && fileUpload.length > 0) {
                for (CommonsMultipartFile aFile : fileUpload){
                    System.out.println("Saving file: " + aFile.getOriginalFilename());

                    item.setPictureName(aFile.getOriginalFilename());
                    item.setPicture(aFile.getBytes());
                    item.setPictureContentType(aFile.getContentType());
                }
            }
            if (item.getId() == null)
                item.setId(id);
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/employee/read-page.html?page=0";
    }
    @RequestMapping(value = { "/employee/download-image.html" }, method = RequestMethod.GET)
    public String downloadDocument(@RequestParam int id,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   HttpServletResponse response) throws Exception {
        EmployeeDTO employee = service.read(id);
        response.setContentType(employee.getPictureContentType());
        response.setContentLength(employee.getPicture().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + employee.getPictureName() +"\"");

        FileCopyUtils.copy(employee.getPicture(), response.getOutputStream());

        return "redirect:/partner/read-page.html?page=0";
    }


}
