package com.invado.finance.controller.report;

import com.invado.finance.service.dto.LedgerCardDTO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import static com.invado.finance.Utils.getMessage;
import com.invado.finance.service.dto.LedgerCardItemDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
/**
 *
 * @author Bobic Dragan
 */
public class ReceivablePayableCardReport implements Printable {

    private static final int HEADER_HEIGHT = 41;
    private static final int BANDS_DELIMITER = 10;
    private static final int COLUMN_HEADER_HEIGHT = 108;
    private static final int SUMMARY_HEIGHT = 18;
    private static final int KONTO_SUMMARY_HEIGHT = 19;
    private static final PageFormat PAGE_FORMAT = getFormat();
    public static final String FONT_NAME = "Lucida Sans Regular";

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
    private List<Integer> breaks;
    private int pageNumber;
    private final DecimalFormat nf;
    private final DateTimeFormatter dateFormat;
    private final LedgerCardDTO reportDTO;
    private final int startIndex;
    private final String username;

    private DecimalFormat getNumberFormat(Locale e) {
        DecimalFormat dec = (DecimalFormat) NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = dec.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        dec.setDecimalFormatSymbols(symbols);
        return dec;
    }

    public ReceivablePayableCardReport(LedgerCardDTO dto, 
                                int index, 
                                String username, 
                                Locale e) {
        this.reportDTO = dto;
        this.startIndex = index;
        this.nf = getNumberFormat(e);
        this.dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(e);
        this.username = username;
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

        if (breaks == null) {
            initDetailBreaks(g2d, pf, detailStart);
        }

        int page = pageIndex - startIndex;

        if (page > pageNumber || pageIndex == -1) {
            return NO_SUCH_PAGE;
        }

        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g.setColor(Color.BLACK);

        this.drawHeader(g2d, pf, page + 1, reportDTO);
        this.drawColumnHeader(g2d, columnHeaderStart, pf.getImageableWidth());
        if (page == pageNumber) {
            float detailEnd = this.drawDetails(
                    g2d, 
                    detailStart, 
                    page,
                    (float) pf.getImageableWidth());
            this.drawSummary(g2d,
                    detailStart + detailEnd,
                    (float) pf.getImageableWidth());
        } else {
            this.drawDetails(g2d,
                    detailStart,
                    page,
                    (float) pf.getImageableWidth());
        }
        return PAGE_EXISTS;
    }

    private void drawSummary(
            Graphics2D g2d, 
            float summaryStart,
            float pageWidth) {
        FontMetrics m = g2d.getFontMetrics();
        float summaryHeight = 0;
        g2d.drawLine(0, (int) (summaryStart + summaryHeight), (int) pageWidth,
                (int) (summaryStart + summaryHeight));
        summaryHeight += (m.getMaxDescent() + m.getLeading());
        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("LedgerCard.Print.Sum"),
                (float) (pageWidth * 65.5 / 100 - m.stringWidth(getMessage("LedgerCard.Print.Sum"))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(reportDTO.debitTotal),
                (float) (pageWidth * 76.5 / 100 - m.stringWidth(nf.format(reportDTO.debitTotal))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(reportDTO.creditTotal),
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(reportDTO.creditTotal))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(reportDTO.balanceTotal),
                (float) (pageWidth - m.stringWidth(nf.format(reportDTO.balanceTotal))),
                summaryStart + summaryHeight);
        summaryHeight += (m.getMaxDescent() + m.getLeading());
    }

