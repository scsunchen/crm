/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.OrgUnit;
import com.invado.core.exception.PageNotExistsException;
import com.invado.core.exception.ReferentialIntegrityException;
import com.invado.core.exception.SystemException;
import com.invado.finance.controller.report.JournalEntryReport;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.domain.journal_entry.Description;
import com.invado.finance.service.JournalEntryItemService;
import com.invado.finance.service.JournalEntryService;
import com.invado.finance.service.MasterDataService;
import com.invado.finance.service.RecordJournalEntryService;
import com.invado.finance.service.dto.JournalEntryDTO;
import com.invado.finance.service.dto.JournalEntryItemDTO;
import com.invado.finance.service.dto.JournalEntryReportDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.exception.JournalEntryConstraintViolationException;
import com.invado.finance.service.exception.JournalEntryExistsException;
import com.invado.finance.service.exception.JournalEntryNotFoundException;
import com.invado.finance.service.exception.PostedJournalEntryUpdateException;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author bdragan
 */
@Controller
public class JournalEntryController {

    @Inject
    private JournalEntryService service;
    @Inject
    private JournalEntryItemService itemsService;
    @Inject
    private RecordJournalEntryService recordService;
    @Inject
    private MasterDataService masterDataservice;

    @RequestMapping("/journal-entry/{page}")
    public String showItems(
            @PathVariable Integer page,
            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<JournalEntryDTO> items = service.readJournalEntryPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "journal-entry-view";
    }

    @RequestMapping("/journal-entry/{page}/{clientId}/{typeId}/{number}/delete.html")
    public String delete(@PathVariable Integer clientId,
            @PathVariable Integer typeId,
            @PathVariable Integer number)
            throws Exception {
        service.deleteJournalEntry(clientId, typeId, number);
        return "redirect:/journal-entry/{page}";
    }
    
    @RequestMapping(value = "/journal-entry/{page}/create.html",
            method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page,
            Map<String, Object> model) {
        model.put("journalEntry", new JournalEntryDTO());
        model.put("action", "create");
        model.put("page", page);
        return "journal-entry-create-grid";
    }

