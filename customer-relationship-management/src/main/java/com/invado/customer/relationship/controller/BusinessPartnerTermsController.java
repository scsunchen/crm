package com.invado.customer.relationship.controller;

import com.invado.core.domain.Article;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.customer.relationship.domain.BusinessPartnerTerms;
import com.invado.customer.relationship.domain.BusinessPartnerTermsItem;
import com.invado.customer.relationship.service.BusinessPartnerTermsService;
import com.invado.customer.relationship.service.MasterDataService;
import com.invado.customer.relationship.service.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;
import java.util.List;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by NikolaB on 6/13/2015.
 */
@Controller
public class BusinessPartnerTermsController {

    @Inject
    private BusinessPartnerTermsService termsService;
    @Inject
    private MasterDataService masterDataService;

    @RequestMapping("/terms/{page}/read-page.html")
    public String readAll(@PathVariable Integer page,
            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<BusinessPartnerTerms> items = termsService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "terms-table";
    }

    @RequestMapping(value = "/terms/{page}/create.html",
            method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("terms", new BusinessPartnerTerms());
        model.put("page", page);
        return "terms-create-grid";
    }

    @RequestMapping(value = "/terms/{page}/create.html", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("terms") BusinessPartnerTerms item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("page", page);
            return "terms-create-grid";
        }
        try {
            this.termsService.create(item);
            status.setComplete();
        } catch (Exception ex) {
            model.put("exception", ex);
            model.put("page", page);
            return "terms-create-grid";
        }
        return "redirect:/terms/{page}/read-page.html";
    }

    @RequestMapping("/terms/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        termsService.delete(id);
        return "redirect:/terms/{page}/read-page.html";
    }

    @RequestMapping(value = "/terms/{page}/{id}/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
            @PathVariable String page,
            Map<String, Object> model)
            throws Exception {
        BusinessPartnerTerms terms = termsService.read(id);
        model.put("page", page);
        model.put("terms", terms);
        return "terms-update-grid";
    }

    @RequestMapping(value = "/terms/{page}/{id}/update.html",
            method = RequestMethod.POST)
    public String update(@ModelAttribute("terms") BusinessPartnerTerms item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            @PathVariable Integer id,
            Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("page", page);            
            model.put("terms", item);
            return "terms-update-grid";
        } else {
            try {
                this.termsService.update(item);
                status.setComplete();
            } catch (Exception ex) {
                model.put("exception", ex);
                model.put("page", page);
                model.put("terms", item);
                return "terms-update-grid";
            }
        }
        return "redirect:/terms/{page}/{id}/update.html";
    }

    @RequestMapping("/terms/read-merchant/{query}")
    public @ResponseBody
    List<BusinessPartner> readMerchantByName(@PathVariable String query) {
        return masterDataService.readBusinessPartnerByNameAndType(
                query,
                BusinessPartner.Type.MERCHANT);
    }

    @RequestMapping("/terms/read-service/{query}")
    public @ResponseBody
    List<Article> readServiceByCode(@PathVariable String query) {
        return masterDataService.readItemByCode(query);
    }
    
    @RequestMapping(value = "/terms/{page}/{itemsPage}/{termsId}/{ordinalNumber}/{termsVersion}/remove-item.html",
            method = RequestMethod.POST)
    public String deleteItem(@PathVariable Integer termsId,
                             @PathVariable Integer ordinalNumber,
                             @PathVariable Long termsVersion) {
        termsService.removeItem(termsId, ordinalNumber, termsVersion);
        return "redirect:/terms/{page}/{itemsPage}/{termsId}/details.html";
    }
    
    @RequestMapping(value = "/terms/{page}/{itemsPage}/{termsId}/details.html", 
                    method = RequestMethod.GET)
    public String initDetails(
            @PathVariable Integer page,
            @PathVariable Integer itemsPage,
            @PathVariable Integer termsId,
            Map<String, Object> model)
            throws Exception {
        model.put("itemsPage", itemsPage);
        BusinessPartnerTerms terms = termsService.read(termsId);
        model.put("page", page);
        model.put("terms", terms);
        BusinessPartnerTermsItem item = new BusinessPartnerTermsItem(terms,null);
        item.setArticle(new Article());
        model.put("termsItemToAdd", item);
        PageRequestDTO requestItemPage = new PageRequestDTO();
        requestItemPage.setPage(itemsPage);
        requestItemPage.addSearchCriterion(new PageRequestDTO.SearchCriterion("id", termsId));
        ReadRangeDTO<BusinessPartnerTermsItem> page1 = termsService.readItemsPage(requestItemPage);
        model.put("items", page1.getData());        
        model.put("numberOfPages", page1.getNumberOfPages());        
        return "terms-details";
    }
    
    @RequestMapping(value = "/terms/{page}/{itemsPage}/{termsId}/add-item.html",
            method = RequestMethod.POST)
    public String addItem(
            @ModelAttribute("termsItemToAdd") BusinessPartnerTermsItem item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            @PathVariable Integer itemsPage,
            @PathVariable Integer termsId,
            Map<String, Object> model) throws Exception {
        if (result.hasErrors()) {
            model.put("showDialog", Boolean.TRUE);
            model.put("page", page);
            model.put("itemsPage", itemsPage);
            BusinessPartnerTerms terms = termsService.read(termsId);
            model.put("terms", terms);
            PageRequestDTO requestItemPage = new PageRequestDTO();
            requestItemPage.setPage(itemsPage);
            requestItemPage.addSearchCriterion(new PageRequestDTO.SearchCriterion("id", termsId));
            ReadRangeDTO<BusinessPartnerTermsItem> i = 
                    termsService.readItemsPage(requestItemPage);
            model.put("items", i.getData());
            model.put("numberOfPages", i.getNumberOfPages());
            return "terms-details";
        }
        try {
            termsService.addItem(item);
            status.setComplete();
            return "redirect:/terms/{page}/{itemsPage}/{termsId}/details.html";
        } catch (Exception ex) {
            model.put("showDialog", Boolean.TRUE);
            model.put("itemException", ex);
            model.put("page", page);
            model.put("itemsPage", itemsPage);
            model.put("terms", termsService.read(termsId));
            PageRequestDTO requestItemPage = new PageRequestDTO();
            requestItemPage.setPage(itemsPage);
            requestItemPage.addSearchCriterion(new PageRequestDTO.SearchCriterion("id", termsId));
            ReadRangeDTO<BusinessPartnerTermsItem> i = termsService.readItemsPage(requestItemPage);
            model.put("items", i.getData());
            model.put("numberOfPages", i.getNumberOfPages());
            return "terms-details";
        }
    }
}
