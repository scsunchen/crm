package com.invado.customer.relationship.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.customer.relationship.domain.BusinessPartnerRelationshipTerms;
import com.invado.customer.relationship.service.BusinessPartnerRelationshipTermsService;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.BusinessPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by NikolaB on 6/13/2015.
 */
@Controller
public class BusinessPartnerRelationshipTermsController {

    @Autowired
    private BusinessPartnerService partnerService;
    @Autowired
    private BusinessPartnerRelationshipTermsService service;


    @RequestMapping("home")
    public String showHomePage() {
        return "home";
    }

    @RequestMapping("/terms/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<BusinessPartnerRelationshipTerms> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "terms-view";
    }

    @RequestMapping(value = "/terms/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {

        System.out.println("to je to ");
        model.put("item", new BusinessPartnerRelationshipTerms());
        List<BusinessPartner> partners = partnerService.readAll(null, null, null);
        model.put("partners", partners);
        System.out.println("to je to "+partners.size());

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
            this.service.create(item);
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
        service.delete(id);
        return "redirect:/terms/{page}";
    }

    @RequestMapping(value = "/terms/{page}/update/{code}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String code,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerRelationshipTerms item = service.read(code);
        model.put("item", item);
        return "terms-grid";
    }

    @RequestMapping(value = "/terms/{page}/update/{code}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerRelationshipTerms item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "terms-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/terms/{page}";
    }
}


