/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.core.domain.Client;
import com.invado.core.domain.Currency;
import com.invado.core.domain.OrgUnit;
import com.invado.core.exception.ConstraintViolationException;
import com.invado.finance.Utils;
import com.invado.finance.controller.report.GLCardReport;
import com.invado.finance.domain.journal_entry.Account;
import com.invado.finance.service.GLCard;
import com.invado.finance.service.MasterDataService;
import com.invado.finance.service.dto.CurrencyTypeDTO;
import com.invado.finance.service.dto.RequestGLCardDTO;
import com.invado.finance.service.dto.LedgerCardDTO;
import com.invado.finance.service.dto.ReadLedgerCardsDTO;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.pdf.BaseFont;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
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
public class GLGridController extends AbstractController {

    @Inject
    private GLCard GLCardService;

    @Inject
    private MasterDataService masterDataservice;

    @RequestMapping(
            value = "/gl-card/read-general-ledger.html",
            method = RequestMethod.GET
    )
    public String initGLForm(Map<String, Object> model) {
        RequestGLCardDTO dto = new RequestGLCardDTO();
        model.put("GLRequest", dto);
        return "GL-grid";
    }

    @RequestMapping(
            value = "/gl-card/read-general-ledger-submit.html",
            method = RequestMethod.GET
    )
    public ResponseEntity<byte[]> processGLForm(
            @ModelAttribute("GLRequest") RequestGLCardDTO request,
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
        ReadLedgerCardsDTO GL = GLCardService.readGLCard(request);
        Book book = new Book();
        for (LedgerCardDTO itemDTO : GL.ledgerCards) {
            GLCardReport report = new GLCardReport(itemDTO,
                    book.getNumberOfPages(),
                    "",
                    LocaleContextHolder.getLocale()
            );
            book.append(report, GLCardReport.getPageFormat(),
                    report.getNumberOfPages(GLCardReport.getPageFormat())
            );
        }
        DefaultFontMapper mapper = new DefaultFontMapper();
        DefaultFontMapper.BaseFontParameters PDFFontParameters
                = new DefaultFontMapper.BaseFontParameters(FREE_SANS_FONT_PATH);
        PDFFontParameters.encoding = BaseFont.CP1250;
        PDFFontParameters.embedded = Boolean.TRUE;
        //Map AWT font Lucida Sans Regular to FreeSans. Lucida Sans 
        //Regular font cannot be embedded due to license restrictions.
        mapper.putName(GLCardReport.FONT_NAME, PDFFontParameters);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = String.format("%s-GL", LocalDateTime.now());
        headers.add("content-disposition", "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(getPDFFile(book, mapper));
    }

    @RequestMapping(value = "/gl-card/read-orgunit/{name}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByName(@PathVariable String name) {
        return masterDataservice.readOrgUnitByName(name);
    }

    @RequestMapping(value = "/gl-card/read-orgunit/{name}/{clientId}")
    public @ResponseBody
    List<OrgUnit> findOrganizationalUnitByClientAndName(
            @PathVariable String name,
            @PathVariable Integer clientId) {
        return masterDataservice.readOrgUnitByClientAndName(clientId, name);
    }

    @RequestMapping(value = "/gl-card/read-account/{number}")
    public @ResponseBody
    List<Account> findAccountByNumber(@PathVariable String number) {
        return masterDataservice.readAccountByCode(number);
    }

    @RequestMapping(value = "/gl-card/read-client/{name}")
    public @ResponseBody
    List<Client> findClientByName(@PathVariable String name) {
        return masterDataservice.readClientByName(name);
    }

    @RequestMapping(value = "/gl-card/read-currency/{iso}")
    public @ResponseBody
    List<Currency> findCurrencyByISO(@PathVariable String iso) {
        return masterDataservice.readCurrencyByISO(iso);
    }
}
