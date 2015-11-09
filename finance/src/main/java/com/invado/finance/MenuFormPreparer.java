/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance;

import com.invado.core.spi.Module;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        finance.put("Fakturisanje", "Artikal", "/finance/item/read-page.html?code=&name=&page=0");
        finance.put("Fakturisanje", "Faktura", "/finance/invoice/read-page.html?document=&partnerName=&partnerId=&dateFrom=&dateTo=&page=0");
        finance.put("Knjigovodstvo", "Opis", "/finance/desc/0");
        finance.put("Knjigovodstvo", "Tip naloga", "/finance/journal-entry-type/0");
        finance.put("Knjigovodstvo", "Konto", "/finance/account/0");
        finance.put("Knjigovodstvo", "Nalog za knjiženje", "/finance/journal-entry/0");
        finance.put("Knjigovodstvo izveštaj", "Analitička kartica kupaca", "/finance/receivable-payable-card/read-customer.html");
        finance.put("Knjigovodstvo izveštaj", "Analitička kartica dobavljača", "/finance/receivable-payable-card/read-supplier.html");
        finance.put("Knjigovodstvo izveštaj", "Analitička kartica glavne knjige", "/finance/gl-card/read-general-ledger.html");
        finance.put("Knjigovodstvo izveštaj", "Otvorene stavke", "/finance/open-item-statements/read.html");
        finance.put("Knjigovodstvo izveštaj", "Specifikacija poslovnih partnera", "/finance/partner-specification/read.html");
//        finance.put("Zatezna kamata", "Obračun zatezne kamate", "/finance/desc/0");
        modules.add(finance);
        Module crm = new Module();
        crm.setPath("crm");
        crm.setName("CRM");
        crm.put("Proba", "Proba", "/crm/proba/0");
        modules.add(crm);
        Module service = new Module();
        service.setPath("service");
        service.setName("Servis aparata");
        service.put("Proba", "Proba", "/service/proba/0");
        modules.add(service);
    }

    @Override
    public void execute(Request request, AttributeContext ac) {
        HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
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
