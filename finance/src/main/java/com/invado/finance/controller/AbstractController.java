/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller;

import com.itextpdf.awt.FontMapper;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author bdragan
 */
abstract class AbstractController {
    
    static final String FREE_SANS_FONT_PATH = "/com/invado/finance/font/FreeSans.otf";
    
    byte[] getPDFFile(Pageable pageable, FontMapper mapper) throws Exception {
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
    }
    
}
