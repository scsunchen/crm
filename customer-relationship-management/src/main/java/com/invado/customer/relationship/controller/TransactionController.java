package com.invado.customer.relationship.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.dto.InvoiceDTO;
import com.invado.core.domain.Device;
import com.invado.customer.relationship.domain.TransactionType;
import com.invado.customer.relationship.service.MasterDataService;
import com.invado.customer.relationship.service.TransactionService;
import com.invado.customer.relationship.service.dto.InvoicingTransactionSetDTO;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(value = "/transactions/{page}")
    public String showTransactions(@PathVariable Integer page,
                                   Map<String, Object> model,
                                   @ModelAttribute TransactionDTO transactionDTO)
            throws Exception {
        PageRequestDTO request = new PageRequestDTO();

        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId",
                transactionDTO.getDistributorId() == null || transactionDTO.getDistributorName() == null || transactionDTO.getDistributorName().isEmpty()
                        ? null : transactionDTO.getDistributorId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("pointOfSaleId",
                transactionDTO.getPointOfSaleId() == null || transactionDTO.getPointOfSaleName() == null || transactionDTO.getPointOfSaleName().isEmpty()
                        ? null : transactionDTO.getPointOfSaleId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("serviceProviderId",
                transactionDTO.getServiceProviderId() == null || transactionDTO.getServiceProviderName() == null || transactionDTO.getServiceProviderName().isEmpty()
                        ? null : transactionDTO.getServiceProviderId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("terminalId",
                transactionDTO.getTerminalId() == null || transactionDTO.getTerminalCustomCode() == null || transactionDTO.getTerminalCustomCode().isEmpty()
                        ? null : transactionDTO.getTerminalId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("typeId",
                transactionDTO.getTypeId() == null || transactionDTO.getTypeDescription() == null || transactionDTO.getTypeDescription().isEmpty()
                        ? null : transactionDTO.getTypeId()));
        ReadRangeDTO<TransactionDTO> items = transactionService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("pageTo", 0);
        model.put("pageBackward", 0);
        model.put("pageForward", 0);
        model.put("transactionDTO", transactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "transactions-view";

    }

    @RequestMapping(value = "/transactions/{paramValues}/{page}")
    public String browsePages(@PathVariable Integer page,
                              @PathVariable String paramValues,
                              Map<String, Object> model) throws Exception {

        String[] params = paramValues.split("-");

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        TransactionDTO transactionDTO = new TransactionDTO();

        for (int i = 0; i < params.length; i++) {
            switch (i) {
                case 0:
                    if (params[i] != null && !params[i].isEmpty()) {
                        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("serviceProviderId", params[i] == null || params[i].isEmpty() ? null : Integer.valueOf(params[i])));
                        transactionDTO.setServiceProviderId(Integer.valueOf(params[i]));
                        transactionDTO.setServiceProviderName(masterDataService.readServiceProviderById(Integer.valueOf(params[i])).getName());
                    }
                    break;
                case 1:
                    if (params[i] != null && !params[i].isEmpty()) {
                        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("pointOfSaleId", params[i] == null || params[i].isEmpty() ? null : Integer.valueOf(params[i])));
                        transactionDTO.setPointOfSaleId(Integer.valueOf(params[i]));
                        transactionDTO.setPointOfSaleName(masterDataService.readPointOfSaleById(Integer.valueOf(params[i])).getName());
                    }
                    break;
                case 2:
                    if (params[i] != null && !params[i].isEmpty()) {
                        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("terminalId", params[i] == null || params[i].isEmpty() ? null : Integer.valueOf(params[i])));
                        transactionDTO.setTerminalId(Integer.valueOf(params[i]));
                        transactionDTO.setTerminalCustomCode(masterDataService.readDevice(Integer.valueOf(params[i])).getCustomCode());
                    }
                    break;
                case 3:
                    if (params[i] != null && !params[i].isEmpty()) {
                        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("typeId", params[i] == null || params[i].isEmpty() ? null : Integer.valueOf(params[i])));
                        transactionDTO.setTypeId(Integer.valueOf(params[i]));
                        transactionDTO.setTypeDescription(transactionService.readTransactionTypeById(Integer.valueOf(params[i])).getDescription());
                    }
                    break;
                case 4:
                    if (params[i] != null && !params[i].isEmpty()) {
                        System.out.println("evo vrednost " + Integer.valueOf(params[i]));
                        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId", params[i] == null || params[i].isEmpty() ? null : Integer.valueOf(params[i])));
                        transactionDTO.setDistributorId(Integer.valueOf(params[i]));
                        transactionDTO.setDistributorName(masterDataService.readClientById(Integer.valueOf(params[i])).getName());
                    }
                    break;
            }
        }
        ReadRangeDTO<TransactionDTO> items = transactionService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("transactionDTO", transactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "transactions-view";
    }


    @RequestMapping(value = "transactions/{page}", method = RequestMethod.POST)
    public String showFilteredTransactions(@PathVariable Integer page, Map<String, Object> model, @ModelAttribute TransactionDTO transactionDTO) throws Exception {
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId",
                transactionDTO.getDistributorId() == null || transactionDTO.getDistributorName() == null || transactionDTO.getDistributorName().isEmpty()
                        ? null : transactionDTO.getDistributorId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("pointOfSaleId",
                transactionDTO.getPointOfSaleId() == null || transactionDTO.getPointOfSaleName() == null || transactionDTO.getPointOfSaleName().isEmpty()
                        ? null : transactionDTO.getPointOfSaleId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("serviceProviderId",
                transactionDTO.getServiceProviderId() == null || transactionDTO.getServiceProviderName() == null || transactionDTO.getServiceProviderName().isEmpty()
                        ? null : transactionDTO.getServiceProviderId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("terminalId",
                transactionDTO.getTerminalId() == null || transactionDTO.getTerminalCustomCode() == null || transactionDTO.getTerminalCustomCode().isEmpty()
                        ? null : transactionDTO.getTerminalId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("typeId",
                transactionDTO.getTypeId() == null || transactionDTO.getTypeDescription() == null || transactionDTO.getTypeDescription().isEmpty()
                        ? null : transactionDTO.getTypeId()));
        ReadRangeDTO<TransactionDTO> items = transactionService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("request", request);

        model.put("numberOfPages", items.getNumberOfPages());
        request = new PageRequestDTO();
        request.setPage(page);
        return "transactions-view";

    }


    @RequestMapping(value = "/in-transactions.html", method = RequestMethod.GET)
    public String showInovicingCandidatesTransactions(@RequestParam Integer page,
                                                      @RequestParam Integer distributorId,
                                                      @RequestParam String invoicingDate,
                                                      Map<String, Object> model,
                                                      @ModelAttribute TransactionDTO transactionDTO) throws Exception{

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId", distributorId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateTo", invoicingDate));
        ReadRangeDTO<InvoicingTransactionSetDTO> items = transactionService.readInvoicingSetPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("transactionDTO", transactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "invoicing-candidates-view";
    }

    @RequestMapping(value = "/invoicing/review-invoices.html", method = RequestMethod.POST)
    public String genInvoices(@ModelAttribute TransactionDTO transactionDTO, Map<String, Object> model) throws Exception {
        System.out.println("izvrsio se bre " + transactionDTO.getInvoicingGenDate() + " " + transactionDTO.getInvoicingDistributorId());
        Map<Integer, InvoiceDTO> genTransactions = transactionService.genInvoicesI(transactionDTO);
        //Map<Integer, InvoiceDTO> genTransactions = transactionService.genInvoicesUI(transactionDTO);
        model.put("data", genTransactions);
        return "invoice-table";
    }

    @RequestMapping(value = "/masterdata/read-distributor/{name}")
    public
    @ResponseBody
    List<Client> findClientByName(@PathVariable String name) {
        System.out.println("ovo se sigurno izvršava a šta je problem da ga jebbem...");
        return masterDataService.readClientMinSetByName(name);
    }

    @RequestMapping(value = "/masterdat/read-pointofsale/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findPointOfSaleByName(@PathVariable String name) {
        return masterDataService.readPointOfSaleByName(name);
    }

    @RequestMapping(value = "/masterdat/read-businesspartner/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findBusinessPartnerByName(@PathVariable String name) {
        return masterDataService.readBusinessPartnerByName(name);
    }

    @RequestMapping(value = "/masterdat/read-serviceprovider/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findServiceProviderByName(@PathVariable String name) {
        return masterDataService.readServiceProviderByName(name);
    }

    @RequestMapping(value = "/masterdat/read-terminal/{name}")
    public
    @ResponseBody
    List<Device> findTerminalByCustomCode(@PathVariable String name) {
        return masterDataService.readDeviceByCustomCode(name);
    }

    @RequestMapping(value = "/masterdat/read-transactiontype/{name}")
    public
    @ResponseBody
    List<TransactionType> findTransactiontypeByType(@PathVariable String name) {
        return transactionService.readTransactionTypeByType(name);
    }
}