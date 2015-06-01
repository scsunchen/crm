/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.invado.finance.domain.Article;
import com.invado.finance.service.InvoiceService;
import com.invado.finance.service.dto.InvoiceDTO;
import com.invado.finance.service.dto.PageRequestDTO;
import com.invado.finance.service.dto.ReadRangeDTO;
import com.invado.finance.service.exception.PageNotExistsException;
import com.invado.finance.service.exception.SystemException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author bdragan
 */
@Controller
public class InvoiceController {

    @Autowired
    private InvoiceService service;

    @RequestMapping("/invoice/{page}")
    public String showInvoices(@PathVariable Integer page,
            Map<String, Object> model) {
        try {
            PageRequestDTO request = new PageRequestDTO();
            request.setPage(page);
            ReadRangeDTO<InvoiceDTO> items = service.readPage(request);
            model.put("data", items.getData());
            model.put("page", items.getPage());
            model.put("numberOfPages", items.getNumberOfPages());
            return "invoice-table";
        } catch (SystemException ex) {
            System.out.println(ex);
            throw new UnsupportedOperationException();
        } catch (PageNotExistsException ex) {
            System.out.println(ex);
            throw new UnsupportedOperationException();
        }
    }
}
