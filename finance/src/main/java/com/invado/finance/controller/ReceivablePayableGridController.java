/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.Currency;
import com.invado.core.domain.OrgUnit;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.finance.Utils;
import com.invado.finance.controller.report.ReceivablePayableCardReport;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.service.MasterDataService;
import com.invado.finance.service.ReceivablePayableCard;
import com.invado.finance.service.dto.CurrencyTypeDTO;
import com.invado.finance.service.dto.LedgerCardDTO;
import com.invado.finance.service.dto.RequestLedgerCardDTO;
import com.invado.finance.service.dto.ReadLedgerCardsDTO;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.pdf.BaseFont;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class ReceivablePayableGridController extends AbstractController {
     
    @Inject
    private ReceivablePayableCard cardService;
    
    @Inject
    private MasterDataService masterDataservice;
    
    @RequestMapping(value = "/receivable-payable-card/read-orgunit/{name}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByName(@PathVariable String name) {
        return masterDataservice.readOrgUnitByName(name);
    }
    
    @RequestMapping(value = "/receivable-payable-card/read-orgunit/{name}/{clientId}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByClientAndName(
            @PathVariable String name,
            @PathVariable Integer clientId) {
        return masterDataservice.readOrgUnitByClientAndName(clientId, name);
    }

    @RequestMapping(value = "/receivable-payable-card/read-businesspartner/{name}")
    public @ResponseBody
    List<BusinessPartner> findBussinesPartnerByName(@PathVariable String name) {
        return masterDataservice.readBusinessPartnerByName(name);
    }

    @RequestMapping(value = "/receivable-payable-card/read-account/{number}")
    public @ResponseBody
    List<Account> findAccountByNumber(@PathVariable String number) {
        return masterDataservice.readAccountByCode(number);
    }
    
    @RequestMapping(value = "/receivable-payable-card/read-client/{name}")
    public @ResponseBody List<Client> findClientByName(@PathVariable String name) {
        return masterDataservice.readClientByName(name);
    }
    
    @RequestMapping(value = "/receivable-payable-card/read-currency/{iso}")
    public @ResponseBody List<Currency> findCurrencyByISO(@PathVariable String iso) {
        return masterDataservice.readCurrencyByISO(iso);
    }
    
    @RequestMapping(
            value = "/receivable-payable-card/read-customer.html",
            method = RequestMethod.GET
    )
    public String initCustomerForm(Map<String, Object> model) {
        RequestLedgerCardDTO dto = new RequestLedgerCardDTO();
        dto.setStatus(RequestLedgerCardDTO.Status.ALL);
        dto.setType(RequestLedgerCardDTO.Type.CUSTOMER);
        model.put("ledgerCardRequest", dto);
        model.put("status", RequestLedgerCardDTO.Status.values());
        return "receivable-payable-customer-grid";
    }
    
    @RequestMapping(
            value = "/receivable-payable-card/read-supplier.html",
            method = RequestMethod.GET
    )
    public String initSupplierForm(Map<String, Object> model) {
        RequestLedgerCardDTO dto = new RequestLedgerCardDTO();
        dto.setStatus(RequestLedgerCardDTO.Status.ALL);
        dto.setType(RequestLedgerCardDTO.Type.SUPPLIER);
        model.put("ledgerCardRequest", dto);
        model.put("status", RequestLedgerCardDTO.Status.values());
        return "receivable-payable-supplier-grid";
    }

    @RequestMapping(
            value = "/receivable-payable-card/read-submit.html",
            method = RequestMethod.GET
    )
    public ResponseEntity<byte[]> processForm(
            @ModelAttribute("ledgerCardRequest") RequestLedgerCardDTO request,
            BindingResult result,
            SessionStatus status,
            Map<String, Object> model) throws Exception {
        if (result.hasErrors()) {
            throw new ConstraintViolationException(
                    "", 
                    result
                    .getFieldErrors()
                    .stream()
                    .map(p -> {       
                        for (String key : p.getCodes()) {
                            if(Utils.containsKey(key)){
                                return Utils.getMessage(key);
                            }
                        }
                        return "";                         
                    })
                    .collect(Collectors.toList())
            );               
        }
        if (request.getForeignCurrencyISOCode() == null
                || request.getForeignCurrencyISOCode().isEmpty()) {
            request.setCurrency(CurrencyTypeDTO.DOMESTIC);
        } else {
            request.setCurrency(CurrencyTypeDTO.FOREIGN);
        }
        ReadLedgerCardsDTO GL = cardService.readReceivablePayableCard(request);
        Book book = new Book();
        for (LedgerCardDTO itemDTO : GL.ledgerCards) {
            ReceivablePayableCardReport report = new ReceivablePayableCardReport(
                    itemDTO,
                    book.getNumberOfPages(),
                    "",
                    LocaleContextHolder.getLocale()
            );
            book.append(report,
                    ReceivablePayableCardReport.getPageFormat(),
                    report.getNumberOfPages(ReceivablePayableCardReport.getPageFormat()));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = String.format("%s%s", LocalDateTime.now(), 
                request.getType() == RequestLedgerCardDTO.Type.CUSTOMER ? 
                        "AP" : "AR"
        );
        headers.add("content-disposition", "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        //embed FreeSans(http://www.gnu.org/software/freefont/) 
        //font into PDF document
        DefaultFontMapper mapper = new DefaultFontMapper();
        DefaultFontMapper.BaseFontParameters PDFFontParameters
                = new DefaultFontMapper.BaseFontParameters(FREE_SANS_FONT_PATH);
        PDFFontParameters.encoding = BaseFont.CP1250;
        PDFFontParameters.embedded = Boolean.TRUE;
        //Map AWT font Lucida Sans Regular to FreeSans. Lucida Sans 
        //Regular font cannot be embedded due to license restrictions.
        mapper.putName(ReceivablePayableCardReport.FONT_NAME, PDFFontParameters);
        return new ResponseEntity<>(super.getPDFFile(book, mapper), headers, HttpStatus.OK);
    }

}
