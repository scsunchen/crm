package com.invado.masterdata.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.core.dto.POSTypeDTO;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.BPService;
import com.invado.masterdata.service.POSTypeService;
import com.invado.masterdata.service.TelekomWSClient;
import com.invado.masterdata.service.dto.MasterPartnerDTO;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 26/08/2015.
 */
@Controller
public class BusinessPartnerController {

    @Inject
    private BPService service;
    @Inject
    private POSTypeService posTypeService;
    @Inject
    private TelekomWSClient telekomWSClient;


    @RequestMapping(value = "/partner/{page}")
    public String showItems(@PathVariable Integer page,
                            @RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "type", required = true) String type, @RequestParam(value = "masterPartnerId", required = false) Integer selectedId,
                            @RequestParam(value = "masterPartnerName", required = false) String selectedName,
                            @ModelAttribute("businessPartnerDTO") BusinessPartnerDTO businessPartnerDTO, BindingResult partnerResult,
                            Map<String, Object> model)
            throws Exception {


        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);

        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("id", id));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("name", name));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type", type));
        businessPartnerDTO.setTypeValue(type);
        ReadRangeDTO<BusinessPartnerDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("businessPartnerDTO", businessPartnerDTO);
        model.put("masterPartnerDTO", new MasterPartnerDTO());
        model.put("selectedName", new String());
        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "partner-view";
    }

    @RequestMapping(value = "/partner/details/{page}", params = "contacts")
    public String showDetailContacts(@PathVariable Integer page,
                                     @RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "type", required = true) String type, @RequestParam(value = "masterPartnerId", required = false) Integer selectedId,
                                     @RequestParam(value = "masterPartnerName", required = false) String selectedName,
                                     @ModelAttribute("businessPartnerDTO") BusinessPartnerDTO businessPartnerDTO, BindingResult partnerResult,
                                     Map<String, Object> model) throws Exception{
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);

        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("id", id));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("name", name));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type", type));
        businessPartnerDTO.setTypeValue(type);
        ReadRangeDTO<BusinessPartnerDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("businessPartnerDTO", businessPartnerDTO);
        model.put("masterPartnerDTO", new MasterPartnerDTO());
        model.put("selectedName", new String());
        model.put("numberOfPages", items.getNumberOfPages());

        return "Izvrsio se kontakti";
    }

    @RequestMapping(value = "/partner/details/{page}", params = "documents")
    public String showDetailDocuments() {
        System.out.println("Izvrsio se document");
        return "Izvrsio se dokuemnta";
    }

    @RequestMapping(value = "partner/details/{page}", params = "pointOfSale")
    public String showDetailItems(HttpServletRequest httpServletRequest, @PathVariable Integer page,
                                  @RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "type", required = false) String type, @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerID,
                                  @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                  @ModelAttribute("businessPartnerDTO") BusinessPartnerDTO businessPartnerDTO, BindingResult partnerResult,
                                  Map<String, Object> model)
            throws Exception {


        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("id", id));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("name", name));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type", type));
        if (masterPartnerID != null)
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("parentBusinessPartner", service.readById(masterPartnerID)));
        businessPartnerDTO.setTypeValue(type);
        ReadRangeDTO<BusinessPartnerDTO> items = service.readPage(request);
        model.put("masterPartnerDTO", new MasterPartnerDTO(id, name, BusinessPartner.Type.getEnum(type)));
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("businessPartnerDTO", businessPartnerDTO);

        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "partner-details-view";
    }

    @RequestMapping(value = "/partner/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, @RequestParam(value = "type", required = true) String type, @RequestParam(value = "masterPartnerId", required = false) Integer selectedId,
                                 @RequestParam(value = "masterPartnerName", required = false) String selectedName,
                                 Map<String, Object> model) {

        List<BusinessPartner> parents = service.readParentPartners();
        List<BusinessPartner.Type> types = service.getTypes();
        List<POSTypeDTO> POStypes = posTypeService.readAll(null, null);
        BusinessPartnerDTO item = new BusinessPartnerDTO();
        item.setType(BusinessPartner.Type.getEnum(type));
        item.setTypeDescription(item.getType().getDescription());
        item.setParentBusinessPartnerId(selectedId);
        item.setParentBusinesspartnerName(selectedName);
        model.put("item", item);
        model.put("types", types);
        model.put("parents", parents);
        model.put("POStypes", POStypes);
        model.put("action", "create");

        return "partner-grid";
    }

    @RequestMapping(value = "/partner/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") BusinessPartnerDTO item,
                                      HttpServletRequest httpServletRequest,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model,
                                      final RedirectAttributes redirectAttributes)
            throws Exception {
        String partnerType = httpServletRequest.getParameter("type");
        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("message", Utils.getMessage("Processing.Save.Failure"));
            return "partner-grid";
        } else {
            this.service.create(item);
            status.setComplete();
            redirectAttributes.addFlashAttribute("alertType", "success");
            redirectAttributes.addFlashAttribute("message", Utils.getMessage("Processing.Save.Succes"));
            redirectAttributes.addFlashAttribute("partner", item);

        }
        return "redirect:/partner/{page}/create?type=" + partnerType;
    }

    @RequestMapping("/partner/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/partner/{page}";
    }

    @RequestMapping(value = "/partner/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerDTO item = service.read(id);
        model.put("item", item);
        return "partner-grid";
    }

    @RequestMapping(value = "/partner/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "partner-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }

        return "redirect:/partner/{page}?type=" + item.getType();
    }

    @RequestMapping(value = "partner/registerMerchant")
    public String processTelekomMerchantRegistration(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(telekomWSClient.merchatnRegistration(item));
        this.service.update(item);

        return "partner-grid";
    }

    @RequestMapping(value = "partner/updateMerchant")
    public String processTelekomMerchantUpdate(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(telekomWSClient.merchantUpdate(item));
        this.service.update(item);

        return "partner-grid";
    }

    @RequestMapping(value = "partner/deactivateMerchant")
    public String processTelekomMerchantDeactivation(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(telekomWSClient.merchantDeactivation(item.getTelekomId()));
        this.service.update(item);

        return "partner-grid";
    }

    @RequestMapping(value = "/partner/read-partner/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findItemByDescription(@PathVariable String name) {
        return service.readPartnerByName(name);
    }

}
