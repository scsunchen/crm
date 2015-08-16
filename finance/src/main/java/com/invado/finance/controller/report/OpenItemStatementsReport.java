/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller.report;


import com.invado.finance.service.dto.OpenItemStatementsDTO;
import java.awt.*;
import java.awt.font.*;
import java.awt.print.*;
import java.text.*;
import java.util.*;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.invado.finance.Utils.getMessage;
import java.time.format.FormatStyle;
/**
 *
 * @author bdragan
 */
public class OpenItemStatementsReport implements Printable {

    static final int HEADER_HEIGHT = 170;
    static final int DETAILS_HEADER_HEIGHT = 15;
    static final int SUMMARY_HEIGHT = 45;
    static final int FOOTER_HEIGHT = 115;
    static final int BANDS_DELIMITER = 10;
    public static final String FONT_NAME = "Lucida Sans Regular";
    
    private List<Integer> detailBreaks;
    private final DateTimeFormatter dateFormat;
    private final OpenItemStatementsDTO dto;
    private final LocalDate valueDate;
    private final LocalDate printDate;
    private final DecimalFormat nf;
    private Integer startPage;
    private Integer pageNumber;
    private Float detailEndHeight;

    private DecimalFormat getNumberFormat(Locale e) {
        DecimalFormat dec = (DecimalFormat) NumberFormat.getCurrencyInstance(e);
        DecimalFormatSymbols symbols = dec.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        dec.setDecimalFormatSymbols(symbols);
        return dec;
    }

    OpenItemStatementsReport(
            LocalDate printDate,
            LocalDate valueDate,
            OpenItemStatementsDTO dto,
            Integer startPage,
            Locale locale) {
        this.dto = dto;
        this.valueDate = valueDate;
        this.printDate = printDate;
        this.startPage = startPage;
        this.dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(locale);
        this.nf = getNumberFormat(locale);
    }

    int getNumberOfPages(PageFormat pf) {
        Graphics2D g = (Graphics2D) new java.awt.image.BufferedImage(2, 2, java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics();
        try {
            this.print(g, pf, -1);
        } catch (PrinterException ex) {
        }
        return pageNumber + 1;
    }

    @Override
    public int print(Graphics graphics, PageFormat pf, int pageIndex)
            throws PrinterException {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setFont(new Font(FONT_NAME, Font.PLAIN, 10));
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.setColor(Color.BLACK);

        if (detailBreaks == null) {
            this.initDetailsBreak(g2d, pf);
        }
        int page = pageIndex - startPage;
        if (page > pageNumber || pageIndex == -1) {
            return NO_SUCH_PAGE;
        }

        int detailsHeaderStart = 0;
        float detailHeight = 0;
        int detailStart = 0;
        if (page == 0) {//prva strana stavi zaglavlje
            drawHeader(g2d, pf);//205
            detailsHeaderStart = HEADER_HEIGHT + BANDS_DELIMITER;
        }
        if (page <= detailBreaks.size()) {
            drawDetailsHeader(g2d, detailsHeaderStart, (float) pf.getImageableWidth());
            detailStart = detailsHeaderStart + DETAILS_HEADER_HEIGHT;
        }
        detailHeight = drawDetails(g2d,
                detailStart,
                page,
                (float) pf.getImageableWidth());
        if (page == pageNumber && pageNumber == detailBreaks.size()) {
            drawSummary(g2d, detailStart + detailHeight, (float) pf.getImageableWidth());
            drawFooter(g2d, detailStart + detailHeight + SUMMARY_HEIGHT + BANDS_DELIMITER, (float) pf.getImageableWidth());
        }
        //futer i suma ne mogu da stanu na istu stranu kao i items
        // a) futer prelazi na sledecu stranu
        // b) suma i futer prelaze na sledecu stranu
        if (pageNumber > detailBreaks.size()) {
            if (page == detailBreaks.size()
                    && (detailEndHeight - SUMMARY_HEIGHT) > 0) {
                drawSummary(g2d, detailStart + detailHeight, (float) pf.getImageableWidth());
            }
            if (page == pageNumber && (detailEndHeight - SUMMARY_HEIGHT) > 0) {
                drawFooter(g2d, 0, (float) pf.getImageableWidth());
            }
            if (page == pageNumber && (detailEndHeight - SUMMARY_HEIGHT) < 0) {
                drawDetailsHeader(g2d, 0, (float) pf.getImageableWidth());
                drawSummary(g2d, DETAILS_HEADER_HEIGHT, (float) pf.getImageableWidth());
                drawFooter(g2d, DETAILS_HEADER_HEIGHT + SUMMARY_HEIGHT + BANDS_DELIMITER,
                        (float) pf.getImageableWidth());
            }
        }
        return PAGE_EXISTS;
    }

    private void drawFooter(Graphics2D g,
            float footerStart,
            double pageWidth) {
        FontMetrics m = g.getFontMetrics();
        float userSignatureHeight = 20;
        float creditorColumnStart = 5;
        float creditorColumnEnd = (float) (pageWidth / 2) - 10;
        float debtorColumnStart = (float) (pageWidth / 2) + 10;
        float debtorColumnEnd = (float) pageWidth - 5;

        float creditorColumnHeight = m.getMaxAscent();
        creditorColumnHeight += m.getLeading() + m.getMaxDescent();
        creditorColumnHeight += userSignatureHeight;
        g.drawLine((int) creditorColumnStart,
                (int) (footerStart + creditorColumnHeight),
                (int) creditorColumnEnd,
                (int) (footerStart + creditorColumnHeight));
        creditorColumnHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.CreditorSignature"),
                creditorColumnStart, footerStart + creditorColumnHeight);
        creditorColumnHeight += m.getLeading() + m.getMaxDescent();

        creditorColumnHeight += userSignatureHeight;
        g.drawLine((int) creditorColumnStart,
                (int) (footerStart + creditorColumnHeight),
                (int) creditorColumnEnd,
                (int) (footerStart + creditorColumnHeight));
        String datum = printDate == null ? "" : dateFormat.format(printDate);
        g.drawString(dto.creditorPlace + " " + datum,
                creditorColumnStart,
                footerStart + creditorColumnHeight - m.getDescent());
        creditorColumnHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.Mesto"),
                creditorColumnStart, footerStart + creditorColumnHeight);

