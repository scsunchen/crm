/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.domain.Article;
import com.invado.core.domain.BankCreditor;
import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.Currency;
import com.invado.core.domain.OrgUnit;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.core.exception.EntityNotFoundException;
import com.invado.core.exception.PageNotExistsException;
import com.invado.finance.controller.report.InvoiceReport;
import com.invado.core.domain.InvoiceBusinessPartner;
import com.invado.core.domain.InvoiceType;
import com.invado.finance.domain.journal_entry.Description;
import com.invado.finance.service.MasterDataService;
import com.invado.finance.service.InvoiceService;
import com.invado.finance.service.RecordInvoiceService;
import com.invado.core.dto.InvoiceDTO;
import com.invado.core.dto.InvoiceItemDTO;
import com.invado.finance.service.dto.InvoiceReportDTO;
import com.invado.finance.service.dto.JournalEntryTypeDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.dto.RequestInvoiceRecordingDTO;
import com.invado.finance.service.dto.RequestInvoicesDTO;
import com.invado.finance.service.exception.JournalEntryExistsException;
import com.invado.finance.service.exception.PostedInvoiceException;
import com.invado.finance.service.exception.ProformaInvoicePostingException;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author bdragan
 */
@Controller
public class InvoiceController {

    @Inject
    private InvoiceService invoiceService;
    @Inject
    private RecordInvoiceService recordService;
    @Inject
    private MasterDataService masterDataservice;

