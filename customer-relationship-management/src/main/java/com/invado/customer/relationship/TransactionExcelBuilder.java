package com.invado.customer.relationship;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.invado.customer.relationship.domain.Transaction;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * Created by Nikola on 23/01/2016.
 */
public class TransactionExcelBuilder extends AbstractExcelView {

        @Override
        protected void buildExcelDocument(Map<String, Object> model,
                                          HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
            // get data model which is passed by the Spring container
            List<Transaction> listTransactions = (List<Transaction>) model.get("listTrasnactions");

            // create a new Excel sheet
            HSSFSheet sheet = workbook.createSheet("Java Books");
            sheet.setDefaultColumnWidth(30);

            // create style for header cells
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            style.setFillForegroundColor(HSSFColor.BLUE.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.WHITE.index);
            style.setFont(font);

            // create header row
            HSSFRow header = sheet.createRow(0);


            header.createCell(0).setCellValue("Id");
            header.getCell(0).setCellStyle(style);

            header.createCell(1).setCellValue("Amount");
            header.getCell(1).setCellStyle(style);

            header.createCell(2).setCellValue("Request Time");
            header.getCell(2).setCellStyle(style);

            header.createCell(3).setCellValue("Response Time");
            header.getCell(3).setCellStyle(style);

            header.createCell(4).setCellValue("Status Id");
            header.getCell(4).setCellStyle(style);

            header.createCell(5).setCellValue("Client Id");
            header.getCell(5).setCellStyle(style);

            header.createCell(5).setCellValue("Prodavac Id");
            header.getCell(5).setCellStyle(style);

            header.createCell(5).setCellValue("Service Provider Id");
            header.getCell(5).setCellStyle(style);

            header.createCell(5).setCellValue("Terminal Id");
            header.getCell(5).setCellStyle(style);

            header.createCell(5).setCellValue("Tip Id");
            header.getCell(5).setCellStyle(style);

            header.createCell(5).setCellValue("Prodajno mesto Id");
            header.getCell(5).setCellStyle(style);

            header.createCell(5).setCellValue("Inovicing Status Id");
            header.getCell(5).setCellStyle(style);

            header.createCell(5).setCellValue("Transaction Status Id");
            header.getCell(5).setCellStyle(style);

            // create data rows
            int rowCount = 1;
            for (Transaction item : listTransactions) {
                HSSFRow aRow = sheet.createRow(rowCount++);
                aRow.createCell(0).setCellValue(item.getId().toString());
                aRow.createCell(1).setCellValue(item.getAmount().toString());
                aRow.createCell(2).setCellValue(item.getRequestTime().toString());
                aRow.createCell(3).setCellValue(item.getResponseTime().toString());
                aRow.createCell(4).setCellValue(item.getStatusId());
                aRow.createCell(5).setCellValue(item.getDistributor().getId());
                aRow.createCell(6).setCellValue(item.getMerchant().getId());
                aRow.createCell(7).setCellValue(item.getServiceProvider().getId());
                aRow.createCell(8).setCellValue(item.getTerminal().getId());
                aRow.createCell(9).setCellValue(item.getType().getId());
                aRow.createCell(10).setCellValue(item.getPointOfSale().getId());
                aRow.createCell(11).setCellValue(item.getInvoicingStatus());
                aRow.createCell(12).setCellValue(item.getStatusId());

            }
        }


}
