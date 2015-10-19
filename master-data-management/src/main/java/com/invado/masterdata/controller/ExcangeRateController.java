package com.invado.masterdata.controller;

import com.invado.core.domain.Currency;
import com.invado.masterdata.service.dto.ExchangeRateDTO;
import com.invado.masterdata.service.CurrencyService;
import com.invado.masterdata.service.ExchangeRateService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 04/10/2015.
 */
@Controller
public class ExcangeRateController {

    @Autowired
    private ExchangeRateService service;
    @Autowired
    private CurrencyService currencyService;

    @RequestMapping("/exchange-rate/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model, HttpServletRequest httpServletRequest)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);

        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("applicationDate", httpServletRequest.getParameter("applicationDate")));

        ReadRangeDTO<ExchangeRateDTO> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("exchangeRateDTO", new ExchangeRateDTO());
        model.put("numberOfPages", items.getNumberOfPages());
        return "exchange-rate-view";
    }

    @RequestMapping(value = "/exchange-rate/{page}/create", method = RequestMethod.GET)
    public String initCreateForm(@PathVariable String page, Map<String, Object> model) {
        model.put("item", new ExchangeRateDTO());
        model.put("action", "create");
        return "exchange-rate-grid";
    }

    @RequestMapping(value = "/exchange-rate/{page}/create", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("item") ExchangeRateDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {


        if (result.hasErrors()) {
            model.put("action", "create");
            return "exchange-rate-grid";
        }
        try {
            this.service.create(item);
            model.put("message", item.getApplicationDate() + " " + item.getCurrencyISOCode());
            status.setComplete();
        } catch (Exception ex) {
            model.put("exception", ex);
            return "exchange-rate-grid";
        }
        return "redirect:/exchange-rate/{page}/create";
    }

    @RequestMapping("/exchange-rate/{page}/{applicationDate}/{ISOCode}/delete.html")
    public String delete(@PathVariable LocalDate applicationDate, @PathVariable String ISOCode) throws Exception {
        service.delete(applicationDate, ISOCode);
        return "redirect:/exchange-rate/{page}";
    }

    @RequestMapping(value = "/exchange-rate/{page}/update/{applicationDate}/{ISOCode}",
            method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable LocalDate applicationDate, @PathVariable String ISOCode,
                                 Map<String, Object> model)
            throws Exception {
        ExchangeRateDTO item = service.read(applicationDate, ISOCode);
        model.put("item", item);
        return "exchange-rate-grid";
    }

    @RequestMapping(value = "/exchange-rate/{page}/update/{applicationDate}/{ISOCode}",
            method = RequestMethod.POST)
    public String processUpdationForm(@ModelAttribute("item") ExchangeRateDTO item,
                                      BindingResult result,
                                      SessionStatus status,
                                      Map<String, Object> model)
            throws Exception {
        if (result.hasErrors()) {
            return "exchange-rate-grid";
        } else {
            this.service.update(item);
            status.setComplete();
        }
        return "redirect:/exchange-rate/{page}";
    }

    @RequestMapping(value = "/masterdata/read-currency/byiso/{iso}")
    public
    @ResponseBody
    List<Currency> findCurrencyByISO(@PathVariable String iso) {
        System.out.println("ISO je");
        return currencyService.readByISOCode(iso);
    }

    @RequestMapping(value = "/read-currency/byname/{name}")
    public
    @ResponseBody
    List<Currency> findCurrencyByName(@PathVariable String name) {
        System.out.println("name je");
        return currencyService.readByName(name);
    }


}
