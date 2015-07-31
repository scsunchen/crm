package com.invado.finance.controller.report;


import com.invado.finance.service.dto.PartnerSpecificationDTO;
import com.invado.finance.service.dto.StavkaSpecifikacijeDTO;
import java.awt.*;
import java.awt.font.*;
import java.awt.print.*;
import java.text.*;
import java.util.*;
import java.util.List;
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
public class SpecificationReport implements Printable {

    public static final int HEADER_HEIGHT = 41;
    public static final int BANDS_DELIMITER = 10;
    public static final int COLUMN_HEADER_HEIGHT = 80;
    public static final int SUMMARY_HEIGHT = 28;
    public static final String FONT_NAME = "Lucida Sans Regular";
    
    private List<Integer> breaks;
    private int pageNumber;
    private final DateTimeFormatter dateFormat;
    private final DecimalFormat nf;
    private final String username;
    private final int startIndex;
    private final PartnerSpecificationDTO dto;
    private static final PageFormat PAGE_FORMAT = getFormat();
    
    private static PageFormat getFormat() {
        Paper paper = new Paper();
        final double margin = 36;
        final double paperWidth =
                MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4).getX(MediaSize.INCH) * 72;
        final double paperHeight =
                MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4).getY(MediaSize.INCH) * 72;
        paper.setImageableArea(margin,
                margin,
                paperWidth - (2 * margin),
                paperHeight - (2 * margin));
        paper.setSize(paperWidth, paperHeight);

        PageFormat pageFormat = new PageFormat();
        pageFormat.setOrientation(PageFormat.LANDSCAPE);
        pageFormat.setPaper(paper);
        return pageFormat;
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

    public SpecificationReport(
            PartnerSpecificationDTO dto, 
            int startPageIndex, 
            String username, 
            Locale e) {
        this.startIndex = startPageIndex;
        this.dto = dto;                
        this.username = username;                
        this.dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(e);
        this.nf = getNumberFormat(e);
    }

    @Override
    public int print(Graphics graphics, 
                     PageFormat format, 
                     int pageIndex) 
                     throws PrinterException {
        Graphics2D g2d = (Graphics2D) graphics;
        graphics.setFont(new Font(FONT_NAME, Font.PLAIN, 10));

        float columnHeaderStart = HEADER_HEIGHT + BANDS_DELIMITER;
        float detailStart = columnHeaderStart + COLUMN_HEADER_HEIGHT;

        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                             RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (breaks == null) {
            initDetailBreaks(g2d, format, detailStart);
        }
        g2d.translate(format.getImageableX(), format.getImageableY());
        graphics.setColor(Color.BLACK);

        int page = pageIndex - startIndex;
        if (page > pageNumber || pageIndex == -1) {
            return NO_SUCH_PAGE;
        }
        this.drawHeader(g2d, format, page + 1);
        this.drawColumnHeader(g2d, columnHeaderStart, format.getImageableWidth());
        if (page == pageNumber) {
            float detailEnd = this.drawDetails(g2d, 
                                               detailStart, 
                                               page, 
                                               (float) format.getImageableWidth());
            this.drawSummary(g2d, detailStart + detailEnd, (float) format.getImageableWidth());
        } else {
            this.drawDetails(g2d, detailStart, page, (float) format.getImageableWidth());
        }

        return PAGE_EXISTS;
    }

    private void initDetailBreaks(Graphics2D g, PageFormat pf, float detailStart) {
        int detailHeight = (int) (pf.getImageableHeight() - detailStart);//719
        breaks = new ArrayList<>();
        float linesCountHeight = 0;
        float lineHeight;
        for (int i = 0; i < dto.items.size(); i++) {
            lineHeight = getLineHeight(g, dto.items.get(i), pf.getImageableWidth());
            linesCountHeight += lineHeight;
            if (linesCountHeight >= detailHeight) {
                breaks.add(i);
                linesCountHeight = lineHeight;
            }
        }
        if (linesCountHeight + SUMMARY_HEIGHT > detailHeight) {
            pageNumber = breaks.size() + 1;
        } else {
            pageNumber = breaks.size();
        }
    }

    private float getLineHeight(Graphics2D g, 
                                StavkaSpecifikacijeDTO line, 
                                double pageWidth) {
        FontMetrics m = g.getFontMetrics();
        if (m.stringWidth(line.businessPartnerName) < pageWidth * 36.75 / 100) {
            return m.getHeight();
        }
        final Map<TextAttribute, Object> mapa = new HashMap<>();
        mapa.put(TextAttribute.FONT, g.getFont());
        AttributedCharacterIterator it = new AttributedString(line.businessPartnerName, mapa).getIterator();
        LineBreakMeasurer lb = new LineBreakMeasurer(it, g.getFontRenderContext());
        float height = 0;
        while (lb.getPosition() < it.getEndIndex()) {
            TextLayout tl = lb.nextLayout((float) (pageWidth * 36.75 / 100));
            height += (tl.getAscent() + tl.getDescent() + tl.getLeading());
        }
        return height;
    }

    private void drawHeader(Graphics2D g, PageFormat pf, int page) {
        FontMetrics m = g.getFontMetrics();

        float headerHeight = m.getMaxAscent();
        g.drawString(getMessage("PartnerSpecification.Print.Title"), 
                     0, 
                     headerHeight);
        String pageText = getMessage("PartnerSpecification.Print.Page", 
                                              page);
        g.drawString(pageText, (float) (pf.getImageableWidth() - m.stringWidth(pageText)), headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        String datum = dateFormat.format(LocalDate.now());
        g.drawString(dto.clientName, 0, headerHeight);
        g.drawString(datum, (float) (pf.getImageableWidth() - m.stringWidth(datum)), headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("PartnerSpecification.Print.User", username), 0, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());
    }

    private void drawColumnHeader(
            Graphics2D g,
            float columnHeaderStart,
            double pageWidth) {
        FontMetrics m = g.getFontMetrics();
        
        float columnHeaderHeight = m.getMaxAscent();
        if(dto.orgUnitID != null) {
            g.drawString(getMessage("PartnerSpecification.Print.HeaderOrgUnit",
                                             dto.orgUnitName),
                    0,
                    columnHeaderStart + columnHeaderHeight);
        } else {
            g.drawString(getMessage("PartnerSpecification.Print.AllOrgUnits"),
                    0,
                    columnHeaderStart + columnHeaderHeight);
        }
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        
        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("PartnerSpecification.Print.Account", 
                                         dto.accountNumber),
                0,
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        
        columnHeaderHeight += m.getMaxAscent();
        if(dto.partnerID != null && dto.partnerID.isEmpty() == false) {
             g.drawString(getMessage("PartnerSpecification.Print.Partner", 
                                         dto.partnerName),
                0,
                columnHeaderStart + columnHeaderHeight);
        } else {
             g.drawString(getMessage("PartnerSpecification.Print.AllPartners"),
                0,
                columnHeaderStart + columnHeaderHeight);
        }
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        
        columnHeaderHeight += m.getMaxAscent();
        if (dto.creditDebitRelationDateFrom == null && dto.creditDebitRelationDateTo == null) {
            g.drawString(getMessage("PartnerSpecification.Print.AllCreditDebitRelationDates"),
                    0,
                    columnHeaderStart + columnHeaderHeight);
        } else {
             String creditDebitRelationDate = getMessage(
                    "PartnerSpecification.Print.CreditDebitRelationDate"
            );            
            if(dto.creditDebitRelationDateFrom != null) {
                creditDebitRelationDate = creditDebitRelationDate.concat(
                        getMessage(
                            "PartnerSpecification.Print.CreditDebitRelationDateFrom",
                            dto.creditDebitRelationDateFrom
                        )
                );
            }
            if(dto.creditDebitRelationDateTo != null) {
                creditDebitRelationDate = creditDebitRelationDate.concat(
                        getMessage(
                            "PartnerSpecification.Print.CreditDebitRelationDateTo", 
                            dto.creditDebitRelationDateTo
                        )
                );                                
            }
            g.drawString(creditDebitRelationDate,
                         0,
                         columnHeaderStart + columnHeaderHeight);
        }
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());
        columnHeaderHeight += m.getMaxAscent();
        if(dto.valueDateFrom == null && dto.valueDateTo == null) {
            g.drawString(getMessage("PartnerSpecification.Print.ValueDateAll"),
                    0,
                    columnHeaderStart + columnHeaderHeight);            
        } else {
            String valueDateLabel = getMessage(
                    "PartnerSpecification.Print.ValueDate"
            );            
            if(dto.valueDateFrom != null) {
                valueDateLabel = valueDateLabel.concat(getMessage(
                        "PartnerSpecification.Print.ValueDateFrom", 
                        dto.valueDateFrom)
                );
            }
            if(dto.valueDateTo != null) {
                valueDateLabel = valueDateLabel.concat(getMessage(
                        "PartnerSpecification.Print.ValueDateTo", 
                        dto.valueDateTo)
                );                                
            }
            g.drawString(valueDateLabel,
                         0,
                         columnHeaderStart + columnHeaderHeight);
        }
        columnHeaderHeight += (m.getMaxDescent() + m.getLeading());

        int tableHeaderHeight = 15;
        g.drawRect(0,
                (int) (columnHeaderStart + columnHeaderHeight),
                (int) pageWidth,
                tableHeaderHeight);
        columnHeaderHeight += m.getHeight();
        g.drawString(getMessage("PartnerSpecification.Print.OrgUnit"),
                0,
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("PartnerSpecification.Print.BusinessPartner"),
                (float) (pageWidth * 2.75 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("PartnerSpecification.Print.BusinessPartnerName"),
                (float) (pageWidth * 16.25 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("PartnerSpecification.Print.Debit"),
                (float) ((pageWidth * 76.5 / 100) - m.stringWidth(getMessage("PartnerSpecification.Print.Debit"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("PartnerSpecification.Print.Credit"),
                (float) ((pageWidth * 88.25 / 100) - m.stringWidth(getMessage("PartnerSpecification.Print.Credit"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("PartnerSpecification.Print.Balance"),
                (float) (pageWidth - m.stringWidth(getMessage("PartnerSpecification.Print.Balance"))),
                columnHeaderStart + columnHeaderHeight);

        columnHeaderHeight += tableHeaderHeight;
    }

    private float drawDetails(Graphics2D g2d,
            float detailStart,
            int pageIndex,
            float pageWidth) {
        FontMetrics m = g2d.getFontMetrics();
        if (pageIndex > breaks.size()) {
            return 0;
        }
        int start = pageIndex == 0 ? 0 : breaks.get(pageIndex - 1);
        int end = pageIndex == breaks.size() ? dto.items.size() : breaks.get(pageIndex);
        float detailHeight = m.getMaxAscent();
        StavkaSpecifikacijeDTO sDTO = null;
        for (int i = start; i < end; i++) {
            sDTO = dto.items.get(i);
            g2d.drawString(String.valueOf(sDTO.idOrgJedinice),
                    (float) (pageWidth * 2 / 100 - m.stringWidth(String.valueOf(sDTO.idOrgJedinice))),
                    detailStart + detailHeight);
            g2d.drawString(sDTO.businessPartnerRegNo,
                    (float) (pageWidth * 2.75 / 100),
                    detailStart + detailHeight);
            drawMultiLineText(g2d,
                    sDTO.businessPartnerName,
                    (float) (pageWidth * 16.25 / 100),
                    detailStart + detailHeight,
                    (float) (36.75 * pageWidth / 100));
            g2d.drawString(nf.format(sDTO.debit),
                    (float) (pageWidth * 76.50 / 100 - m.stringWidth(nf.format(sDTO.debit))),
                    detailStart + detailHeight);
            g2d.drawString(nf.format(sDTO.credit),
                    (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(sDTO.credit))),
                    detailStart + detailHeight);
            g2d.drawString(nf.format(sDTO.balance),
                    (float) (pageWidth - m.stringWidth(nf.format(sDTO.balance))),
                    detailStart + detailHeight);
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
            dy += (tl.getAscent() + tl.getDescent() + tl.getLeading());
        }
    }

    private void drawSummary(Graphics2D g2d,
            float summaryStart,
            float pageWidth) {
        FontMetrics m = g2d.getFontMetrics();
        g2d.setColor(Color.lightGray);
        float summaryHeight = 0;
        g2d.drawLine(0, (int) (summaryStart + summaryHeight), (int) pageWidth,
                (int) (summaryStart + summaryHeight));
        summaryHeight += (m.getMaxDescent() + m.getLeading());

        g2d.setColor(Color.BLACK);
        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("PartnerSpecification.Print.Total", 
                dto.accountNumber),
                (float) (pageWidth * 53.5 / 100 - m.stringWidth(getMessage("PartnerSpecification.Print.Total", dto.accountNumber))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.debitTotal),
                (float) (pageWidth * 76.50 / 100 - m.stringWidth(nf.format(dto.debitTotal))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.creditTotal),
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(dto.creditTotal))),
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.balanceTotal),
                (float) (pageWidth - m.stringWidth(nf.format(dto.balanceTotal))),
                summaryStart + summaryHeight);
        summaryHeight += (m.getMaxDescent() + m.getLeading());
    }

//  private void drawReportSummary(Graphics2D g2d,
//                                 float summaryStart,
//                                 float pageWidth,
//                                 BigDecimal debitTotal,
//                        BigDecimal creditTotal,
//                        BigDecimal saldoDug,
//                        BigDecimal saldoPotr) {
//    FontMetrics m = g2d.getFontMetrics();
//    float summaryHeight = 0;
//    g2d.drawLine(0, (int) (summaryStart + summaryHeight), (int) pageWidth, (int) (summaryStart + summaryHeight));
//    summaryHeight += (m.getMaxDescent() + m.getLeading());
//
//    summaryHeight += m.getMaxAscent();
//    g2d.drawString("Ukupno :",
//            (float) (pageWidth * 53.5 / 100 - m.stringWidth("Ukupno :")),
//            summaryStart + summaryHeight);
//    g2d.drawString(String.valueOf(debitTotal),
//            (float) (pageWidth * 64.75 / 100 - m.stringWidth(String.valueOf(debitTotal))),
//            summaryStart + summaryHeight);
//    g2d.drawString(String.valueOf(creditTotal),
//            (float) (pageWidth * 76.50 / 100 - m.stringWidth(String.valueOf(creditTotal))),
//            summaryStart + summaryHeight);
//    g2d.drawString(String.valueOf(saldoDug),
//            (float) (pageWidth * 88.25 / 100 - m.stringWidth(String.valueOf(saldoDug))),
//            summaryStart + summaryHeight);
//    g2d.drawString(String.valueOf(saldoPotr),
//            (float) (pageWidth - m.stringWidth(String.valueOf(saldoPotr))),
//            summaryStart + summaryHeight);
//    summaryHeight += (m.getMaxDescent() + m.getLeading());
//  }
    
    public int getNumberOfPages(PageFormat pf) {
        Graphics2D g = (Graphics2D) new java.awt.image.BufferedImage(2, 2,
                java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics();
        try {
            this.print(g, pf, -1);
        } catch (PrinterException ex) {
        }
        return pageNumber + 1;
    }
}