    private void initDetailBreaks(Graphics2D g, PageFormat pf, float detailStart) {
        int detailHeight = (int) (pf.getImageableHeight() - detailStart);//719
        breaks = new ArrayList<>();
        float linesCountHeight = 0;
        float lineHeight;
        String sifraKonta = "";
        for (int i = 0, n = reportDTO.items.size(); i < n; i++) {
            String sifraKonta1 = reportDTO.items.get(i).accountNumber;
            lineHeight = getLineHeight(g, reportDTO.items.get(i),
                    pf.getImageableWidth());
            linesCountHeight += lineHeight;
            if (linesCountHeight >= detailHeight) {
                breaks.add(i);
                linesCountHeight = lineHeight;
            }
            if ((!sifraKonta1.equals(sifraKonta) && i != 0) || (i == n - 1)) {
                sifraKonta = sifraKonta1;
                linesCountHeight += KONTO_SUMMARY_HEIGHT;
                if (linesCountHeight >= detailHeight) {
                    breaks.add(i);
                    linesCountHeight = KONTO_SUMMARY_HEIGHT;
                }
            }
        }
        if (linesCountHeight + SUMMARY_HEIGHT > detailHeight) {
            pageNumber = breaks.size() + 1;
        } else {
            pageNumber = breaks.size();
        }
    }

