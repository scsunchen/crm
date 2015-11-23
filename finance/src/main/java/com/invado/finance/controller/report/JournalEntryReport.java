/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.invado.finance.controller.report;


import com.invado.finance.service.dto.JournalEntryItemDTO;
import com.invado.finance.service.dto.JournalEntryReportDTO;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.print.*;
import java.math.BigDecimal;
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
public class JournalEntryReport implements Printable,Pageable {
    
    public static final int BANDS_DELIMITER = 10;
    public static final int HEADER_HEIGHT = 41;
    public static final int COLUMN_HEADER_HEIGHT = 57;
    public static final int SUMMARY_HEIGHT = 57;
    public static final String FONT_NAME = "Lucida Sans Regular";

    private JournalEntryReportDTO dto;
    private List<Integer> breaks;
    private static PageFormat pageFormat;
    private static DateTimeFormatter dateFormat;
    private int pageNumber;
    private static DecimalFormat nf;
    private String username;
    private Locale locale;
    
    private DecimalFormat getNumberFormat(Locale l) {        
        DecimalFormat dec = (DecimalFormat) NumberFormat.getCurrencyInstance(l);
        DecimalFormatSymbols symbols = dec.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        dec.setDecimalFormatSymbols(symbols);
        return dec;
    }

    public JournalEntryReport(JournalEntryReportDTO dto,
                              String username,
                              Locale locale) {
        if (pageFormat == null) {
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
            pageFormat = new PageFormat();
            pageFormat.setOrientation(PageFormat.LANDSCAPE);
            pageFormat.setPaper(paper);
        }
        if (dateFormat == null) {
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(locale);
        }
        nf = getNumberFormat(locale);
        this.dto = dto;
        this.username = username;
        this.locale = locale;
    }

    @Override
    public int print(Graphics g, 
                     PageFormat pf, 
                     int pageIndex) 
                     throws PrinterException {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font(FONT_NAME, Font.PLAIN, 10));

        float columnHeaderStart = HEADER_HEIGHT + BANDS_DELIMITER;
        float detailStart = columnHeaderStart + COLUMN_HEADER_HEIGHT;

        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, 
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (breaks == null) {
            initReportData((Graphics2D) g, pf, detailStart);
        }
        if (pageIndex > pageNumber || pageIndex == -1) {
            return NO_SUCH_PAGE;
        }

        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g.setColor(Color.BLACK);

