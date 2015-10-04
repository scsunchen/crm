package com.invado.customer.relationship.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.customer.relationship.domain.BusinessPartnerRelationshipTerms;
import com.invado.customer.relationship.service.BusinessPartnerRelationshipTermsService;
import com.invado.customer.relationship.service.dto.*;
import com.invado.masterdata.service.BPService;
import com.invado.masterdata.service.BPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by NikolaB on 6/13/2015.
 */
@Controller
public class BusinessPartnerRelationshipTermsController {

    @Autowired
    private BPService partnerService;
    @Autowired
    private BusinessPartnerRelationshipTermsService businessPartnerRelationshipTermsService;


    @RequestMapping(value = "/terms/read-terms.html")
    public String prepareShowItems(Map<String, Object> model)
            throws Exception {

        System.out.println("izvrsava se kontroler ");
        //BusinessTermsRequestDTO businessTermsRequestDTO = new BusinessTermsRequestDTO();
        model.put("partnerData", new BusinessPartnerDTO());
        model.put("termsData", new ArrayList<BusinessPartnerRelationshipTermsItemsDTO>());
        model.put("requestData", new BusinessTermsRequestDTO());
        return "terms-table";
    }

    @RequestMapping(value = "/terms/{page}", method = RequestMethod.POST)
    public String showItems(@ModelAttribute Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<BusinessPartnerRelationshipTermsDTO> items = businessPartnerRelationshipTermsService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "terms-table";
    }


    @RequestMapping(value = "/terms/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {

        model.put("item", new BusinessPartnerRelationshipTerms());
        List<BusinessPartner> partners = partnerService.readAll(null, null, null, null);
        model.put("partners", partners);

        model.put("action", "create");

        return "terms-grid";
    }

    @RequestMapping(value = "/terms/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") BusinessPartnerRelationshipTerms item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            String resultErrorMessages = null;
            model.put("action", "create");
            /*
            System.out.println(model.get("bank").toString());
            */
            for (ObjectError e : result.getAllErrors()) {
                resultErrorMessages += e.getDefaultMessage();
            }
            model.put("resulterror", resultErrorMessages);
            return "resulterror";
        } else {
            this.businessPartnerRelationshipTermsService.create(item);
            status.setComplete();
        }
        return "redirect:/terms/{page}";
    }

    /*
  @InitBinder
  protected void initBinder(ServletRequestDataBinder binder) throws Exception {
      binder.registerCustomEditor(BankCreditor.class,"bank",new PropertyEditorSupport(){
                  @Override public void setAsText(    String text){
                      BankCreditor stem=new BankCreditor(text,"","");
                      setValue(stem);
                  }
              }
      );
  }
  */
    @RequestMapping("/terms/{page}/{code}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        businessPartnerRelationshipTermsService.delete(id);
        return "redirect:/terms/{page}";
    }

    @RequestMapping(value = "/terms/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerRelationshipTerms item = businessPartnerRelationshipTermsService.read(id);
        model.put("item", item);
        return "terms-grid";
    }

    @RequestMapping(value = "/terms/{page}/{id}/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String page,
                                 @PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerRelationshipTermsDTO tmp = businessPartnerRelationshipTermsService.read(id).getDTO();
        return initUpdateForm(page, tmp, null, model);
    }

    private String initUpdateForm(String page,
                                  BusinessPartnerRelationshipTermsDTO dto,
                                  Exception ex,
                                  Map<String, Object> model)
            throws Exception {
        BusinessPartnerRelationshipTermsItemsDTO itemDTO = new BusinessPartnerRelationshipTermsItemsDTO();
        itemDTO.setBusinessPartnerRelationshipTermsId(dto.getBusinessPartnerId());
        itemDTO.setBusinessPartnerRelationshipTermsVersion(dto.getVersion());
        model.put("page", page);
        model.put("invoiceItem", itemDTO);
        model.put("invoice", dto);
        model.put("items", businessPartnerRelationshipTermsService.readTermsItems(dto.getId()));
        model.put("exception", ex);
        return "terms-update-master-detail";
    }


    @RequestMapping(value = "/terms/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerRelationshipTerms item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "terms-grid";
        } else {
            this.businessPartnerRelationshipTermsService.update(item);
            status.setComplete();
        }
        return "redirect:/terms/{page}";
    }
}


