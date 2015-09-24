/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship;

import com.invado.core.spi.Module;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author bdragan
 */
public class MenuFormPreparer implements ViewPreparer {

    private final Set<Module> modules;

    public MenuFormPreparer() {
        modules = new HashSet<>();
        /*Finansijski modul*/
        Module finance = new Module();
        finance.setActive(false);
        finance.setPath("finance");
        finance.setName("Finansije");
        finance.put("Fakturisanje", "Artikal", "/finance/item/0");
        finance.put("Fakturisanje", "Faktura", "/finance/invoice/0");
        modules.add(finance);

        /*Mati�nio podaci modul*/
        Module masterdata = new Module();
        masterdata.setPath("masterdata");
        masterdata.setName("Mati�ni Podaci");
        masterdata.put("Op�ti �ifarnici", "Kompanija Klijent", "/masterdata/client/0");
        masterdata.put("Op�ti �ifarnici", "Organizaciona jedinica", "/masterdata/org-unit/0");
        masterdata.put("Op�ti �ifarnici", "Poslovni partner", "/masterdata/partner/0");
        masterdata.put("Op�ti �ifarnici", "Banka", "/masterdata/bank/0");
        masterdata.put("Op�ti �ifarnici", "Op�tina", "/masterdata/township/0");
        masterdata.put("Op�ti �ifarnici", "Valuta", "/masterdata/currency/0");
        masterdata.put("Op�ti �ifarnici", "Kurs", "/core/exchange-rate/0");
        modules.add(masterdata);

        /*CRM modul*/
        Module crm = new Module();
        crm.setPath("crm");
        crm.put("CRM", "Status terminala", "/crm/devicestatus/0");
        crm.put("CRM", "Terminal", "/crm/device/0");
        crm.put("CRM", "Transakcije", "/crm/transactions/0");
        crm.put("CRM", "CRM", "/crm/terms/0/read-page.html");
        crm.put("CRM", "Usluge dobavljača", "/crm/service-provider-services/0/read-page.html");
        modules.add(crm);

        /*Servis modul*/
        Module service = new Module();
        service.setPath("service");
        service.setName("Servis aparata");
        service.put("Proba", "Proba", "/service/proba/0");
        modules.add(service);

    }

    @Override
    public void execute(Request  request, AttributeContext ac) {
        HttpServletRequest request1 = ((ServletRequestAttributes) 
                RequestContextHolder.getRequestAttributes())
                .getRequest();
        ac.putAttribute("modules", new Attribute(modules));
        for (Module module : modules) {

            if (request1.getRequestURI().contains(module.getPath())) {
                ac.putAttribute("selectedModule", new Attribute(module));
            }
        }
    }

}