        this.drawHeader(g2d, pf, pageIndex + 1);
        this.drawColumnHeader(g2d,
                              columnHeaderStart, 
                              (float) pf.getImageableWidth());
        if (pageIndex == pageNumber) {
            float detailHeight = this.drawDetails(
                    g2d, 
                    detailStart, 
                    pageIndex, 
                    (float) pf.getImageableWidth()
            );
            this.drawSummary(g2d, 
                             detailStart + detailHeight, 
                             (float) pf.getImageableWidth());
        } else {
            this.drawDetails(g2d, 
                             detailStart, 
                             pageIndex, 
                             (float) pf.getImageableWidth());
        }
        return PAGE_EXISTS;
    }

    private void initReportData(Graphics2D g, PageFormat pf, float detailStart) {
        int detailHeight = (int) (pf.getImageableHeight() - detailStart);//719
        breaks = new ArrayList<>();
        float linesCountHeight = 0;
        float lineHeight = 0;
        for (int i = 0; i < dto.items.size(); i++) {
            lineHeight = this.getLineHeight(g,
                    (float) pf.getImageableWidth(), 
                    dto.items.get(i));
            linesCountHeight = linesCountHeight + lineHeight;
            if (linesCountHeight >= detailHeight) {
                breaks.add(i);
                linesCountHeight = lineHeight;
            }
        }
        if ((linesCountHeight + SUMMARY_HEIGHT) > detailHeight) {
            pageNumber = breaks.size() + 1;
        } else {
            pageNumber = breaks.size();
        }
    }
    
    private float getLineHeight(Graphics2D g2d, 
                               float pageWidth, 
                               JournalEntryItemDTO DTO) {
        Map<TextAttribute, Object> map = new HashMap<>();
        map.put(TextAttribute.FONT, g2d.getFont());
        AttributedCharacterIterator iterator;
        LineBreakMeasurer measurer;
        float documentHeight = 0;
        
        if(DTO.getDocument().isEmpty() == false ){
            iterator = new AttributedString(
                    DTO.getDocument(),
                    map).getIterator();
            measurer = new LineBreakMeasurer(iterator,
                    g2d.getFontRenderContext());

            while (measurer.getPosition() < iterator.getEndIndex()) {
                TextLayout tl = measurer.nextLayout((float) (pageWidth * 11.75 / 100));
                documentHeight += (tl.getAscent() + tl.getDescent()
                        + tl.getLeading());
            }
        }
        
        float interDocumentHeight = 0;
        if(DTO.getInternalDocument() != null && DTO.getInternalDocument().isEmpty() == false) {
            iterator = new AttributedString(
                    DTO.getInternalDocument(),
                    map).getIterator();
            measurer = new LineBreakMeasurer(iterator,
                    g2d.getFontRenderContext());

            while (measurer.getPosition() < iterator.getEndIndex()) {
                TextLayout tl = measurer.nextLayout((float) (pageWidth * 11.5 / 100));
                interDocumentHeight += (tl.getAscent() + tl.getDescent() 
                        + tl.getLeading());
            }
        } 
        return Math.max(g2d.getFontMetrics().getHeight(), 
                        Math.max(documentHeight,interDocumentHeight));
    }
    
    //ok
    private void drawHeader(Graphics2D g, PageFormat pf, int page) {
        FontMetrics m = g.getFontMetrics();
        float headerHeight = 0;

        headerHeight += m.getMaxAscent();
        g.drawString(getMessage("JournalEntry.Print.Title"), 0, 
                headerHeight);
        String pageNum = getMessage("JournalEntry.Print.Page",  page);
        g.drawString(pageNum, 
                (float) (pf.getImageableWidth() - m.stringWidth(pageNum)), 
                headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        String date = dateFormat.format(LocalDate.now());
        g.drawString(dto.clientName == null 
                ? "" : dto.clientName, 
                0, headerHeight);
        g.drawString(date, 
                    (float) (pf.getImageableWidth() - m.stringWidth(date)), 
                    headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());

        headerHeight += m.getMaxAscent();
        String user = username;
        g.drawString(getMessage("JournalEntry.Print.User") + user, 0, headerHeight);
        headerHeight += (m.getMaxDescent() + m.getLeading());
    }

    private void drawColumnHeader(Graphics2D g,
            float columnHeaderStart,
            float pageWidth) {
        FontMetrics m = g.getFontMetrics();
        float columnHeaderHeight = 0;
        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("JournalEntry.Print.Type") + dto.typeName, 
                0, columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getLeading() + m.getMaxDescent());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("JournalEntry.Print.Number") + dto.journalEntryNumber
                , 0, columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getLeading() + m.getMaxDescent());

        columnHeaderHeight += m.getMaxAscent();
        g.drawString(getMessage("JournalEntry.Print.PostingDate") + dateFormat.format(dto.date), 0,
                columnHeaderStart + columnHeaderHeight);
        columnHeaderHeight += (m.getLeading() + m.getMaxDescent());

