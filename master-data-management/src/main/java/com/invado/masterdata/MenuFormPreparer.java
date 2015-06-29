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
import java.util.Set;

/**
 *
 * @author bdragan
 */
public class MenuFormPreparer implements ViewPreparer {

    private final Set<Module> modules;

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
        masterdata.put("Opsti sifarnici", "Preduzece", "/masterdata/client/0");
        masterdata.put("Opsti sifarnici", "Organizaciona jedinica", "/masterdata/org-unit/0");
        masterdata.put("Opsti sifarnici", "Poslovni partner", "/masterdata/partner/0");
        masterdata.put("Opsti sifarnici", "Opstina", "/masterdata/township/0");
        masterdata.put("Opsti sifarnici", "Banka", "/masterdata/bank/0");
        masterdata.put("Opsti sifarnici", "Radno mesto", "/masterdata/job/0");
        masterdata.put("Opsti sifarnici", "Radnik", "/masterdata/employee/0");
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
