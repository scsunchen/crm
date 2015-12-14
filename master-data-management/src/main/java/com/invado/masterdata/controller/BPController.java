package com.invado.masterdata.controller;

import bg_bu_gos.webservicelokator.service1.SelectMesto;
import com.invado.core.domain.*;
import com.invado.core.dto.*;
import com.invado.finance.service.PartnerSpecificationByDate;
import com.invado.masterdata.Utils;
import com.invado.masterdata.service.*;
import com.invado.masterdata.service.dto.MasterPartnerDTO;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import com.invado.masterdata.service.dto.RequestPartnerDTO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import telekomWS.client.AddressServiceClient;
import telekomWS.client.dto.SelectHouseNumber;
import telekomWS.client.dto.SelectPlace;
import telekomWS.client.dto.SelectStreet;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 26/11/2015.
 */
@Controller
public class BPController {


    @Inject
    private BPService service;
    @Inject
    private POSTypeService posTypeService;
    @Inject
    private CurrencyService currencyService;
    @Inject
    private BusinessPartnerContactDetailsService contactDetailsService;
    @Inject
    private DeviceService deviceService;
    @Inject
    private DeviceHolderPartnerService deviceHolderPartnerService;
    @Inject
    private AddressServiceClient addressServiceClient;


    @RequestMapping(value = "/partner/read-page.html", method = RequestMethod.GET)
    public String showItems(@ModelAttribute("businessPartnerDTO") RequestPartnerDTO requestPartner,
                            BindingResult partnerResult,
                            SessionStatus status,
                            Map<String, Object> model)
            throws Exception {


        List<BusinessPartner.Type> types = service.getTypes();

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(requestPartner.getPage());

        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("id", requestPartner.getId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("name", requestPartner.getName()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type", requestPartner.getType()));
        ReadRangeDTO<BusinessPartnerDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("types", types);
        model.put("requestPartner", requestPartner);
        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "partner-view";
    }


    /* PODREDJENI POSLOVNI PARTNERI */
    @RequestMapping(value = "/partner/read-subpartners.html", method = RequestMethod.GET)
    public String showDetailItems(@RequestParam Integer page,
                                  @RequestParam(value = "masterPartnerId", required = false) Integer id,
                                  @RequestParam(value = "masterPartnerName", required = false) String name,
                                  @RequestParam(value = "type", required = false) String type,
                                  @ModelAttribute("businessPartnerDTO") BusinessPartnerDTO businessPartnerDTO, BindingResult partnerResult,
                                  Map<String, Object> model)
            throws Exception {


        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("parentBusinessPartner", id));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("name", businessPartnerDTO.getName()));
        //request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type", type));
        ReadRangeDTO<BusinessPartnerDTO> items = service.readPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("businessPartnerDTO", businessPartnerDTO);

        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "partner-details-view";
    }