        float debtorColumnHeight = m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.Sender"),
                creditorColumnStart, footerStart + debtorColumnHeight);
        g.drawString(getMessage("OpenStatements.Potvrda"),
                debtorColumnStart, footerStart + debtorColumnHeight);
        debtorColumnHeight += m.getLeading() + m.getMaxDescent();

        debtorColumnHeight += userSignatureHeight;
        g.drawLine((int) debtorColumnStart,
                (int) (footerStart + debtorColumnHeight),
                (int) debtorColumnEnd,
                (int) (footerStart + debtorColumnHeight));
        debtorColumnHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.DebtorSignature"),
                debtorColumnStart, footerStart + debtorColumnHeight);
        debtorColumnHeight += m.getLeading() + m.getMaxDescent();
        debtorColumnHeight += userSignatureHeight;
        g.drawLine((int) debtorColumnStart,
                (int) (footerStart + debtorColumnHeight),
                (int) debtorColumnEnd,
                (int) (footerStart + debtorColumnHeight));
        debtorColumnHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.Mesto"),
                debtorColumnStart, footerStart + debtorColumnHeight);
        String s = getMessage("OpenStatements.Napomena");
        float textStart = footerStart + creditorColumnHeight + 15;
        final Map<TextAttribute, Object> map = new HashMap<>();
        map.put(TextAttribute.FONT, g.getFont());
        AttributedString as = new AttributedString(s, map);
        AttributedCharacterIterator it = as.getIterator();
        LineBreakMeasurer lb = new LineBreakMeasurer(it, g.getFontRenderContext());
        float dy = 0;
        TextLayout tl;
        int start = 0;
        while (lb.getPosition() < it.getEndIndex()) {
            if (lb.nextOffset((float) pageWidth) == it.getEndIndex()) {
                tl = lb.nextLayout((float) pageWidth);
            } else {
                tl = lb.nextLayout((float) pageWidth).getJustifiedLayout((float) pageWidth);
            }

            dy += tl.getAscent();
            g.drawString(s.substring(start, start + tl.getCharacterCount()),
                    0, textStart + dy);
            start = start + tl.getCharacterCount();
//      tl.draw(g, 0, textStart + dy);
            dy += tl.getDescent() + tl.getLeading();
        }
    }

    private void initDetailsBreak(Graphics2D g,
            PageFormat pf) {
        int firstPageDetailStart = HEADER_HEIGHT + BANDS_DELIMITER + DETAILS_HEADER_HEIGHT;
        
        detailBreaks = new ArrayList<>();
        float linesCountHeight = 0;
        int detailHeight = (int) (pf.getImageableHeight() - firstPageDetailStart);
        
        for (int i = 0; i < dto.items.size(); i++) {
            float lineHeight = getLineHeight(pf, g, dto.items.get(i));
            linesCountHeight = linesCountHeight + lineHeight;
            if (linesCountHeight >= detailHeight) {
                detailHeight = (int) pf.getImageableHeight() - DETAILS_HEADER_HEIGHT;//next page
                linesCountHeight = lineHeight;
                detailBreaks.add(i);
            }
        }
        if ((linesCountHeight + SUMMARY_HEIGHT + BANDS_DELIMITER + FOOTER_HEIGHT) > detailHeight) {
            pageNumber = detailBreaks.size() + 1;
        } else {
            pageNumber = detailBreaks.size();
        }
        detailEndHeight = detailHeight - linesCountHeight;
    }
    
    private float getLineHeight(PageFormat pf,
                                Graphics2D g2d,
                                OpenItemStatementsDTO.Item item) {
        
        AttributedCharacterIterator col1 = new AttributedString(item.document,
                g2d.getFont().getAttributes()).getIterator();
        LineBreakMeasurer lb1 = new LineBreakMeasurer(
                col1,
                g2d.getFontRenderContext()
        );
        float result = 0;
        result = g2d.getFontMetrics().getMaxAscent();
        boolean firstLine = true;
        float documentWidth = (float) (pf.getImageableWidth() * 35 /100);
        while (lb1.getPosition() < col1.getEndIndex()) {
            TextLayout tl = lb1.nextLayout(documentWidth);
            if(firstLine == false) {
                result += (tl.getAscent() + tl.getDescent() + tl.getLeading());
            }
            firstLine = false;
        }
        result += (g2d.getFontMetrics().getDescent() 
                + g2d.getFontMetrics().getLeading());
        return result;
    }
    
    //ok
    private void drawHeader(Graphics2D g,
            PageFormat pf) {
        FontMetrics m = g.getFontMetrics();
        float headerHeight = 0;
        float imageableWidth = (float) pf.getImageableWidth();
        //POVERILAC
        double halfPageWigth = pf.getImageableWidth() / 2;
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 11));
        headerHeight += m.getMaxAscent();
        AttributedString duznikAS = new AttributedString(getMessage("OpenStatements.Print.Debtor"));
        duznikAS.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, 6);
        g.drawString(duznikAS.getIterator(), (int) halfPageWigth, headerHeight);
        AttributedString poverilacAS = new AttributedString(
                getMessage("OpenStatements.Print.Creditor"));
        poverilacAS.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, 9);
        g.drawString(poverilacAS.getIterator(), 0, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 10));

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.Print.Id"), (int) halfPageWigth, headerHeight);
        g.drawString(dto.creditorCompanyID == null ? "" : dto.creditorCompanyID,
                (int) halfPageWigth + m.stringWidth(getMessage("OpenStatements.Print.Id")),
                headerHeight);
        g.drawString(getMessage("OpenStatements.Print.Id"), 0, headerHeight);
        g.drawString((dto.debtorCompanyID == null) ? "" : dto.debtorCompanyID,
                m.stringWidth(getMessage("OpenStatements.Print.Id")),
                headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.Print.VatId"), 0, headerHeight);
        g.drawString(dto.creditorTIN == null ? "" : dto.creditorTIN,
                m.stringWidth(getMessage("OpenStatements.Print.VatId")), headerHeight);
        g.drawString(getMessage("OpenStatements.Print.Name"), (int) halfPageWigth, headerHeight);
        g.drawString(dto.debtorName == null ? "" : dto.debtorName,
                (int) halfPageWigth + m.stringWidth(getMessage("OpenStatements.Print.Name")),
                headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.Print.Phone"), 0, headerHeight);
        g.drawString(dto.creditorPhone == null ? "" : dto.creditorPhone,
                m.stringWidth(getMessage("OpenStatements.Print.Phone")),
                headerHeight);
        g.drawString(getMessage("OpenStatements.Print.VatId"), (int) halfPageWigth, headerHeight);
        g.drawString(dto.debtorTIN == null ? "" : dto.debtorTIN,
                (int) halfPageWigth + m.stringWidth(getMessage("OpenStatements.Print.VatId")),
                headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.Print.Account"), 0, headerHeight);
        g.drawString(dto.creditorCurrentAccount == null ? "" : dto.creditorCurrentAccount,
                m.stringWidth(getMessage("OpenStatements.Print.Account")),
                headerHeight);
        g.drawString(getMessage("OpenStatements.Print.Address"), (int) halfPageWigth,
                headerHeight);
        g.drawString(dto.debtorStreet == null ? "" : dto.debtorStreet + " "
                + dto.debtorPostCode == null ? "" : dto.debtorPostCode + " "
                + dto.debtorPlace == null ? "" : dto.debtorPlace,
                (int) halfPageWigth + m.stringWidth(getMessage("OpenStatements.Print.Address")),
                headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("OpenStatements.Print.Address"), 0, headerHeight);
        g.drawString(dto.creditorStreet == null ? "" : dto.creditorStreet + " "
                + dto.creditorPostCode == null ? "" : dto.creditorPostCode + " "
                + dto.creditorPlace == null ? "" : dto.creditorPlace,
                m.stringWidth(getMessage("OpenStatements.Print.Address")),
                headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += 40;

        g.setFont(g.getFont().deriveFont(Font.PLAIN, 11));
        headerHeight += m.getMaxAscent();
        String title = getMessage("OpenStatements.Print.Title",
                dto.accountNumber, dateFormat.format(valueDate));
        g.drawString(title, (imageableWidth - g.getFontMetrics().stringWidth(title)) / 2, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 10));
    }

    //ok
    private void drawDetailsHeader(Graphics2D g,
            float detailsHeaderStart,
            double pageWidth) {
        FontMetrics m = g.getFontMetrics();
        g.drawRect(0, (int) (detailsHeaderStart), (int) pageWidth, 15);

        float detailHeaderHeight = m.getHeight();
        g.drawString(getMessage("OpenStatements.Print.Ordinal"),
                (float) (pageWidth * 9 / 100 - m.stringWidth(getMessage("OpenStatements.Print.Ordinal"))),
                detailsHeaderStart + detailHeaderHeight);
        g.drawString(getMessage("OpenStatements.Print.Document"),
                (float) (pageWidth * 10.75 / 100),
                detailsHeaderStart + detailHeaderHeight);
        g.drawString(getMessage("OpenStatements.Print.MaturityDate"),
                (float) (pageWidth * 46.5 / 100),
                detailsHeaderStart + detailHeaderHeight);
        g.drawString(getMessage("OpenStatements.Print.Debit"),
                (float) (pageWidth * 79.25 / 100 - m.stringWidth(getMessage("OpenStatements.Print.Debit"))),
                detailsHeaderStart + detailHeaderHeight);
        g.drawString(getMessage("OpenStatements.Print.Credit"),
                (float) (pageWidth - m.stringWidth(getMessage("OpenStatements.Print.Credit"))),
                detailsHeaderStart + detailHeaderHeight);
    }

    private float drawDetails(Graphics2D g,
            int detailStart,
            int pageIndex,
            float pageWidth) {
        FontMetrics m = g.getFontMetrics();
        if (pageIndex > detailBreaks.size()) {
            return 0;
        }
        int start = pageIndex == 0 ? 0 : detailBreaks.get(pageIndex - 1);
        int end = pageIndex == detailBreaks.size() ? dto.items.size() : detailBreaks.get(pageIndex);
        float detailHeight = 0;
        OpenItemStatementsDTO.Item sDTO = null;
        for (int i = start; i < end; i++) {
            sDTO = dto.items.get(i);
            detailHeight = detailHeight + m.getMaxAscent();
            //10
            g.drawString(String.valueOf(sDTO.ordinalNumber),
                    pageWidth * 9 / 100 - m.stringWidth(
                    String.valueOf(sDTO.ordinalNumber)),
                    detailStart + detailHeight);
            
//document can be wider than 10.75% of pagewidth so determine*******************
//line break and draw line by line**********************************************
            AttributedCharacterIterator col1 = new AttributedString(
                    sDTO.document,
                    g.getFont().getAttributes())
                    .getIterator();
            LineBreakMeasurer lb1 = new LineBreakMeasurer(
                    col1,
                    g.getFontRenderContext());
            int dy = 0;
            int firstCharIndex = 0;
            while (lb1.getPosition() < col1.getEndIndex()) {
                float documentWrappingWidth = (float) (pageWidth * 35 / 100);
                TextLayout tl = lb1.nextLayout(documentWrappingWidth);
                int lastCharIndex = firstCharIndex + tl.getCharacterCount();
                //if it's not first line or last line add ascent of TextLayout 
                //to dy
                if (firstCharIndex > 0) {
                    dy += tl.getAscent();
                }
                String substring = sDTO.document.substring(
                        firstCharIndex,
                        lastCharIndex);
                g.drawString(substring,
                             (float) (pageWidth * 10.75 / 100),
                             (int) (detailStart + detailHeight + dy)
                );
                //if it's not last line  of document add descent and leading 
                //height to dy
                if( lastCharIndex != (sDTO.document.length()) ) {
                    dy += (tl.getDescent() + tl.getLeading());
                }
                firstCharIndex += tl.getCharacterCount();
            }            
//******************************************************************************
            //15
            g.drawString(dateFormat.format(sDTO.valueDate),
                    (float) (pageWidth * 46.5 / 100),
                    detailStart + detailHeight);
            //20
            g.drawString(nf.format(sDTO.debit),
                    (float) (pageWidth * 79.25 / 100 - m.stringWidth(nf.format(sDTO.debit))),
                    detailStart + detailHeight);
            //20
            g.drawString(nf.format(sDTO.credit),
                    (float) (pageWidth - m.stringWidth(nf.format(sDTO.credit))),
                    detailStart + detailHeight);
            detailHeight += m.getMaxDescent() + m.getLeading() + dy;
        }
        return detailHeight;
    }

//ok
    private void drawSummary(Graphics2D g2d,
            float summaryStart,
            float pageWidth) {
        FontMetrics m = g2d.getFontMetrics();
        float summaryHeight = 0;
        g2d.setColor(Color.lightGray);
        summaryHeight += m.getMaxAscent();
        g2d.drawLine(0, (int) (summaryStart + summaryHeight), (int) pageWidth,
                (int) (summaryStart + summaryHeight));
        summaryHeight += (m.getMaxDescent() + m.getLeading());

        g2d.setColor(Color.BLACK);
        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("OpenStatements.Print.Sum"),
                (float) (pageWidth * 62 / 100 - m.stringWidth(getMessage("OpenStatements.Print.Sum"))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.totalDebit),
                (float) (pageWidth * 79.25 / 100 - m.stringWidth(nf.format(dto.totalDebit))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.totalCredit),
                (float) (pageWidth - m.stringWidth(nf.format(dto.totalCredit))),
                summaryStart + summaryHeight);
        summaryHeight += m.getMaxDescent() + m.getLeading();
        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("OpenStatements.Print.Balance"),
                (float) (pageWidth * 62 / 100 - m.stringWidth(getMessage("OpenStatements.Print.Balance"))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.balanceDebit),
                (float) (pageWidth * 79.25 / 100 - m.stringWidth(nf.format(dto.balanceDebit))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.balanceCredit),
                (float) (pageWidth - m.stringWidth(nf.format(dto.balanceCredit))),
                summaryStart + summaryHeight);
        summaryHeight += m.getMaxDescent() + m.getLeading();
    }

