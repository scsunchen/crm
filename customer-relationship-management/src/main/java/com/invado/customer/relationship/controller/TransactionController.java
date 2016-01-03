package com.invado.customer.relationship.controller;

import com.invado.core.domain.*;
import com.invado.core.dto.InvoiceDTO;
import com.invado.core.dto.InvoiceReportDTO;
import com.invado.core.dto.InvoicingTransactionDTO;
import com.invado.core.report.InvoiceReport;
import com.invado.customer.relationship.service.InvoicingTransactionService;
import com.invado.customer.relationship.service.MasterDataService;
import com.invado.customer.relationship.service.TransactionService;
import com.invado.customer.relationship.service.dto.InvoicingTransactionSetDTO;
import com.invado.customer.relationship.service.dto.PageRequestDTO;
import com.invado.customer.relationship.service.dto.ReadRangeDTO;
import com.invado.customer.relationship.service.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;

/**
 * Created by Nikola on 26/08/2015.
 */
@Controller
public class TransactionController {

    @Inject
    private TransactionService transactionService;
    @Inject
    private MasterDataService masterDataService;
    @Inject
    private InvoicingTransactionService invoicingTransactionService;

    @RequestMapping(value = "/transactions/view-transactions-page.html", method = RequestMethod.GET)
    public String showTransactions(@RequestParam Integer page,
                                   @RequestParam(value = "serviceProviderName", required = false) String serviceProviderName,
                                   @RequestParam(value = "serviceProviderId", required = false) Integer serviceProviderId,
                                   @RequestParam(value = "pointOfSaleName", required = false) String pointOfSaleName,
                                   @RequestParam(value = "pointOfSaleId", required = false) Integer pointOfSaleId,
                                   @RequestParam(value = "terminalCustomCode", required = false) String terminalCustomCode,
                                   @RequestParam(value = "terminalId", required = false) Integer terminalId,
                                   @RequestParam(value = "typeDescription", required = false) String typeDescription,
                                   @RequestParam(value = "typeId", required = false) Integer typeId,

                                   Map<String, Object> model,
                                   @ModelAttribute TransactionDTO transactionDTO)
            throws Exception {

        Client client = masterDataService.readClient();
        PageRequestDTO request = new PageRequestDTO();

        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("distributorId", client.getId()));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("pointOfSaleId", pointOfSaleId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("serviceProviderId", serviceProviderId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("terminalId", terminalId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("typeId", typeId));
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


    @RequestMapping(value = "/transactions/in-transactions.html", method = RequestMethod.GET)
    public String showInvoicingCandidatesTransactions(@RequestParam Integer page,
                                                      @RequestParam(value = "merchantId", required = false) Integer merchantId,
                                                      @RequestParam(value = "invoicingDate", required = false) String invoicingDate,
                                                      @RequestParam(value = "invoicingStatus", required = false) Integer invoicingStatus,
                                                      Map<String, Object> model,
                                                      @ModelAttribute TransactionDTO transactionDTO) throws Exception {


        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateTo", invoicingDate));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("merchantId", merchantId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingStatus", 0));
        ReadRangeDTO<InvoicingTransactionSetDTO> items = transactionService.readInvoicingSetPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("transactionDTO", transactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "invoicing-candidates-view";
    }

    @RequestMapping(value = "/transactions/in-transactions-per-pos.html", method = RequestMethod.GET)
    public String showInvoicingCandidatesTransactionsPerPos(@RequestParam Integer page,
                                                            @RequestParam(value = "invoicingDate", required = false) String invoicingDate,
                                                            @RequestParam(value = "merchantId", required = false) Integer merchantId,
                                                            Map<String, Object> model,
                                                            @ModelAttribute TransactionDTO transactionDTO) throws Exception {

        BigDecimal totalAmount = BigDecimal.valueOf(0.0);
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateTo", invoicingDate));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("merchantId", merchantId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingStatus", 0));

        ReadRangeDTO<InvoicingTransactionSetDTO> items = transactionService.readInvoicingPerPosSetPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        for (InvoicingTransactionSetDTO item : items.getData())
            totalAmount = totalAmount.add(item.getAmount());
        model.put("totalAmount", totalAmount);
        model.put("transactionDTO", transactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "invoicing-candidates-per-pos-view";
    }


