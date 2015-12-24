package com.invado.masterdata.controller;

import com.invado.core.dto.DocumentTypeDTO;
import com.invado.masterdata.service.DocumentTypeService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by Nikola on 24/12/2015.
 */
@Controller
public class DocumentTypeController {
    @Inject
    private DocumentTypeService service;


    @RequestMapping("/documenttype/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<DocumentTypeDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "documenttype-view";
    }

    @RequestMapping(value = "/documenttype/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new DocumentTypeDTO());
        model.put("action", "create");
        return "documenttype-grid";
    }

    @RequestMapping(value = "/documenttype/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") DocumentTypeDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            model.put("action", "create");
            return "DocumentType-grid";
        } else {
            this.service.create(item);
            model.put("message", item.getId()+" "+item.getDescription());
            status.setComplete();
        }
        //return "redirect:/DocumentType/{page}";
        return "redirect:/documenttype/{page}/create";
    }

    @RequestMapping("/documenttype/{page}/{id}/delete.html")
    public String delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return "redirect:/documenttype/{page}";
    }

    @RequestMapping(value = "/documenttype/{page}/update/{id}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable Integer id,
                                 Map<String, Object> model)
            throws Exception {
        DocumentTypeDTO item = service.read(id).getDTO();
        model.put("item", item);
        return "documenttype-grid";
    }

    @RequestMapping(value = "/documenttype/{page}/update/{id}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") DocumentTypeDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "documenttype-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/documenttype/{page}";
    }
}
