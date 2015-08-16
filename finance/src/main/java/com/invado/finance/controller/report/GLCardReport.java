/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller.report;

import com.invado.finance.service.dto.LedgerCardDTO;
import com.invado.finance.service.dto.LedgerCardItemDTO;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.print.*;
import java.text.*;
import java.util.*;
import java.util.List;//awt paket ima klasu List!!
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import static com.invado.finance.Utils.getMessage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
/**
 *
 * @author bdragan
 */
public class GLCardReport implements Printable {

    private static final int HEADER_HEIGHT = 41;
    private static final int BANDS_DELIMITER = 10;
    private static final int COLUMN_HEADER_HEIGHT = 82;
    private static final int SUMMARY_HEIGHT = 28;
    private static final PageFormat PAGE_FORMAT = getFormat();
    
    public static final String FONT_NAME = "Lucida Sans Regular";
    
    private final LedgerCardDTO dto;
    private final DateTimeFormatter dateFormat;
    private final DecimalFormat decimal;
    private final String username;
    private final int startPage;
    private List<Integer> detailBreaks;    
    private int pageNumber;

    private static PageFormat getFormat() {
        Paper paper = new Paper();
        final double margin = 36;
        final double paperWidth =
                MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4)
                .getX(MediaSize.INCH) * 72;
        final double paperHeight =
                MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4)
                .getY(MediaSize.INCH) * 72;
        paper.setImageableArea(margin,
                margin,
                paperWidth - (2 * margin),
                paperHeight - (2 * margin));
        paper.setSize(paperWidth, paperHeight);
        PageFormat result = new PageFormat();
        result.setOrientation(PageFormat.LANDSCAPE);
        result.setPaper(paper);
        return result;
    }

    public static PageFormat getPageFormat() {
        return PAGE_FORMAT;
    }

    private DecimalFormat getNumberFormat(Locale e) {
        DecimalFormat dec = (DecimalFormat) NumberFormat.getCurrencyInstance(e);
        DecimalFormatSymbols symbols = dec.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        dec.setDecimalFormatSymbols(symbols);
        return dec;
    }

    public GLCardReport(LedgerCardDTO dto, int startPageIndex, String username, Locale locale) {
        this.dto = dto;
        this.startPage = startPageIndex;
        this.username = username;
        this.dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(locale);
        this.decimal = getNumberFormat(locale);
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) 
            throws PrinterException {
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font(FONT_NAME, Font.PLAIN, 10));

        float columnHeaderStart = HEADER_HEIGHT + BANDS_DELIMITER;
        float detailStart = columnHeaderStart + COLUMN_HEADER_HEIGHT;

        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (detailBreaks == null) {
            initDetailBreaks(g2d, pf, detailStart);
        }
        int page = pageIndex - startPage;

        if (page > pageNumber || pageIndex == -1) {
            return NO_SUCH_PAGE;
        }

        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g.setColor(Color.BLACK);

        this.drawHeader(g2d, pf, page + 1);//ok
        this.drawColumnHeader(g2d, columnHeaderStart, pf.getImageableWidth());//ok
        if (page == pageNumber) {
            float detailEnd = this.drawDetails(detailStart, page, (float) pf.getImageableWidth(), g2d);
            this.drawSummary(g2d, detailStart + detailEnd, (float) pf.getImageableWidth());
        } else {
            this.drawDetails(detailStart, page, (float) pf.getImageableWidth(), g2d);
        }
        return PAGE_EXISTS;
    }

    private void initDetailBreaks(Graphics2D g, PageFormat pf, float detailStart) {
        int detailHeight = (int) (pf.getImageableHeight() - detailStart);//719
        detailBreaks = new ArrayList<>();
        float linesCountHeight = 0;
        float lineHeight;
        for (int i = 0; i < dto.items.size(); i++) {
            lineHeight = getLineHeight(g, dto.items.get(i), pf.getImageableWidth());
            linesCountHeight = linesCountHeight + lineHeight;
            if (linesCountHeight >= detailHeight) {
                detailBreaks.add(i);
                linesCountHeight = lineHeight;
            }
        }
        if ((linesCountHeight + SUMMARY_HEIGHT) > detailHeight) {
            pageNumber = detailBreaks.size() + 1;
        } else {
            pageNumber = detailBreaks.size();
        }
    }

    private float getLineHeight(Graphics2D g, LedgerCardItemDTO line, double pageWidth) {
        FontMetrics m = g.getFontMetrics();
        if (m.stringWidth(line.description) < pageWidth * 13 / 100
                && m.stringWidth(line.document + " " + line.internalDocument) < pageWidth * 13 / 100) {

            return m.getHeight();
        }
        return Math.max(getMultilineStringHeight(g, line.description, pageWidth * 13 / 100),
                getMultilineStringHeight(g, line.document + " " + line.internalDocument, pageWidth * 13 / 100));
    }

    private float getMultilineStringHeight(Graphics2D g, String s, double lineWidth) {
        final Map<TextAttribute, Object> mapa = new HashMap<>();
        mapa.put(TextAttribute.FONT, g.getFont());
        AttributedCharacterIterator it = new AttributedString(s, mapa).getIterator();
        LineBreakMeasurer lb = new LineBreakMeasurer(it, g.getFontRenderContext());
        float height = 0;
        while (lb.getPosition() < it.getEndIndex()) {
            TextLayout tl = lb.nextLayout((float) lineWidth);
            height += (tl.getAscent() + tl.getDescent() + tl.getLeading());
        }
        return height;
    }

    private void drawHeader(Graphics2D g,
            PageFormat pf,
            int page) {
        FontMetrics m = g.getFontMetrics();
        float headerHeight = 0;
        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.GLedgerTitle"), 0, headerHeight);
        String pageText = getMessage("LedgerCard.Print.Page", page);
        g.drawString(pageText, (float) (pf.getImageableWidth() - m.stringWidth(pageText)), headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        String date = dateFormat.format(LocalDate.now());
        g.drawString(dto.clientName, 0, headerHeight);
        g.drawString(date, (float) (pf.getImageableWidth() - m.stringWidth(date)), headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.User", username), 0, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());
    }

    private void drawColumnHeader(
            Graphics2D g,
            float columnHeaderStart,
            double pageWidth) {
        FontMetrics m = g.getFontMetrics();
        float columnHeaderHeight = 0;
        String allText = getMessage("LedgerCard.Print.All");
        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.Account")
                + (dto.accountNumber == null ? allText : dto.accountNumber + "  " + dto.accountName),
                0, columnHeaderStart + columnHeaderHeight);

        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.OrgUnit")
                + (dto.orgUnitID == null ? allText : dto.orgUnitID + "  " + dto.orgUnitName),
                0, columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.CreditDebitRelationDate")
                + (dto.creditDebitRelationDateFrom == null ? "" : dateFormat.format(dto.creditDebitRelationDateFrom))
                + ".." + (dto.creditDebitRelationDateTo == null ? "" : dateFormat.format(dto.creditDebitRelationDateTo)),
                0,
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        
        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.Value")
                + (dto.valueDateFrom == null ? "" : dateFormat.format(dto.valueDateFrom))
                + ".." + (dto.valueDateTo == null ? "" : dateFormat.format(dto.valueDateTo)),
                0,
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        
        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.Currency",
                dto.currencyISOCode),
                0,
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        int tableHeaderHeight = 15;
        g.drawRect(0, (int) (columnHeaderStart + columnHeaderHeight), (int) pageWidth, tableHeaderHeight);

        columnHeaderHeight += (m.getHeight());
        g.drawString(getMessage("LedgerCard.Print.RecordDate"),
                0f,
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.JournalEntry"),
                (float) (pageWidth * 9.25 / 100),
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.Ordinal"),
                (float) (pageWidth * 19 / 100 - m.stringWidth(getMessage("LedgerCard.Print.Ordinal"))),
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.Date"),
                (float) (pageWidth * 19.75 / 100),
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.ValueDate"),
                (float) (pageWidth * 29 / 100),
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.Desc"),
                (float) (pageWidth * 38.25 / 100),
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.Document"),
                (float) (pageWidth * 52 / 100),
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.Debit"),
                (float) (pageWidth * 76.5 / 100 - m.stringWidth(getMessage("LedgerCard.Print.Debit"))),
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.Credit"),
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(getMessage("LedgerCard.Print.Credit"))),
                (float) (columnHeaderStart + columnHeaderHeight));
        g.drawString(getMessage("LedgerCard.Print.Balance"),
                (float) (pageWidth - m.stringWidth(getMessage("LedgerCard.Print.Balance"))),
                (float) (columnHeaderStart + columnHeaderHeight));

        columnHeaderHeight += tableHeaderHeight;
    }

    private float drawDetails(float detailStart,
            int pageIndex,
            float pageWidth,
            Graphics2D g2d) {
        FontMetrics m = g2d.getFontMetrics();
        if (pageIndex > detailBreaks.size()) {
            return 0;
        }
        int start = pageIndex == 0 ? 0 : detailBreaks.get(pageIndex - 1);
        int end = pageIndex == detailBreaks.size() ? dto.items.size() : detailBreaks.get(pageIndex);
        float detailHeight = m.getMaxAscent();
        LedgerCardItemDTO sDTO = null;
        for (int i = start; i < end; i++) {
            sDTO = dto.items.get(i);
            g2d.drawString(dateFormat.format(sDTO.journalEntryDate),
                    0,
                    detailStart + detailHeight);
            g2d.drawString(getJournalEntryDesc(sDTO.typeID, sDTO.journalEntryNumber),
                    (float) (pageWidth * 9.25 / 100),
                    detailStart + detailHeight);
            g2d.drawString(String.valueOf(sDTO.ordinal),
                    (float) (pageWidth * 19 / 100 - m.stringWidth(String.valueOf(sDTO.ordinal))),
                    detailStart + detailHeight);
            g2d.drawString(dateFormat.format(sDTO.creditDebitRelationDate),
                    (float) (pageWidth * 19.75 / 100),
                    detailStart + detailHeight);
            g2d.drawString(dateFormat.format(sDTO.valueDate),
                    (pageWidth * 29 / 100),
                    detailStart + detailHeight);
            drawMultiLineText(g2d,
                    (sDTO.description == null ? "" : sDTO.description),
                    (float) (pageWidth * 38.25 / 100),
                    detailStart + detailHeight,
                    pageWidth * 13 / 100);
            drawMultiLineText(g2d,
                    (sDTO.document == null ? "" : sDTO.document) + " " + (sDTO.internalDocument == null ? "" : sDTO.internalDocument),
                    (pageWidth * 52 / 100),
                    detailStart + detailHeight,
                    pageWidth * 13 / 100);
            g2d.drawString(decimal.format(sDTO.debit),
                    (float) (pageWidth * 76.5 / 100 - m.stringWidth(decimal.format(sDTO.debit))),
                    detailStart + detailHeight);
            g2d.drawString(decimal.format(sDTO.credit),
                    (float) (pageWidth * 88.25 / 100 - m.stringWidth(decimal.format(sDTO.credit))),
                    detailStart + detailHeight);
            g2d.drawString(decimal.format(sDTO.balance),
                    (float) (pageWidth - m.stringWidth(decimal.format(sDTO.balance))),
                    detailStart + detailHeight);
            detailHeight = detailHeight + getLineHeight(g2d, sDTO, pageWidth);
        }
        return detailHeight;
    }

    private void drawMultiLineText(Graphics2D g, String s, float x, float y, float lineWidth) {
        if (g.getFontMetrics().stringWidth(s) < lineWidth) {
            g.drawString(s, x, y);
            return;
        }
        final Map<TextAttribute, Object> mapa = new HashMap<>();
        mapa.put(TextAttribute.FONT, g.getFont());
        AttributedCharacterIterator it = new AttributedString(s, mapa).getIterator();
        LineBreakMeasurer lb = new LineBreakMeasurer(it, g.getFontRenderContext());
        int dy = 0;
        int start = 0;
        while (lb.getPosition() < it.getEndIndex()) {
            TextLayout tl = lb.nextLayout(lineWidth);
            g.drawString(s.substring(start, start + tl.getCharacterCount()),
                    x, y + dy);
            start = start + tl.getCharacterCount();
            dy += (tl.getAscent() + tl.getDescent() + tl.getLeading());
        }
    }

    private void drawSummary(Graphics2D g2d,
            float summaryStart,
            float pageWidth) {
        FontMetrics m = g2d.getFontMetrics();
        g2d.setColor(Color.lightGray);
        float summaryHeight = 0;
        g2d.drawLine(0, (int) (summaryStart + summaryHeight), (int) pageWidth, (int) (summaryStart + summaryHeight));
        summaryHeight += (m.getMaxDescent() + m.getLeading());
        g2d.setColor(Color.BLACK);
        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("LedgerCard.Print.Sum"),
                (float) (pageWidth * 65.5 / 100 - m.stringWidth(getMessage("LedgerCard.Print.Sum"))),
                summaryStart + summaryHeight);
        g2d.drawString(decimal.format(dto.debitTotal),
                (float) (pageWidth * 76.5 / 100 - m.stringWidth(decimal.format(dto.debitTotal))),
                summaryStart + summaryHeight);
        g2d.drawString(decimal.format(dto.creditTotal),
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(decimal.format(dto.creditTotal))),
                summaryStart + summaryHeight);
        g2d.drawString(decimal.format(dto.balanceTotal),
                (float) (pageWidth - m.stringWidth(decimal.format(dto.balanceTotal))),
                summaryStart + summaryHeight);
        summaryHeight += (m.getMaxDescent() + m.getLeading());
    }

    private String getJournalEntryDesc(Integer idTipa, Integer broj) {
        return Integer.toString(idTipa).concat("/").concat(Integer.toString(broj));
    }

    public int getNumberOfPages(PageFormat pf) {
        Graphics2D g = (Graphics2D) new java.awt.image.BufferedImage(2, 2, java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics();
        try {
            this.print(g, pf, -1);//samo da kreira breaks listu
        } catch (PrinterException ex) {
        }
        return pageNumber + 1;
    }
    
}