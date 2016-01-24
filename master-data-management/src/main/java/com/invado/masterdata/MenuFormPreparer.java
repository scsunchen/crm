/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.masterdata;

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
        finance.setPath("finance/home");
        finance.setName("Finansije");
        finance.put("Fakturisanje", "Artikal", "/finance/item/0");
        finance.put("Fakturisanje", "Faktura", "/finance/invoice/0");
        modules.add(finance);

        Module masterdata = new Module();
        masterdata.setPath("masterdata");
        masterdata.setName("Maticni Podaci");

        masterdata.put("Terminali", "Status terminala", "/masterdata/devicestatus/read-page.html?page=0");
        masterdata.put("Terminali", "Terminal", "/masterdata/device/read-page.html?page=0");
        masterdata.put("Terminali", "Zaduženje Terminala", "/masterdata/deviceholder/device-assignment.html?businessPartnerId=&deviceCustomCode=&page=0");
        masterdata.put("Terminali", "Terminal - Service provider", "/masterdata/deviceservprovider/device-serv-provider.html?page=0");


        masterdata.put("Partneri", "Poslovni partner Status", "/masterdata/partnerstatus/read-page.html?page=0");
        masterdata.put("Partneri", "Poslovni partner", "/masterdata/partner/read-page.html?id=&name=&page=0");
        masterdata.put("Partneri", "Prodavac - prodajno mesto", "/masterdata/partner/read-merchant-page.html?id=&name=&type=MERCHANT&page=0");

        masterdata.put("Opšti sifarnici", "Kompanija Korisnik", "/masterdata/client/0");
        masterdata.put("Opšti sifarnici", "Organizaciona jedinica", "/masterdata/org-unit/0");
        masterdata.put("Opšti sifarnici", "Opstina", "/masterdata/township/0");
        masterdata.put("Opšti sifarnici", "Kontakti", "/masterdata/contact/0");
        masterdata.put("Opšti sifarnici", "Banka", "/masterdata/bank/0");
        masterdata.put("Opšti sifarnici", "Valuta", "/masterdata/currency/0");
        masterdata.put("Opšti sifarnici", "Kursna Lista", "/masterdata/exchange-rate/0");
        masterdata.put("Opšti sifarnici", "Tip Dokumenta", "/masterdata/documenttype/0");

        modules.add(masterdata);


        Module crm = new Module();
        crm.setPath("crm/auto-login");
        crm.setName("CRM");
        modules.add(crm);


        Module service = new Module();
        service.setPath("service");
        service.setName("Servis aparata");
        service.put("Proba 1", "Proba 2", "/service/proba/0");
        modules.add(service);





        Module hr = new Module();
        hr.setPath("HR");
        hr.setName("Human Resource");
        hr.put("Human Resource", "Radno mesto", "/HR/job/0");
        hr.put("Human Resource", "Radnik", "/HR/employee/0");
        modules.add(hr);

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
        System.out.println("locale "+defaultLocale.toString());
        return Stream.of(DATEPICKER_LANGUAGES)
                .filter(p -> p.equalsIgnoreCase(defaultLocale.getLanguage()))
                .findFirst()
                .orElse("");
    }
}