//<editor-fold defaultstate="collapsed" desc="Main test method">
//    public static void main(String[] args) throws Exception {
//
//        OpenItemStatementsDTO item = new OpenItemStatementsDTO();
//        item.debtorStreet = "";
//        item.creditorStreet = "";
//        item.debtorPlace = "";
//        item.creditorPlace = "";
//        item.debtorCompanyID = "";
//        item.creditorCompanyID = "";
//        item.debtorName = "";
//        item.accountName = "";
//        item.creditorName = "";
//        item.debtorTIN = "";
//        item.creditorTIN = "";
//        item.debtorPostCode = "";
//        item.creditorPostCode = "";
//        item.accountNumber = "";
//        item.balanceDebit = BigDecimal.valueOf(1);
//        item.balanceCredit = BigDecimal.valueOf(1);
//        for (int i = 0; i < 600; i++) {
//            OpenItemStatementsDTO.Item stavka = new OpenItemStatementsDTO.Item();
//            if(i %3 == 0 ) {
//                stavka.document = "1234567890123456123456789012345678901234567";
//            } else {
//                stavka.document = "jhsdukahc uksdhuk7";
//            }
//            
//            stavka.debit = BigDecimal.valueOf(0);
//            stavka.credit = BigDecimal.valueOf(0);
//            stavka.ordinalNumber = i;
//            stavka.valueDate = new Date();
//            item.items.add(stavka);
//        }
//        SPPrintPreview p = new SPPrintPreview();
//        IOSKnjiga izvestaj = new IOSKnjiga(new Date(), new Date(), Arrays.asList(item));
//
//        p.addPages(izvestaj);
//        JFrame f = new JFrame("IOS pregled stampe");
//        f.add(p);
//        f.setVisible(true);
//        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
//    }
//</editor-fold>
}