    @RequestMapping(value = "/transactions/in-transactions-per-article.html", method = RequestMethod.GET)
    public String showInvoicingCandidatesTransactionsPerArticle(@RequestParam Integer page,
                                                                @RequestParam(value = "invoicingDate", required = false) String invoicingDate,
                                                                @RequestParam(value = "merchantName", required = false) String merchantName,
                                                                @RequestParam(value = "merchantId", required = false) Integer merchantId,
                                                                @RequestParam(value = "posName", required = false) String posName,
                                                                @RequestParam(value = "posId", required = false) Integer posId,
                                                                Map<String, Object> model,
                                                                @ModelAttribute TransactionDTO transactionDTO) throws Exception {

        BigDecimal totalAmount = BigDecimal.valueOf(0.0);
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateTo", invoicingDate));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("merchantId", merchantId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingStatus", 0));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("posId", posId));

        ReadRangeDTO<InvoicingTransactionSetDTO> items = transactionService.readInvoicingPerArticleSetPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        for (InvoicingTransactionSetDTO item : items.getData())
            totalAmount = totalAmount.add(item.getAmount());
        model.put("totalAmount", totalAmount);
        model.put("transactionDTO", transactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "invoicing-candidates-per-article-view";
    }

    @RequestMapping(value = "/transactions/review-invoicing-transactions.html", method = RequestMethod.GET)
    public String showInvoicesPerPeriod(@RequestParam(value = "partnerId", required = false) Integer partnerId,
                                        @RequestParam(value = "id", required = false) Integer invoicingPeriod,
                                        @RequestParam(value = "partnerName", required = false) String partnerName,
                                        @RequestParam Integer page,
                                        @ModelAttribute InvoicingTransactionDTO invoicingTransactionDTO, Map<String, Object> model) throws Exception {
/* ne menjaj */
        PageRequestDTO request = new PageRequestDTO();
        model.put("invoicingTransactions", invoicingTransactionService.getAllPeriods());
        //List<InvoiceDTO> invoices = transactionService.readInvoiciePerPeriod(invoicingPeriod, partnerId);
        ReadRangeDTO<InvoiceDTO> result = transactionService.readInvPerPeriodPage(invoicingPeriod, partnerId, page);
        model.put("data", result.getData());
        model.put("numberOfPages", result.getNumberOfPages());
        model.put("page", page);
        return "review-invoicing-transactions";
    }


    @RequestMapping(value = "/invoicing/review-invoices.html", method = RequestMethod.POST)
    public String genInvoices(@ModelAttribute TransactionDTO transactionDTO, Map<String, Object> model) throws Exception {

        Map<Integer, InvoiceDTO> genTransactions = null;
        Client client = masterDataService.readClient();
        if (client.getInvoicingType() == Client.InvoicingType.OUTPUT) {
            genTransactions = transactionService.genInvoicesI(transactionDTO);
        } else {
            genTransactions = transactionService.genInvoicesUI(transactionDTO);
        }
        //Map<Integer, InvoiceDTO> genTransactions = transactionService.genInvoicesUI(transactionDTO);
        model.put("data", genTransactions);
        return "invoice-table";
    }


    @RequestMapping(value = "/transactions/in-transactions-per-period.html", method = RequestMethod.GET)
    public String showInvoicedTransactionsPerPeriod(@RequestParam Integer page,
                                                  @RequestParam(value = "merchantId", required = false) Integer merchantId,
                                                  @RequestParam(value = "id", required = false) Integer id,
                                                  Map<String, Object> model,
                                                  @ModelAttribute InvoicingTransactionDTO invoicingTransactionDTO) throws Exception {


        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        InvoicingTransactionDTO it = null;
        if (id != null) {
            it = invoicingTransactionService.getbyId(id);
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateTo", it.getInvoicedTo().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateFrom", it.getInvoicedFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        } else {
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateTo", null));
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateFrom", null));
        }
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("merchantId", merchantId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingStatus", 1));

        ReadRangeDTO<InvoicingTransactionSetDTO> items = transactionService.readInvoicingSetPage(request);

        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("invoicingTransactions", invoicingTransactionService.getAllPeriods());
        model.put("invoicingTransactionDTO", invoicingTransactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "invoiced-candidates-per-period-view";
    }

    @RequestMapping(value = "/transactions/in-transactions-per-pos-per-period.html", method = RequestMethod.GET)
    public String showInvoicedTransactionsPerPeriodPerPOS(@RequestParam Integer page,
                                                  @RequestParam(value = "merchantId", required = false) Integer merchantId,
                                                  @RequestParam(value = "id", required = false) Integer id,
                                                  Map<String, Object> model,
                                                  @ModelAttribute InvoicingTransactionDTO invoicingTransactionDTO) throws Exception {


        BigDecimal totalAmount = BigDecimal.valueOf(0.0);
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        InvoicingTransactionDTO it = null;
        if (id != null) {
            it = invoicingTransactionService.getbyId(id);
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateTo", it.getInvoicedTo().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateFrom", it.getInvoicedFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        } else {
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateTo", null));
            request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingDateFrom", null));
        }
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("merchantId", merchantId));
        request.addSearchCriterion(new PageRequestDTO.SearchCriterion("invoicingStatus", 1));

        ReadRangeDTO<InvoicingTransactionSetDTO> items = transactionService.readInvoicingPerPosSetPage(request);

        for (InvoicingTransactionSetDTO item : items.getData())
            totalAmount = totalAmount.add(item.getAmount());
        model.put("totalAmount", totalAmount);


        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("invoicingTransactions", invoicingTransactionService.getAllPeriods());
        model.put("invoicingTransactionDTO", invoicingTransactionDTO);
        model.put("numberOfPages", items.getNumberOfPages());
        return "invoiced-candidates-per-period-per-pos-view";
    }


    @RequestMapping(value = "/transactions/mass-print-preview.html",
            method = RequestMethod.GET)
    public ResponseEntity<byte[]> showMassPDF(
            @RequestParam Integer clientId,
            @RequestParam Integer unitId,
            @RequestParam String document)
            throws Exception {
        try {
            InvoiceReportDTO dto = invoicingTransactionService.readInvoiceReport(clientId,
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

    @RequestMapping(value = "/transactions/print-preview.html",
            method = RequestMethod.GET)
    public ResponseEntity<byte[]> showPDF(
            @RequestParam Integer clientId,
            @RequestParam Integer unitId,
            @RequestParam String document)
            throws Exception {
        try {
            InvoiceReportDTO dto = invoicingTransactionService.readInvoiceReport(clientId,
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
        try {
            DefaultFontMapper mapper = new DefaultFontMapper();
            DefaultFontMapper.BaseFontParameters PDFFontParameters
                    = new DefaultFontMapper.BaseFontParameters(
                    "/com/invado/customer/relationship/font/FreeSans.otf");
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/crm/read-distributor/{name}")
    public
    @ResponseBody
    List<Client> findClientByName(@PathVariable String name) {
        return masterDataService.readClientMinSetByName(name);
    }

    @RequestMapping(value = "/crm/read-pointofsale/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findPointOfSaleByName(@PathVariable String name) {
        return masterDataService.readPointOfSaleByName(name);
    }

    @RequestMapping(value = "/crm/read-businesspartner/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findBusinessPartnerByName(@PathVariable String name) {
        return masterDataService.readBusinessPartnerByName(name);
    }

    @RequestMapping(value = "/crm/read-serviceprovider/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findServiceProviderByName(@PathVariable String name) {
        return masterDataService.readServiceProviderByName(name);
    }

    @RequestMapping(value = "/crm/read-terminal/{name}")
    public
    @ResponseBody
    List<Device> findTerminalByCustomCode(@PathVariable String name) {
        return masterDataService.readDeviceByCustomCode(name);
    }

    @RequestMapping(value = "/crm/read-transactiontype/{name}")
    public
    @ResponseBody
    List<TransactionType> findTransactiontypeByType(@PathVariable String name) {
        return transactionService.readTransactionTypeByType(name);
    }

    @RequestMapping(value = "/masterdata/read-merchant/{name}")
    public
    @ResponseBody
    List<BusinessPartner> findMerchantByName(@PathVariable String name) {
        return masterDataService.readMerchantByName(name);
    }


}