    @RequestMapping(value = "/invoice/read-page.html", method = RequestMethod.GET)
    public String search(Map<String, Object> model,
            @ModelAttribute("requestInvoices") RequestInvoicesDTO request,
            BindingResult result,
            SessionStatus status)
            throws PageNotExistsException {
        if (result.hasErrors()) {
            model.put("page", request.getPage());
            model.put("expand", true);
            return "invoice-table";
        }
        PageRequestDTO dto = new PageRequestDTO();
        dto.setPage(request.getPage());
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                "document",
                request.getDocument()));
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                "from",
                request.getDateFrom()));
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                "to",
                request.getDateTo()));
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                "partner",
                request.getPartnerName())
        );
        ReadRangeDTO<InvoiceDTO> items = invoiceService.readPage(dto);
        status.setComplete();
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        model.put("requestInvoices", request);
        return "invoice-table";
    }

    @RequestMapping("/invoice/{page}/{clientId}/{unitId}/{document}/delete.html")
    public String delete(
            @PathVariable Integer clientId,
            @PathVariable Integer unitId,
            @PathVariable String document,
            Map<String, Object> model)
            throws Exception {
        invoiceService.deleteInvoice(clientId, unitId, document);
        return "redirect:/invoice/read-page.html?document=&partnerName=&dateFrom=&dateTo=&page=0";
    }

    @RequestMapping(value = "/invoice/record.html", method = RequestMethod.GET)
    public String initRecordInvoice(
            @RequestParam Integer clientId,
            @RequestParam Integer unitId,
            @RequestParam String document,
            @RequestParam(required = false) Integer pageNumber,
            Map<String, Object> model) throws Exception {
        RequestInvoiceRecordingDTO dto = new RequestInvoiceRecordingDTO();        
        dto.setClientId(clientId);
        dto.setOrgUnitId(unitId);
        dto.setDocument(document);
        model.put("requestInvoiceRecording", dto);
        model.put("page", pageNumber);
        model.put("invoice", invoiceService.readInvoice(clientId, unitId, document));
        return "invoice-record";
    }

    @RequestMapping(value = "/invoice/{page}/record.html", method = RequestMethod.POST)
    public String recordInvoice(
            @ModelAttribute("requestInvoiceRecording") RequestInvoiceRecordingDTO dto,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model) throws Exception {
        if (result.hasErrors()) {
            model.put("page", page);
            model.put("invoice", invoiceService.readInvoice(dto.getClientId(), 
                                                            dto.getOrgUnitId(), 
                                                            dto.getDocument()));
            return "invoice-record";
        }
        try {
            String username = ((User) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUsername();
            dto.setUser(username);
            recordService.perform(dto);
            status.setComplete();
        } catch (ConstraintViolationException | EntityNotFoundException 
                | JournalEntryExistsException | PostedInvoiceException 
                | ProformaInvoicePostingException ex) {
            model.put("page", page);
            model.put("invoice", invoiceService.readInvoice(dto.getClientId(), 
                                                            dto.getOrgUnitId(), 
                                                            dto.getDocument()));
            model.put("exception", ex);
            return "invoice-record";
        }
        return "redirect:/invoice/read-page.html?document=&partnerName=&dateFrom=&dateTo=&page=0";
    }

    @RequestMapping(value = "/invoice/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("invoice", new InvoiceDTO());
        model.put("page", page);
        model.put("partnerTypes", InvoiceBusinessPartner.values());
        model.put("invoiceTypes", InvoiceType.values());
        return "invoice-create-grid";
    }

    @RequestMapping(value = "/invoice/{page}/create", method = RequestMethod.POST)
    public String processCreateForm(@ModelAttribute("invoice") InvoiceDTO invoice,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model) throws Exception {
        if (invoice.getCurrencyISOCode() == null || invoice.getCurrencyISOCode().isEmpty()) {
            invoice.setIsDomesticCurrency(Boolean.TRUE);
        } else {
            invoice.setIsDomesticCurrency(Boolean.FALSE);
        }
        String username = ((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
        invoice.setUsername(username);
        invoice.setPassword(username.toCharArray());
        if (result.hasErrors()) {
            model.put("partnerTypes", InvoiceBusinessPartner.values());
            model.put("invoiceTypes", InvoiceType.values());
            return "invoice-create-grid";
        }
        try {
            invoiceService.createInvoice(invoice);
        } catch (Exception e) {
            model.put("exception", e);
            model.put("partnerTypes", InvoiceBusinessPartner.values());
            model.put("invoiceTypes", InvoiceType.values());
            return "invoice-create-grid";
        }
        return String.format(
                "redirect:/invoice/update.html?clientId=%1$s&unitId=%2$s&document=%3$s&page=%4$s", 
                invoice.getClientId(),
                invoice.getOrgUnitId(),
                invoice.getDocument(),                
                page);
    }

    @RequestMapping(value = "/invoice/print-preview.html", 
                    method = RequestMethod.GET)
    public ResponseEntity<byte[]> showPDF(
            @RequestParam Integer clientId,
            @RequestParam Integer unitId,
            @RequestParam String document)
            throws Exception {
        try {
            InvoiceReportDTO dto = invoiceService.readInvoiceReport(clientId,
                    unitId,
                    document);
            Locale locale = LocaleContextHolder.getLocale();
            InvoiceReport report = new InvoiceReport(dto, locale);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String filename = String.format("%s%d%d", document, clientId, unitId);
            headers.add("content-disposition", "inline;filename=" + filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<>(
                    getPDFFile(report), headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            return null;
        }
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

    @RequestMapping(value = "/invoice/read-client/{name}")
    public @ResponseBody
    List<Client> findClientByName(@PathVariable String name) {
        return masterDataservice.readClientByName(name);
    }

    @RequestMapping(value = "/invoice/read-orgunit/{name}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByName(@PathVariable String name) {
        return masterDataservice.readOrgUnitByName(name);
    }
    
    @RequestMapping(value = "/invoice/read-orgunit/{name}/{client}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByClientAndName(@PathVariable String name,
                                                        @PathVariable Integer client) {
        return masterDataservice.readOrgUnitByClientAndName(client, name);
    }

    @RequestMapping(value = "/invoice/read-businesspartner/{name}")
    public @ResponseBody
    List<BusinessPartner> findBussinesPartnerByName(@PathVariable String name) {
        return masterDataservice.readBusinessPartnerByName(name);
    }
    
    @RequestMapping(value = "/invoice/read-businesspartner/{name}/{max}")
    public @ResponseBody
    List<BusinessPartner> findBussinesPartnerByName(
            @PathVariable String name,
            @PathVariable Integer max) {
        return masterDataservice.readBusinessPartnerByName(name, max);
    }
    
    @RequestMapping(value = "/invoice/read-bank/{name}")
    public @ResponseBody
    List<BankCreditor> findBankByName(@PathVariable String name) {
        return masterDataservice.readBankByName(name);
    }

    @RequestMapping(value = "/invoice/read-currency/{iso}")
    public @ResponseBody
    List<Currency> findCurrencyByISO(@PathVariable String iso) {
        return masterDataservice.readCurrencyByISO(iso);
    }

    @RequestMapping(value = "/invoice/read-item/{code}")
    public @ResponseBody List<Article> findItemByDescription(@PathVariable String code) {
        return masterDataservice.readItemByCode(code);
    }

    @RequestMapping(value = "/invoice/read-description/{name}")
    public @ResponseBody
    List<Description> findDescriptionByName(@PathVariable String name) {
        return masterDataservice.readDescByName(name);
    }
    
    @RequestMapping(value = "/invoice/read-journal-entry-type/{name}/{clientId}")
    public @ResponseBody List<JournalEntryTypeDTO> findTypeByNameAndClientId(
            @PathVariable String name,
            @PathVariable Integer clientId) {
        return masterDataservice.readJournalEntryTypeByNameAndClient(
                clientId, name);
    }
    
    @RequestMapping(value = "/invoice/update.html",
                    method = RequestMethod.GET)
    public String initUpdateForm(@RequestParam String page,
            @RequestParam Integer clientId,
            @RequestParam Integer unitId,
            @RequestParam String document,
            Map<String, Object> model)
            throws Exception {
        InvoiceDTO tmp = invoiceService.readInvoice(clientId, unitId, document);
        model.put("page", page);
        model.put("invoice", tmp);
        model.put("partnerTypes", InvoiceBusinessPartner.values());
        model.put("invoiceTypes", InvoiceType.values());     
        return "invoice-update";
    }
    
    @RequestMapping(value = "/invoice/{page}/update.html", 
                    method = RequestMethod.POST)
    public String processUpdateForm(
            @ModelAttribute("invoice") InvoiceDTO invoice,
            BindingResult result,
            SessionStatus status,
            @PathVariable String page,
            Map<String, Object> model) throws Exception {
        String username = ((User)SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal()).getUsername();
        invoice.setUsername(username);        
        if (result.hasErrors()) {
            model.put("page", page);
            model.put("partnerTypes", InvoiceBusinessPartner.values());
            model.put("invoiceTypes", InvoiceType.values());
            return "invoice-update";
        }
        try{
            invoiceService.updateInvoice(invoice);
        } catch(Exception ex) {
            model.put("exception", ex);
            model.put("page", page);
            model.put("partnerTypes", InvoiceBusinessPartner.values());
            model.put("invoiceTypes", InvoiceType.values());
            return "invoice-update";
        }
        return String.format(
                "redirect:/invoice/update.html?clientId=%1$s&unitId=%2$s&document=%3$s&page=%4$s", 
                invoice.getClientId(),
                invoice.getOrgUnitId(),
                invoice.getDocument(),                
                page);
    }
    
    @RequestMapping(value = "/invoice/details.html", method = RequestMethod.GET)
    public String initDetailsForm(@RequestParam(required = false) String page,
            @RequestParam Integer clientId,
            @RequestParam Integer unitId,
            @RequestParam String document,
            @RequestParam(required = false) Integer itemsPage,
            Map<String, Object> model)
            throws Exception {
        PageRequestDTO dto = new PageRequestDTO();
        dto.setPage(itemsPage);
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                "document",
                document));
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                "clientId",
                clientId));
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                "unitId",
                unitId));
        ReadRangeDTO<InvoiceItemDTO> items = invoiceService.readIncomeItemsPage(dto);
        model.put("itemsPage", items.getPage());        
        model.put("numberOfPages", items.getNumberOfPages());        
        model.put("items", items.getData());
        InvoiceDTO invoice = invoiceService.readInvoice(clientId, unitId, document);
        model.put("invoice", invoice);
        model.put("page", page);
        InvoiceItemDTO item = new InvoiceItemDTO();
        item.setClientId(invoice.getClientId());
        item.setUnitId(invoice.getOrgUnitId());
        item.setInvoiceDocument(invoice.getDocument());
        item.setInvoiceVersion(invoice.getVersion());
        model.put("invoiceItem", item);
        return "invoice-details";
    }
    
    @RequestMapping(value = "/invoice/deleteItem.html",
                    method = RequestMethod.POST)
    public String deleteItem(@RequestParam Integer clientId,
                             @RequestParam Integer unitId,
                             @RequestParam String invoiceDocument,
                             @RequestParam Integer ordinal,
                             @RequestParam Long version,
                             @RequestParam(required = false) String page,
                             @RequestParam(required = false) String itemsPage)
                             throws Exception {
        String username = ((User)SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal()).getUsername();
        invoiceService.removeItem(
                clientId,
                unitId,
                invoiceDocument,
                ordinal,
                username,
                version
        );
        return String.format("redirect:/invoice/details.html?clientId=%1$s&unitId=%2$s&document=%3$s&page=%4$s&itemsPage=%5$s",
                          clientId,
                          unitId,
                          invoiceDocument,
                          page,
                          itemsPage);
    }

    @RequestMapping(value = "/invoice/{page}/{itemsPage}/addItem.html",
                    method = RequestMethod.POST)
    public String addItem(@ModelAttribute("invoiceItem") InvoiceItemDTO item,
                          BindingResult result,
                          SessionStatus status,
                          @PathVariable String page,
                          @PathVariable String itemsPage,
                          Map<String, Object> model) throws Exception {
        PageRequestDTO dto = new PageRequestDTO();
        dto.setPage(Integer.valueOf(itemsPage));
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                 "document",
                 item.getInvoiceDocument()));
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                 "clientId",
                 item.getClientId()));
        dto.addSearchCriterion(new PageRequestDTO.SearchCriterion(
                 "unitId",
                 item.getUnitId()));
         if (result.hasErrors()) {
            model.put("showDialog", Boolean.TRUE);
            model.put("page", page);
            ReadRangeDTO<InvoiceItemDTO> range = invoiceService.readIncomeItemsPage(dto);
            model.put("itemsPage", range.getPage());
            model.put("numberOfPages", range.getNumberOfPages());        
            model.put("items", range.getData());
            InvoiceDTO invoice = invoiceService.readInvoice(item.getClientId(), 
                                                            item.getUnitId(), 
                                                            item.getInvoiceDocument());
            model.put("invoice", invoice);
            return "invoice-details";
        }
        try {
            String username = ((User)SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal()).getUsername();
            item.setUsername(username);
            invoiceService.addItem(item);
            return String.format("redirect:/invoice/details.html?clientId=%1$s&unitId=%2$s&document=%3$s&page=%4$s&itemsPage=%5$s",
                          item.getClientId(),
                          item.getUnitId(),
                          item.getInvoiceDocument(),
                          page,
                          itemsPage);
        } catch(Exception ex) {
            model.put("showDialog", Boolean.TRUE);
            model.put("addItemException", ex);
            model.put("page", page);
            ReadRangeDTO<InvoiceItemDTO> range = invoiceService.readIncomeItemsPage(dto);
            model.put("itemsPage", range.getPage());
            model.put("numberOfPages", range.getNumberOfPages());        
            model.put("items", range.getData());
            InvoiceDTO invoice = invoiceService.readInvoice(
                    item.getClientId(), 
                    item.getUnitId(), 
                    item.getInvoiceDocument());
            model.put("invoice", invoice);
            return "invoice-details";
        }
    }
    
}
