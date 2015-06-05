package com.invado.masterdata.contoller;

import com.invado.core.domain.BusinessPartner;
import com.invado.masterdata.service.BusinessPartnerService;
import com.invado.masterdata.service.dto.PageRequestDTO;
import com.invado.masterdata.service.dto.ReadRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.Map;


/**
 * Created by NikolaB on 6/2/2015.
 */
@Controller
public class BusinessPartnerController {


    @RequestMapping("/home")
    public String showHomePage(){
        System.out.println("ide na home page sada");
        return "home";
    }
    /*
    @RequestMapping("/partner/{page}")
    public String showItems(@PathVariable Integer page,
                            Map<String, Object> model)
            throws Exception {
        System.out.println("izvrsava ");
        PageRequestDTO request = new PageRequestDTO();
        request.setPage(page);
        ReadRangeDTO<BusinessPartner> items = service.readPage(request);
        model.put("data", items.getData());
        model.put("page", items.getPage());
        model.put("numberOfPages", items.getNumberOfPages());
        //return "item-table";
        return "partner-view";
    } */
}
