/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.controller;

import com.invado.core.domain.Article;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.exception.*;
import com.invado.customer.relationship.domain.ServiceProviderServices;
import com.invado.customer.relationship.service.MasterDataService;
import com.invado.customer.relationship.service.ServiceProviderService;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.exception.ArticleNotFoundException;
import com.invado.customer.relationship.service.exception.BusinessPartnerIsNotServiceProviderException;
import com.invado.customer.relationship.service.exception.BusinessPartnerNotFoundException;
import com.invado.customer.relationship.service.exception.ServiceProviderServicesConstraintViolationException;
import com.invado.customer.relationship.service.exception.ServiceProviderServicesIsNotUniqueException;
import com.invado.customer.relationship.service.exception.ServiceProviderServicesNotFoundException;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author bdragan
 */
@Controller
public class ServiceProviderServicesController {

    @Inject
    private ServiceProviderService service;
    @Inject
    private MasterDataService masterDataService;

    @RequestMapping("/service-provider-services/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer page,
            @PathVariable Integer id) {
        service.delete(id);
        return "redirect:/service-provider-services/{page}/read-page.html";
    }

    @RequestMapping("/service-provider-services/{page}/read-page.html")
    public String readPage(@PathVariable Integer page,
            Map<String, Object> model)
            throws PageNotExistsException {
        PageRequestDTO dto = new PageRequestDTO();
        dto.setPage(page);
        ReadRangeDTO<ServiceProviderServices> result = service.readPage(dto);
        model.put("data", result.getData());
        model.put("page", result.getPage());
        model.put("numberOfPages", result.getNumberOfPages());
        return "service-provider-services-table";
    }

    @RequestMapping(value = "/service-provider-services/{page}/create.html",
            method = RequestMethod.GET)
    public String initCreate(@PathVariable Integer page,
            Map<String, Object> model) {
        ServiceProviderServices services = new ServiceProviderServices();
        services.setService(new Article());
        services.setServiceProvider(new BusinessPartner());
        model.put("action", "create");
        model.put("service", services);
        model.put("page", page);
        return "service-provider-services-grid";
    }

    @RequestMapping(value = "/service-provider-services/{page}/create.html",
            method = RequestMethod.POST)
    public String create(
            @ModelAttribute("service") ServiceProviderServices services,
            BindingResult result,
            SessionStatus status,
            @PathVariable Integer page,
            Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("page", page);
            return "service-provider-services-grid";
        }
        try {
            service.create(services);
            status.setComplete();
        } catch (ServiceProviderServicesConstraintViolationException 
                | ServiceProviderServicesIsNotUniqueException 
                | ArticleNotFoundException 
                | BusinessPartnerNotFoundException 
                | BusinessPartnerIsNotServiceProviderException 
                | SystemException ex) {
            model.put("page", page);
            model.put("exception", ex);
            model.put("action", "create");
            return "service-provider-services-grid";
        }
        return "redirect:/service-provider-services/{page}/read-page.html";
    }

    @RequestMapping(value = "/service-provider-services/{page}/{id}/update.html",
            method = RequestMethod.GET)
    public String initUpdate(@PathVariable Integer page,
            @PathVariable Integer id,
            Map<String, Object> model) throws EntityNotFoundException {
        model.put("service", service.read(id));
        model.put("page", page);
        model.put("action", "update");
        return "service-provider-services-grid";
    }
    
    @RequestMapping(value = "/service-provider-services/{page}/{id}/update.html",
                    method = RequestMethod.POST)
    public String update(@ModelAttribute("service") ServiceProviderServices services,
                         BindingResult result,
                         SessionStatus status,
                         @PathVariable Integer page,
                         @PathVariable Integer id,
                         Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("action", "update");
            model.put("page", page);
            return "service-provider-services-grid";
        }
        try {
            service.update(services);
            status.setComplete();
        } catch (ServiceProviderServicesConstraintViolationException
                | ServiceProviderServicesNotFoundException
                | ArticleNotFoundException 
                | BusinessPartnerNotFoundException 
                | BusinessPartnerIsNotServiceProviderException 
                | SystemException ex) {
            model.put("exception", ex);
            model.put("action", "update");
            model.put("page", page);
            return "service-provider-services-grid";
        }
        return "redirect:/service-provider-services/{page}/read-page.html";
    }
    
    @RequestMapping("/service-provider-services/read-businesspartner/{query}")
    public @ResponseBody
    List<BusinessPartner> readServiceProviderByName(@PathVariable String query) {
        return masterDataService.readServiceProviderByName(query);
    }

    @RequestMapping("/service-provider-services/read-item/{query}")
    public @ResponseBody
    List<Article> readArticleByDescription(@PathVariable String query) {
        return masterDataService.readItemByDescription(query);
    }
}
