/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.hr;

import com.invado.core.spi.Module;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author bdragan
 */
public class MenuFormPreparer implements ViewPreparer {

    private final Set<Module> modules;
    private static final String[] DATEPICKER_LANGUAGES = new String[]{
        "ar",
        "az",
        "bg",
        "bs",
        "ca",
        "cs",
        "cy",
        "da",
        "de",
        "el",
        "en-GB",
        "es",
        "et",
        "eu",
        "fa",
        "fi",
        "fo",
        "fr-CH",
        "fr",
        "gl",
        "he",
        "hr",
        "hu",
        "hy",
        "id",
        "is",
        "it-CH",
        "it",
        "ja",
        "ka",
        "kh",
        "kk",
        "kr",
        "lt",
        "lv",
        "me",
        "mk",
        "ms",
        "nb",
        "nl-BE",
        "nl",
        "no",
        "pl",
        "pt-BR",
        "pt",
        "ro",
        "rs-latin",
        "rs",
        "ru",
        "sk",
        "sl",
        "sq",
        "sr-latin",
        "sr",
        "sv",
        "sw",
        "th",
        "tr",
        "uk",
        "vi",
        "zh-CN",
        "zh-TW"
    };

    public MenuFormPreparer() {
        modules = new HashSet<>();
        Module finance = new Module();
        finance.setActive(false);
        finance.setPath("finance");
        finance.setName("Finansije");
        finance.put("Fakturisanje", "Artikal", "/finance/item/0");
        finance.put("Fakturisanje", "Faktura", "/finance/invoice/0");
        modules.add(finance);
        Module masterdata = new Module();
        masterdata.setPath("masterdata");
        masterdata.setName("Maticni Podaci");
        masterdata.put("Opsti sifarnici", "Kompanija Korisnik", "/masterdata/client/0");
        masterdata.put("Opsti sifarnici", "Organizaciona jedinica", "/masterdata/org-unit/0");
        masterdata.put("Opsti sifarnici", "Poslovni partner", "/masterdata/partner/0");
        masterdata.put("Opsti sifarnici", "Opstina", "/masterdata/township/0");
        masterdata.put("Opsti sifarnici", "Banka", "/masterdata/bank/0");
        masterdata.put("Opsti sifarnici", "Status terminala", "/masterdata/devicestatus/0");
        masterdata.put("Opsti sifarnici", "Terminal", "/masterdata/device/0");
        masterdata.put("Opsti sifarnici", "Valuta", "/masterdata/currency/0");
        masterdata.put("Opsti sifarnici", "Kurs", "/core/exchange-rate/0");
        modules.add(masterdata);
        Module service = new Module();
        service.setPath("service");
        service.setName("Servis aparata");
        service.put("Proba", "Proba", "/service/proba/0");
        modules.add(service);
        Module HR = new Module();
        HR.setPath("HR");
        HR.setName("Human Resource");
        HR.put("Human Resource", "Radno mesto", "/HR/job/0");
        HR.put("Human Resource", "Radnik", "/HR/employee/0");
        modules.add(HR);
    }

    @Override
    public void execute(Request  request, AttributeContext ac) {
        HttpServletRequest request1 = ((ServletRequestAttributes) 
                RequestContextHolder.getRequestAttributes())
                .getRequest();
        ac.putAttribute("modules", new Attribute(modules));
        ac.putAttribute(
                "datepickerLanguage",
                new Attribute(resolveBootstrapDatepickerLanguage())
        );
        for (Module module : modules) {
            if (request1.getRequestURI().contains(module.getPath())) {
                ac.putAttribute("selectedModule", new Attribute(module));
            }
        }
    }
    
    private String resolveBootstrapDatepickerLanguage() {
        Locale defaultLocale = LocaleContextHolder.getLocale();
        return Stream.of(DATEPICKER_LANGUAGES)
                .filter(p -> p.equalsIgnoreCase(defaultLocale.getLanguage()))
                .findFirst()
                .orElse("");
    }
}