//***********************details header*****************************************

        g.drawRect(0, (int) (columnHeaderStart + columnHeaderHeight), (int) pageWidth, 15);
        columnHeaderHeight += m.getHeight();

        g.drawString(getMessage("JournalEntry.Print.Ordinal"),
                (float) (pageWidth * 4 / 100 - 
                m.stringWidth(getMessage("JournalEntry.Print.Ordinal"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.OrgUnit"),
                (float) (pageWidth * 6.75 / 100 - 
                m.stringWidth(getMessage("JournalEntry.Print.OrgUnit"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Date"),
                (float) (pageWidth * 7.5 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Document"),
                (float) (pageWidth * 16.75 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Desc"),
                (float) (pageWidth * 31 / 100 - 
                m.stringWidth(getMessage("JournalEntry.Print.Desc"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Account"),
                (float) (pageWidth * 31.75 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Document1"),
                (float) (pageWidth * 41.5/ 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Partner"),
                (float) (pageWidth * 53.75 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Value"),
                (float) (pageWidth * 68 / 100),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Debit"),
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(getMessage("JournalEntry.Print.Debit"))),
                columnHeaderStart + columnHeaderHeight);
        g.drawString(getMessage("JournalEntry.Print.Credit"),
                (float) (pageWidth - m.stringWidth(getMessage("JournalEntry.Print.Credit"))),
                columnHeaderStart + columnHeaderHeight);
    }

    private float drawDetails(Graphics2D g2d, 
                              float detailStart, 
                              int pageIndex,
                              float pageWidth) {
        FontMetrics m = g2d.getFontMetrics();
        if (pageIndex > breaks.size()) {
            return 0f;
        }
        int start = pageIndex == 0 ? 0 : breaks.get(pageIndex - 1);
        int end = (pageIndex == breaks.size()) ? 
                dto.items.size() : breaks.get(pageIndex);
        float detailHeight = 0;
        float rowDrawStart = 0f;
        JournalEntryItemDTO itemDTO = null;
        for (int i = start; i < end; i++) {
            itemDTO = dto.items.get(i);
            
            rowDrawStart = detailStart + detailHeight + m.getMaxAscent();
            
            g2d.drawString(String.valueOf(itemDTO.getOrdinalNumber()),
                    pageWidth * 4 / 100 - m.stringWidth(
                    String.valueOf(itemDTO.getOrdinalNumber())),
                    rowDrawStart);
            g2d.drawString(String.valueOf(itemDTO.getUnitId()),
                    (float) (pageWidth * 6.75 / 100 - m.stringWidth(
                    String.valueOf(itemDTO.getUnitId()))),
                    rowDrawStart);
            g2d.drawString(dateFormat.format(itemDTO.getCreditDebitRelationDate()),
                    (float) (pageWidth * 7.5 / 100),
                    rowDrawStart);
            this.drawMultiLineText(g2d,
                    itemDTO.getDocument(),
                    (float) (pageWidth * 16.75 / 100),
                    rowDrawStart,
                    (float) (pageWidth * 11.75 / 100));
            
            g2d.drawString(String.valueOf(itemDTO.getDescId()),
                    (float) (pageWidth * 31 / 100 - m.stringWidth(
                    String.valueOf(itemDTO.getDescId()))),
                    rowDrawStart);
            g2d.drawString(itemDTO.getAccountCode(),
                    (float) (pageWidth * 31.75 / 100),
                    rowDrawStart);
            this.drawMultiLineText(g2d,
                    itemDTO.getInternalDocument() == null ? "" : itemDTO.getInternalDocument(),
                    (float)(pageWidth * 41.5 / 100),
                    rowDrawStart,
                    (float) (pageWidth * 11.5 / 100));
            g2d.drawString(
                    itemDTO.getBusinessPartnerCompanyId()== null 
                    ? "" : itemDTO.getBusinessPartnerCompanyId(),
                    (float) (pageWidth * 53.75 / 100),
                    rowDrawStart);
            g2d.drawString(dateFormat.format(itemDTO.getValueDate()),
                    (float) (pageWidth * 68 / 100),
                    rowDrawStart);
            g2d.drawString(nf.format(itemDTO.getDebit()),
                    (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(itemDTO.getDebit()))),
                    rowDrawStart);            
            g2d.drawString(nf.format(itemDTO.getCredit()),
                    pageWidth - m.stringWidth(nf.format(itemDTO.getCredit())),
                    rowDrawStart);
            detailHeight += this.getLineHeight(g2d, pageWidth, itemDTO);
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
        AttributedCharacterIterator it = new AttributedString(s, mapa)
                .getIterator();
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
        float summaryHeight = 0;
        summaryHeight += 5;
        g2d.setColor(Color.lightGray);
        //10.75 je odokativno
        g2d.drawLine(0, (int) (summaryStart + summaryHeight), (int) pageWidth, (int) (summaryStart + summaryHeight));
        g2d.setColor(Color.BLACK);
        summaryHeight += 5;
        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("JournalEntry.Print.Sum"), 
                (float) (pageWidth * 57 / 100), summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.totalBalance), 
                (float) (pageWidth * 76.5 / 100 - m.stringWidth(nf.format(dto.totalBalance))), 
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.totalDebit), 
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(dto.totalDebit))), 
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.totalCredit), 
                (float) (pageWidth - m.stringWidth(nf.format(dto.totalCredit))), 
                summaryStart + summaryHeight);
        summaryHeight += (m.getLeading() + m.getMaxDescent());


        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("JournalEntry.Print.Ledger"), 
                (float) (pageWidth * 57 / 100), summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.generalLedgerBalance), 
                (float) (pageWidth * 76.5 / 100 - m.stringWidth(nf.format(dto.generalLedgerBalance))), 
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.generalLedgerDebit), 
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(dto.generalLedgerDebit))), 
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.generalLedgerCredit), 
                (float) (pageWidth - m.stringWidth(nf.format(dto.generalLedgerCredit))), 
                summaryStart + summaryHeight);
        summaryHeight += (m.getLeading() + m.getMaxDescent());

        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("JournalEntry.Print.Customer"), 
                (float) (pageWidth * 57 / 100), summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.customerBalance), 
                (float) (pageWidth * 76.5 / 100 - m.stringWidth(nf.format(dto.customerBalance))), 
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.customerDebit), 
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(dto.customerDebit))), 
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.customerCredit), 
                (float) (pageWidth - m.stringWidth(nf.format(dto.customerCredit))), 
                summaryStart + summaryHeight);
        summaryHeight += (m.getLeading() + m.getMaxDescent());

        summaryHeight += m.getMaxAscent();
        g2d.drawString(getMessage("JournalEntry.Print.Supplier"), 
                (float) (pageWidth * 57 / 100), summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.supplierBalance), 
                (float) (pageWidth * 76.5 / 100 - m.stringWidth(nf.format(dto.supplierBalance))), 
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.supplierDebit), 
                (float) (pageWidth * 88.25 / 100 - m.stringWidth(nf.format(dto.supplierDebit))), 
                summaryStart + summaryHeight);
        g2d.drawString(nf.format(dto.supplierCredit), 
                (float) (pageWidth - m.stringWidth(nf.format(dto.supplierCredit))), 
                summaryStart + summaryHeight);
        summaryHeight += (m.getLeading() + m.getMaxDescent());
    }

  // <editor-fold defaultstate="collapsed" desc="Pageable interface implementation">
    @Override
    public int getNumberOfPages() {
        Graphics2D g = (Graphics2D) new java.awt.image.BufferedImage(2, 2, java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics();
        try {
            this.print(g, pageFormat, -1);//samo da kreira breaks listu
        } catch (PrinterException ex) {
        }
        return pageNumber + 1;
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return pageFormat;
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return this;
    }
    // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Main test method">
//  public static void main(String[] args) throws Exception {      
//      SecurityUtils.setSecurityManager(new DefaultSecurityManager());
//      Subject korisnik = SecurityUtils.getSubject();
//      Session sesija = korisnik.getSession();
//      sesija.setTimeout(-1);//iskljuci time out
//      sesija.setAttribute("KorisnickoIme", "admin");
//      sesija.setAttribute("Lozinka", new char[]{'a', 'd','m','i','n','0','9','2','2'}
//      );
//      StampaNalogaDTO dto = new StampaNalogaDTO();
//      dto.brojNaloga = 1;
//      dto.idTipaNaloga = 1;
//      dto.date = new Date();
//      dto.nalogDuguje = BigDecimal.ZERO;
//      dto.nalogPotrazuje = BigDecimal.ZERO;
//      dto.nalogUkupno = BigDecimal.ZERO;
//      dto.nazivPreduzecaKorisnika = "";
//      dto.nazivTipaNaloga = "";
//      
//      dto.dobavljaciDuguje = BigDecimal.ZERO;
//      dto.dobavljaciPotrazuje = BigDecimal.ZERO;
//      dto.dobavljaciUkupno = BigDecimal.ZERO;
//      dto.kupciDuguje = BigDecimal.ZERO;
//      dto.kupciPotrazuje = BigDecimal.ZERO;
//      dto.kupciUkupno = BigDecimal.ZERO;
//      dto.glavnaKnjigaDuguje = BigDecimal.ZERO;
//      dto.glavnaKnjigaPotrazuje = BigDecimal.ZERO;
//      dto.glavnaKnjigaUkupno = BigDecimal.ZERO;
//      
//      for (int i = 0; i < 500; i++) {
//          StavkaKnjizenjaDTO stavka =  new StavkaKnjizenjaDTO();
//          stavka.redniBroj = i;
//          stavka.datumDPO = new Date();
//          stavka.valuta = new Date();
//          stavka.matBrojSaradnika = "10.75";
//          stavka.iznos = BigDecimal.valueOf(2);
//          stavka.tip = 0;
//          if(i%4 == 0 ) {
//              stavka.dokument = "dokument10.7534578915317498791534156789 ";
//          } else {
//              stavka.dokument = "dchsdh cahs";
//          }
//          if(i%3==0) {
//              stavka.interDokument = "dokument10.753457891531749879 1534156789 ";
//          } else {
//              stavka.interDokument = "dokument10.75345789153174";
//          }
//          
//          stavka.nazivKonta = "22";
//          stavka.sifraKonta = "22";
//          
//          stavka.idOpisa = 99;
//          dto.stavke.add(stavka);
//      }
//      SPPrintPreview p = new SPPrintPreview();
//      p.addPages(new JournalEntryReport(dto));
//      JFrame f = new JFrame("IOS pregled stampe");
//      f.add(p);
//      f.setVisible(true);
//      f.setExtendedState(JFrame.MAXIMIZED_BOTH);
//  }
  //</editor-fold>
    
}