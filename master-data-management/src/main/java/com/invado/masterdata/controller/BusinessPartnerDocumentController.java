package com.invado.masterdata.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.BusinessPartnerDocument;
import com.invado.core.domain.DocumentType;
import com.invado.core.dto.BusinessPartnerDTO;
import com.invado.core.dto.BusinessPartnerDocumentDTO;
import com.invado.core.dto.DocumentTypeDTO;
import com.invado.masterdata.service.BPService;
import com.invado.masterdata.service.BusinessPartnerDocumentService;
import com.invado.masterdata.service.DocumentTypeService;
import com.invado.masterdata.service.dto.FilterObjectsList;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 24/12/2015.
 */
@Controller
public class BusinessPartnerDocumentController {

    @Inject
    private BusinessPartnerDocumentService service;
    @Inject
    private BPService bpService;
    @Inject
    private DocumentTypeService documentTypeService;



    @RequestMapping("/document/{page}")
    public String showItems(@PathVariable Integer page,
                            @ModelAttribute("filterObjectsList") FilterObjectsList filterList,
                            ModelMap modelMap, Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        if (filterList.getBusinessPartnerDTOs().size() > 0) {
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("merchantId", filterList.getBusinessPartnerDTOs().get(0).getId()));
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("pointOfSaleId", filterList.getBusinessPartnerDTOs().get(1).getId()));
        } else {
            filterList.getBusinessPartnerDTOs().add(new BusinessPartnerDTO());
            filterList.getBusinessPartnerDTOs().add(new BusinessPartnerDTO());
        }

        ReadRangeDTO<BusinessPartnerDocumentDTO> items = service.readPage(request);

        modelMap.addAttribute("filterObjects", filterList);
        modelMap.addAttribute("data", items.getData());
        modelMap.addAttribute("page", items.getPage());
        modelMap.addAttribute("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "business-partner-document-view";
    }

    @RequestMapping(value = "/document/create.html", method = RequestMethod.GET)
    public String initCreateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 Map<String, Object> model) {
        BusinessPartnerDocumentDTO businessPartnerDocumentDTO = new BusinessPartnerDocumentDTO();
        BusinessPartnerDTO businessPartner = null;
        if (pointOfSaleId != null) {
            businessPartner = bpService.read(pointOfSaleId);
        } else {
            businessPartner = bpService.read(masterPartnerId);
        }

        businessPartnerDocumentDTO.setBusinessPartnerOwnerId(businessPartner.getParentBusinessPartnerId());
        businessPartnerDocumentDTO.setBusinessPartnerOwnerName(businessPartner.getParentBusinesspartnerName());

        List<BusinessPartnerDocument.DocumentStatus> statuses = service.getDocumentStatuses();
        List<DocumentTypeDTO> types = documentTypeService.readAll(null, null);
        model.put("statuses", statuses);
        model.put("types", types);
        model.put("item", businessPartnerDocumentDTO);
        model.put("action", "create");
        return "business-partner-document-grid";
    }

    @RequestMapping(value = "/document/create.html", method = RequestMethod.POST)
    public String processCreationForm(@RequestParam String page,
                                      @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                      @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                      @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                      @RequestParam (required = false) CommonsMultipartFile[] fileUpload,
                                      @ModelAttribute("item") BusinessPartnerDocumentDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "business-partner-document-grid";
        } else {
            if (fileUpload != null && fileUpload.length > 0) {
                for (CommonsMultipartFile aFile : fileUpload){
                    System.out.println("Saving file: " + aFile.getOriginalFilename());

                    item.setFileName(aFile.getOriginalFilename());
                    item.setFile(aFile.getBytes());
                    item.setFileContentType(aFile.getContentType());
                }
            }
            service.create(item);
            model.put("message", item.getId() + " " + item.getDescription());
            status.setComplete();
        }
        //return "redirect:/township/{page}";
        return "redirect:/document/create.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }

    @RequestMapping("/document/delete.html")
    public String delete(@RequestParam Integer id, @RequestParam String page,
                         @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                         @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName) throws Exception {
        service.delete(id);
        //return "redirect:/contact/{page}";
        return "redirect:/partner/read-documents-page.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }

    @RequestMapping(value = "/document/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam String page,
                                 @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                 @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                 @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                 @RequestParam(value = "id") Integer id,
                                 Map<String, Object> model)
            throws Exception {
        BusinessPartnerDocumentDTO item = service.read(id).getDTO();
        model.put("item", item);
        return "business-partner-document-grid";
    }

    @RequestMapping(value = "/document/update.html",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") BusinessPartnerDocumentDTO item,
                                      BindingResult result,
                                      @RequestParam String page,
                                      @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                      @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                      @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                      @RequestParam(value = "id") Integer id,
                                      @RequestParam (required = false) CommonsMultipartFile[] fileUpload,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "business-partner-document-grid";
        } else {
            if (fileUpload != null && fileUpload.length > 0) {
                for (CommonsMultipartFile aFile : fileUpload){
                    System.out.println("Saving file: " + aFile.getOriginalFilename());

                    item.setFileName(aFile.getOriginalFilename());
                    item.setFile(aFile.getBytes());
                    item.setFileContentType(aFile.getContentType());
                }
            }
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/partner/read-documents-page.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }

    @RequestMapping(value = { "/document/download-document.html" }, method = RequestMethod.GET)
    public String downloadDocument(@RequestParam int id,
                                   @RequestParam(value = "masterPartnerId", required = false) Integer masterPartnerId,
                                   @RequestParam(value = "masterPartnerName", required = false) String masterPartnerName,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   HttpServletResponse response) throws Exception {
        BusinessPartnerDocument document = service.read(id);
        response.setContentType(document.getFileContentType());
        response.setContentLength(document.getFile().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + document.getFileName() +"\"");

        FileCopyUtils.copy(document.getFile(), response.getOutputStream());

        return "redirect:/partner/read-documents-page.html?masterPartnerId=" + masterPartnerId + "&masterPartnerName=" + masterPartnerName + "&page=" + 0;
    }

}
