package com.invado.customer.relationship.controller;

import com.invado.core.domain.Article;
import com.invado.core.domain.BusinessPartner;
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
            model.put("action", "create");
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
        BusinessPartnerTermsItem itemToAdd = new BusinessPartnerTermsItem(terms, null);
        itemToAdd.setArticle(new Article());
        model.put("termsItemToAdd", itemToAdd);
        return "terms-update-master-detail";
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
            BusinessPartnerTermsItem itemToAdd = new BusinessPartnerTermsItem(item, null);
            itemToAdd.setArticle(new Article());
            item.addAll(termsService.read(id).getItems());
            model.put("terms", item);
            model.put("termsItemToAdd", itemToAdd);
            return "terms-update-master-detail";
        } else {
            try {
                this.termsService.update(item);
                status.setComplete();
            } catch (Exception ex) {
                model.put("exception", ex);
                item.addAll(termsService.read(id).getItems());
                model.put("page", page);
                model.put("terms", item);
                BusinessPartnerTermsItem itemToAdd = new BusinessPartnerTermsItem(item, null);
                itemToAdd.setArticle(new Article());
                model.put("termsItemToAdd", itemToAdd);
                return "terms-update-master-detail";
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
    List<Article> readServiceByDesc(@PathVariable String query) {
        return masterDataService.readItemByDescription(query);
    }

    @RequestMapping(value = "/terms/{page}/{termsId}/{ordinalNumber}/{termsVersion}/remove-item.html",
            method = RequestMethod.POST)
    public String deleteItem(@PathVariable Integer termsId,
            @PathVariable Integer ordinalNumber,
            @PathVariable Long termsVersion) {
        termsService.removeItem(termsId, ordinalNumber, termsVersion);
        return "redirect:/terms/{page}/{termsId}/update.html";
    }

    @RequestMapping(value = "/terms/{page}/{termsId}/add-item.html",
            method = RequestMethod.POST)
    public String addItem(
            @ModelAttribute("termsItemToAdd") BusinessPartnerTermsItem item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("showDialog", Boolean.TRUE);
            model.put("page", page);
            try {
                BusinessPartnerTerms termsToDisplay=termsService.read(
                        item.getTerms().getId());
                model.put("terms", termsToDisplay);      
                item.setTerms(termsToDisplay);
                model.put("termsItemToAdd", item);
            }catch(Exception ex) {
                //DO NOTHING
            }
            return "terms-update-master-detail";
        }
        try {
            termsService.addItem(item);
            status.setComplete();
            return "redirect:/terms/{page}/{termsId}/update.html";
        } catch (Exception ex) {
            model.put("showDialog", Boolean.TRUE);
            model.put("itemException", ex);
            model.put("page", page);
            try {
                BusinessPartnerTerms termsToDisplay=termsService.read(
                        item.getTerms().getId());
                model.put("terms", termsToDisplay);                
                BusinessPartnerTermsItem itemToAdd = new BusinessPartnerTermsItem(termsToDisplay, null);
                itemToAdd.setArticle(new Article());
                model.put("termsItemToAdd", itemToAdd);
            }catch(Exception ex1) {
                //DO NOTHING
            }
            return "terms-update-master-detail";
        }
    }

}
