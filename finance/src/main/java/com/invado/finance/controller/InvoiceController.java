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
import com.invado.finance.controller.report.InvoiceReport;
import com.invado.finance.domain.InvoiceBusinessPartner;
import com.invado.finance.domain.InvoiceType;
import com.invado.finance.service.MasterDataService;
import com.invado.finance.service.InvoiceService;
import com.invado.finance.service.dto.InvoiceDTO;
import com.invado.finance.service.dto.InvoiceItemDTO;
import com.invado.finance.service.dto.InvoiceReportDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
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
public class InvoiceController {

    @Inject
    private InvoiceService invoiceService;
    @Inject
    private MasterDataService masterDataservice;
    private final String username = "a";

    @RequestMapping("/invoice/{page}")
    public String showInvoices(@PathVariable Integer page,
            Map<String, Object> model)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<InvoiceDTO> items = invoiceService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        return "invoice-table";
    }

    @RequestMapping("/invoice/{page}/{clientId}/{unitId}/{document}/delete.html")
    public String delete(
            @PathVariable Integer clientId,
            @PathVariable Integer unitId,
            @PathVariable String document)
            throws Exception {
        invoiceService.deleteInvoice(clientId, unitId, document);
        return "redirect:/invoice/{page}";
    }

    @RequestMapping(value = "/invoice/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("invoice", new InvoiceDTO());
        model.put("action", "create");
        model.put("partnerTypes", InvoiceBusinessPartner.values());
        model.put("invoiceTypes", InvoiceType.values());
        return "invoice-create-grid";
    }

    @RequestMapping(value = "/invoice/{page}/create", method = RequestMethod.POST)
    public String processCreateForm(@ModelAttribute("invoice") InvoiceDTO invoice,
            BindingResult result,
            SessionStatus status,
            Map<String, Object> model) throws Exception {
        if (invoice.getCurrencyISOCode() == null || invoice.getCurrencyISOCode().isEmpty()) {
            invoice.setIsDomesticCurrency(Boolean.TRUE);
        } else {
            invoice.setIsDomesticCurrency(Boolean.FALSE);
        }
        invoice.setUsername(username);
        invoice.setPassword(username.toCharArray());
        if (result.hasErrors()) {
            model.put("action", "create");
            model.put("partnerTypes", InvoiceBusinessPartner.values());
            model.put("invoiceTypes", InvoiceType.values());
            return "invoice-create-grid";
        }
        invoiceService.createInvoice(invoice);
        return String.format("redirect:/invoice/{page}/%d/%d/%s/update.html",
                invoice.getClientId(),
                invoice.getOrgUnitId(),
                invoice.getDocument());
    }

    @RequestMapping(value = "/invoice/{page}/{clientId}/{unitId}/{document}/update.html",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable String page,
            @PathVariable Integer clientId,
            @PathVariable Integer unitId,
            @PathVariable String document,
            Map<String, Object> model)
            throws Exception {
        InvoiceDTO tmp = invoiceService.readInvoice(clientId, unitId, document);
        InvoiceItemDTO itemDTO = new InvoiceItemDTO();
        itemDTO.setClientId(clientId);
        itemDTO.setUnitId(unitId);
        itemDTO.setInvoiceDocument(document);                
        itemDTO.setInvoiceVersion(tmp.getVersion());                
        model.put("item", itemDTO);
        model.put("invoice",tmp);
        model.put("items", invoiceService.readInvoiceItems(clientId, unitId, document));
        model.put("action", "update");
        model.put("partnerTypes", InvoiceBusinessPartner.values());
        model.put("invoiceTypes", InvoiceType.values());
        return "invoice-update-master-detail";
    }

    @RequestMapping(value = "/invoice/{page}/{clientId}/{unitId}/{document}/update.html", 
            method = RequestMethod.POST)
    public String processUpdateForm(@ModelAttribute("invoice") InvoiceDTO invoice,
            BindingResult result,
            SessionStatus status,
            Map<String, Object> model) throws Exception {
        if (invoice.getCurrencyISOCode() == null || invoice.getCurrencyISOCode().isEmpty()) {
            invoice.setIsDomesticCurrency(Boolean.TRUE);
        } else {
            invoice.setIsDomesticCurrency(Boolean.FALSE);
        }
        invoice.setUsername(username);
        invoice.setPassword(username.toCharArray());
        if (result.hasErrors()) {
            model.put("action", "update");
            model.put("partnerTypes", InvoiceBusinessPartner.values());
            model.put("invoiceTypes", InvoiceType.values());
            return "invoice-update-master-detail";
        }
        invoiceService.updateInvoice(invoice);
        return "redirect:/invoice/{page}/{clientId}/{unitId}/{document}/update.html";
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

    @RequestMapping(value = "/invoice/read-businesspartner/{name}")
    public @ResponseBody List<BusinessPartner> findBussinesPartnerByName(@PathVariable String name) {
        return masterDataservice.readBusinessPartnerByName(name);
    }

    @RequestMapping(value = "/invoice/read-bank/{name}")
    public @ResponseBody List<BankCreditor> findBankByName(@PathVariable String name) {
        return masterDataservice.readBankByName(name);
    }

    @RequestMapping(value = "/invoice/read-currency/{iso}")
    public @ResponseBody List<Currency> findCurrencyByISO(@PathVariable String iso) {
        return masterDataservice.readCurrencyByISO(iso);
    }
    
    @RequestMapping(value = "/invoice/read-item/{desc}")
    public @ResponseBody List<Article> findItemByDescription(@PathVariable String desc) {
        return masterDataservice.readItemByDescription(desc);
    }

    @RequestMapping(value = "/invoice/{page}/{clientId}/{unitId}/{document}/{ordinal}/{version}/deleteItem.html",
            method = RequestMethod.GET)
    public String deleteItem(@PathVariable Integer clientId,
            @PathVariable Integer unitId,
            @PathVariable String document,
            @PathVariable Integer ordinal,
            @PathVariable Long version)
            throws Exception {
        invoiceService.removeItem(clientId,
                unitId,
                document,
                ordinal,
                username,
                username.toCharArray(),
                version);
        return "redirect:/invoice/{page}/{clientId}/{unitId}/{document}/update.html";
    }
    @RequestMapping(value = "/invoice/{page}/{clientId}/{unitId}/{document}/addItem.html",
            method = RequestMethod.POST)
    public String addItem(@ModelAttribute("item") InvoiceItemDTO item,
            BindingResult result,
            SessionStatus status,
            Map<String, Object> model) throws Exception {
        if (result.hasErrors()) {
            model.put("action", "update");
            model.put("partnerTypes", InvoiceBusinessPartner.values());
            model.put("invoiceTypes", InvoiceType.values());
            return "invoice-update-master-detail";
        }
        item.setPassword(username.toCharArray());
        item.setUsername(username);
        invoiceService.addItem(item);
        return "redirect:/invoice/{page}/{clientId}/{unitId}/{document}/update.html";
    }

    public String updateItem(@ModelAttribute("item") InvoiceItemDTO item,
            BindingResult result,
            SessionStatus status,
            Map<String, Object> model) throws Exception {
        if (result.hasErrors()) {
            model.put("action", "update");
            model.put("partnerTypes", InvoiceBusinessPartner.values());
            model.put("invoiceTypes", InvoiceType.values());
            return "invoice-update-master-detail";
        }
        item.setPassword(username.toCharArray());
        item.setUsername(username);
        invoiceService.updateItem(item);
        return "redirect:/invoice/{page}/{clientId}/{unitId}/{document}/update.html";
    }

    @RequestMapping(value = "/invoice/{clientId}/{unitId}/{document}/print-preview.html")
    public ResponseEntity<byte[]> showPDF(
            @PathVariable Integer clientId,
            @PathVariable Integer unitId,
            @PathVariable String document)
            throws Exception {
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
}
