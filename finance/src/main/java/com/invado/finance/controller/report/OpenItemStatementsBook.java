/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.controller.report;

import com.invado.finance.service.dto.OpenItemStatementsDTO;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author bdragan
 */
public class OpenItemStatementsBook extends Book {

    private static PageFormat pageFormat;

    public OpenItemStatementsBook(LocalDate datumKreiranjaIOS,
            LocalDate datumValuta,
            Locale l,
            List<OpenItemStatementsDTO> dto) {
        if (pageFormat == null) {
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
            pageFormat = new PageFormat();
            pageFormat.setPaper(paper);
        }
        for (int i = 0, n = dto.size(); i < n; i++) {
            OpenItemStatementsReport ios = new OpenItemStatementsReport(
                    datumKreiranjaIOS, 
                    datumValuta, 
                    dto.get(i), 
                    getNumberOfPages(),
                    l
            );
            append(ios, pageFormat, ios.getNumberOfPages(pageFormat));
        }
    }

    //    // <editor-fold defaultstate="collapsed" desc="Main method">
//    public static void main(String[] args) throws Exception {
//        PrikaziIOSDTO dto = new PrikaziIOSDTO();
//        dto.datum = new Date();
//        dto.idPreduzeca = 2;
//        dto.sifraKonta = "433000";
//        dto.maticniBroj = "1";
//        dto.p = dto.p.ANALITIKA;
//        List<IOSDTO> listaIOSDTO = Stub.getMaticniPodaciStub().prikaziIOS(dto);
//        SPPrintPreview p = new SPPrintPreview();
//        p.addPages(new OpenItemStatementsBook(new Date(), dto.datum, listaIOSDTO));
//        JFrame f = new JFrame("IOS pregled stampe");
//        f.add(p);
//        f.setVisible(true);
//        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
    //}// </editor-fold>
}
