/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.report;


import com.invado.core.dto.InvoiceReportDTO;
import com.invado.core.utils.Utils;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;




/**
 * @author root
 */
public class InvoiceReport implements Printable, Pageable {

    private static final int HEADER_HEIGHT = 275;
    private static final int DETAIL_HEIGHT = 240;
    private static final String PRINT_FONT_NAME
            = "Lucida Sans Regular";
    private static final PageFormat A4_PORTRAIT_PAGE_FORMAT;

    static {
        Paper paper = new Paper();
        final double margin = 36;
        final double paperWidth
                = MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4).getX(MediaSize.INCH) * 72;
        final double paperHeight
                = MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4).getY(MediaSize.INCH) * 72;
        paper.setImageableArea(margin,
                margin,
                paperWidth - (2 * margin),
                paperHeight - (2 * margin));
        paper.setSize(paperWidth, paperHeight);
        A4_PORTRAIT_PAGE_FORMAT = new PageFormat();
        A4_PORTRAIT_PAGE_FORMAT.setOrientation(PageFormat.PORTRAIT);
        A4_PORTRAIT_PAGE_FORMAT.setPaper(paper);
    }

    private int pageCount;
    private List<Integer> detailBreak;
    private InvoiceReportDTO dto;
    private DateTimeFormatter date;
    private NumberFormat currency;
    private NumberFormat number;
    private NumberFormat percent;

    public InvoiceReport(InvoiceReportDTO dto) {
        this(dto, Locale.getDefault());
    }

    public InvoiceReport(InvoiceReportDTO dto, Locale locale) {
        detailBreak = new ArrayList<>();
        this.dto = dto;
        this.initPageBreak();
        date = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        currency = this.getCurrencyNoSymbolFormat(locale);
        number = NumberFormat.getNumberInstance(locale);
        percent = NumberFormat.getPercentInstance(locale);
    }

    private void initPageBreak() {
        Font font = new Font(PRINT_FONT_NAME, Font.PLAIN, 10);
        FontRenderContext context = new FontRenderContext(null, true, true);
        float linesCountHeight = 0;
        float lineHeight = 0;
        for (int i = 0; i < dto.items.size(); i++) {
            lineHeight = this.getMultilineHeight(font, context, dto.items.get(i).serviceDesc,
                    (float) (A4_PORTRAIT_PAGE_FORMAT.getImageableWidth() * 32 / 100));
            linesCountHeight += lineHeight;
            if (linesCountHeight >= DETAIL_HEIGHT) {
                detailBreak.add(i);
                linesCountHeight = lineHeight;
                pageCount = pageCount + 1;
            }
        }
    }

    private float getMultilineHeight(Font font, FontRenderContext frc, String text, float dx) {
        AttributedCharacterIterator charIterator = new AttributedString(text,
                font.getAttributes()).getIterator();
        LineBreakMeasurer lb1 = new LineBreakMeasurer(charIterator, frc);
        float dy = 0;
        while (lb1.getPosition() < charIterator.getEndIndex()) {
            TextLayout tl = lb1.nextLayout(dx);
            dy += (tl.getAscent() + tl.getDescent() + tl.getLeading());
        }
        return dy;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex)
            throws PrinterException {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font(PRINT_FONT_NAME, Font.PLAIN, 10));
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        float detailStart = HEADER_HEIGHT;

        if (pageIndex > pageCount || pageIndex == -1) {
            return NO_SUCH_PAGE;
        }
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.setColor(Color.BLACK);
        boolean showRebate = false;
        for (InvoiceReportDTO.Item item : dto.items) {
            if (item.rebatePercent.compareTo(BigDecimal.ZERO) != 0) {
                showRebate = true;
            }
        }
        this.drawHeader(g2d, pf, showRebate);
        if (showRebate == true) {
            this.drawDetails(g2d, detailStart, pageIndex, (float) pf.getImageableWidth());
        } else {
            this.drawDetailsNoRebate(g2d, detailStart, pageIndex, (float) pf.getImageableWidth());
        }

        if (pageIndex == pageCount) {
            float summaryStart = HEADER_HEIGHT + DETAIL_HEIGHT;
            this.drawSummary(g2d, summaryStart, (float) pf.getImageableWidth());
        }
        String s = Utils.getMessage("Invoice.Print.Page", (pageIndex + 1), (pageCount + 1));
        g2d.drawString(s, (int) (pf.getImageableWidth() - g2d.getFontMetrics().stringWidth(s)),
                (int) pf.getImageableHeight() - g2d.getFontMetrics().getHeight());

        return PAGE_EXISTS;
    }

    private void drawHeader(Graphics2D g, PageFormat pf, Boolean showRebate) {
        FontMetrics m = g.getFontMetrics();
        float headerHeight = 10;

        headerHeight += m.getMaxAscent();
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 13));
        g.drawString(dto.clientName,
                0,
                headerHeight);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 10));
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(String.format("%1$s %2$s", dto.clientPost, dto.clientCity),
                0,
                headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(dto.clientAddress, 0, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(dto.clientPhone, 0, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(Utils.getMessage("Invoice.Print.Account", dto.bankAccount),
                0, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(Utils.getMessage("Invoice.Print.TID", dto.clientTIN),
                0, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        if (dto.clientVatCertificateNumber != null
                && dto.clientVatCertificateNumber.equals("") == false) {
            headerHeight += m.getMaxAscent();
            g.drawString(Utils.getMessage("Invoice.Print.VATCertificateId",
                    dto.clientVatCertificateNumber), 0, headerHeight);
            headerHeight += (m.getMaxDescent() + m.getLeading());
        }

        if (dto.printed == true) {
            g.drawString("|", (int) pf.getImageableWidth() - 5, m.getMaxAscent());
        }

        if (dto.clientLogo != null) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(dto.clientLogo);
                BufferedImage image1 = ImageIO.read(bis);
                g.drawImage(image1, (int) (pf.getImageableWidth() - image1.getWidth() - 10), 0, null);
            } catch (IOException e) {
                // TODO : LOG
            }
        }

        float clientTextStart = headerHeight + 70;
        float customerTextStart = headerHeight + 70;

        customerTextStart += m.getMaxAscent();
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 10f));
        g.drawString(Utils.getMessage("Invoice.Print.Customer"),
                0,
                customerTextStart);
        customerTextStart += (m.getMaxDescent() + m.getLeading());

        customerTextStart += m.getMaxAscent();
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 13));
        g.drawString(dto.partnerName != null ? dto.partnerName : "",
                0,
                customerTextStart);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 10));
        customerTextStart += (m.getMaxDescent() + m.getLeading());

        if (getNotNullString(dto.partnerCity).isEmpty() == false
                && getNotNullString(dto.partnerCity).isEmpty() == false) {
            customerTextStart += m.getMaxAscent();
            g.drawString(dto.partnerPost + " " + dto.partnerCity,
                    0,
                    customerTextStart);
            customerTextStart += (m.getMaxDescent() + m.getLeading());
        }

        if (getNotNullString(dto.partnerAddress).isEmpty() == false) {
            customerTextStart += m.getMaxAscent();
            g.drawString(dto.partnerAddress != null ? dto.partnerAddress : "",
                    0,
                    customerTextStart);
            customerTextStart += (m.getMaxDescent() + m.getLeading());
        }

        customerTextStart += m.getMaxAscent();
        g.drawString(Utils.getMessage("Invoice.Print.PartnerTIN",
                dto.partnerTIN),//id nikad nije null
                0,
                customerTextStart);
        customerTextStart += (m.getMaxDescent() + m.getLeading());

        if (getNotNullString(dto.partnerAccount).isEmpty() == false) {
            customerTextStart += m.getMaxAscent();
            g.drawString(Utils.getMessage("Invoice.Print.PartnerAccount",
                    dto.partnerAccount),//nikad nije null
                    0,
                    customerTextStart);
            customerTextStart += (m.getMaxDescent() + m.getLeading());
        }

        float docHeight = 0;
        if (dto.proforma == true) {
            docHeight = this.drawMultilineString(g, Utils.getMessage("Invoice.Print.Proforma", dto.document),
                    (float) pf.getImageableWidth() / 2,
                    clientTextStart + m.getMaxAscent(),
                    (float) pf.getImageableWidth() / 2);
        } else {
            docHeight = this.drawMultilineString(g, Utils.getMessage("Invoice.Print.Invoice", dto.document),
                    (float) pf.getImageableWidth() / 2,
                    clientTextStart + m.getMaxAscent(),
                    (float) pf.getImageableWidth() / 2);
        }

        clientTextStart += docHeight;

        clientTextStart += m.getMaxAscent();
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 10f));
        g.drawString(dto.clientCity + (dto.invoiceDate == null ? ""
                : ", " + date.format(dto.invoiceDate)),
                (float) pf.getImageableWidth() / 2,
                clientTextStart);
        clientTextStart += (m.getMaxDescent() + m.getLeading());

        if (dto.proforma == false) {
            clientTextStart += m.getMaxAscent();
            g.drawString(Utils.getMessage("Invoice.Print.DeliveryDate")
                    + (dto.creditRelationDate == null ? ""
                            : date.format(dto.creditRelationDate)),
                    (float) pf.getImageableWidth() / 2,
                    clientTextStart);
            clientTextStart += (m.getMaxDescent() + m.getLeading());
        }

        if (dto.proforma == false) {
            clientTextStart += m.getMaxAscent();
            g.drawString(Utils.getMessage("Invoice.Print.ValueDate")
                    + (dto.valueDate == null ? ""
                            : date.format(dto.valueDate)),
                    (float) pf.getImageableWidth() / 2,
                    clientTextStart);
            clientTextStart += (m.getMaxDescent() + m.getLeading());
        }
        clientTextStart += m.getMaxAscent();
        g.drawString(Utils.getMessage("Invoice.Print.Currency",
                dto.currencyISOCode),
                (float) pf.getImageableWidth() / 2,
                clientTextStart);

        clientTextStart += (m.getMaxDescent() + m.getLeading());
        if (getNotNullString(dto.contractNumber).isEmpty() == false) {
            clientTextStart += m.getMaxAscent();
            g.drawString(Utils.getMessage("Invoice.Print.ContractNumber",
                    dto.contractNumber),//nikad nije null
                    (float) pf.getImageableWidth() / 2,
                    clientTextStart);
            clientTextStart += (m.getMaxDescent() + m.getLeading());
        }
        if (dto.contractDate != null) {
            clientTextStart += m.getMaxAscent();
            g.drawString(Utils.getMessage("Invoice.Print.ContractDate",
                    date.format(dto.contractDate)),//nikad nije null
                    (float) pf.getImageableWidth() / 2,
                    clientTextStart);
            clientTextStart += (m.getMaxDescent() + m.getLeading());
        }

        final float tableHeaderStart = 260;
        int tableHeaderHeight = 15;

        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(Font.PLAIN));

        float tableHeaderTextHeight = tableHeaderStart + 11;
        if (showRebate == true) {
            //<editor-fold defaultstate="collapsed" desc="comment">
            g.drawString(Utils.getMessage("Invoice.Print.Ordinal"),
                    (float) ((int) pf.getImageableWidth() * 5 / 100
                    - g.getFontMetrics().stringWidth(
                            Utils.getMessage("Invoice.Print.Ordinal"))),
                    tableHeaderTextHeight);
            //32
            g.drawString(Utils.getMessage("Invoice.Print.ServiceName"),
                    (float) ((int) pf.getImageableWidth() * 7 / 100),
                    tableHeaderTextHeight);
            //10
            g.drawString(Utils.getMessage("Invoice.Print.Quantity"),
                    (float) ((int) pf.getImageableWidth() * 49 / 100
                    - g.getFontMetrics().stringWidth(
                            Utils.getMessage("Invoice.Print.Quantity"))),
                    tableHeaderTextHeight);
            //5
            g.drawString(Utils.getMessage("Invoice.Print.UnitOfMeasure"),
                    (float) ((int) pf.getImageableWidth() * 51 / 100),
                    tableHeaderTextHeight);
            //15
            g.drawString(Utils.getMessage("Invoice.Print.ServicePrice"),
                    (float) ((int) pf.getImageableWidth() * 69 / 100
                    - g.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.ServicePrice"))),
                    tableHeaderTextHeight);
            //8
            g.drawString(Utils.getMessage("Invoice.Print.VAT"),
                    (float) ((int) pf.getImageableWidth() * 77 / 100
                    - g.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.VAT")) - 1),
                    tableHeaderTextHeight);
            //8
            g.drawString(Utils.getMessage("Invoice.Print.Rebate"),
                    (float) ((int) pf.getImageableWidth() * 85 / 100
                    - g.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.Rebate")) - 1),
                    tableHeaderTextHeight);
            //15
            g.drawString(Utils.getMessage("Invoice.Print.Total"),
                    (float) ((int) pf.getImageableWidth() - 2
                    - g.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.Total"))),
                    tableHeaderTextHeight);
            g.setFont(g.getFont().deriveFont(Font.PLAIN));

            g.setColor(Color.LIGHT_GRAY);
            double pageWidth = pf.getImageableWidth();
            g.drawLine(0, (int) (tableHeaderStart), (int) pf.getImageableWidth(), (int) tableHeaderStart);
            g.drawLine(0, (int) (tableHeaderStart + tableHeaderHeight), (int) pf.getImageableWidth(),
                    (int) (tableHeaderHeight + tableHeaderStart));
            g.drawLine(0, (int) tableHeaderStart, 0, (int) tableHeaderStart
                    + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 6 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 6 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 40 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 40 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 50 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 50 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 55 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 55 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 69 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 69 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 77 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 77 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 85 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 85 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth, (int) tableHeaderStart, (int) pageWidth,
                    (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine(0, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight, (int) pageWidth,
                    (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            //</editor-fold>
        } else {
            g.drawString(Utils.getMessage("Invoice.Print.Ordinal"),
                    (float) ((int) pf.getImageableWidth() * 5 / 100
                    - g.getFontMetrics().stringWidth(
                            Utils.getMessage("Invoice.Print.Ordinal"))),
                    tableHeaderTextHeight);
            //32
            g.drawString(Utils.getMessage("Invoice.Print.ServiceName"),
                    (float) ((int) pf.getImageableWidth() * 7 / 100),
                    tableHeaderTextHeight);
            //10
            g.drawString(Utils.getMessage("Invoice.Print.Quantity"),
                    (float) ((int) pf.getImageableWidth() * 49 / 100
                    - g.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.Quantity"))),
                    tableHeaderTextHeight);
            //5
            g.drawString(Utils.getMessage("Invoice.Print.UnitOfMeasure"),
                    (float) ((int) pf.getImageableWidth() * 51 / 100),
                    tableHeaderTextHeight);
            //17
            g.drawString(Utils.getMessage("Invoice.Print.ServicePrice"),
                    (float) ((int) pf.getImageableWidth() * 73 / 100
                    - g.getFontMetrics().stringWidth(
                            Utils.getMessage("Invoice.Print.ServicePrice")) - 1),
                    tableHeaderTextHeight);
            //10
            g.drawString(Utils.getMessage("Invoice.Print.VAT"),
                    (float) ((int) pf.getImageableWidth() * 83 / 100
                    - g.getFontMetrics().stringWidth(
                            Utils.getMessage("Invoice.Print.VAT")) - 1),
                    tableHeaderTextHeight);
            //17
            g.drawString(Utils.getMessage("Invoice.Print.Total"),
                    (float) ((int) pf.getImageableWidth() - 2
                    - g.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.Total"))),
                    tableHeaderTextHeight);
            g.setFont(g.getFont().deriveFont(Font.PLAIN));

            g.setColor(Color.LIGHT_GRAY);
            double pageWidth = pf.getImageableWidth();
            g.drawLine(0, (int) (tableHeaderStart), (int) pf.getImageableWidth(), (int) tableHeaderStart);
            g.drawLine(0, (int) (tableHeaderStart + tableHeaderHeight), (int) pf.getImageableWidth(),
                    (int) (tableHeaderHeight + tableHeaderStart));
            g.drawLine(0, (int) tableHeaderStart, 0, (int) tableHeaderStart
                    + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 6 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 6 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 40 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 40 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 50 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 50 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 55 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 55 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 73 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 73 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth * 83 / 100, (int) tableHeaderStart,
                    (int) pageWidth * 83 / 100, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine((int) pageWidth, (int) tableHeaderStart, (int) pageWidth,
                    (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
            g.drawLine(0, (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight, (int) pageWidth,
                    (int) tableHeaderStart + DETAIL_HEIGHT + tableHeaderHeight);
        }

    }

    private String getNotNullString(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    private DecimalFormat getCurrencyNoSymbolFormat(Locale l) {
        DecimalFormat dec = (DecimalFormat) NumberFormat.getCurrencyInstance(l);
        DecimalFormatSymbols symbols = dec.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        dec.setDecimalFormatSymbols(symbols);
        return dec;
    }

    private void drawDetails(
            Graphics2D g2d,
            float detailStart,
            int pageIndex,
            float pageWidth) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 10));//DON'T CHANGE FONT
        FontMetrics m = g2d.getFontMetrics();
        int start;
        if (pageIndex > detailBreak.size()) {
            //ako je poslednja strana koja nema stavki samo suma
            start = dto.items.size();
        } else {
            start = (pageIndex == 0) ? 0 : detailBreak.get(pageIndex - 1);
        }
        int end = (pageIndex >= detailBreak.size()) ? dto.items.size()
                : detailBreak.get(pageIndex);
        float detailHeight = g2d.getFontMetrics().getMaxAscent();

        for (int i = start; i < end; i++) {//ne smes da stavis <= end
            InvoiceReportDTO.Item item = dto.items.get(i);
            g2d.drawString(String.valueOf(item.ordinal),
                    pageWidth * 5 / 100 - m.stringWidth(String.valueOf(item.ordinal)),
                    detailStart + detailHeight);
            float height = this.drawMultilineString(g2d, item.serviceDesc,
                    pageWidth * 7 / 100,
                    detailStart + detailHeight,
                    pageWidth * 32 / 100);
            g2d.drawString(number.format(item.quantity),
                    pageWidth * 50 / 100 - m.stringWidth(number.format(item.quantity)) - 2,
                    detailStart + detailHeight);
            g2d.drawString(item.unitOfMeasure,
                    pageWidth * 50 / 100,
                    detailStart + detailHeight);
            g2d.drawString(currency.format(item.netPrice),
                    pageWidth * 69 / 100 - m.stringWidth(currency.format(item.netPrice)) - 2,
                    detailStart + detailHeight);
            g2d.drawString(percent.format(item.VATPercent),
                    pageWidth * 77 / 100 - m.stringWidth(percent.format(item.VATPercent)) - 2,
                    detailStart + detailHeight);
            g2d.drawString(percent.format(item.rebatePercent),
                    pageWidth * 85 / 100 - m.stringWidth(percent.format(item.rebatePercent)) - 2,
                    detailStart + detailHeight);
            g2d.drawString(currency.format(item.itemTotal),
                    pageWidth - 2 - m.stringWidth(currency.format(item.itemTotal)),
                    detailStart + detailHeight);
            detailHeight = detailHeight + height;
        }
    }

    private void drawDetailsNoRebate(Graphics2D g2d, float detailStart, int pageIndex,
            float pageWidth) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 10));//DON'T CHANGE FONT
        FontMetrics m = g2d.getFontMetrics();
        int start;
        if (pageIndex > detailBreak.size()) {
            //ako je poslednja strana koja nema stavki samo suma
            start = dto.items.size();
        } else {
            start = (pageIndex == 0) ? 0 : detailBreak.get(pageIndex - 1);
        }
        int end = (pageIndex >= detailBreak.size()) ? dto.items.size()
                : detailBreak.get(pageIndex);
        float detailHeight = g2d.getFontMetrics().getMaxAscent();
        for (int i = start; i < end; i++) {//ne smes da stavis <= end
            InvoiceReportDTO.Item item = dto.items.get(i);
            g2d.drawString(String.valueOf(item.ordinal),
                    pageWidth * 5 / 100 - m.stringWidth(String.valueOf(item.ordinal)),
                    detailStart + detailHeight);
            float height = this.drawMultilineString(g2d, item.serviceDesc,
                    pageWidth * 7 / 100,
                    detailStart + detailHeight,
                    pageWidth * 32 / 100);
            g2d.drawString(number.format(item.quantity),
                    pageWidth * 50 / 100 - m.stringWidth(
                            number.format(item.quantity)) - 2,
                    detailStart + detailHeight);
            g2d.drawString(item.unitOfMeasure,
                    pageWidth * 50 / 100,
                    detailStart + detailHeight);
            g2d.drawString(currency.format(item.netPrice),
                    pageWidth * 73 / 100 - m.stringWidth(
                            currency.format(item.netPrice)) - 2,
                    detailStart + detailHeight);
            g2d.drawString(percent.format(item.VATPercent),
                    pageWidth * 83 / 100 - m.stringWidth(
                            percent.format(item.VATPercent)) - 2,
                    detailStart + detailHeight);
            g2d.drawString(currency.format(item.itemTotal),
                    pageWidth - 2 - m.stringWidth(currency.format(item.itemTotal)),
                    detailStart + detailHeight);
            detailHeight = detailHeight + height;
        }
    }

    private float drawMultilineString(Graphics2D g2d, String text, float x,
            float y, float dx) {
        if (text.length() == 0) {
            return g2d.getFontMetrics().getHeight();
        }
        AttributedCharacterIterator charIterator = new AttributedString(text,
                g2d.getFont().getAttributes()).getIterator();
        LineBreakMeasurer lb1 = new LineBreakMeasurer(charIterator,
                g2d.getFontRenderContext());
        float dy = 0;
        int start = 0;
        while (lb1.getPosition() < charIterator.getEndIndex()) {
            TextLayout tl = lb1.nextLayout(dx);
            g2d.drawString(text.substring(start, start + tl.getCharacterCount()),
                    x, y + dy);
            dy += (tl.getAscent() + tl.getDescent() + tl.getLeading());
            start = start + tl.getCharacterCount();
        }
        return dy;
    }

    private void drawSummary(Graphics2D g2d, float summaryStart, float pageWidth) {
        float summaryHeight = 3;//ovo je margina
        g2d.setColor(Color.BLACK);
        summaryHeight += g2d.getFontMetrics().getMaxAscent();
        g2d.drawString(Utils.getMessage("Invoice.Print.ServicePriceTotal"),
                pageWidth * 78 / 100 - g2d.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.ServicePriceTotal")),
                summaryStart + summaryHeight);
        g2d.drawString(currency.format(dto.netPriceTotal),
                pageWidth - g2d.getFontMetrics().stringWidth(currency.format(dto.netPriceTotal)) - 2,
                summaryStart + summaryHeight);
        summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();
        if (dto.rebateTotal.compareTo(BigDecimal.ZERO) != 0) {
            summaryHeight += g2d.getFontMetrics().getMaxAscent();
            g2d.drawString(Utils.getMessage("Invoice.Print.RebateTotal"),
                    pageWidth * 78 / 100 - g2d.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.RebateTotal")),
                    summaryStart + summaryHeight);
            g2d.drawString(currency.format(dto.rebateTotal),
                    pageWidth - g2d.getFontMetrics().stringWidth(currency.format(dto.rebateTotal)) - 2,
                    summaryStart + summaryHeight);
            summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();
        }
//        for (InvoiceReportDTO.VATItem vat : dto.vatitems) {
        if (dto.generalRateBasis.compareTo(BigDecimal.ZERO) != 0) {
            summaryHeight += g2d.getFontMetrics().getMaxAscent();
            String text1 = Utils.getMessage("Invoice.Print.TaxBasis", dto.generalRatePercent);
            g2d.drawString(text1,
                    pageWidth * 78 / 100 - g2d.getFontMetrics().stringWidth(text1),
                    summaryStart + summaryHeight);
            g2d.drawString(currency.format(dto.generalRateBasis),
                    pageWidth - g2d.getFontMetrics().stringWidth(currency.format(dto.generalRateBasis)) - 2,
                    summaryStart + summaryHeight);
            summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();

            summaryHeight += g2d.getFontMetrics().getMaxAscent();
            String text2 = Utils.getMessage("Invoice.Print.Tax", dto.generalRatePercent);
            g2d.drawString(text2,
                    pageWidth * 78 / 100 - g2d.getFontMetrics().stringWidth(text2),
                    summaryStart + summaryHeight);
            g2d.drawString(currency.format(dto.generalRateTax),
                    pageWidth - g2d.getFontMetrics().stringWidth(currency.format(dto.generalRateTax)) - 2,
                    summaryStart + summaryHeight);
            summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();
        }

        if (dto.lowerRateBasis.compareTo(BigDecimal.ZERO) != 0) {
            summaryHeight += g2d.getFontMetrics().getMaxAscent();
            String text1 = Utils.getMessage("Invoice.Print.TaxBasis", dto.lowerRatePercent);
            g2d.drawString(text1,
                    pageWidth * 78 / 100 - g2d.getFontMetrics().stringWidth(text1),
                    summaryStart + summaryHeight);
            g2d.drawString(currency.format(dto.lowerRateBasis),
                    pageWidth - g2d.getFontMetrics().stringWidth(currency.format(dto.lowerRateBasis)) - 2,
                    summaryStart + summaryHeight);
            summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();

            summaryHeight += g2d.getFontMetrics().getMaxAscent();
            String text2 = Utils.getMessage("Invoice.Print.Tax", dto.lowerRatePercent);
            g2d.drawString(text2,
                    pageWidth * 78 / 100 - g2d.getFontMetrics().stringWidth(text2),
                    summaryStart + summaryHeight);
            g2d.drawString(currency.format(dto.lowerRateTax),
                    pageWidth - g2d.getFontMetrics().stringWidth(currency.format(dto.lowerRateTax)) - 2,
                    summaryStart + summaryHeight);
            summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();
        }

        summaryHeight += g2d.getFontMetrics().getMaxAscent();
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 11));
        g2d.drawString(Utils.getMessage("Invoice.Print.Sum"),
                pageWidth * 78 / 100 - g2d.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.Sum")),
                summaryStart + summaryHeight);
        g2d.drawString(currency.format(dto.invoiceTotalAmount),
                pageWidth - g2d.getFontMetrics().stringWidth(currency.format(dto.invoiceTotalAmount)) - 2,
                summaryStart + summaryHeight);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 10));
        summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine((int) pageWidth * 54 / 100, (int) summaryStart, (int) pageWidth * 54 / 100,
                (int) (summaryStart + summaryHeight + 3));
        g2d.drawLine((int) pageWidth, (int) summaryStart, (int) pageWidth,
                (int) (summaryStart + summaryHeight + 3));
        g2d.drawLine((int) pageWidth * 54 / 100, (int) (summaryStart + summaryHeight + 3), (int) pageWidth,
                (int) (summaryStart + summaryHeight + 3));
        g2d.setColor(Color.BLACK);

        summaryHeight += 50;
        if (dto.proforma == true) {
            summaryHeight += g2d.getFontMetrics().getMaxAscent();
            g2d.drawString(
                    Utils.getMessage("Invoice.Print.ProformaValid",
                            date.format(dto.valueDate)),
                    0,
                    summaryStart + summaryHeight);
            return;
        }

        summaryHeight += g2d.getFontMetrics().getMaxAscent();
        g2d.drawString(Utils.getMessage("Invoice.Print.DeliveredBy"),
                0,
                summaryStart + summaryHeight);
        g2d.drawLine(g2d.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.DeliveredBy")),
                (int) (summaryStart + summaryHeight),
                g2d.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.DeliveredBy")) + 150,
                (int) (summaryStart + summaryHeight));
        g2d.drawString(Utils.getMessage("Invoice.Print.ReceivedBy"),
                pageWidth / 2,
                summaryStart + summaryHeight);
        g2d.drawLine(g2d.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.ReceivedBy")) + ((int) pageWidth / 2),
                (int) (summaryStart + summaryHeight),
                g2d.getFontMetrics().stringWidth(Utils.getMessage("Invoice.Print.ReceivedBy")) + ((int) pageWidth / 2) + 150,
                (int) (summaryStart + summaryHeight));
        summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();
        summaryHeight += 35;

        summaryHeight += g2d.getFontMetrics().getMaxAscent();
        this.drawMultilineString(g2d, Utils.getMessage("Invoice.Print.Info"),
                0, summaryStart + summaryHeight, pageWidth);
        summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();
        summaryHeight += g2d.getFontMetrics().getMaxAscent();
        if (dto.isDomesticPartner == true) {
            this.drawMultilineString(g2d, Utils.getMessage("Invoice.Print.NoTaxExemption"),
                    0, summaryStart + summaryHeight, pageWidth);
        } else {
            this.drawMultilineString(g2d, Utils.getMessage("Invoice.Print.TaxExemption"),
                    0, summaryStart + summaryHeight, pageWidth);
        }
        summaryHeight += g2d.getFontMetrics().getMaxDescent() + g2d.getFontMetrics().getLeading();
    }

    @Override
    public int getNumberOfPages() {
        return pageCount + 1;
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return A4_PORTRAIT_PAGE_FORMAT;
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return this;
    }

}