    private void drawHeader(Graphics2D g,
            PageFormat pf,
            int page,
            LedgerCardDTO dto) {
        FontMetrics m = g.getFontMetrics();
        float headerHeight = m.getMaxAscent();
        switch (dto.type) {
            case CUSTOMER:
                g.drawString(getMessage("LedgerCard.Print.CutomerTitle"),
                        0,
                        headerHeight);
                break;
            case SUPPLIER:
                g.drawString(getMessage("LedgerCard.Print.SupplierTitle"),
                        0,
                        headerHeight);
                break;
        }
        String pageText = getMessage("LedgerCard.Print.Page", page);
        g.drawString(pageText, (float) (pf.getImageableWidth() - m.stringWidth(pageText)), headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        String datum = dateFormat.format(LocalDate.now());
        g.drawString(dto.clientName, 0, headerHeight);
        g.drawString(datum, (float) (pf.getImageableWidth() - m.stringWidth(datum)), headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.User", username),
                0,
                headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());
    }

 
    private void drawColumnHeader(Graphics2D g,
            float columnHeaderStart,
            double pageWidth) {
        float columnHeaderHeight = 0;
        FontMetrics m = g.getFontMetrics();
        String allText = getMessage("LedgerCard.Print.All");

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.Account")
                + (reportDTO.accountNumber == null ? allText
                : reportDTO.accountNumber + "  " + reportDTO.accountName),
                0, columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.OrgUnit")
                + ((reportDTO.orgUnitID == null) ? allText
                : reportDTO.orgUnitID + "  " + reportDTO.orgUnitName),
                0, columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.PartnerID")
                + (reportDTO.partnerRegistrationNumber == null ? allText
                : reportDTO.partnerRegistrationNumber + " " + reportDTO.partnerName),
                0, columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.Status")
                + (reportDTO.status), 0, columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.CreditDebitRelationDate")
                + (reportDTO.creditDebitRelationDateFrom == null
                ? ""
                : dateFormat.format(reportDTO.creditDebitRelationDateFrom))
                + ".."
                + (reportDTO.creditDebitRelationDateTo == null ? "" : 
                        dateFormat.format(reportDTO.creditDebitRelationDateTo)),
                0,
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        
        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.Value")
                + (reportDTO.valueDateFrom == null
                ? ""
                : dateFormat.format(reportDTO.valueDateFrom))
                + ".."
                + (reportDTO.valueDateTo == null ? "" : dateFormat.format(reportDTO.valueDateTo)),
                0,
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("LedgerCard.Print.Currency",
                reportDTO.currencyISOCode),
                0,
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        int tableHeaderHeight = 15;
        g.drawRect(0, (int) (columnHeaderStart + columnHeaderHeight),
                (int) pageWidth, tableHeaderHeight);
        columnHeaderHeight += m.getHeight();
        g.drawString(getMessage("LedgerCard.Print.RecordDate"),
                0,
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.JournalEntry"),
                (float) (pageWidth * 9.25 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.Ordinal"),
                (float) ((pageWidth * 19 / 100) - m.stringWidth(
                getMessage("LedgerCard.Print.Ordinal"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.Date"),
                (float) (pageWidth * 19.75 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.ValueDate"),
                (float) (pageWidth * 29 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.Desc"),
                (float) (pageWidth * 38.25 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.Document"),
                (float) (pageWidth * 52 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.Debit"),
                (float) ((pageWidth * 76.5 / 100) - m.stringWidth(
                getMessage("LedgerCard.Print.Debit"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.Credit"),
                (float) ((pageWidth * 88.25 / 100) - m.stringWidth(
                getMessage("LedgerCard.Print.Credit"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("LedgerCard.Print.Balance"),
                (float) ((pageWidth) - m.stringWidth(
                getMessage("LedgerCard.Print.Balance"))),
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += tableHeaderHeight;
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

    private float drawDetails(Graphics2D g2d,
            float detailStart,
            int pageIndex,
            float pageWidth) {
        FontMetrics m = g2d.getFontMetrics();
        if (pageIndex > breaks.size()) {
            return 0;
        }
        int start = (pageIndex == 0) ? 0 : breaks.get(pageIndex - 1);
        int end = (pageIndex == breaks.size()) ? reportDTO.items.size()
                : breaks.get(pageIndex);
        float detailHeight = m.getMaxAscent();
        LedgerCardItemDTO sDTO = null;
        for (int i = start; i < end; i++) {
            sDTO = reportDTO.items.get(i);
            g2d.drawString(dateFormat.format(sDTO.journalEntryDate),
                    0,
                    detailStart + detailHeight);
            g2d.drawString(vratiNalog(sDTO.typeID, sDTO.journalEntryNumber),
                    (float) (pageWidth * 9.25 / 100),
                    detailStart + detailHeight);
            g2d.drawString(String.valueOf(sDTO.ordinal),
                    (float) (pageWidth * 19 / 100 - m.stringWidth(
                    String.valueOf(sDTO.ordinal))),
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
                    (sDTO.document == null ? "" : sDTO.document) + " "
                    + (sDTO.internalDocument == null ? "" : sDTO.internalDocument),
                    (pageWidth * 52 / 100),
                    detailStart + detailHeight,
                    pageWidth * 13 / 100);
            g2d.drawString(nf.format(sDTO.debit),
                    (float) (pageWidth * 76.5 / 100 - m.stringWidth(
                    nf.format(sDTO.debit))),
                    detailStart + detailHeight);
            g2d.drawString(nf.format(sDTO.credit),
                    (float) (pageWidth * 88.25 / 100 - m.stringWidth(
                    nf.format(sDTO.credit))),
                    detailStart + detailHeight);
            g2d.drawString(nf.format(sDTO.balance),
                    (float) (pageWidth - m.stringWidth(nf.format(sDTO.balance))),
                    detailStart + detailHeight);

            //da li postoji stavka posle nje nje
            //ako postoji proveri da li su jednake
            if ((reportDTO.items.size() > (i + 1) && sDTO.accountNumber.equals(reportDTO.items.get(i + 1).accountNumber) == false)
                    || (i == reportDTO.items.size() - 1)) {
                this.drawSummary(g2d,
                        detailStart + detailHeight,
                        pageWidth,
                        reportDTO.items.get(i).accountNumber);
                detailHeight += KONTO_SUMMARY_HEIGHT;
            }
            detailHeight = detailHeight + getLineHeight(g2d, sDTO, pageWidth);
        }
        return detailHeight;
    }

    private void drawMultiLineText(Graphics2D g,
            String s,
            float x,
            float y,
            float lineWidth) {
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
//      tl.draw(g, x, y + dy);
            dy += (tl.getAscent() + tl.getDescent() + tl.getLeading());
        }
    }

    private String vratiNalog(Integer idTipa, Integer broj) {
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

    private void drawSummary(Graphics2D g2d,
            float summaryStart,
            float pageWidth,
            String accountCode) {
        FontMetrics m = g2d.getFontMetrics();
        float summaryHeight = m.getMaxDescent() + m.getLeading();
        g2d.setColor(Color.lightGray);
        g2d.drawLine(0, (int) (summaryStart + summaryHeight), (int) pageWidth, (int) (summaryStart + summaryHeight));
        g2d.setColor(Color.BLACK);
        summaryHeight += (m.getMaxDescent() + m.getLeading());
        summaryHeight += m.getMaxAscent();
        String sumTxt = getMessage("LedgerCard.Print.AccountSum",
                accountCode);
        for (LedgerCardDTO.AccountTotal accTotal : reportDTO.accountTotalItems) {
            if (accTotal.accountCode.equals(accountCode)) {
                g2d.drawString(sumTxt,
                        (float) (pageWidth * 65.5 / 100 - m.stringWidth(sumTxt)),
                        summaryStart + summaryHeight);
                g2d.drawString(nf.format(accTotal.debit),
                        (float) (pageWidth * 76.5 / 100 - m.stringWidth(nf.format(accTotal.debit))),
                        summaryStart + summaryHeight);
                g2d.drawString(nf.format(accTotal.credit),
                        (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(accTotal.credit))),
                        summaryStart + summaryHeight);
                g2d.drawString(nf.format(accTotal.balance),
                        (float) (pageWidth - m.stringWidth(nf.format(accTotal.balance))),
                        summaryStart + summaryHeight);
            }
        }
        summaryHeight += (m.getMaxDescent() + m.getLeading());
    }

//    public static void main(String[] args) {
//        LedgerCardDTO dto = new LedgerCardDTO();
//        dto.accountName="fds";
//        dto.accountNumber="fds";
//        dto.clientName="fds";
//        dto.currencyISOCode="fds";
//        dto.orgUnitName="fds";
//        dto.partnerAccount="fds";
//        dto.partnerName="fds";
//        dto.partnerPlace="fds";
//        dto.partnerRegistrationNumber="fds";
//        dto.partnerStreet="fds";
//        dto.status="fds";
//        dto.balanceTotal=BigDecimal.ZERO;
//        dto.creditTotal=BigDecimal.ZERO;
//        dto.debitTotal=BigDecimal.ZERO;
//        dto.currency= CurrencyTypeDTO.DOMESTIC;
//        dto.type = LedgerCardTypeDTO.CUSTOMER;
//        for (int i = 0; i < 100; i++) {
//            LedgerCardItemDTO item = new LedgerCardItemDTO();
//            item.accountNumber="222"+i;
//            item.description="222";
//            item.document="222";
//            item.internalDocument="222";
//            item.creditDebitRelationDate = new Date();
//            item.journalEntryDate = new Date();
//            item.valueDate = new Date();
//            item.balance = BigDecimal.ONE;
//            item.credit = BigDecimal.ONE;
//            item.debit = BigDecimal.ONE;
//            item.orgUnitID = 1;
//            item.ordinal = i;
//            item.typeID = 1;
//            item.journalEntryNumber = 1;
//            dto.items.add(item);
//        }
//        for (int i = 0; i < 100; i++) {
//            LedgerCardDTO.AccountTotal total = new LedgerCardDTO.AccountTotal();
//            total.accountCode = "222"+i;
//            total.balance = BigDecimal.ZERO;
//            total.credit = BigDecimal.ZERO;
//            total.debit = BigDecimal.ZERO;
//            dto.accountTotalItems.add(total);
//        }
//        LoggedInUser sesija = LoggedInUser.getInstance();
//        sesija.setPassword("".toCharArray());//iskljuci time out
//        sesija.setTenant("");
//        sesija.setUsername("");
//        SPPrintPreview p = new SPPrintPreview();
//        Book book = new Book();
//        ReceivablePayableCardReport report = new ReceivablePayableCardReport(
//                dto,
//                0);
//        book.append(report, PAGE_FORMAT, report.getNumberOfPages(PAGE_FORMAT));
//        p.addPages(book);
//        JFrame f = new JFrame("Software Product");
//        f.add(p);
//        f.setVisible(true);
//        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
//    }
}