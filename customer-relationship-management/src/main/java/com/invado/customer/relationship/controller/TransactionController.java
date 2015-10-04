package com.invado.customer.relationship.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.customer.relationship.domain.Device;
import com.invado.customer.relationship.domain.TransactionType;
import com.invado.customer.relationship.service.DeviceService;
import com.invado.customer.relationship.service.TransactionService;
import com.invado.customer.relationship.service.dto.InvoicingTransactionSetDTO;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import com.invado.finance.service.MasterDataService;
import com.invado.finance.service.dto.InvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 26/08/2015.
 */
@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private MasterDataService masterDataService;

    @Autowired
    private DeviceService deviceService;


    @RequestMapping(value = "transactions/{page}")
    public String showTransactions(@PathVariable Integer page, Map<String, Object> model, @ModelAttribute TransactionDTO transactionDTO, HttpServletRequest httpServletRequest)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();

        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId", httpServletRequest.getParameter("distributorId")));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("pointOfSaleId", httpServletRequest.getParameter("pointOfSaleId")));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("serviceProviderId", httpServletRequest.getParameter("serviceProviderId")));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("terminalId", httpServletRequest.getParameter("terminalId")));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("typeId", httpServletRequest.getParameter("typeId")));

        ReadRangeDTO<TransactionDTO> items = transactionService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("transactionDTO", transactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "transactions-view";

    }




    @RequestMapping(value = "invoicing/{page}")
    public String showInvoincingCandidatesSet(@PathVariable Integer page, Map<String, Object> model, @ModelAttribute TransactionDTO transactionDTO,
                                              HttpServletRequest httpServletRequest) throws Exception {

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);

        model.put("transactionDTO", transactionDTO);

        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDate", httpServletRequest.getParameter("invoicingDate")));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId", httpServletRequest.getParameter("distributorId")));

        ReadRangeDTO<InvoicingTransactionSetDTO> items = transactionService.readInvoicingSetPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());

        return "invoicingSet-view";

    }




    @RequestMapping(value = "/invoicing/review-invoices.html",method = RequestMethod.POST)
    public String genInvoices(@ModelAttribute TransactionDTO transactionDTO, Map<String, Object> model) throws Exception{
        System.out.println("izvrsio se bre " + transactionDTO.getInvoicingGenDate() + " " + transactionDTO.getInvoicingDistributorId());
        Map<Integer, InvoiceDTO> genTransactions =  transactionService.genInvoices(transactionDTO);
        model.put("data", genTransactions);
        return "invoice-table";
    }

    @RequestMapping(value = "/masterdata/read-distributor/{name}")
    public
    @ResponseBody
    List<Client> findClientByName(@PathVariable String name) {
        return masterDataService.readClientMinSetByName(name);
    }

    @RequestMapping(value = "/masterdata/read-pointofsale/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findPointOfSaleByName(@PathVariable String name) {
        return masterDataService.readPointOfSaleByName(name);
    }

    @RequestMapping(value = "/masterdata/read-businesspartner/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findBusinessPartnerByName(@PathVariable String name) {
        return masterDataService.readBusinessPartnerByName(name);
    }

    @RequestMapping(value = "/masterdata/read-serviceprovider/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findServiceProviderByName(@PathVariable String name) {
        return masterDataService.readServiceProviderByName(name);
    }

    @RequestMapping(value = "/masterdata/read-terminal/{name}")
    public
    @ResponseBody
    List<Device> findTerminalByCustomCode(@PathVariable String name) {
        return deviceService.readDeviceByCustomCode(name);
    }

    @RequestMapping(value = "/masterdata/read-transactiontype/{name}")
    public
    @ResponseBody
    List<TransactionType> findTransactiontypeByType(@PathVariable String name) {
        return transactionService.readTransactionTypeByType(name);
    }

}