    @RequestMapping(value = "/journal-entry/{page}/create.html",
            method = RequestMethod.POST)
    public String processCreateForm(
            @ModelAttribute("journalEntry") JournalEntryDTO item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("page", page);
            return "journal-entry-create-grid";
        }
        try {
            service.createJournalEntry(item);
            status.setComplete();
        } catch (JournalEntryConstraintViolationException 
                | JournalEntryExistsException 
                | ReferentialIntegrityException 
                | SystemException ex) {
            model.put("action", "create");
            model.put("exception", ex);
            model.put("page", page);
            return "journal-entry-create-grid";
        }
        return "redirect:/journal-entry/{page}";
    }
    
    @RequestMapping(value = "/journal-entry/read-orgunit/{name}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByName(@PathVariable String name) {
        return masterDataservice.readOrgUnitByName(name);
    }
    
    
    @RequestMapping(value = "/journal-entry/read-businesspartner/{name}")
    public @ResponseBody
    List<BusinessPartner> findBussinesPartnerByName(@PathVariable String name) {
        return masterDataservice.readBusinessPartnerByName(name);
    }
    
    @RequestMapping(value = "/journal-entry/read-businesspartner/{name}/{max}")
    public @ResponseBody
    List<BusinessPartner> findBussinesPartnerByName(@PathVariable String name, @PathVariable Integer max) {
        return masterDataservice.readBusinessPartnerByName(name, max);
    }

    @RequestMapping(value = "/journal-entry/read-description/{name}")
    public @ResponseBody
    List<Description> findDescriptionByName(
            @PathVariable String name) {
        return masterDataservice.readDescByName(name);
    }

    @RequestMapping(value = "/journal-entry/read-account/{number}")
    public @ResponseBody
    List<Account> findAccountByNumber(@PathVariable String number) {
        return masterDataservice.readAccountByCode(number);
    }
    
    @RequestMapping(value = "/journal-entry/{clientId}/{typeId}/{number}/print.html")
    public ResponseEntity<byte[]> showPDF(
            @PathVariable Integer clientId,
            @PathVariable Integer typeId,
            @PathVariable Integer number)
            throws Exception {
        try {
        JournalEntryReportDTO dto = service.printJournalEntry(clientId,
                typeId,
                number);
        String username = "";
        JournalEntryReport report = new JournalEntryReport(dto, username, LocaleContextHolder.getLocale());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = String.format("%snalog", dto.date.toString());
        headers.add("content-disposition", "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(
                getPDFFile(report), headers, HttpStatus.OK);
        return response;
        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @RequestMapping(value = "/journal-entry/{page}/{clientId}/{typeId}/{number}/{version}/record.html")
    public String record(
            @PathVariable Integer clientId,
            @PathVariable Integer typeId,
            @PathVariable Integer number,
            @PathVariable Long version)
            throws Exception {
        recordService.recordJournalEntry(clientId, typeId, number, version);
        return "redirect:/journal-entry/{page}";
    }

    private byte[] getPDFFile(Pageable pageable) throws Exception {
        //embed FreeSans(http://www.gnu.org/software/freefont/) 
        //font into PDF document
        DefaultFontMapper mapper = new DefaultFontMapper();
        DefaultFontMapper.BaseFontParameters PDFFontParameters
                = new DefaultFontMapper.BaseFontParameters(
                        "/com/invado/finance/font/FreeSans.otf");
        PDFFontParameters.encoding = BaseFont.CP1250;
        PDFFontParameters.embedded = Boolean.TRUE;
        //Map AWT font Lucida Sans Regular to FreeSans. Lucida Sans 
        //Regular font cannot be embedded due to license restrictions.
        mapper.putName("Lucida Sans Regular", PDFFontParameters);
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, byteStream);
            doc.open();
            if (pageable.getNumberOfPages() > 0) {
                float width = (float) pageable.getPageFormat(0).getWidth();
                float height = (float) pageable.getPageFormat(0).getHeight();
                doc.setPageSize(new Rectangle(0, 0, width, height));
            }
            for (int i = 0, n = pageable.getNumberOfPages(); i < n; i++) {
                doc.newPage();
                PageFormat format = pageable.getPageFormat(i);
                Graphics2D content = new PdfGraphics2D(
                        writer.getDirectContent(),
                        (float) format.getWidth(),
                        (float) format.getHeight(),
                        mapper);
                pageable.getPrintable(i).print(content, format, i);
                content.dispose();
            }
            doc.close();
            return byteStream.toByteArray();
        }
    }
    
    @RequestMapping(value = "/journal-entry/{clientId}/{typeId}/{number}/export.html")
    public ResponseEntity<byte[]> exportJournalEntry(
            @PathVariable Integer clientId,
            @PathVariable Integer typeId,
            @PathVariable Integer number)
            throws Exception {

        JournalEntryReportDTO dto = service.printJournalEntry(clientId,
                typeId,
                number);
        JAXBContext context = JAXBContext.newInstance(
                JournalEntryReportDTO.class
        );
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(
                Marshaller.JAXB_FORMATTED_OUTPUT,
                Boolean.TRUE
        );
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        marshaller.marshal(
                dto,
                byteStream
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/xml"));
        String filename = String.format("%snalog", dto.date.toString());
        headers.add("content-disposition", "attachment;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(
                byteStream.toByteArray(), headers, HttpStatus.OK);
        return response;

    }
    
    @RequestMapping(value = "/journal-entry/{page}/{clientId}/{typeId}/{number}/update.html",
                    method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String page,
            @PathVariable Integer clientId,
            @PathVariable Integer typeId,
            @PathVariable Integer number,
            Map<String, Object> model)
            throws JournalEntryNotFoundException,
            PageNotExistsException {
        JournalEntryDTO journalEntry = service.readJournalEntry(clientId,
                typeId,
                number);
        model.put("journalEntry", journalEntry);
        model.put("page", page);
        return "journal-entry-update-grid";
    }

    @RequestMapping(value = "/journal-entry/{page}/update.html",
            method = RequestMethod.POST)
    public String processUpdateForm(
            @ModelAttribute("journalEntry") JournalEntryDTO entry,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("page", page);
            return "journal-entry-update-grid";
        }
        try {
            service.updateJournalEntry(entry);
        } catch (JournalEntryNotFoundException 
                | JournalEntryConstraintViolationException 
                | PostedJournalEntryUpdateException 
                | ReferentialIntegrityException 
                | SystemException ex) {
            model.put("exception", ex);
            model.put("page", page);
            return "journal-entry-update-grid";
        }
        return String.format("redirect:/journal-entry/{page}/%1$s/%2$s/%3$s/update.html",
                             entry.getClientId(),
                             entry.getTypeId(),
                             entry.getJournalEntryNumber());
    }
    
    @RequestMapping("/journal-entry/{page}/{itemsPage}/{clientId}/{typeId}/{number}/{ordinal}/{version}/deleteItem.html")
    public String deleteItem(@PathVariable Integer clientId,
            @PathVariable Integer typeId,
            @PathVariable Integer number,
            @PathVariable Integer ordinal,
            @PathVariable Long version)
            throws Exception {
        itemsService.deleteJournalEntryItem(clientId, typeId, number, ordinal, version);
        return String.format(
                    "redirect:/journal-entry/{page}/{itemsPage}/%1$s/%2$s/%3$s/details.html",
                    clientId,
                    typeId,
                    number);
    }
    
    @RequestMapping(
            value = "/journal-entry/{page}/{itemsPage}/{clientId}/{typeId}/{number}/details.html", 
            method = RequestMethod.GET
    ) 
    public String initDetails(@PathVariable Integer clientId,
                              @PathVariable Integer typeId,
                              @PathVariable Integer number,
                              @PathVariable Integer page,
                              @PathVariable Integer itemsPage,
                              Map<String, Object> model) 
                              throws Exception {
        JournalEntryDTO journalEntry = service.readJournalEntry(clientId,
                typeId,
                number);
        model.put("journalEntry", journalEntry);
        model.put("page", page);
        PageRequestDTO itemsPageRequest = new PageRequestDTO(itemsPage);
        itemsPageRequest.addSearchCriterion("clientId", clientId);
        itemsPageRequest.addSearchCriterion("journalEntryType", typeId);
        itemsPageRequest.addSearchCriterion("journalEntryNumber", number);        
        ReadRangeDTO<JournalEntryItemDTO> items = itemsService
                .readJournalEntryItemPage(itemsPageRequest);
        model.put("items", items.getData());
        model.put("numberOfPages", items.getNumberOfPages());
        model.put("itemsPage", items.getPage());
        JournalEntryItemDTO dto = new JournalEntryItemDTO();
        dto.setClientId(clientId);
        dto.setTypeId(typeId);
        dto.setJournalEntryNumber(number);
        model.put("journalEntryItem", dto); 
        return "journal-entry-details";
    }
    
    @RequestMapping(value = "/journal-entry/{page}/{itemsPage}/{version}/addItem.html",
            method = RequestMethod.POST)
    public String addItem(
            //nemam pojma zasto invoice parametar moram da stavim
            @ModelAttribute("journalEntryItem") JournalEntryItemDTO item,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            @PathVariable String itemsPage,
            @PathVariable Long version,
            Map<String, Object> model) throws Exception {
        
        if (result.hasErrors()) {
            model.put("showDialog", Boolean.TRUE);
            model.put("itemsPage", itemsPage);
            model.put("page", page);
            model.put("journalEntry", service.readJournalEntry(
                    item.getClientId(),
                    item.getTypeId(),
                    item.getJournalEntryNumber())
            );
            model.put("items", itemsService.readAllJournalItems(
                    item.getClientId(),
                    item.getTypeId(),
                    item.getJournalEntryNumber()));
            return "journal-entry-details";
        }
        try {
            String username = ((User) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUsername();
            item.setUsername(username);
            itemsService.createJournalEntryItem(item, version);
            return String.format(
                    "redirect:/journal-entry/{page}/{itemsPage}/%1$s/%2$s/%3$s/details.html",
                    item.getClientId(),
                    item.getTypeId(),
                    item.getJournalEntryNumber());
        } catch (Exception ex) {
            model.put("showDialog", Boolean.TRUE);
            model.put("dialogException", ex);
            model.put("page", page);
            model.put("itemsPage", itemsPage);
            model.put("journalEntry", service.readJournalEntry(
                    item.getClientId(),
                    item.getTypeId(),
                    item.getJournalEntryNumber())
            );
            model.put("items", itemsService.readAllJournalItems(
                    item.getClientId(),
                    item.getTypeId(),
                    item.getJournalEntryNumber()));
            return "journal-entry-details";
        }
    }
    
}