    @RequestMapping(value = "/partner/read-subpartners.html", method = RequestMethod.POST)
    public String showDetailItemsByPost(@RequestParam Integer page,
                                        @RequestParam(value = "masterPartnerId", required = false) Integer id,
                                        @RequestParam(value = "masterPartnerName", required = false) String name,
                                        @RequestParam(value = "type", required = false) String type,
                                        @ModelAttribute("businessPartnerDTO") BusinessPartnerDTO businessPartnerDTO, BindingResult partnerResult,
                                        Map<String, Object> model)
            throws Exception {


        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("parentBusinessPartner", id));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("name", businessPartnerDTO.getName()));
        //request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type", type));
        ReadRangeDTO<BusinessPartnerDTO> items = service.readPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("businessPartnerDTO", businessPartnerDTO);

        model.put("numberOfPages", items.getNumberOfPages());

        //return "item-table";
        return "partner-details-view";
    }

    /*KONTAKTI*/
    @RequestMapping(value = "/partner/read-contactsdetals-page.html", method = RequestMethod.GET)
    public String showDetailContacts(@RequestParam Integer page,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                     @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                     @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                     @RequestParam(value = "contactName", required = false) String contactName,
                                     @ModelAttribute("businessPartnerDTO") BusinessPartnerDTO businessPartnerDTO, BindingResult partnerResult,
                                     Map<String, Object> model) throws Exception {

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        if (pointOfSaleId == null) {
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("partnerId", masterPartnerId));
        } else {
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("partnerId", pointOfSaleId));
        }
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("contactName", contactName));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type", type));
        businessPartnerDTO.setTypeValue(type);
        ReadRangeDTO<BusinessPartnerContactDetailsDTO> items = contactDetailsService.readPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("businessPartnerDTO", businessPartnerDTO);
        model.put("masterPartnerDTO", new MasterPartnerDTO());
        model.put("selectedName", new String());
        model.put("numberOfPages", items.getNumberOfPages());

        return "business-partner-contact-view";
    }

    /* TERMINALI */
    @RequestMapping(value = "/partner/read-deviceholderdetails-page.html", method = RequestMethod.GET)
    public String showDetailDeviceHolder(@RequestParam Integer page,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                     @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                     @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                     Map<String, Object> model) throws Exception {

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("partnerId", pointOfSaleId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("type", type));
        ReadRangeDTO<DeviceHolderPartnerDTO> items = deviceHolderPartnerService.readPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());

        return "business-partner-device-view";
    }


    @RequestMapping(value = "/partner/create.html", method = RequestMethod.GET)
    public String initCreateForm(@RequestParam Integer page,
                                 @RequestParam(value = "type", required = false) String type,
                                 Map<String, Object> model) {

        List<BusinessPartner> parents = service.readParentPartners();
        List<BusinessPartner.Type> types = service.getTypes();
        List<POSTypeDTO> POSTypes = posTypeService.readAll(null, null);
        BusinessPartnerDTO item = new BusinessPartnerDTO();
        model.put("item", item);
        model.put("types", types);
        model.put("action", "create");

        return "partner-grid";
    }

    @RequestMapping(value = "/partner/create.html", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") BusinessPartnerDTO item, BindingResult result,
                                      @RequestParam Integer page,
                                      @RequestParam(value = "masterPartnerId", required = false) Integer id,
                                      @RequestParam(value = "masterPartnerName", required = false) String name,
                                      @RequestParam(value = "type", required = false) String type,
                                      SessionStatus status,
                                      Map<String, Object> model,
                                      final RedirectAttributes redirectAttributes)
            throws Exception {

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
        return "redirect:/partner/create.html?type=" + type + "&masterPartnerId=&masterPartnerName=&page=" + page;
    }

    @RequestMapping(value = "/partner/create-detail.html", method = RequestMethod.GET)
    public String initCreateDetailForm(@RequestParam Integer page,
                                       @RequestParam(value = "type", required = false) String type,
                                       @RequestParam(value = "masterPartnerId", required = false) Integer id,
                                       @RequestParam(value = "masterPartnerName", required = false) String name,
                                       Map<String, Object> model) {

        List<BusinessPartner> parents = service.readParentPartners();
        List<BusinessPartner.Type> types = service.getTypes();
        List<POSTypeDTO> POSTypes = posTypeService.readAll(null, null);
        List<CurrencyDTO> currencyDesignation = currencyService.readAll(null, null, null);
        BusinessPartnerDTO item = new BusinessPartnerDTO();
        if (id != null)
            item.setParentBusinessPartnerId(id);
        if (name != null)
            item.setParentBusinesspartnerName(name);
        model.put("item", item);
        model.put("types", types);
        model.put("currencyDesignation", currencyDesignation);
        model.put("POSTypes", POSTypes);
        model.put("action", "create");

        return "partner-details-grid";
    }

    @RequestMapping(value = "/partner/create-detail.html", method = RequestMethod.POST)
    public String processCreationDetailForm(@ModelAttribute("item") BusinessPartnerDTO item, BindingResult result,
                                            @RequestParam Integer page,
                                            @RequestParam(value = "masterPartnerId", required = false) Integer id,
                                            @RequestParam(value = "masterPartnerName", required = false) String name,
                                            /*@RequestParam(value = "type", required = false) String type,*/
                                            SessionStatus status,
                                            Map<String, Object> model,
                                            final RedirectAttributes redirectAttributes)
            throws Exception {

        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("message", Utils.getMessage("Processing.Save.Failure"));
            redirectAttributes.addFlashAttribute("alertType", "error");
            redirectAttributes.addFlashAttribute("message", Utils.getMessage("Processing.Save.Failure"));
            redirectAttributes.addFlashAttribute("partner", item);
        } else {
            BusinessPartnerDTO masterPartner = service.read(id);
            item.setTIN(masterPartner.getTIN());
            item.setVAT(masterPartner.getVAT());
            this.service.createDetail(item);
            status.setComplete();
            redirectAttributes.addFlashAttribute("alertType", "success");
            redirectAttributes.addFlashAttribute("message", Utils.getMessage("Processing.Save.Succes"));
            redirectAttributes.addFlashAttribute("partner", item);
        }
        return "redirect:/partner/create-detail.html?type=&masterPartnerId=" + id + "&masterPartnerName=" + name + "&page=" + page;
    }


    @RequestMapping(value = "/partner/details/{page}", params = "documents")
    public String showDetailDocuments() {
        System.out.println("Izvrsio se document");
        return "Izvrsio se dokuemnta";
    }


    @RequestMapping("/partner/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/partner/{page}";
    }

    @RequestMapping(value = "/partner/update.html", method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam Integer id, @RequestParam Integer page,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerDTO item = service.read(id);
        model.put("item", item);
        return "partner-grid";
    }

    @RequestMapping(value = "/partner/update.html",
            method = RequestMethod.POST)
    public String processUpdateForm(@ModelAttribute("item") BusinessPartnerDTO item,
                                    @RequestParam Integer id,
                                    @RequestParam Integer page,
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

        return "redirect:/partner/read-page.html?id=&name=" + "&page=0";
    }


    @RequestMapping(value = "/partner/update-subpartner.html", method = RequestMethod.GET)
    public String initUpdateSubpartnerForm(@RequestParam Integer id, @RequestParam Integer page,
                                           @RequestParam(value = "masterPartnerId", required = false) Integer masterpartnerId,
                                           @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                           Map<String, Object> model)
            throws Exception {
        BusinessPartnerDTO item = service.read(id);
        BusinessPartnerDTO masterPartner = service.read(masterpartnerId);
        item.setTIN(masterPartner.getTIN());
        item.setVAT(masterPartner.getVAT());
        model.put("item", item);
        return "partner-details-grid";
    }

    @RequestMapping(value = "/partner/update-subpartner.html",
            method = RequestMethod.POST)
    public String processUpdateSubPartnerForm(@ModelAttribute("item") BusinessPartnerDTO item,
                                              @RequestParam Integer id,
                                              @RequestParam Integer page,
                                              BindingResult result,
                                              SessionStatus status,
                                              Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "partner-grid";
        } else {
            BusinessPartnerDTO masterPartner = service.read(item.getParentBusinessPartnerId());
            item.setTIN(masterPartner.getTIN());
            item.setVAT(masterPartner.getVAT());
            this.service.update(item);
            status.setComplete();
        }

        return "redirect:/partner/read-subpartners.html?posId=" + item.getId() + "&masterPartnerId=" + item.getParentBusinessPartnerId() + "&masterPartnerName=" + item.getParentBusinesspartnerName() + "&page=0";
    }

    @RequestMapping(value = "partner/register/input/MERCHANT")
    public String processTelekomMerchantRegistration(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(service.merchatnRegistration(item));
        service.update(item);

        return "partner-grid";
    }

    @RequestMapping(value = "partner/register/update/MERCHANT")
    public String processTelekomMerchantUpdate(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(service.merchantUpdate(item));
        service.update(item);

        return "partner-grid";
    }

    @RequestMapping(value = "partner/register/deactivate/MERCHANT")
    public String processTelekomMerchantDeactivation(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(service.merchantDeactivation(item.getTelekomId()));
        this.service.update(item);

        return "partner-grid";
    }



    @RequestMapping(value = "partner/register/input/POINT_OF_SALE")
    public String processTelekomPOSRegistration(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(service.pointOfSaleRegistration(item));
        service.update(item);

        return "partner-grid";
    }

    @RequestMapping(value = "partner/register/update/POINT_OF_SALE")
    public String processTelekomPOSUpdate(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(service.pointOfSaleUpdate(item));
        service.update(item);

        return "partner-grid";
    }

    @RequestMapping(value = "partner/register/deactivation/POINT_OF_SALE")
    public String processTelekomPOSDeactivation(@ModelAttribute("item") BusinessPartnerDTO item) throws Exception {

        item.setTelekomId(service.pointOfSaleDeactivation(item));
        this.service.update(item);

        return "partner-grid";
    }



    @RequestMapping(value = "/partner/read-partner/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findPartnerByName(@PathVariable String name) {
        return service.readPartnerByName(name);
    }

    @RequestMapping(value = "/partner/read-merchant/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findMerchantByName(@PathVariable String name) {
        return service.readMerchantByName(name);
    }


    @RequestMapping(value = "/partner/read-pos/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findPointOfSaleByName(@PathVariable String name, @RequestParam("merchantId") Integer merchantId) {
        System.out.println(merchantId);
        return service.readPointOfSaleByName(name);
    }

    @RequestMapping(value = "/partner/read-all-pos/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findPointOfSaleByName(@PathVariable String name) {
        return service.readPointOfSaleByName(name);
    }

    @RequestMapping(value = "/partner/read-device/{name}")
    public
    @ResponseBody
    List<Device> findDeviceByCusotmCode(@PathVariable String name) {
        System.out.println("izvrsava se kontroler");
        return deviceService.readDeviceByCustomCodeAnassigned(name);
    }

    @RequestMapping(value = "partner/address/read-mesto/{name}")
    public @ResponseBody List<SelectPlace> selectPlace(String pattern) throws Exception{
        return addressServiceClient.listOfPlaces(pattern);
    }

    @RequestMapping(value = "partner/address/read-streets/{name}")
    public @ResponseBody List<SelectStreet> selectStreets(@RequestParam("place") String place, String pattern) throws Exception{
        return addressServiceClient.listOfStreetsPerPlace(place, pattern);
    }

    @RequestMapping(value = "partner/address/read-housenumber/{name}")
    public @ResponseBody List<SelectHouseNumber> selectHouseNumber(@RequestParam("place") String place, @RequestParam("street") String street, String pattern) throws Exception{
        return addressServiceClient.listOfHouseNumbersPerStreet(place, street, pattern);
    }
}


