package com.invado.customer.relationship.controller;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.customer.relationship.domain.Device;
import com.invado.customer.relationship.domain.TransactionType;
import com.invado.customer.relationship.service.DeviceService;
import com.invado.customer.relationship.service.TransactionService;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import com.invado.finance.service.MasterDataService;
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

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "transactions/{page}")
    public String showTransactions(@PathVariable Integer page, Map<String, Object> model) throws Exception {

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        TransactionDTO transactionDTO = new TransactionDTO();
        ReadRangeDTO<TransactionDTO> items = transactionService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("transactionDTO", transactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "transactions-view";

    }

    @RequestMapping(value = "transactions/{page}", method = RequestMethod.POST)
    public String showTransactions(@PathVariable Integer page, Map<String, Object> model, @ModelAttribute TransactionDTO transactionDTO) throws Exception {

        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId", transactionDTO.getDistributorId() == null ? null : transactionDTO.getDistributorId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("pointOfSaleId", transactionDTO.getPointOfSaleId() == null ? null : transactionDTO.getPointOfSaleId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("serviceProviderId", transactionDTO.getServiceProviderId() == null ? null : transactionDTO.getServiceProviderId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("terminalId", transactionDTO.getTerminalId() == null ? null : transactionDTO.getTerminalId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("typeId", transactionDTO.getTypeId() == null ? null : transactionDTO.getTypeId()));
        ReadRangeDTO<TransactionDTO> items = transactionService.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("request", request);
        model.put("numberOfPages", items.getNumberOfPages());
        request = new PageRequestDTO();
        request.setPage(page);


        return "transactions-view";
    }

    @RequestMapping(value = "/masterdat/read-distributor/{name}")
    public
    @ResponseBody
    List<Client> findClientByName(@PathVariable String name) {
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
        return deviceService.readDeviceByCustomCode(name);
    }
    @RequestMapping(value = "/masterdat/read-transactiontype/{name}")
    public
    @ResponseBody
    List<TransactionType> findTransactiontypeByType(@PathVariable String name) {
        return transactionService.readTransactionTypeByType(name);
    }

}
