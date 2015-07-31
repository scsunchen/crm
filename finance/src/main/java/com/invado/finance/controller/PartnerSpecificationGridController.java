/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.OrgUnit;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.finance.Utils;
import com.invado.finance.controller.report.ReceivablePayableCardReport;
import com.invado.finance.controller.report.SpecificationReport;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.service.MasterDataService;
import com.invado.finance.service.PartnerSpecificationByDate;
import com.invado.finance.service.dto.PartnerSpecificationDTO;
import com.invado.finance.service.dto.ReadSpecificationsDTO;
import com.invado.finance.service.dto.RequestPartnerSpecificationDTO;
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
public class PartnerSpecificationGridController extends AbstractController {
    
    @Inject
    private PartnerSpecificationByDate service;
    @Inject
    private MasterDataService masterDataservice;
    
    @RequestMapping(value = "/partner-specification/read-orgunit/{name}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByName(@PathVariable String name) {
        return masterDataservice.readOrgUnitByName(name);
    }
    
    @RequestMapping(value = "/partner-specification/read-orgunit/{name}/{clientId}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByClientAndName(
            @PathVariable String name,
            @PathVariable Integer clientId) {
        return masterDataservice.readOrgUnitByClientAndName(clientId, name);
    }

    @RequestMapping(value = "/partner-specification/read-businesspartner/{name}")
    public @ResponseBody
    List<BusinessPartner> findBussinesPartnerByName(@PathVariable String name) {
        return masterDataservice.readBusinessPartnerByName(name);
    }

    @RequestMapping(value = "/partner-specification/read-account/{number}")
    public @ResponseBody
    List<Account> findAccountByNumber(@PathVariable String number) {
        return masterDataservice.readAccountByCode(number);
    }
    
    @RequestMapping(value = "/partner-specification/read-client/{name}")
    public @ResponseBody List<Client> findClientByName(@PathVariable String name) {
        return masterDataservice.readClientByName(name);
    }
    
    @RequestMapping(
            value = "/partner-specification/read.html",
            method = RequestMethod.GET
    )
    public String initForm(Map<String, Object> model) {
        RequestPartnerSpecificationDTO dto = new RequestPartnerSpecificationDTO();
        model.put("specDTO", dto);
        return "partner-specification-grid";
    }
    
    @RequestMapping(
            value = "/partner-specification/read-submit.html",
            method = RequestMethod.GET
    )
    public ResponseEntity<byte[]> processForm(
            @ModelAttribute("specDTO") RequestPartnerSpecificationDTO request,
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
        ReadSpecificationsDTO spec = service.readPartnerSpecificationByDate(request);
        
        Book book = new Book();
        for (PartnerSpecificationDTO itemDTO : spec.specifications) {
            SpecificationReport report = new SpecificationReport(
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
        String filename = String.format("%sspecification", LocalDateTime.now());
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
        mapper.putName(SpecificationReport.FONT_NAME, PDFFontParameters);
        return new ResponseEntity<>(super.getPDFFile(book, mapper), headers, HttpStatus.OK);
    }

}
